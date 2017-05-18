<div class="aside">
    <div class="logo">
        <span class="plate-con">透明车间看板</span>
        <a href="javascript:void(0);" class="iconfont toggle slide on" title="透明车间看板">&#xe609;</a>
    </div>
    <div class="aside-nav">
        <div class="nav-menu js-nav-menu">
            <div class="nav-root js-nav-root">
                <a href="javascript:void(0);" class="iconfont plate toggle"  title="负载看板">&#xe60a;</a>
                <a href="${BASE_PATH}/workshop/loadplate/loadplate-show" class="nav-root plate-con">负载看板</a>
            </div>
        </div>
        <div class="nav-menu js-nav-menu">
            <div class="nav-root js-nav-root">
                <a href="javascript:void(0);" class="iconfont plate toggle" title="流水线工序看板">&#xe60b;</a>
                <span class="plate-con">流水线工序看板</span>
            </div>
            <ul class="nav-sub-menu js-nav-sub-menu plate-con plate-content">
                <#list productionLines as productionLine>
                <li class="nav-sub-item js-nav-sub-item <#if productionLine.id == lineId>active</#if>">
                    <a href="${BASE_PATH}/board/process-kx?lineId=${productionLine.id}">${productionLine.name}</a>
                </li>
                </#list>
            </ul>
        </div>
    </div>
</div>

<script defer>
    jQuery(function ($) {
        var $act = $('.active');
        if ( $act.hasClass('js-nav-sub-item') ) {
            $act.parents('.js-nav-sub-menu').show(500);
            $act.parents('.ja-nav-menu').addClass('active');
        }

        $('.js-nav-root').click(function () {
            var $el = $(this),
                $p = $el.parent(),
                $active = $p.siblings('.active');
            if ( $p.find('.js-nav-sub-menu').size() ) {
                $active.find('.js-nav-sub-menu').hide(500);
                $p.find('.js-nav-sub-menu').show(500);
            }
            $active.removeClass('active');
            $p.addClass('active');
        });
    });
</script>