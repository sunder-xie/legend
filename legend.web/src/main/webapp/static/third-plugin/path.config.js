/**
 * ch 2016-03-24
 * 所有cmd模块的路径,使用可直接使用别名,例如require("layer")($);
 */

seajs.config({
    base: BASE_PATH + "/static/",
    alias: {
        //库或者插件模块
        "layer": "third-plugin/layer/layer.min",
        "chosen": "third-plugin/chosen/chosen",
        "art": "third-plugin/artTemplate/template.native",
        "dp": "third-plugin/My97DatePicker/WdatePicker",
        "qrCode": "third-plugin/qrcode/jquery.qrcode.min",

        //自定义功能模块
        "cookie": "js/module/cookie",
        "ajax": "js/module/ajax",
        "table": "js/module/table",
        "formData": "js/module/formData",
        "paging": "js/module/paging",
        "dialog": "js/module/dialog",
        "downlist": "js/module/downlist",
        "select": "js/module/select",
        "check": "js/module/check",
        "upload": "js/module/upload",
        "storage": "js/module/storage",
        "date": "js/module/datepicker",
        "audio": "js/module/audio",
        "treeView": "js/module/treeView",
        "banner": "js/module/banner",

        'revisitDialog': 'tpl/revisitDialog/revisit',

        "group": "js/module/group",
        "listStyle": "js/module/listStyle"
    },
    path: {
        "module": "js/module"
    },
    charset: "utf-8"
});
