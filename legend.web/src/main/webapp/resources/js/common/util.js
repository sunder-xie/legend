/**
 * Created by lixiao on 14-11-6.
 */
//搜索插件
function handleChoosenSelect(selectorName, width) {
    $(selectorName).each(function () {
        if ($(this).find('option').length > 1) {
            $(this).chosen({
                search_contains: true,
                display_width: width, //默认是220px
                allow_single_deselect: $(this).attr("data-with-diselect") === "1" ? true : false,
                no_results_text: '没有搜索到结果'
            });
        }
    });
}
/**
 * 获取url中的某个参数参数
 * 格式为param1=value1&param2=value2...2..
 * @param param 需要取出的参数
 * @return String
 */
function getUrlParam(param)
{
    var tmp = '';
    var currentUrl = window.location.href;
    var rs = new RegExp("(^|)" + param + "=([^\&]*)(\&|$)", "gi").exec(currentUrl);
    if (tmp = rs) {
        return tmp[2];
    }

    return '';
}
$(function() {
    handleChoosenSelect('.chosen');
    //输入限制
    $(document).on("keyup", ".J_input_limit", function(){
        var self = $(this);
        var limit_type = self.data('limit_type');
        var value = self.val();
        var t_value = '';
        if(limit_type === 'number'){
            t_value = value.replace(/[^0-9]/g,'');//剔除非数字字符
          //  t_value = t_value.replace(/^0+/g,'');//剔除首位为.的数字
            if(t_value !== value){
                self.val(t_value);
            }
        }
        if(limit_type === 'price'){
            t_value = value.replace(/[^0-9.]/g,'');//剔除非数字字符，除去.
              t_value = t_value.replace(/^\.+/g,'');//剔除首位为.的数字
            if(t_value !== value){
                self.val(t_value);
            }
        }
        if(limit_type === 'digits_letters'){
            t_value = value.replace(/[^0-9a-z]/gi,'');//剔除非数字和英文字符
            if(t_value !== value){
                self.val(t_value);
            }
        }
        if (limit_type === 'phone') {
            t_value = value.replace(/[^0-9-]/gi,'');
            if(t_value !== value){
                self.val(t_value);
            }
        }
        if (limit_type === 'account') {
            t_value = value.replace(/[^0-9\-]/gi,'');
            if(t_value !== value){
                self.val(t_value);
            }
        }
    });
});
