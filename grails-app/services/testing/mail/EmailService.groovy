package testing.mail

import com.datingfun.users.*
import grails.core.GrailsApplication
import grails.core.support.GrailsApplicationAware
import grails.util.Environment
import grails.util.Holders
import grails.web.mapping.LinkGenerator
import org.grails.core.io.ResourceLocator
import org.imgscalr.Scalr
import org.springframework.util.FileCopyUtils

import javax.imageio.ImageIO
import java.security.SecureRandom

class EmailService implements GrailsApplicationAware{
    GrailsApplication grailsApplication
    def config
    def mailService
    LinkGenerator grailsLinkGenerator
    String hostname="http://www.example.com"
    static prng = new SecureRandom()
    String siteLogo="/opt/site/logo.png"
    ResourceLocator grailsResourceLocator


    private void sendWeekly(toconfig,mysubject,template,templateModel,List images) throws Exception {
        List<String> recipients = []
        String email = calculateAddresses(recipients, toconfig)
        try {
            mailService.sendMail {
                multipart true
                if (recipients) {
                    to recipients
                }
                else {
                    to email
                }
                if (config.admin.emailFrom) {
                    if (Environment.current == Environment.DEVELOPMENT && config.admin.emailFromDev ) {
                        from "${config.admin.emailFromDev}"
                    } else {
                        from "${config.admin.emailFrom}"
                    }
                }
                subject mysubject
                html Holders.grailsApplication.mainContext.groovyPageRenderer.render(template: template, model: templateModel)
                inline 'inlineImage', 'image/png', new File(siteLogo)
                if (images) {
                    images?.each { a ->
                        println "-- encoding inline image ${a.id}"
                        inline "${a.id}", "${a.contentType}", new File("${a.file}")
                    }
                }
            }
        }
        catch (e) {
            //throw new Exception(e.message)
            log.error "Problem sending email ${e.message}"
        }
    }

    private String calculateAddresses(List<String> recipients, config) {
        String address = ''
        if (config) {
            if (config.toString().indexOf('@') > -1) {
                address = config
            }
            else {
                address = config.mailconfig[config] ?: ''
                if (address.toString().indexOf(',') > -1) {
                    recipients.addAll(address.split(',').collect { it.trim() })
                }
            }
            if (config.toString().indexOf(',') > -1) {
                recipients.addAll(config.split(',').collect { it.trim() })
            }
        }
        return address
    }

    void setGrailsApplication(GrailsApplication ga) {
        config = ga.config
    }
}
