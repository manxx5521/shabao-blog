<style>
.hottag .tags { overflow: hidden; float: left; padding-left: 0px;}
.hottag .tags > li { display: inline-block; margin-right: 6px;line-height: 32px; }
.hottag .tags .tag { display: inline-block; padding: 2px 5px; color: #666; border: 1px solid #ebebeb; border-radius: 2px; line-height: 20px; font-weight: normal; font-size: 12px; text-align: center; }
.hottag .tags .tag-sm { padding: 0 8px; line-height: 22px; font-size: 12px; border-radius: 2px;}
.hottag .tag[href]:hover,.tag[href]:focus{background-color:#32a5e7;color:#fff;text-decoration:none}
.hottag .tag-sm[href]:hover,.tag-sm[href]:focus{background-color:#32a5e7;color:#fff;text-decoration:none}
</style>
<div class="panel panel-default corner-radius panel-hot-topics">
	<div class="panel-heading <#--text-center"-->>
		<h3 class="panel-title"><i class="fa fa-tags"></i> 热门标签</h3>
	</div>
	<div class="panel-body hottag">
		<ul class="tags m10" id="hottags" style="float: none;">
			<img src="${base}/static/dist/images/spinner.gif">
    	</ul>
	</div>
</div>

<div class="panel panel-default corner-radius panel-hot-topics">
	<div class="panel-heading <#--text-center"-->">
		<h3 class="panel-title"><i class="fa fa-area-chart"></i> 热门文章</h3>
	</div>
	<div class="panel-body">
		<ul class="list" id="hots">
            <img src="${base}/static/dist/images/spinner.gif">
		</ul>
	</div>
</div>

<div class="panel panel-default corner-radius panel-hot-topics">
	<div class="panel-heading <#--text-center"-->">
		<h3 class="panel-title"><i class="fa fa-bars"></i> 最新发布</h3>
	</div>
	<div class="panel-body">
		<ul class="list" id="latests">
			<img src="${base}/static/dist/images/spinner.gif">
		</ul>
	</div>
</div>

<div class="panel panel-default corner-radius panel-hot-topics">
    <div class="panel-heading <#--text-center"-->">
        <h3 class="panel-title"><i class="fa fa-users "></i> 热门用户</h3>
    </div>
    <div class="panel-body remove-padding-horizontal">
        <ul class="hotusers" id="hotuser">
            <img src="${base}/static/dist/images/spinner.gif">
        </ul>
    </div>
</div>

<script type="text/javascript">

var li_template = '<li>{0}. <a href="${base}/view/{1}">{2}</a></li>';

var hotUser_li_template = '<li><a href="{1}"><img src="${base}{0}" class="avatar avatar-small"/></a></li>'

var tag_li_template	= '<li class="mb6"><a href="${base}/tag/{0}/" class="tag" title="有{1}篇文章">{2}</a></li>';


seajs.use('sidebox', function (sidebox) {
	sidebox.init({
        tagsUrl : '${base}/api/hottags?1=1<#if access_token??>&access_token=${access_token}</#if>',
        latestUrl : '${base}/api/latests?1=1<#if access_token??>&access_token=${access_token}</#if>',
    	hotUrl : '${base}/api/hots?1=1<#if access_token??>&access_token=${access_token}</#if>',
		hotTagUrl : '${base}/api/hot_tags?1=1<#if access_token??>&access_token=${access_token}</#if>',
		hotUserUrl:'${base}/api/hotusers?1=1<#if access_token??>&access_token=${access_token}</#if>',

        size :10,
        // callback
        onLoadHotTag : function (i, data) {
      		var item = jQuery.format(tag_li_template, data.name, data.posts, data.name);
      		return item;
        },
        onLoadHot : function (i, data) {
      		return jQuery.format(li_template, i + 1, data.id, data.title);
        },
        onLoadLatest : function (i, data) {
      		return jQuery.format(li_template, i + 1, data.id, data.title);
        },
        onLoadHotUser : function (i, data) {
        var url = '${base}/users/' + data.id+'?1=1<#if access_token??>&access_token=${access_token}</#if>';
      		var item = jQuery.format(hotUser_li_template,data.avatar,url,data.name, data.fans);
      		return item;
        }
	});
});
</script>