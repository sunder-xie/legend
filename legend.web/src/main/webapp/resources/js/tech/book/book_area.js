//artTemplate模板 {name:组件名字,area_id:选中的地区ID,data:地区的对象}
var areaTemplate = '<option value="">请选择</option>' + '<%for(var index in data){%>' + '<%var item = data[index]%>' + '<option value="<%=item.id%>" <%if(item.id == select_id){%>selected="selected"<%}%>><%=item.name%></option>' + '<%}%>';

/**
 * 切换书籍车型
 * @param _self
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
    createBookCarSelect(childArray[0], parent_id);
}

$(function () {
    //初始化书籍车型
    template.compile("areaTemplate", areaTemplate);

    createBookCarSelect('level2', 0, 0);
});

/**
 * 生成书籍车型js联动下拉框
 */
function createBookCarSelect(name, parent_id, select_id) {
    //生成书籍车型
    $.getJSON(BASE_PATH+"/shop/tech/book/get_bookcar_level", {"parentId": parent_id}, function (json) {
        $('#' + name + '').html(template.render('areaTemplate', {"select_id": select_id, "data": json.data}));
    });
}