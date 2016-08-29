<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'photos.upload.title.label')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		<style>
		.modal-dialog {}
		.thumbnail {margin-bottom:6px;}
		.carousel-control.left,.carousel-control.right{
			background-image:none;
		}
		</style>
	</head>
	<body>

	<div class="col-xs-12 col-sm-12 col-md-10 col-lg-10 col-xs-offset-0 col-sm-offset-0 col-md-offset-1 col-lg-offset-1 " >
		<div class="panel99 panel-info9">
			<div class="panel-heading">
				<h3 class="panel-title">
					<div id="logo-container" class="site-logo centered">

					<g:message code="photos.title" args="[instance.user.username]"/>
					</div>
				</h3>
			</div>
			<div class="panel-body9">
				<div class="container " id="inbox">

		<div class="content" role="main">
			<div class="text-center">

				<label for="description">
					<span class="question-answers"><g:message code="your.avatar"/></span></label>
				<div class="kv-avatar2 center-block" style="width:200px">
					<div class="file-input"><div class="file-preview ">
						<div class="file-drop-disabled">
							<div class="file-preview-thumbnails"><div class="file-default-preview">
								<testing:showPhoto id="profileImage" class="profiles-image img-circle"  username="${sec.loggedInUserInfo(field: 'username')}"  style="width:130px" width="130" height="130"/>
							</div></div>
							<div class="clearfix"></div>
							<div class="file-preview-status text-center text-success"></div>
							<div class="kv-fileinput-error"></div>
						</div>
					</div>

						<div tabindex="500" class="btn btn-primary btn-file" title="${g.message(code:'add.photo')}"><i class="glyphicon glyphicon-folder-open"></i>
							<input  name="imageFile" id="imageFile" class=""  type="file">
							<g:hiddenField id="b64" name="imagebase64"/>
						</div>
					</div>
				</div>




			</div>
		</div>
	<div class="container row col-sm-12" style="display:inline;">
		<div id="uploads">
		<g:include controller="photos" action="listPhotos" />
		</div>
	</div>
	</div>
		</div>
	<div id="captionContainer" style="display:none;">
		<g:render template="/modalContainer"/>
	</div>

</div>
		</div>

<g:javascript>
	$('.user-photos .photo-box .photo-box-image .img-responsive').on('load', function() {
	}).each(function(i) {
  		if(this.complete) {
  			var item = $('<div class="item"></div>');
    		var itemDiv = $(this).parents('div');
    		var title = $(this).parent('a').attr("title");
    		item.attr("title",title);
  			$(itemDiv.html()).appendTo(item);
  			item.appendTo('.carousel-inner');
    		if (i==0){
     			item.addClass('active');
    		}
  		}
	});

    $(function() {


    function applyAjaxFileUpload(element) {
    	$(element).AjaxFileUpload({
    		onChange: function(filename) {
				// Create a span element to notify the user of an upload in progress
				var $span = $("<span />")
					.attr("class", $(this).attr("id"))
					.text("Uploading")
					.insertAfter($(this));

				$(this).remove();

				interval = window.setInterval(function() {
					var text = $span.text();
					if (text.length < 13) {
						$span.text(text + ".");
					} else {
						$span.text("Uploading");
					}
				}, 200);
			}, onSubmit: function(filename) {
				// Return false here to cancel the upload
				var $fileInput = $("<input />")
					.attr({
						type: "file",
						name: $(this).attr("name"),
						id: $(this).attr("id")
					});

				$("span." + $(this).attr("id")).replaceWith($fileInput);

				applyAjaxFileUpload($fileInput);
				return true;
			}, onComplete: function(filename, response) {
				window.clearInterval(interval);
				if (response.image != '') {
					 reloadPhotos();
<g:if test="${!instance.userImages}">
	$("#headingImage").attr("src", 'data:'+response.contentType+';base64,'+response.image).attr("width", 120);
					$("#profileImage").attr("src", 'data:'+response.contentType+';base64,'+response.image).attr("width", 130);
					$("#menuImage").attr("src", 'data:'+response.contentType+';base64,'+response.image).attr("width", 130);
</g:if>
	} else {
					$('#imageErrors').html(response.error);
				}
			}
		});
	}
	function  reloadPhotos() {
	 	$.ajax({timeout:1000,cache:true,type: 'GET',url: "${g.createLink(controller: 'photos', action: 'listPhotos')}",
			success: function(data) {
				$("#uploads").html(data);
			}
		});
	}
	applyAjaxFileUpload("#imageFile");
	function display(id) {
    	$('#modalCarousel .item').html('');
		var item = $('<div class="item"></div>');
    	var itemDiv = $('#img'+id).parents('div');
    	var title = $('#img'+id).parent('a').attr("title");
    	item.attr("title",title);
  		$(itemDiv.html()).appendTo(item);
  		item.appendTo('.carousel-inner');
     	item.addClass('active');
        var idx = $('#img'+id).parents('div').index();
  		var id = parseInt(idx);
  		$('#myModal2').modal('show');
    	$('#modalCarousel').carousel(id);
    }
	});


</g:javascript>
	</body>
</html>

