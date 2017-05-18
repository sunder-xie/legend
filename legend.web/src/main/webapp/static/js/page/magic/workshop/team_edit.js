/*
 *  zmx 2016/07/04
 *  添加班组
 */
$(function(){
    var doc = $(document);
    var teamId = util.getPara('teamId');
    seajs.use([
        'art',
        'dialog'
    ],function(at,dg){

        //左侧模板填充
        getPartners();
        function getPartners(){
            $.ajax({
                type: 'get',
                url: BASE_PATH + "/workshop/team/ungrouped",
                data: {},
                success: function (result) {
                    if (result.success) {
                        //模板填充
                        var lefthtml = at('LeftBoxTpl',{json:result});
                        $("#leftBox").html(lefthtml);
                        $('#shopId').val(result.data.shopId);
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        };
        getRightPartners();

        //右侧模板填充
        function getRightPartners(){
            $.ajax({
                type: 'get',
                url: BASE_PATH + "/workshop/team/detail",
                data: {
                    teamId:teamId
                },
                success: function (result) {
                    if (result.success) {
                        //模板填充
                        var rightHtml = at('rightBoxTpl',{json:result});
                        $("#rightBox").html(rightHtml);
                        $('#name').val(result.data.name);
                        $('#remark').val(result.data.remark);
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        }



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
            var teamVO={
                name: $('#name').val(),
                remark: $('#remark').val(),
                shopId: $('#shopId').val(),
                id:teamId
            };
            //右侧列表信息
            var shopManagerExtVOList=[];
            $('.js-boxb li').each(function() {
                var id = $(this).data("id"),
                    name = $(this).text();
                shopManagerExtVOList.push({
                    id: id,
                    teamId:teamId,
                    teamName:$('#name').val()
                });
            });
            //左侧列表信息
            $('.js-boxa li').each(function() {
                var id = $(this).data("id"),
                    name = $(this).text();
                shopManagerExtVOList.push({
                    id: id,
                    teamId:'0',
                    teamName:''
                });
            });
            teamVO['shopManagerExtVOList'] = shopManagerExtVOList;

            $.ajax({
                type: 'post',
                url: BASE_PATH + "/workshop/team/edit",
                data: JSON.stringify(teamVO),
                contentType: 'application/json',
                success: function (result) {
                    if (result.success) {
                        dg.success('保存成功',function(){
                            window.location.href= BASE_PATH + "/workshop/team/listpage"
                        })
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });

    });


});