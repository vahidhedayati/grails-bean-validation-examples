<div class="container" role="main">
	<g:if test="${instanceTotal==0}">
		<div class="message">
			<g:message code="noMatchesFound.message" />
		</div>
		<br/>
	</g:if>
	<g:elseif test="${instanceTotal>0}">
		<sec:ifLoggedIn>
			<sec:ifAllGranted roles="ROLE_ADMIN">
				<g:set var="maintain" value="${true}"/>
			</sec:ifAllGranted>
		</sec:ifLoggedIn>
		<g:set var="displayLabel" value="${message(code: 'display.label')}"/>
		<table >
			<thead>
				<tr>
					<th class="noSort"><g:message code="image.label"/></th>
					<g:sortableColumn action="list" property="caption" titleKey="image.caption.label" class="caption" params="${search}" />
					<sec:ifLoggedIn>
					<sec:ifAllGranted roles="ROLE_ADMIN">
						<g:sortableColumn action="list" property="contentType" titleKey="default.contentType.label" class="contentType" params="${search}" />
						<g:sortableColumn action="list" property="fileExtension" titleKey="default.fileExtension.label" class="fileExtension" params="${search}" />
					</sec:ifAllGranted>
					</sec:ifLoggedIn>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${instanceList}" status="i" var="instance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td class="image1"><img src="data:${instance.contentType};base64,${instance.scaledImage}"/></td>
						<td class="caption1">${instance?.caption ?: ''}</td>
						<sec:ifLoggedIn>
						<sec:ifAllGranted roles="ROLE_ADMIN">
							<td class="contentType1">${instance.contentType}</td>
							<td class="contentType2">${instance.fileExtension}</td>
						</sec:ifAllGranted>
						</sec:ifLoggedIn>
						<td class="dropdown">
							<g:link action="show" class="menuAction btn btn-default" id="${instance.id}">${displayLabel}</g:link>
							<g:if test="${maintain}">
								<a class="btn btn-default actionButton"
								   data-toggle="dropdown" href="#" data-iteration-id="${instance.id}" >
									<span class="glyphicon glyphicon-menu-hamburger"></span>
								</a>
							</g:if>
						</td>
					</tr>
				</g:each>

			</tbody>
		</table>
		<div class="pagination">
			<span class="listTotal"><g:message code="page.listing" args="${[instanceTotal]}"/></span>
			<g:paginate action="list" total="${instanceTotal}" params="${search}" />
		</div>
		<g:if test="${maintain}">
			<ul id="contextMenu" class="dropdown-menu" role="menu" >
				<li><a id="edit"><g:message code="default.button.edit.label"/></a></li>
				<li><a id="delete" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"><g:message code="default.button.delete.label"/></a></li>
			</ul>
		</g:if>
		<script>
			$(function() {
				$dropdown = $("#contextMenu");
				$(".actionButton").click(function() {
					var id = $(this).attr('data-iteration-id');
					$(this).after($dropdown);
					$dropdown.find("#edit").attr("href", "${createLink(action: "edit")}/"+id);
					$dropdown.find("#delete").attr("href", "${createLink(action: "delete")}/"+id);
					$(this).dropdown();
					$dropdown.css({
						top: 38
					});
				});
			});
		</script>
	</g:elseif>
</div>
