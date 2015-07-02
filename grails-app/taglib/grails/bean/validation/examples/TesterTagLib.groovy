package grails.bean.validation.examples

import testing.ConnectionBean

class TesterTagLib {
    //static defaultEncodeAs = [taglib:'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
	static namespace = "testing"
	def tester = { attrs ->
		def bean = new ConnectionBean(attrs)
		if (!bean.validate()) {
			bean.errors.allErrors.each {err ->
				throwTagError("Tag [connect] is missing required attribute [${err.field}]")
			}
		}
		out << g.render(template:'/test/taglibtest', model: [bean:bean])
	}
}
