// 页面初始化模块
define("legend/resources/online/script/pageInit-debug", [ "./eventBind-debug", "./check-debug", "./dialog-debug", "../libs/layer/layer-debug", "./ajax-debug", "./cascadeSelect-debug", "../libs/artTemplate/artTemplate-debug", "./chosenSelect-debug", "../libs/chosen/chosen-debug", "../libs/My97DatePicker/WdatePicker-debug", "./formData-debug", "./footer-debug", "./select-debug" ], function(require, exports, module) {
    require("./eventBind-debug");
    var chosen = require("./chosenSelect-debug");
    var footer = require("./footer-debug");
    var select = require("./select-debug");
    var cascadeSelect = require("./cascadeSelect-debug");
    exports.init = function() {
        //下拉框数据获取
        select.init();
        //美化select下拉框
        chosen.handleChoosenSelect(".chosen");
        //底部动画初始化
        footer.init();
        cascadeSelect.init(".cascadeSelect");
        $(".qxy_vmenu").vmenu();
    };
});