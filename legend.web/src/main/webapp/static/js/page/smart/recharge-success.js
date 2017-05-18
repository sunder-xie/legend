/**
 * Created by huage on 2016/12/21.
 */
$(function () {
    seajs.use('dialog',function (dg) {
        addHeadStyle(3);
        var $doc =  $(document);
        //点击事件
        $doc.on('click','.go-insure',function () {
            window.location.href = BASE_PATH + '/smart/bihu/flow/bihu-flow'
        });
        $doc.on('click','.go-record',function () {
            window.location.href = BASE_PATH + '/smart/bihu/usedView/recharge-list';
        });

    })
});
