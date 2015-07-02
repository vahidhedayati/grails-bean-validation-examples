package testing

class TestController {

	/*
	 * index 1 parent and child bean are encapsulated as one object and behave as one 
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
	 * index 3 required 1 child list elements 
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
