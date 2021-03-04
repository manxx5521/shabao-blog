
<textarea id="content" name="content"><#if view??>${view.content}</#if></textarea>

<script type="text/javascript">
function setContent(content) {
	if (content != null && content.length > 0) {
    	if (mblog.browser.ios || mblog.browser.android) {
    		$('#content').text(content);
    	} else {
    		UE.getEditor('content').setContent(content);
    	}
	}
}

$(function () {
	if (!mblog.browser.ios && !mblog.browser.android) {
        seajs.use('editor', function(editor) {
            editor.init(function () {
                $('#content').removeClass('form-control');
			});
        });
    }
})
</script>
