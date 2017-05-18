/**
 * Created by jxf on 2016-04-26.
 */

function bankBindWithLayer() {
    seajs.use(['dialog','art','select'], function (dg,art,st) {
        regionInit();

        var layerId = dg.open({
            area: ['413px', '600px'],
            content: $("#bandBindTpl").html()
        });

        //初始化银行
        st.init({
            dom: '#bank',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '工商银行',
                value: '工商银行'
            },{
                key: '农业银行',
                value: '农业银行'
            },{
                key: '中国银行',
                value: '中国银行'
            },{
                key: '建设银行',
                value: '建设银行'
            },{
                key: '招商银行',
                value: '招商银行'
            }]
        });

        //initBankForm(art);

        $(document).on("click","#bankOk",function(){
            doBindBank(layerId,dg);
        }).on("click","#bankCancle",function(){
                dg.close(layerId);
        });
    });

}




/**
 * 切换城市地区
 * @param _self 地域对象
 */
function regionInit() {
    $.ajax({
        url: BASE_PATH+"/index/region/sub?pid=1",
        success: function (result) {
                var $region=$('.dialog .provinceId');
            for (var i = 0; i < result.data.length; i++) {
                var item = result.data[i];
                var options = '<option value="' + item.id + '">' + item.regionName + '</option>';
                    $region.append(options);
            }
        }
    });
    $(document).on('change','.dialog .provinceId',function (){
        $.ajax({
            url: BASE_PATH+"/index/region/sub",
            data:{
              pid:$(this).val()
            },
            success: function (result) {
                var $region=$('.dialog .cityId');
                $region.find('option:not(:eq(0))').remove();
                for (var i = 0; i < result.data.length; i++) {
                    var item = result.data[i];
                    var options = '<option value="' + item.id + '">' + item.regionName + '</option>';
                    $region.append(options);
                }
            }
        });
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
    seajs.use('art', function (art) {
        $('[name="' + name + '"]').html(art("bankTemplate", {"select_id": select_id, "data":  bankData}));
    });
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
    seajs.use('dialog', function (dg) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: BASE_PATH+'/shop/settlement/activity/bind',
            data: data,
            success: function (result) {
                if (result.success) {
                    if(layerId){
                        dg.success("绑定成功");
                        window.location.reload();
                    }else{
                        dg.success("保存成功");
                        window.location.reload();
                    }
                } else {
                    dg.fail(result.errorMsg);
                }
            },
            error: function (a, b, c) {
                dg.fail("网络出错，请刷新页面重试");
            }
        });
    });
    hintMsg("",layerId);
};

function hintMsg(msg,layerId){
    if(layerId&&$(".errormsg")){
        $(".errormsg").html(msg);
    }else if(msg!=""){
        seajs.use('dialog', function (dg) {
            dg.fail(msg);
        });
    }

}