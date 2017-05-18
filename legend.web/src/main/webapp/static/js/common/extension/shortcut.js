/**
 * Created by sky on 2017/2/23.
 * 测试页面：
 *  1. /legend/home 云修生态首页
 *  2. /legend/guide 云修首页
 *  3. /legend/shop/buy/tqmall_goods/index 淘汽采购 —— 淘汽配件
 *  4. /legend/marketing/ng/maintain 客户营销 —— 客情维护 —— 客情维护汇总
 */

$(function () {
    var shortcutTpl = 
        '<div class="shortcut-dialog">\
            <div class="shortcut-content">\
                <div class="shortcut-icon">\
                    <img src="<%= BASE_PATH %>/static/img/common/shortcut/shortcut-icon.png" alt="">\
                </div>\
                <div class="shortcut-info">\
                    <h3 class="shortcut-title">桌面快捷方式</h3>\
                    <p class="shortcut-primary">您是否常常为找不到网址而苦苦烦恼？</p>\
                    <p class="shortcut-minor">快把“淘汽云修”添加到桌面快捷吧！</p>\
                    <% if (hasPrompt !== false) { %>\
                    <div class="shortcut-not-prompt">\
                        <label><input type="checkbox" class="js-visible-shortcut">不再提示</label>\
                    </div>\
                    <% } %>\
                    <div class="shortcut-btns">\
                        <button class="yqx-btn yqx-btn-1 js-shortcut-cancel">残忍拒绝</button><a\
                            href="<%= shortcutUrl %>" \
                            class="yqx-btn yqx-btn-3 js-shortcut-download" download="淘汽云修.url">创建桌面快捷</a>\
                    </div>\
                </div>\
            </div>\
        </div>';
    
    var compatibleStyle = 
        '<style>' +
        '.shortcut-dialog .yqx-btn {' +
            'box-sizing: border-box;' +
            'display: inline-block;' +
            'padding: 0 17px;' +
            'line-height: 35px;' +
            'font-family: inherit;' +
            'font-size: 14px;' +
            'vertical-align: middle;' +
            'border-radius: 3px;' +
        '}' +
        '.shortcut-dialog .yqx-btn-1 {' +
            'padding: 0 16px;' +
            'line-height: 33px;' +
            'color: #333;' +
            'background: #f9f9f9;' +
            'border: 1px solid #d2d2d2;' +
        '}' +
        '.shortcut-dialog .yqx-btn-1:hover {' +
            'background: #ddd;' +
        '}' +
        '.shortcut-dialog .yqx-btn-3 {' +
            'color: #fff;' +
            'background: #607b0a;' +
        '}' +
            '.shortcut-dialog .yqx-btn-3:hover {' +
            'background: #415011;' +
        '}' +
        '</style>';
    
    var shortcutUrl = 'http://tqmall-flash.img-cn-hangzhou.aliyuncs.com/legend/html/%E6%B7%98%E6%B1%BD%E4%BA%91%E4%BF%AE.url';

    Components.getShortcutDialog = function (/* 是否展示“不再提示” */ hasPrompt) {
        var checkboxSelector = '.js-visible-shortcut',
            $doc = $(document),
            templateData = {
                BASE_PATH: BASE_PATH,
                hasPrompt: hasPrompt,
                shortcutUrl: shortcutUrl
            },
            dialog, did, html;

        function layerOpen(html) {
            // 兼容老版本框架
            return $.layer({
                type: 1,
                title: false,
                area: ['auto'],
                border: [0],
                closeBtn: false,
                move: false,
                btns: 0,
                shift: 'top',
                page: {
                    html: html
                }
            });
        }

        function noRemind(checked) {
            var key = 'hideShortcut';
            try {
                checked ? localStorage.setItem(key, 'hide') :
                    localStorage.removeItem(key);
            } catch(ex) {
                console.error('该浏览器不支持localStorage！');
            }
        }

        try {
            seajs.use(['dialog', 'art'], function (dg, tpl) {
                
                if (dg && dg.open) {
                    dialog = dg;
                    html = tpl.compile(shortcutTpl)(templateData);
                    did = dg.open({
                        shadeClose: false,
                        move: false,
                        closeBtn: false,
                        content: html
                    });
                } else {
                    //COMPATIBLE 兼容seajs的老框架
                    seajs.use(['artTemplate', 'layer'], function (tpl) {
                        dialog = layer;
                        html = tpl.compile(shortcutTpl)(templateData);
                        $('head').append(compatibleStyle);
                        did = layerOpen(html);
                    });
                }

                
            });
        } catch (ex) {
            //COMPATIBLE 兼容不支持seajs的老框架
            dialog = layer;
            html = template.compile(shortcutTpl)(templateData);
            $('head').append(compatibleStyle);
            did = layerOpen(html);
        }

        $doc
            .on('click', '.js-shortcut-cancel', function () {
                dialog.close(did);
            })
            .on('click', '.js-shortcut-download', function () {
                noRemind(true);
                dialog.close(did);
            });

        if (hasPrompt !== false) {
            $doc.on('change', checkboxSelector, function () {
                var checked = $(this).prop('checked');
                noRemind(checked);
            })
        }
    }
});