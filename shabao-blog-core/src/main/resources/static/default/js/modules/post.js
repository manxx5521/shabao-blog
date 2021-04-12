define(function(require, exports, module) {
	J = jQuery;
	require('plugins');

	var PostView = function () {};
	
	PostView.prototype = {
        name : 'PostView',
        init : function () {
        	this.bindEvents();
        },
        defaults: {
        	type : 'text',
        	defaultEditor: 'ueditor',
        	maxFiles : 6,
        },
        bindEvents : function () {
        	var that = this;
        	
        	that.bindTagit();
        	that.bindValidate();
        	
        	$.fn.serializeObject = function()
        	{
        		var o = {};
        		var a = this.serializeArray();
        		$.each(a, function() {
        			if (o[this.name] !== undefined) {
        				if (!o[this.name].push) {
        					o[this.name] = [o[this.name]];
        				}
        				o[this.name].push(this.value || '');
        			} else {
        				o[this.name] = this.value || '';
        			}
        		});
        		return o;
        	};
        	$('button.btn-primary').click(function(){
        		var url=$('form.form-horizontal').attr('action');
        		var formdata = $("form.form-horizontal").serializeObject();
        		var content=tinymce.get('content').getContent();
        		formdata.content=content;
        		
        		$.ajax({
					type : "POST",
					url : url,
					data:formdata,
					dataType : "json",
					success : function(data) {
						if (!data.success) {
							alert(data.error)
						}else{
							//重写onbeforeunload 不会提示是否离开
							window.onbeforeunload =function() {
								    return null;
							}
							 window.location.href=app.base+'/user?method=posts&access_token='+formdata.access_token;
						}
					},
					error:function(XMLHttpRequest, textStatus, errorThrown) {
						//console.log(errorThrown)
						alert('系统错误');
					}
				});
        	})
        	
        },
        
        bindTagit : function () {
        	$('#tags').tagit({
                singleField: true,
                singleFieldNode: $('#fieldTags'),
                tagLimit: 4
            });
        },
        
        bindValidate: function () {
        	$('form').validate({
                onKeyup: true,
                onChange: true,
                eachValidField: function () {
                    $(this).closest('div').removeClass('has-error').addClass('has-success');
                },
                eachInvalidField: function () {
                    $(this).closest('div').removeClass('has-success').addClass('has-error');
                },
                conditional: {
                    content: function () {
                        return $(this).val().trim().length > 0;
                    }
                },
                description: {
                    content: {
                        required: '<div class="alert alert-danger"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span></button>写点内容吧</div>'
                    }
                }
            });
        }
    };
	
	exports.init = function () {
		new PostView().init();
	}
	
});