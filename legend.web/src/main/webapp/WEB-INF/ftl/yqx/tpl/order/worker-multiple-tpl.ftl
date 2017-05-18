<#-- 
    洗车工，维修工多选模板，用到此模板页面，请在下面登记一下
    ch 2016-04-12
    
    用到的页面：
    新建洗车单：/shop/order/carwash
    新建快修快保单: /shop/order/speedily
 -->
<style>
    .worker-chosen-box {
        background: #fff;
        position: absolute;
        padding: 10px 0 0 10px;
        border: 1px solid #d2d2d2;
        box-shadow: 1px 1px 2px #ccc;
        z-index: 2017;
        display: none;
        white-space: nowrap;
        overflow-y: auto;
        max-height: 132px;
    }

    .worker-chosen-box label {
        float: left;
        margin-right: 10px;
        margin-bottom: 10px;
        display: block;
        height: 30px;
        line-height: 30px;
    }

    .worker-chosen-box label > input {
        margin-right: 8px;
    }
</style>

<div class="worker-chosen-box clearfix" data-tpl-ref="worker-multiple-tpl">
    <label><input type="checkbox" name="" value="">111</label>
    <label><input type="checkbox" name="" value="">111</label>
    <label><input type="checkbox" name="" value="">111</label>
    <a href="javascript:;" class="yqx-btn yqx-btn-2 yqx-btn-small">确定</a>
</div>

<script type="text/html" id="worker-chosen-tpl">
    <div class="worker-chosen-box clearfix" style="<%=width%><%=align%>" data-tpl-ref="worker-multiple-tpl">
        <div class="clearfix">
            <%for(var i=0;i
            <json.data.length
            ;i++){%>
            <%var item = json.data[i];%>
            <label><input type="checkbox" name="" value="<%=item.id%>"><%=item.name%></label>
            <%}%>
        </div>
    </div>
</script>

<script>
    function workerInit(opt, url) {
        var defaultOpt = {
            dom: '',
            hiddenDom: '',
            width: '700px',
            align: 'left',
            url: url || BASE_PATH + '/shop/manager/get_manager'
        };
        var args = $.extend({}, defaultOpt, opt),
                doc = $(document);

        $(args.dom).prop('readonly', true);
        seajs.use([
            'ajax',
            'art',
            'dialog'
        ], function (ax, at, dg) {
            var open = function (parent) {
                parent.find('.worker-chosen-box').fadeIn('fast', function () {
                    var hiddenDom = $(args.hiddenDom, parent).val();
                    var idList = hiddenDom && hiddenDom.split(',');
                    var bodyHeight = $(document).outerHeight();
                    var coverHtml = '<div class="yqx-cover" style="height:' + bodyHeight + 'px"></div>';
                    if (idList && idList.length > 0) {
                        for (var i = 0; i < idList.length; i++) {
                            $('input[value="' + idList[i] + '"]', parent).prop('checked', true);
                        }
                    }
                    if ($('.yqx-cover').size() > 0) {
                        $('.yqx-cover').show();
                    } else {
                        $('body').append(coverHtml);
                    }
                });

                parent.find('.fa').removeClass('icon-angle-down').addClass('icon-angle-up');
            }
            var close = function () {
                $('.worker-chosen-box:visible').fadeOut('fast', function () {
                    $('.yqx-cover').hide();
                });
                $(args.dom).siblings('.fa').removeClass('icon-angle-up').addClass('icon-angle-down');
            }

            //单击绑定dom显示维修工或洗车工下拉框，
            //只有第一次会去服务器请求数据，第二次在本地取。
            doc.on('click', args.dom, function () {
                var parent = $(this).parent(),
                        box = parent.find('.worker-chosen-box');
                if (box.size() > 0) {
                    open(parent);
                } else {
                    $.ajax({
                        url: args.url
                    }).done(function (json) {
                        if (json.success) {
                            if (args.align == "right") {
                                args.align = "right:0;top:36px;"
                            } else {
                                args.align = "left:0;top:36px;"
                            }
                            var wid = '';
                            if (args.width != "") {
                                wid = 'width:' + args.width + ';';
                            } else {
                                wid = 'width:700px;';
                            }
                            var html = at('worker-chosen-tpl', {
                                json: json,
                                width: wid,
                                align: args.align
                            });
                            parent.append(html);
                            open(parent);
                            calWorkerWidth(parent[0], parent.find('.worker-chosen-box')[0]);

                            //选择洗车工下拉框，选择事件。
                            parent.find('input').on('click', function () {
                                var $this = $(this),
                                        parent = $this.parents('.form-item'),
                                        nameArr = [],
                                        idArr = [],
                                        selectedList = $('input:checked', parent);

                                if (selectedList.size() > 8) {
                                    dg.warn('最多只能选择8个人');

                                    $(this).removeAttr('checked');
                                    return;
                                }

                                $('input:checked', parent).each(function () {
                                    var name = $(this).parent().text();
                                    var id = $(this).val();
                                    nameArr.push(name);
                                    idArr.push(id);
                                });
                                $(args.dom, parent).val(nameArr.join(','));
                                $(args.hiddenDom, parent).val(idArr.join(','));
                            });
                        }
                    });
                }
            });

            //支持图标点击
            $(document).on('click', args.dom + ' ~ ' + '.fa', function (e) {
                var $e = $(this).parent().find(args.dom);
                $e.trigger('click')
                        .focus();

                e.stopImmediatePropagation();
            });

            //清空事件
            doc.off('click.clear').on('click.clear', '.js-worker-clear', function () {
                var $this = $(this),
                        parent = $this.parents('.form-item');
                $(args.dom, parent).val('');
                $(args.hiddenDom, parent).val('');
                $(':checkbox', parent).prop('checked', false);
            });

            //单击空白隐藏下拉框。
            doc.on('click', '.yqx-cover', function (e) {
                close();
                return false;
            });

            // 避免超出屏幕
            function calWorkerWidth(parent, ele) {
                var offsetParent = $('.order-right')[0];

                var left = parent.offsetLeft - offsetParent.offsetLeft;
                var parentWidth = offsetParent.clientWidth;

                var width;

                if (parent && ele) {
                    if (ele.clientWidth + left > parentWidth) {
                        width = parentWidth - left;
                        if (width < 150) {
                            width = left;
                            $(ele).css({
                                'right': '0',
                                'left': 'auto'
                            });
                        }
                        $(ele).css('width', width + 'px');
                    }
                }
            }
        });
    }
</script>