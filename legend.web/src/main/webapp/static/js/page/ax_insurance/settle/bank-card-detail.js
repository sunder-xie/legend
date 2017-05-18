/**
 * Created by zmx on 2016/11/30.
 */
$(function(){
    var $doc = $(document);
    seajs.use([
        'formData',
        'dialog',
        'ajax',
        'check'
    ],function(fd,dg,aj,ck) {
        //修改
        $doc.on('click', '.js-modify', function () {
            window.location.href = BASE_PATH+"/insurance/anxin/settle/bank-card?refer=bank-card";
        });
        //返回按钮
        $doc.on('click', '.js-goback', function () {
            util.goBack();
        });

    });
});