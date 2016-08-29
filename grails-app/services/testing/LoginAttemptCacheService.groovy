package testing
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import grails.core.GrailsApplication
import grails.core.support.GrailsApplicationAware
import grails.transaction.Transactional

import java.util.concurrent.TimeUnit
import org.apache.commons.lang.math.NumberUtils
import javax.annotation.PostConstruct

class LoginAttemptCacheService implements GrailsApplicationAware {
    def config
    GrailsApplication grailsApplication
    private LoadingCache attempts
    private int allowedNumberOfAttempts


    @PostConstruct
    void init() {
        allowedNumberOfAttempts = config.brutforce.loginAttempts.allowedNumberOfAttempts
        int time = config.brutforce.loginAttempts.time
        log.info "account block configured for $time minutes"
        attempts = CacheBuilder.newBuilder()
                .expireAfterWrite(time, TimeUnit.MINUTES)
                .build({0} as CacheLoader);
    }

    /**
     * Triggers on each unsuccessful login attempt and increases number of attempts in local accumulator
     * @param login - username which is trying to login
     * @return
     */
    def failLogin(String login) {
        def numberOfAttempts = attempts.get(login)
        log.debug "fail login $login previous number for attempts $numberOfAttempts"
        numberOfAttempts++
        if (numberOfAttempts > allowedNumberOfAttempts) {
            blockUser(login)
            attempts.invalidate(login)
        } else {
            attempts.put(login, numberOfAttempts)
        }
    }

    /**
     * Triggers on each successful login attempt and resets number of attempts in local accumulator
     * @param login - username which is login
     */
    def loginSuccess(String login) {
        log.debug "successfull login for $login"
        attempts.invalidate(login)
    }

    /**
     * Disable user account so it would not able to login
     * @param login - username that has to be disabled
     */
    @Transactional
    private void blockUser(String login) {
        log.debug "blocking user: $login"
            def user = User.findByUsername(login)
            if (user) {
                user.accountLocked = true
                user.save(flush: true)
            }
    }

    void setGrailsApplication(GrailsApplication ga) {
        config = ga.config
    }
}