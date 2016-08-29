package testing.mail

import grails.core.GrailsApplication
import grails.core.support.GrailsApplicationAware
import grails.transaction.Transactional
import testing.PhotosBean
import testing.User

class SchedulerService  implements GrailsApplicationAware {

    def config
    GrailsApplication grailsApplication

    private final static int MAX_RECORDS_EMAIL=4
    private final static String TEMPLATE='/emails/weeklyEmail'

    def emailService
    def messageSource

    def weeklyEmail() {
        def userList = User.where {
            enabled==true && attributes!=null
        }
        def domainName=config.domainName ?: 'example.com'
        def fqdn= "http://"+(config.domainName ?: 'example.com')
        userList?.eachWithIndex { User user, i ->
            Locale locale = Locale.ENGLISH
            String subject = messageSource.getMessage('weekly.subject', [domainName, new Date().format('dd/MMM/YYYY')].toArray(), "", locale)
            List images=[]
            user.attributes?.photos?.each { ui ->
                def res = PhotosBean.getPhoto(ui)
                if (res) {
                    println "-- adding image : $res"
                    images << [id: "uImage${ui.id}", contentType: "${res.contentType}", file: res.file]
                }
            }
            emailService.sendWeekly(user?.attributes?.email, subject, TEMPLATE, [instance: user, domainName:domainName, fqdn:fqdn ], images)
        }
    }

    void setGrailsApplication(GrailsApplication ga) {
        config = ga.config
    }
}
