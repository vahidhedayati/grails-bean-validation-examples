<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'photos.upload.title.label')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list" params="${session.search}"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<sec:ifLoggedIn>
				<sec:ifAllGranted roles="ROLE_ADMIN">
				<li><g:link class="create" action="index">
						<g:message code="default.new.label" args="[entityName]" />
					</g:link>
				</li>
				<li><g:link class="delete" action="delete" id="${instance?.id}"
						onclick="return confirm('${message(code: 'delete.confirm.message')}');">
						<g:message code="delete.label" />
					</g:link>
				</li>
				</sec:ifAllGranted>
			</sec:ifLoggedIn>
			</ul>
		</div>
		<div class="content" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<fieldset>
				<div class="fieldgroup">
					<img src="data:${instance.contentType};base64,${instance.image}"/>
				</div>
				<div class="fieldgroup caption">
					${instance.caption}
				</div>
			</fieldset>
		</div>
	</body>
</html>
