//javascript code
seajs.use("downlist");
//选择服务套餐后的回调方法
function serviceListCallFn(obj,item){
    var scope = obj.parents(".form_item");

    $("input[name=serviceId]", scope).val(item.id);
    $("input[name='serviceName']",scope).val(item.name);
    $("input[name='serviceNote']",scope).val(item.serviceNote);
    $("input[name='servicePrice']",scope).val(item.servicePrice);

}

$(function() {
    $(document).on("keyup", "#service_note", function() {
        //限制活动描述字数
        var $this = $(this),
            valNum = $this.val().length;
        if(valNum > 255) {
            $this.val($this.val().substr(0, 255));
        }

    })/*.on("click", ".btn-edit-url", function() {
        //编辑用户活动url
        var $this = $(this),
            $ipt = $(".ipt-act-url"),
            statusClass = "rOnly";
            if($this.hasClass(statusClass)) {
                $ipt.removeAttr("readonly");
                $this.removeClass(statusClass).text("　完成　");
            } else {
                $ipt.prop("readonly", true);
                $this.addClass(statusClass).text("编辑地址");
            }
    })*/.on("click", ".btn-cancel", function() {

        location.href = BASE_PATH + "/shop/activity/ng";
    }).on("click", ".btn-Qr-code", function() {
        //生成二维码，将数据传给后台，获得二维码地址与图片并弹窗展示
        var $this = $(this);

        seajs.use(["ajax","artTemplate", "dialog"], function(ajax,template, dg) {
            //START 创建数据对象
            var maxCol = $("input[name=serviceNum]").val(),
                templateId = $("input[name=templateId]").val(),
                saveData = {
                    id: $("input[name=id]").val(),
                    status: $("input[name=status]").val(),
                    shopId: $("input[name=shopId]").val(),
                    templateId: templateId,
                    title: $("input[name=title]").val(),
                    templateUrl: $("input[name=templateUrl]").val(),
                    //oldTemplateUrl: $("input[name=oldTemplateUrl]").val(),
                    serviceNum: maxCol
                    //content: $("[name=content]").val()
                },
                url = BASE_PATH + "/shop/activity/save",
                arr = [],num = 0;
            if(!templateId) {
                location.href = BASE_PATH + "/shop/activity/ng";
                return false;
            }
            $("#serviceItems").find(".form_item:gt(0)").each(function() {
                var serviceId = $("input[name=serviceId]", this).val();
                if(serviceId){
                    var $this = $(this),
                        obj = {
                            id:$this.find("input[name=marketingServiceId]").val(),
                            serviceId: serviceId,
                            serviceName: $this.find("input[name=serviceName]").val(),
                            serviceNote: $this.find("input[name=serviceNote]").val(),
                            servicePrice: $this.find("input[name=servicePrice]").val()
                        };
                    arr.push(obj);
                    num++;
                }
            });
            if(num > parseInt(maxCol)) {
                dg.info("最多只能加"+maxCol+"条,请核对信息！",3);
                return false;
            }
            saveData.serviceInfos = arr;
            //END 创建数据对象

            ajax.post({
                url: url,
                data: JSON.stringify(saveData),
                contentType: "application/json",
                success: function (result) {
                   if(result.success) {
                       var data = {
                           src: result.data.imgUrl,
                           url: result.data.templateUrl
                       };
                       $("input[name=id]").val(result.data.id);
                       dg.dialog({
                           html: template("scancopyTpl", {data: data})
                       });
                       $("#btn-copy").zclip({  //复制插件，复制url到剪切板
                           path: BASE_PATH + "/resources/script/libs/zclip/ZeroClipboard.swf",
                           copy: function() {
                               return $("#copy-input").val();
                           }
                       });
                   } else {
                       dg.info(result.errorMsg, 3);
                   }
                }
            });

        });
    });
});
