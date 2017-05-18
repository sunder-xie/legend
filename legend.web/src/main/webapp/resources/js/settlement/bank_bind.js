/**
 * Created by jxf on 2016-04-26.
 */

var areaTemplateProvince = '<option value="">请选择省</option>' + '<%for(var index in data){%>' + '<%var item = data[index]%>' + '<option value="<%=item.id%>" <%if(item.id == select_id){%>selected="selected"<%}%>><%=item.regionName%></option>' + '<%}%>';
var areaTemplateCity = '<option value="">请选择市</option>' + '<%for(var index in data){%>' + '<%var item = data[index]%>' + '<option value="<%=item.id%>" <%if(item.id == select_id){%>selected="selected"<%}%>><%=item.regionName%></option>' + '<%}%>';
var bankTemplate = '<option value="">请选择银行</option>' + '<%for(var index in data){%>' + '<%var item = data[index]%>' + '<option value="<%=item.id%>" <%if(item.id == select_id){%>selected="selected"<%}%>><%=item.name%></option>' + '<%}%>';



function bankBindWithLayer() {

    var layerId = $.layer({
        type: 1,
        title: false,
        area: ['auto', 'auto'],
        border: [0], //去掉默认边框
        shade: [0.5, '#000000'],
        closeBtn: [0, false], //去掉默认关闭按钮
        shift: 'top',
        end: function () {
            layerId = undefined;
        },
        page: {
            html: $("#bandBindTpl").html()
        },
        success: function(){
            initBankForm();
            $(document).on("click","#bankOk",function(){doBindBank(layerId);})
                .on("click","#bankCancle",function(){
                    layer.close(layerId);
                })
        }
    });


}

function initBankForm(){
    var province = $("#provinceInt").val();
    var city = $("#cityInt").val();
    var bank = $("#bankString").val();
    //初始化地域联动
    template.compile("areaTemplateProvince", areaTemplateProvince);
    template.compile("areaTemplateCity", areaTemplateCity);
    template.compile("bankTemplate", bankTemplate);

    createAreaSelect('bankProvince', 1, province,true);
    createAreaSelect('bankCity', province, city,true);
    createBankSelect('bank',bank);
}


function doBindBank(layerId) {
    var id = $("#id").val();
    var province = $("#bankProvince").val();
    var city = $("#bankCity").val();
    var provinceName = $.trim($("#bankProvince option:selected").html());
    var cityName = $.trim($("#bankCity option:selected").html());
    var bank = $("#bank").val();
    var accountBank = $("#accountBank").val();
    var accountUser = $("#accountUser").val();
    var account = $("#account").val();
    var identifyingCode = $("#identifyingCode").val();
    var reg = /^\d{15,19}$/;

    if(bank === "") {
        hintMsg("开户银行不能为空！",layerId);
        return false;
    }
    if(province === "") {
        hintMsg("开户省份不能为空！",layerId);
        return false;
    }
    if(city === "") {
        hintMsg("开户地市不能为空！",layerId);
        return false;
    }
    if(accountBank === "") {
        hintMsg("开户支行不能为空！",layerId);
        return false;
    }
    if(accountUser === "") {
        hintMsg("收款人不能为空！",layerId);
        return false;
    }
    if(account === "") {
        hintMsg("银行卡号不能为空！",layerId);
        return false;
    }else if(!reg.test(account)){
        hintMsg("请输入正确的银行卡号！",layerId);
        return false;
    }

    if(identifyingCode === "") {
        hintMsg("验证码不能为空！",layerId);
        return false;
    }

    var data = {
        id:id,
        bankProvince: provinceName,
        bankCity: cityName,
        bankProvinceId:province,
        bankCityId: city,
        bank:bank,
        accountBank:accountBank,
        accountUser:accountUser,
        account:account,
        identifyingCode:identifyingCode
    };
    var loading = taoqi.loading("保存中...",0);
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: BASE_PATH+'/shop/settlement/activity/bind',
        data: data,
        success: function (result) {
            taoqi.close(loading);
            if (result.success) {
                if(layerId){
                    layer.close(layerId);
                    taoqi.success("绑定成功");
                    window.location.reload();
                }else{
                    taoqi.success("保存成功");
                    window.location.reload();
                }
            } else {
                taoqi.error(result.errorMsg);
            }
        },
        error: function (a, b, c) {
            taoqi.error("网络出错，请刷新页面重试");
        }
    });

    hintMsg("",layerId);
};

function hintMsg(msg,layerId){
    if(layerId&&$(".errormsg")){
        $(".errormsg").html(msg);
    }else if(msg!=""){
        taoqi.error(msg);
    }

}

/**
 * 切换城市地区
 * @param _self 地域对象
 */
function areaChangeCallback(_self) {

    var parent_id = _self.value;
    var childName = $(_self).attr('child');

    if (!childName) {
        return;
    }

    //拆分子对象
    var childArray = childName.split(',');
    for (var index in childArray) {
        $('[name="' + childArray[index] + '"]').empty();
    }

    //生成js联动菜单
    createAreaSelect(childArray[0], parent_id);
}
/**
 * 生成地域js联动下拉框
 * @param name
 * @param parent_id
 * @param select_id
 */
function createAreaSelect(name, parent_id, select_id,isInit) {
    if (!isInit&&(parent_id == '' || parent_id == 0)) {
        return;
    }
    var renderDiv = name=="bankProvince"?"areaTemplateProvince":"areaTemplateCity";

    //生成地区
    $.getJSON(BASE_PATH+"/index/region/sub", {"pid": parent_id}, function (json) {

        $('[name="' + name + '"]').html(template.render(renderDiv, {"select_id": select_id, "data": json.data}));
    });
}

/**
 * 生成银行下拉框
 */
function createBankSelect(name,select_id) {
    var bankData = [{id:"工商银行",name:"工商银行"}
        ,{id:"农业银行",name:"农业银行"}
        ,{id:"中国银行",name:"中国银行"}
        ,{id:"建设银行",name:"建设银行"}
        ,{id:"招商银行",name:"招商银行"}];
    $('[name="' + name + '"]').html(template.render("bankTemplate", {"select_id": select_id, "data":  bankData}));

}
