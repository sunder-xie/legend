/**
 * Created by wushuai on 16/4/22.
 * 用于页面列表样式的读取和设置
 */
define(function(require,exports,module) {

    require('ajax');
    /**
     * 获取列表样式(卡片/列表)
     * @return string
     */
    exports.getListStyle=function(){
        var localKey = arguments[0] ? arguments[0] : "shop.listStyle";
        var url = arguments[1] ? arguments[1] : BASE_PATH + "/shop/conf/get-list-style";
        var value = localStorage.getItem(localKey);
        if(value==null){
            $.ajax({
                url: url,
                data: {
                    localKey: localKey
                },
                success: function (result) {
                    if (result.success == true) {
                        value = result.data;
                        //同步到本地缓存
                        localStorage.setItem(localKey,result.data);
                    }
                }
            });
        }
        if(value==null){//取不到值的默认值
            value = "card";
            localStorage.setItem(localKey,"card");
        }
        return value;
    };

    /**
     * 设置列表样式(卡片/列表)
     * @param value "card" "table"
     */
    exports.setListStyle= function(value){
        var localKey = arguments[1] ? arguments[1] : "shop.listStyle";
        var url = arguments[2] ? arguments[2] : BASE_PATH + "/shop/conf/set-list-style";
        if((typeof value)!="string"){
            value = JSON.stringify(value);
        }
        var localValue = localStorage.getItem(localKey);
        if(localValue==value){
            return;
        }
        localStorage.setItem(localKey,value);
        $.ajax({
            type:'post',
            url: url,
            data: {
                localKey: localKey,
                confValue:value
            }
        });
    };

    /**
     * 获取表格动态列配置
     * @param localKey string
     * @return []
     */
    exports.getTableColumn = function(localKey) {
        var url = arguments[1] ? arguments[1] : BASE_PATH + "/shop/conf/get-table-column";
        var value = JSON.parse(localStorage.getItem(localKey));
        if(value==null){
            $.ajax({
                async: false,
                url: url,
                data: {
                    localKey: localKey
                },
                success: function (result) {
                    if (result.success == true) {
                        value = result.data;
                        //同步到本地缓存
                        localStorage.setItem(localKey,JSON.stringify(value));
                    }
                }
            });
        }
        if(value==null){
            value = [];
        }
        return value;
    };

    /**
     * 设置表格动态列
     * @param localKey string
     * @param value []
     */
    exports.setTableColumn = function(localKey,value){
        var url = arguments[2] ? arguments[2] : BASE_PATH + "/shop/conf/set-table-column";
        if((typeof value)!="string"){
            value = JSON.stringify(value);
        }
        var localValue = localStorage.getItem(localKey);
        if(localValue==value){
            return;
        }
        localStorage.setItem(localKey,value);
        $.ajax({
            type:'post',
            url: url,
            data: {
                localKey: localKey,
                confValue:value
            }
        });
    };
});