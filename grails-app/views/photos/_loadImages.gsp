<div id="phOtOs">
<div id="imageErrors"></div>
<div class="clearall"></div>


    <br/>
<g:each in="${instance.userImages}" var="photo">
    <div class="user-photos" style="display:inline; width:200px; display:inline-block; padding: 10px;" id="ph_${photo.id}">
        <div class="photo-box"><a class="photo-box-main " title=""><i class="expander fa fa-user"></i></a>
            <div class="photo-box-image">
                <img src="data:${photo.contentType};base64,${photo.image}"  class="img-responsive thumbnail" >
                <div class="photo-box-info2">

                        <i onClick="delPhoto('${photo.id}', this)" class="glyphicon glyphicon-remove" title="${g.message(code:'del.photo')}" ></i>

                    <a onClick="makeDefault('${photo.id}', this)"  title="${g.message(code:'make.photo.def')}"
                       data-image="${photo.image}" data-contentType="${photo.contentType}"
                       class="btn btn-xs btn-default btn-outline">
                        <i class="glyphicon glyphicon-tag"></i>
                    </a>
                    <a href="#masterContainer" data-modal="toggle" class="" title="${g.message(code:'add.photo.tag')}" onclick="loadTag('${photo.id}',this)">
                        <span class="pull-right">
                            <i class="glyphicon glyphicon-comment"></i>
                        </span>
                    </a>
                    <div class="clearall"></div>
                    ${photo.caption}
                   </div>
            </div></div></div>
</g:each>


<div class="modal" id="myModal2" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close" type="button" data-dismiss="modal">×</button>
                <h3 class="modal-title"></h3>
            </div>
            <div class="modal-body">
                <div id="modalCarousel" class="carousel">
                    <div class="carousel-inner"></div>
                    <a class="carousel-control left" href="#modaCarousel" data-slide="prev"><i class="glyphicon glyphicon-chevron-left"></i></a>
                    <a class="carousel-control right" href="#modalCarousel" data-slide="next"><i class="glyphicon glyphicon-chevron-right"></i></a>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<g:javascript>
    function makeDefault(id,item) {
		var contentType=$(item).data('contentType')
		var image=$(item).data('image')
 		$.ajax({type : "POST",url : "${g.createLink(controller:'photos',action:'makeDefault')}",
            data : {id:id},
            success: function (response) {
               	$("#headingImage").attr("src", 'data:'+contentType+';base64,'+image).attr("width", 120);
                $("#profileImage").attr("src", 'data:'+contentType+';base64,'+image).attr("width", 130);
                $("#menuImage").attr("src", 'data:'+contentType+';base64,'+image).attr("width", 130);
            },
            statusCode: {
                417: function() {
                }
            },
            error: function (e) {
            }
        });
	}
	function delPhoto(id,item) {

            $.ajax({timeout:1000,cache:true,type: 'GET',url: "${g.createLink(controller: 'photos', action: 'delPhoto')}?id="+id,
                success: function(data) {
                   //$(item).attr('data-toggle','modal').attr('href','#masterContainer');
                    //$('#ph_'+id).html('');
                    reloadPhotos();
                }
            });

	}
	function loadTag(value,item) {
        if (value != '') {
            $.ajax({timeout:1000,cache:true,type: 'GET',url: "${g.createLink(controller: 'photos', action: 'caption')}?id="+value,
                success: function(data) {
                   //$(item).attr('data-toggle','modal').attr('href','#masterContainer');
                    $('#masterContainer').modal('show');
                    $('#masterContent').html(data).fadeIn('slow');
                    $('#captionContainer').show();
                }
            });
        }
    }

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
        $('#modalCarousel').carousel({interval:false});
        $('#modalCarousel').on('slid.bs.carousel', function () {
            $('.modal-title').html($(this).find('.active').attr("title"));
        })
        $('.user-photos .photo-box  a.photo-box-main i.expander.fa.fa-user').click(function(){
            var idx = $(this).parents('div').index();
            var id = parseInt(idx);
            $('#myModal2').modal('show'); // show the modal
            $('#modalCarousel').carousel(id); // slide carousel to selected
        });
        $("#imageErrors").hover(function(){
            $('#imageErrors').html('');
        })

    });
    function  reloadPhotos() {
	 	$.ajax({timeout:1000,cache:true,type: 'GET',url: "${g.createLink(controller: 'photos', action: 'listPhotos')}",
			success: function(data) {
				$("#phOtOs").html(data);
			}
		});
    <g:if test="${!instance.userImages}">
        $("#headingImage").attr("src", 'data:'+response.contentType+';base64,'+response.image).attr("width", 120);
                        $("#profileImage").attr("src", 'data:'+response.contentType+';base64,'+response.image).attr("width", 130);
                        $("#menuImage").attr("src", 'data:'+response.contentType+';base64,'+response.image).attr("width", 130);
    </g:if>
	}
</g:javascript>
</div>
