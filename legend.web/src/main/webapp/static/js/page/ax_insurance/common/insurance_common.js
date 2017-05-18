/**
 * Created by huage on 2017/4/26.
 */
var Insurance = function () {
    var check_shipping_Address = function (dg,inf) {
        var flag = true,
            obj = {};
        var receiverProvince = $.trim(inf.find(".receiverProvince").text());
        if (!receiverProvince || receiverProvince == "请选择省") {
            dg.warn("请选择收件人省份");
            flag = false;
            return false;
        }
        obj['receiverProvince'] = receiverProvince;
        obj['receiverProvinceCode'] = inf.find(".receiverProvince").data('code');
        var receiverCity = $.trim(inf.find(".receiverCity").text());
        if (!receiverCity || receiverCity == "请选择市") {
            dg.warn("请选择收件人城市");
            flag = false;
            return false;
        }
        obj['receiverCity'] = receiverCity;
        obj['receiverCityCode'] = inf.find(".receiverCity").data('code2');
        var receiverArea = $.trim(inf.find(".receiverArea").text());
        if (!receiverArea || receiverArea == "请选择区") {
            dg.warn("请选择收件人地区");
            flag = false;
            return false;
        }
        obj['receiverArea'] = receiverArea;
        obj['receiverAreaCode'] = inf.find(".receiverArea").data('code3');
        var receiverAddr = $.trim(inf.find(".receiverAddr").val());
        if (!receiverAddr) {
            dg.warn("请填写收件人详细地址");
            flag = false;
            return false;
        }
        obj['receiverAddr'] = receiverAddr;
        return flag && obj;
    };



    return {
        shipping_Address:function (dg,inf) {
            return check_shipping_Address(dg,inf)
        }
    }
}();
