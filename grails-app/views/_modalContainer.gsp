<div class="modal fade" id="masterContainer" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class='modal-header'>
    		<button type='button' class='close' data-dismiss='modal'
    			aria-hidden='true'>×</button>
    		</div>
            <div class="modal-body">
                <div id="masterConfirmation"></div>
                <div id="masterContent">
                </div>
            </div>
        </div>
    </div>
</div>
<g:javascript>
    function closeCaptionModal() {
        $('#masterContainer').modal('hide');
        $('body').removeClass('modal-open');
        $('.modal-backdrop').remove();
    }
</g:javascript>