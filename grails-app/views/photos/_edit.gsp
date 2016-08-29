<g:hiddenField name="language" value="${session.language}"/>
<fieldset>
	<label for="caption" class="property-label">
		<g:message code="default.image.caption.label" />

	</label>
	<g:textField name="caption" value="${instance.caption}"/>
</fieldset>
<fieldset>
	<label for="citysearch" class="property-label">
		<g:message code="default.choose.image.label" />
		<span class="required-indicator">*</span>
	</label>
	<img src="data:${instance.contentType};base64,${instance.image}"/>
</fieldset>
