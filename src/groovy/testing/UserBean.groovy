package testing

import grails.validation.Validateable

@Validateable
class UserBean {

	String username
	
	static constraints = {
		username(nullable: false, validator:validateInput)
	}

	static def validateInput={value,object,errors->
		if (!value) {
			return errors.rejectValue(propertyName,"invalid.$propertyName",[''] as Object[],'')
		}
	}

}
