import groovy.time.TimeCategory
import testing.Role
import testing.User
import testing.UserAttributes
import testing.UserRole

class BootStrap {

    def springSecurityService

    def init = { servletContext ->

        String admin='admin'
        String user='user'
        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
        def adminUser = User.findByUsername(admin)
        def normalUser = User.findByUsername(user)
        if (!adminUser) {
            //adminUser = new User(username: 'admin', password: springSecurityService.encodePassword('admin'),attributes:attributes, enabled: true).save(flush:true)
            adminUser = new User(username: admin, password: admin ,attributes:attributes, enabled: true).save(flush:true)
            println "-- Username: ${adminUser.username} password: ${admin} id: ${adminUser.id} added "
        }
        if (!adminUser.authorities.contains(adminRole)) {
            UserRole.create adminUser, adminRole, true
            println "-- Roles ${adminUser.getAuthorities()}"
        }
        if (!normalUser) {
            normalUser = new User(username: user, password: user,attributes:attributes, enabled: true).save(flush:true)
            println "-- Username: ${normalUser.username} password: ${user}  id: ${normalUser.id} added "
        }
        if (!normalUser.authorities.contains(userRole)) {
            UserRole.create normalUser, userRole, true
            println "-- Roles ${normalUser.getAuthorities()}"
        }
    }
    private UserAttributes getAttributes() {
        UserAttributes attributes = new UserAttributes()
        Date d = new Date()
        //Make our admin user 18 years old today - override dob which then gives out dob zodiac sign etc
        use (TimeCategory) {
            d=  d - UserAttributes.MIN_AGE.year
        }
        attributes.dob=d
        attributes.email='badvad@gmail.com'
        attributes.save(failOnError: true)
        return attributes
    }
    def destroy = {
    }
}
