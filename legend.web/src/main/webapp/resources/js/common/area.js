//artTemplate模板 {name:组件名字,area_id:选中的地区ID,data:地区的对象}
var areaTemplate = '<option value="">请选择</option>' + '<%for(var index in data){%>' + '<%var item = data[index]%>' + '<option value="<%=item.id%>" <%if(item.id == select_id){%>selected="selected"<%}%>><%=item.regionName%></option>' + '<%}%>';

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
        $('#' + childArray[index] + '').empty();
    }

    //生成js联动菜单
    createAreaSelect(childArray[0], parent_id);
}

$(function () {

//
//    //初始化地域联动
//    template.compile("areaTemplate", areaTemplate);
//
//    createAreaSelect('province', 1, 0);

    //初始化地域联动
    template.compile("areaTemplate", areaTemplate);

    var province = $("#provinceInt").val();
    var city = $("#cityInt").val();
    var district = $("#districtInt").val();
    var street = $("#streetInt").val();

    if(typeof(province)=='undefined'){
        createAreaSelect('province', 1, 0);
    }else{
        createAreaSelect('province', 1, province);
        createAreaSelect('city', province, city);
        createAreaSelect('district', city, district);
        createAreaSelect('street', district, street);
    }
});

/**
 * 生成地域js联动下拉框
 * @param name
 * @param parent_id
 * @param select_id
 */
function createAreaSelect(name, parent_id, select_id) {
    if (parent_id == '' || parent_id == 0) {
        return;
    }
    //生成地区
    $.getJSON(BASE_PATH+"/index/region/sub", {"pid": parent_id}, function (json) {

        $('#' + name + '').html(template.render('areaTemplate', {"select_id": select_id, "data": json.data}));
    });
}