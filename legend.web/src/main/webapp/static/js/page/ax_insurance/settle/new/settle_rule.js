
$(function () {
    var $cashTab = $('#cash_tab'),
        $packageTab = $('#package_tab');
    
    function insertFrame($el, $tpl) {
        var html = $tpl.html(),
            frame = document.createElement('iframe');

        frame.style.cssText = 'border: none;width: 100%;';
        frame.src = 'javascript:void((function () {document.open();' +
            'document.write(\'<!DOCTYPE html><head></head><body>'+
            html +'</body></html>\');document.close();})())';


        $el.html(frame);

        frame.style.height = frame.contentDocument.documentElement.scrollHeight + 'px';
    }
    
    insertFrame($cashTab, $('#cashDataTpl'));
    insertFrame($packageTab, $('#packageDataTpl'));
    
    seajs.use([], function () {
        //顶部菜单栏的切换
        $(document).on("click",".tab_page_a",function(){
            if($(this).parent().hasClass("active")){
                // 自身点击
                return false;
            }
            // 菜单栏选中更改、
            $(".tab_page_a").parent().removeClass("active");
            $(this).parent().addClass("active");

            //页面展示
            var tab_pane_id = $(this).data("type");
            $(".tab_bonus").removeClass("active");
            $("#"+tab_pane_id).addClass("active");
            //数据展示，模拟按钮电锯
            $("#"+tab_pane_id+"_search_button").trigger("click");
        });
    })
});
