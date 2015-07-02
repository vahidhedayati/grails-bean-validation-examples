<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'myCity.label', default: 'MyCity')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-myCity" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-myCity" class="content scaffold-list" role="main">

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			
			<!--  for both -->
			<g:hasErrors bean="${cb}">
			<ul class="errors" role="alert">
				<g:eachError bean="${cb}" var="err">
				${err.field } 
 				<g:eachError bean="${err}" var="error">
 				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
				</g:eachError>
			</ul>
			</g:hasErrors>
			
			
			<!--  this is only for index3   -->
				<g:hasErrors bean="${cb?.userDetails}">
				${cb.userDetails}
			<ul class="errors" role="alert">
				<g:eachError bean="${cb.userDetails}" var="cba">
				<g:eachError bean="${cba}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
				</g:eachError>
			</ul>
			</g:hasErrors>
			
			${params }
			
	<g:textField name="username"/>
	<g:textField name="name"/>
	</div>
	</body>
	</html>
	