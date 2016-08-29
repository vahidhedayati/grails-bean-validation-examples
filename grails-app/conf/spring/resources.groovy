import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import security.CustomSecurityEventListener
import security.CustomSecurityFailureEventListener
import security.RedirectFailureToRegistration

// Place your Spring DSL code here
beans = {
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
