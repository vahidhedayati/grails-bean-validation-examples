package testing

import grails.validation.Validateable

@Validateable
class U2Bean {

	String[] username
	String[] name

	static constraints = {
		username(nullable: false, validator:validateInput)
		name(nullable: false, validator:validateInput)
	}

	static def validateInput={value,object,errors->
		if (!value) {
			value.each { val ->
				return errors.rejectValue(propertyName,"invalid.$propertyName",[''] as Object[],'')
			}
			
		}
	}
}
