function commit(){
    util.submit({
        formid: "shopConfInfo",
        callback: function(data){
            window.location.href = BASE_PATH + "/shop/conf/print/ng";
        }
    });
}