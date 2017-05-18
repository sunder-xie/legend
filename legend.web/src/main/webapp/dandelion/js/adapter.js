/**
 * Created by sky on 2016/5/16.
 * 适配手机屏幕
 * 注：标签的尺寸单位请使用 rem(根据html的font-size定义宽度)
 *    页面按照640px宽度定义，如果想要得到640px的宽度，只要修改成6.4rem即可，以此类推！(rem = px/100)
 */

(function (doc, win) {
    var docEl = doc.documentElement,
        docBody = doc.body,
        maxWidth = docEl.dataset['mw'] || 640,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        // 当文档正在加载时,返回loading,
        // 当文档结束渲染但内在资源正在加载时,返回interactive
        // 当文档加载完成时,返回complete
        readyRE = /complete|loaded|interactive/,
        recalc = function () {
            var clientWidth;
            if (win.innerWidth) {
                clientWidth = win.innerWidth;
            } else if (docEl && docEl.clientWidth) {
                clientWidth = docEl.clientWidth;
            } else {
                clientWidth = docBody.clientWidth;
            }
            if (!clientWidth) return;
            docEl.style.fontSize = 100 * (clientWidth / maxWidth) + 'px';
            //window.devicePixelRatio是设备上物理像素和设备独立像素(device-independent pixels (dips))的比例。
            //公式就是: window.devicePixelRatio = 物理像素 / dips (非视网膜屏为1， 视网膜屏为2)
            //docEl.setAttribute("dpr", window.devicePixelRatio ? window.devicePixelRatio : "");
        };
    window.orientation; //safair bug

    if (document.addEventListener) {
        win.addEventListener(resizeEvt, recalc, false);
        doc.addEventListener('DOMContentLoaded', recalc, false);
    } else if (document.attachEvent) {
        win.attachEvent(resizeEvt, recalc);
        doc.attachEvent('onreadystatechange', function() {
            if (readyRE.test(doc.readyState)) {
                recalc();
            }
        });
    }
})(document, window);
