/**
 * Created by sky on 15/8/7.
 */

$(function() {

    $(document).on("mouseover", ".model-list li", function(e) {
        //鼠标移入模板时展示二维码动作
        var s = e.target;
        if(!$(s).hasClass("code-hover")) return; //排除按钮

        $(this).find(".scan-show").stop().animate({"height": "258px"}, 500);
    }).on("mouseleave", ".model-list li", function() {
        //鼠标移出模板时隐藏二维码动作
        $(this).find(".scan-show").stop().animate({"height": "0"}, 500);
    }).on("click", ".btn-forward", function() {
        //触发弹窗，显示二维码图片和路径
        var $this = $(this);

        seajs.use(["artTemplate", "dialog"], function(template, dg) {

            var data = {
                    src: $this.data("src"),
                    url: $this.data("url")
                };
            dg.dialog({
                html: template("scancopyTpl", {data: data})
            });
            $("#btn-copy").zclip({  //复制插件，复制url到剪切板
                path: BASE_PATH + "/resources/script/libs/zclip/ZeroClipboard.swf",
                copy: function() {
                    return $("#copy-input").val();
                }
            });
        });
    });
});
