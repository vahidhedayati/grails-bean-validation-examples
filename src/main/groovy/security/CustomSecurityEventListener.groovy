package security

import grails.util.Holders
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.userdetails.UserDetails

class CustomSecurityEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    //private static Collection activeUsers = Collections.synchronizedList(new ArrayList())
    def loginAttemptCacheService = Holders.grailsApplication.mainContext.getBean('loginAttemptCacheService')

    void onApplicationEvent(AuthenticationSuccessEvent event) {
        def userDetails = (UserDetails) event.getAuthentication().getPrincipal()
        println "-- we have ${userDetails}"
        if (userDetails) {
            SessionListener.userLoggedIn(userDetails.getUsername())
            println "-- should now be logged in"
            loginAttemptCacheService.loginSuccess(event.authentication.name)
        }
    }
    // public static Collection getActiveUsers() {
    //     return Collections.unmodifiableList(activeUsers)
    //  }

}