package testing

import org.springframework.web.context.request.RequestContextHolder

import javax.servlet.http.HttpSession

class UserService {

	def springSecurityService

	def find(String user) {
		return User.findByUsername(user)
	}
	def getUser() {
		def principal= springSecurityService.principal
		def aa = principal.permission
	}

	def getCurrentUser() {
		def principal = springSecurityService?.principal
		String username = principal?.username
		if (username) {
			return find(username)
		}
	}

	Map userInfo() {
		def principal = springSecurityService.principal
		return [username:principal.username,authorities:principal.authorities,enabled:principal.enabled ]
	}


}