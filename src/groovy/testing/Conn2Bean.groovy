package testing

import grails.validation.Validateable

@Validateable
class Conn2Bean {
	String host
	
	// SINGLE element of child object so userdetails[0].username userdetails[0].name &&
	List<U2Bean> userDetails = [ new U2Bean()]

	static constraints = {
		host(nullable:false,validator:validateInput)
		userDetails(validator:validateInput3)
	}

	static def validateInput={value,object,errors->
		if (!value) {
			return errors.rejectValue(propertyName,"invalid.$propertyName",[''] as Object[],'')
		}
	}

	static def validateInput3={value,object,errors->
		if (!value) {
			errors.rejectValue(propertyName,'','')
			return true
		}
		value.eachWithIndex { value2, i ->

			def result = value2.validate()
			if (!result && errors != null) {
				def source = value2.errors
				source.allErrors.each{ error ->
					errors.rejectValue(propertyName+"[${i}]."+error.field+"","invalid.$error.field[${i}]",[''] as Object[],'')
				}
			}
			return result
		}
	}
}
