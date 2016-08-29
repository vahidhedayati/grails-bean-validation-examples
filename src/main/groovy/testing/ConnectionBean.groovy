package testing

import grails.validation.Validateable


class ConnectionBean extends UserBean implements Validateable{

	String host
	
	public ConnectionBean() {
		super()
	}

	static constraints = {
		host(nullable: false, validator:validateInput)
	}

	static def validateInput={value,object,errors->
		if (!value) {
			return errors.rejectValue(propertyName,"invalid.$propertyName",[''] as Object[],'')
		}
	}
}
