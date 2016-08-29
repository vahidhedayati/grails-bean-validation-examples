
<div class="container">
    <div class="row">
        <div class="col-sm-6">
            <img src="data:${photo.contentType};base64,${photo.scaledImage}" style="width:100px;height:100px;"/>
        </div>
        <div class="col-sm-6">
            <label for="description"><g:message code="photo.caption.label"/></label>

            <g:hiddenField name="id" value="${photo?.id}"/>
            <g:textArea name="caption"  rows="2"
                        maxlength="100" autocomplete="off"
                        value="${photo.caption?:''}"
            />    <br>

            <span id="counter3"></span> <g:message code="chars.left"/>
        </div>

    </div>
    <div class="col-sm-12 center-block centered">
        <span id="saveForm" class="btn btn-danger btn-outline btn-xlarge pull-right" style="right:40%;">
            <g:message code="save.label" /></span>
    </div>

</div>
<g:javascript>
   $('#caption').simplyCountable({
        counter: '#counter3',
        countType: 'characters',
        maxCount: 100,
        countDirection: 'down'
    });
    $('#saveForm').on('click', function(){
        var value=$('#caption').val();
        var id=$('#id').val();
        $.ajax({type : "POST",url : "${g.createLink(controller:'photos',action:'saveCaption')}",
            data : {id:id, caption:value},
            //contentType: "application/json; charset=UTF-8",
            success: function (response) {
                reloadPhotos();
                closeCaptionModal();
            },
            statusCode: {
                403: function() {
                }
            },
            error: function (e) {
            }
        });

    });

</g:javascript>
