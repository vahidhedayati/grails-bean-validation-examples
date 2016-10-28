package grails.bean.validation.examples

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import security.CustomSecurityEventListener
import security.CustomSecurityFailureEventListener
import security.RedirectFailureToRegistration

class Application extends GrailsAutoConfiguration {
    
    Closure doWithSpring() {
        {->
 redirectFailureHandlerExample(SimpleUrlAuthenticationFailureHandler) {
        defaultFailureUrl = '/failed'
    }

    redirectFailureHandler(RedirectFailureToRegistration) {
        defaultFailureUrl = '/failed'
        registrationUrl = '/'
    }
    customerSecurityEventListener(CustomSecurityEventListener)
    customSecurityFailureEventListener(CustomSecurityFailureEventListener)
        }
        
    }
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}
