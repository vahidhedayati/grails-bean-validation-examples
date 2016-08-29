admin.emailFrom='badvad@gmail.com'  //this is important for my local machine to set send from to this for it to work
admin.emailFromDev='badvad@gmail.com'  //this is important for my local machine to set send from to this for it to work

grails.plugin.springsecurity.useSecurityEventListener = true
grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
brutforce {
	loginAttempts {
		time = 5
		allowedNumberOfAttempts = 3
	}
}

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'testing.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'testing.UserRole'
grails.plugin.springsecurity.authority.className = 'testing.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/fonts/**',      access: ['permitAll']],
	[pattern: '/**/test/**', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

