package testing

import grails.validation.Validateable

class ConnBean implements Validateable{
	String host
	UBean userDetails = new UBean()
	
	static constraints = {
		host(nullable:false,validator:validateInput)
		userDetails(validator:validateInput2)
	}

	static def validateInput={value,object,errors->
		if (!value) {
			return errors.rejectValue(propertyName,"invalid.$propertyName",[''] as Object[],'')
		}
	}
	/*
	 * This is a basic child bean validation
	 * It takes the input of user which for the user part must be as defined 
	 * userDetails.username and userDetails.name
	 * 
	 */
	static def validateInput2={value,object,errors->
		if (!value) {
			errors.rejectValue(propertyName,'','')
			return true
		}
		def result = value.validate()
		if (!result && errors != null) {
			def source = value.errors
			source.allErrors.each { error ->
					errors.rejectValue(propertyName+"."+error.field,"invalid.$error.field",[''] as Object[],'')
			}
		}
		return result
	}
}
