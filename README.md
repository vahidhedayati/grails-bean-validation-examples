# Grails: Groovy Beans for validation of user input

User input validation is an age old question and with a default grails application if a user implements an app based on domainClasses then your domainClasses by default validation objects. 

If you review your auto generated controller code you will notice certain actions just call on the actual object..

```groovy
def save(User userInstance) {
..
}
```

This has now encapsulated that user domain class as its verification mechanism for the parameters received.

This is great if you decide to do everything using domainClasses.

The purpose of this demo site and its documentation is to explore custom src/groovy beans as ways of validating input that is not bound to any actual domain objects.

The power of bean is a lot more than controller action and parameter verification, you can use them in your services to encapsulate the params as an object that you pass around or even in your tag libs so rather than validating attrs has a value or not just call the bean and let it do all of the hard work for you.

This is a neat way of centralising validation.

### Basic bean for non domain class object validation and other configuration object

[ConnectionBean from boselecta plugin](https://github.com/vahidhedayati/grails-boselecta-plugin/blob/master/src/main/groovy/grails/plugin/boselecta/beans/ConnectionBean.groovy)

```groovy
package grails.plugin.boselecta.beans

import grails.validation.Validateable

//Grails 2 uncomment below and remove implements Validateable
//@Validateable
class ConnectionBean implements Validateable {
	String user
	String job
	static constraints = {
		job (nullable: false, validator:validateInput)
		user(nullable: false, validator:validateInput)
	}
	static def validateInput={value,object,errors->
		if (!value) {
			return errors.rejectValue(propertyName,"invalid.$propertyName",[''] as Object[],'')
		}
	}
}
```
So in src/{groovy|main/groovy}/grails/plugin/boselecta/beans/ConnectionBean.groovy

We have a cut down of actual bean above, it says if user or job is not set then to return a reject message with the propertyName being the given field that fails.


### Validating in a taglib call:
```
def connect  =  { attrs ->
		def cBean = new ConnectionBean(attrs)
		if (!cBean.validate()) {
			cBean.errors.allErrors.each {err ->
				throwTagError("Tag [connect] is missing required attribute [${err.field}]")
			}
		}
		...
}		
```
We are saying if the bean did not validate then to throw a tag error and give the actual field that failed. In this example there was 2. It would be the same 5 lines of code for 20 attribute verifications.

### Controllers

In this sample project there is this controller which does validation according to some strict configuration as per below. Any url pattern outside of those will throw some form of an error back on your page.
 
You will need to follow the beans inside src/groovy of this project to better understand the relationships built, remember these are all non domain objects and used purely to ease form validation of / when user inputs.

```groovy
class TestController {

	/*
	 * index 1 parent and child bean are encapsulated as one object and behave as one 
	 * class ConnectionBean extends UserBean{ ...
	 * http://localhost:8080/testing/test/index2?host=a&username=aa         -- works
	 */
	def index2(ConnectionBean cb) {
		render view: 'index2', model:[cb:cb]
		/*
		if (!cb.validate()) {
			cb.errors.allErrors.each {err ->
				println "--- $err.field"
							}
		}else{
			println "-- validated"
		}
		*/
	}
	
	/*
	 * index 3 required 1 NEW child elements 
	 * UBean userDetails = new UBean() 
	 * http://localhost:8080/testing/test/index3?host=a&userDetails.username=aa&userDetails.name=aas works
	 */
	def index3(ConnBean cb) {
		render view: 'index3', model:[cb:cb]
	}
	
	/*
	 * index 4 Fixed list of required 1 child list elements in array format
	 * http://localhost:8080/grails-bean-validation-examples/test/index4?&host=as&userDetails[0].username=aa&userDetails[0].name=aad   works
	 */
	def index4(Conn2Bean cb) {
		render view: 'index4', model:[cb:cb]
	}
	
	/* 
	 * index 5 Fixed list of required 2 child list elements in array format 
	 * http://localhost:8080/grails-bean-validation-examples/test/index5?&host=as&userDetails[0].username=aa&userDetails[0].name=aad&userDetails[1].username=aa&userDetails[1].name=aad  works
	 * 
	 */
	def index5(Conn3Bean cb) {
		render view: 'index4', model:[cb:cb]
	}
	
	
	/* index 6 Dynamic child bean as list validation
	 *
	 * As you can see it needs an entire page of child bean missing elements causes errors
	 * http://localhost:8080/grails-bean-validation-examples/test/index6?&host=as&userDetails[0].username=aa&userDetails[0].name=aad                                                      -- works
	 * http://localhost:8080/grails-bean-validation-examples/test/index6?&host=as&userDetails[0].username=aa&userDetails[0].name=aad&userDetails[1].username=aa                           -- does not work
	 * http://localhost:8080/grails-bean-validation-examples/test/index6?&host=as&userDetails[0].username=aa&userDetails[0].name=aad&userDetails[1].username=aa&userDetails[0].name=aad   -- works
	 *
	 */
	def index6(Conn4Bean cb) {
		render view: 'index4', model:[cb:cb]
	}
	
	/*
	 * index7 calls custom taglib:
	 *  <testing:tester username="testuser" host="somehost"/> and validates via the bean if validated displays back on screen
	 *  <testing:tester  host="somehost"/> will fail with missing username
	 */
	def index7() {
		render view: 'testTaglib'
	}
}

```
