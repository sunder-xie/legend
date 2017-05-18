/*
 *  zmx 2016/05/13
 *  添加股东
 */
$(function(){
    var doc = $(document);

    seajs.use([
        'art',
        'dialog'
        ],function(at,dg){

        getPartners();

        function getPartners(){
            var name = $("#search_name").val();
            if (name == ""){
                name = undefined;
            }
            $.ajax({
                type: 'get',
                url: BASE_PATH + "/share/partner/getAllPartner",
                data: {
                    name:name
                },
                success: function (result) {
                    if (result.success) {
                        var unJoinPartner = result.data;
                        //模板填充
                        var lefthtml = at('LeftBoxTpl',{unJoinPartner:unJoinPartner});
                        $("#leftBox").html(lefthtml);
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        }

        doc.on('click','.js-search',function(){
            getPartners();
        });

        //单击切换
        doc.on('click','.js-boxa li',function(){
            $(this).appendTo('.js-boxb');
        });

        doc.on('click','.js-boxb li',function(){
            $(this).appendTo('.js-boxa');
        });

        //返回按钮
        doc.on('click','.js-goback',function(){
            util.goBack();
        });

        //保存按钮
        doc.on('click','.js-save',function(){
            var partnerIds = "";
            $('.js-boxb li').each(function() {
                partnerIds += $(this).attr("partnerId") + ",";
            });
            if (partnerIds.length > 0) {
                partnerIds = partnerIds.substr(0, partnerIds.length - 1);
            }
            $.ajax({
                type: 'post',
                url: BASE_PATH + "/share/partner/addPartner",
                data: {
                    partnerIds:partnerIds
                },
                success: function (result) {
                    if (result.success) {
                        util.goBack();
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });
    });


});