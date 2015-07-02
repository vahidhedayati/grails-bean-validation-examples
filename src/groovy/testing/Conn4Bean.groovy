package testing

import grails.validation.Validateable

/* Dynamic child bean as list validation
 * 
 * As you can see it needs an entire page of child bean missing elements causes errors
 * http://localhost:8080/grails-bean-validation-examples/test/index6?&host=as&userDetails[0].username=aa&userDetails[0].name=aad                                                      -- works
 * http://localhost:8080/grails-bean-validation-examples/test/index6?&host=as&userDetails[0].username=aa&userDetails[0].name=aad&userDetails[1].username=aa                           -- does not work
 * http://localhost:8080/grails-bean-validation-examples/test/index6?&host=as&userDetails[0].username=aa&userDetails[0].name=aad&userDetails[1].username=aa&userDetails[0].name=aad   -- works
 * 
 */
@Validateable
class Conn4Bean {

	String host

	/* Traditional:
	 * List<UBean> userDetails = ListUtils.lazyList([], {new U2Bean()} as Factory)
	 * 
	 * As well as 
	 * List<UBean> userDetails = [].withLazyDefault { new U2Bean() }
	 * 
	 * None of that required simply declare:
	 */
	List<UBean> userDetails

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
					errors.rejectValue(propertyName+"[${i}]."+error.field,"invalid.$error.field",[''] as Object[],'')
				}
			}
			return result
		}
	}
}
