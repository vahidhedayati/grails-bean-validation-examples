package security

import grails.util.Holders
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent

class CustomSecurityFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    def loginAttemptCacheService = Holders.grailsApplication.mainContext.getBean('loginAttemptCacheService')

    void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = (String) event.getAuthentication().getPrincipal()
        println "-- we got a failed attempt for $username"
        if (username) {
            loginAttemptCacheService.failLogin(event.authentication.name)
        }
    }

}