package testing

import grails.validation.Validateable

class U2Bean  implements Validateable{

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
