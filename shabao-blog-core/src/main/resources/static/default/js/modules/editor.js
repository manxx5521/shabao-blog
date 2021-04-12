define(function(require, exports, module) {


    var ueditor;

    var initEditor = function (callback) {
        require.async(['form', 'tinymce'], function () {

            tinymce.init({
                selector: "#content",
                theme: 'modern',
                upload_image_url: window.app.base + "/post/upload", //配置的上传图片的路由
                relative_urls : false,//不使用相对目录，解决返回path默认加上开头../的问题
                height: 400,
                plugins: [
                    'advlist autolink autosave lists link image print anchor codesample',
                    'searchreplace visualblocks code fullscreen textcolor colorpicker textpattern uploadimage',
                    'contextmenu paste','table'
                ],
                toolbar: "undo redo formatselect bold underline blockquote alignleft aligncenter indent " +
                "forecolor bullist numlist link table uploadimage codesample removeformat fullscreen ",
                menubar: false,
                language: "zh_CN",
                statusbar : false,
                body_class: 'markdown-body',
                codesample_dialog_width: '600',
                codesample_dialog_height: '400',
                block_formats: 'Paragraph=p;标题1=h4;标题2=h5;标题3=h6;Preformatted=pre',

                content_css: [
                    window.app.base + '/static/dist/vendors/bootstrap/css/bootstrap.min.css',
                    window.app.base + '/static/default/css/editor.css',
                ]
                //参考网站 https://www.tinymce.com/
            });

            callback.call(this);
        });

    }

	exports.init = function (callback) {
        initEditor(callback);
    }
});