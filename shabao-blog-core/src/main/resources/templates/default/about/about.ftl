<#include "/default/utils/ui.ftl"/>

<@layout "关于我们" site_keywords site_description>

<div class="row main">
    <div class="col-xs-12 col-md-9 side-left topics-show">
        <!-- view show -->
        <div class="topic panel panel-default">
            <div class="infos panel-heading">

                <h1 class="panel-title topic-title">关于我们</h1>

                <div class="meta inline-block">
                    <a class="author" href="#">官方团队</a>
                </div>
                <div class="clearfix"></div>
            </div>

            <div class="content-body entry-content panel-body ">
                <div class="markdown-body" id="emojify">
                    <p><strong>这是一个以学习为主的个人博客</strong><br/></p>
                    <p>邮箱：382752556@qq.com</p>
				</div>
            </div>
            <div class="panel-footer operate">
            </div>
            <div class="panel-footer operate">
                <div class="hidden-xs">
                    <div class="social-share" data-sites="weibo, wechat, facebook, twitter, google, qzone, qq"></div>
                </div>
                <div class="clearfix"></div>
            </div>
        </div>
        <!-- /view show -->
    </div>
    <div class="col-xs-12 col-md-3 side-right hidden-xs hidden-sm">
		<#include "/default/inc/right.ftl"/>
    </div>
</div>

</@layout>