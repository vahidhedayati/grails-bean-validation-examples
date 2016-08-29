<g:hiddenField name="language" value="${session.language}"/>
<fieldset>
	<label for="citysearch" class="property-label">
		<g:message code="default.choose.image.label" />
		<span class="required-indicator">*</span>
	</label>
	<input id="inp" name="imageFile" type='file' required>
	<g:hiddenField id="b64" name="imagebase64"/>
	<img id="img" style="width: 200px; height: 200px; display:none;">
</fieldset>
<g:javascript>
function EL(id) { return document.getElementById(id); } // Get el by ID helper function
function readFile() {
    if (this.files && this.files[0]) {
        var FR= new FileReader();
        FR.onload = function(e) {
            EL("img").src       = e.target.result;
            EL("b64").value = e.target.result;
        };
        FR.readAsDataURL( this.files[0] );
		$('#img').show();
    }
}
EL("inp").addEventListener("change", readFile, false);
$(function() {
	$('#rtTabHeader').tabs();
});
</g:javascript>