package grails.bean.validation.examples

import testing.ConnectionBean
import testing.PhotosBean

class TesterTagLib {
    //static defaultEncodeAs = [taglib:'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
	static namespace = "testing"
	def userService

	def tester = { attrs ->
		def bean = new ConnectionBean(attrs)
		if (!bean.validate()) {
			bean.errors.allErrors.each {err ->
				throwTagError("Tag [connect] is missing required attribute [${err.field}]")
			}
		}
		out << g.render(template:'/test/taglibtest', model: [bean:bean])
	}


	def showPhoto={attrs->
		def user = userService.currentUser
		if (user) {
			int height = attrs.height ? attrs.height as int :100
			int width = attrs.height ? attrs.height as int : 100
			if (attrs.showUsername) {
				//out << """<a href="#"  class="dropdown-toggle navbar-text" data-toggle="dropdown">"""
				out << "<a>"
			}
			if (user?.attributes?.profilePhoto) {
				def p = PhotosBean.displayPhoto(user?.attributes?.profilePhoto,width,height)
				if (p.scaledImage) {
					out << """<img src="data:${p.contentType};base64,${p.scaledImage}" id="${attrs.id}" style="${
						attrs.style
					}" width="${width}px" height="${height}px" class="${attrs.class}"/>"""
				} else {
					out << """${asset.image(id:"${attrs.id}", src:"default_avatar_${user?.attributes?.gender ?:'MA'}.jpg",
							class:"${attrs.class}",style:"${attrs.style};width:${width}px;height:${height}px", height:"${height}px",width:"${width}px",
							alt:"${g.message(code:'your.avatar')}" )}"""
				}
			} else {
				//style:"width:${attrs.width?:'50'}px;height:${attrs.height?:'50'}px"
				out << """${asset.image(id:"${attrs.id}", src:"default_avatar_${user?.attributes?.gender ?:'MA'}.jpg",
						class:"${attrs.class}",style:"${attrs.style};width:${width}px;height:${height}px", height:"${height}px",width:"${width}px",
						alt:"${g.message(code:'your.avatar')}" )}"""
			}
			if (attrs.showUsername) {

				out << """<span id="userName">${user?.username}</span> <span class="caret"></a>
"""

			}

		}

	}
}
