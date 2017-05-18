/*
 * create by zmx 2017/1/4
 * 个人信息
 */

$(function(){
    var doc = $(document);
    seajs.use([
        'formData',
        'dialog',
        'check'
    ],function(fd,dg,ck){
        dg.titleInit();
        doc.on('click','.js-goBack',function(){
            util.goBack();
        })
        doc.on('click','.js-save',function(){
            if( !ck.check()){
                return;
            }
            var supplierObj = fd.get("#formData");
            var save = dg.load("保存信息中");
            $.ajax({
                type: 'POST',
                url: BASE_PATH + '/shop/setting/user-info/change',
                data: JSON.stringify(supplierObj),
                dataType: 'json',
                contentType: "application/json",
                success: function (data) {
                    dg.close(save);
                    if (data.success != true) {
                        dg.fail(data.message);
                        return;
                    } else {
                        dg.success(data.data);
                    }
                },
                error: function (a, b, c) {
                }
            });
        })

    })
});