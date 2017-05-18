//所有cmd模块的路径,使用可直接使用别名,例如require("layer")($);
seajs.config({
	base : BASE_PATH + "/resources/script/",
	alias: {
		 //库或者插件模块
		"layer" : "libs/layer/layer",
		"chosen" : "libs/chosen/chosen",
		"artTemplate" : "libs/artTemplate/artTemplate",
		"wp" : "libs/My97DatePicker/WdatePicker.js",
		"upload" : "libs/jquery.uploadifive.js",
		
		//自定义功能模块
		"pageInit": "modules/pageInit" ,
		"cookie" : "modules/cookie",
		"chosenSelect" : "modules/chosenSelect",
		"eventBind" : "modules/eventBind",
		"ajax" : "modules/ajax",
		"footer" : "modules/footer",
		"util" : "modules/util",
		"table" : "modules/table",
		"formData" : "modules/formData",
		"paging" : "modules/paging",
		"dialog" : "modules/dialog",
		"tab" : "modules/tab",
		"downlist" : "modules/downlist",
		"jsonSql" : "modules/jsonSql",
		"dict" : "modules/dict",
		"check" : "modules/check",
		"select" : "modules/select",
		"region" : "modules/region",
		"cascadeSelect" : "modules/cascadeSelect"
	}
});