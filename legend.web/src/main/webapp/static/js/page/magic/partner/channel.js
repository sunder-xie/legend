/*
 *  zmx 2016/05/13
 *  股东首页
 */

$(function(){
    var doc = $(document);
    seajs.use([
        'table',
        'select',
        'art',
        'dialog'
    ],function(tb,st,at,dg){
        var listUrl = BASE_PATH + '/share/channel/getChannelPage',
            tabletpl = 'tableTpl';
        tableFill(listUrl, tabletpl);
        //状态下拉列表
        st.init({
            dom: '.js-downlist',
            url: BASE_PATH + '/share/channel/getChannelTypes',
            showKey: "code",
            showValue: "name"
        });
        /*//表格填充
        var html = at('tableTpl',{});
        $("#tableDetail").html(html);*/

        //新增弹窗
        var EditDialog = null;
        doc.on('click','.js-add',function(){
            var html = at('editTpl',{});
            EditDialog = dg.open({
                area: ['600px','auto'],
                content: html
            });
        });
        doc.on('click','.js-back',function(){
            dg.close(EditDialog);
        });

        doc.on('click','.js-save',function(){
            var channelId = $(this).data("channelId");
            var channelName = $("#channelName").val();
            var channelType = $("#channelType").val();
            var contactName = $("#contactName").val();
            var mobile = $("#mobile").val();
            var address = $("#address").val();
            var note = $("#note").val();
            //TODO 参数校验
            var channelVO={};
            channelVO["id"] = channelId;
            channelVO["channelName"] = channelName;
            channelVO["channelType"] = channelType;
            channelVO["contactName"] = contactName;
            channelVO["mobile"] = mobile;
            channelVO["address"] = address;
            channelVO["note"] = note;
            $.ajax({
                type: 'post',
                url: BASE_PATH + "/share/channel/saveChannelInfo",
                contentType:'application/json',
                data: JSON.stringify(channelVO),
                success: function (result) {
                    if (result.success) {
                        dg.close(EditDialog);
                        dg.success("保存成功", function () {
                            window.location.href = BASE_PATH + "/share/channel/index";
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });

        doc.on('click','.js-edit',function(){
            var channelId = $(this).parents(".js-info-btn").data('channelId');
            $.ajax({
                type: 'get',
                url: BASE_PATH + "/share/channel/getChannelInfo",
                data: {
                    channelId : channelId
                },
                success: function (result) {
                    if (result.success) {
                        var data = result.data;
                        var html = at('editTpl',{channelId:data.id,channelName:data.channelName,channelType:data.channelType,
                            contactName:data.contactName,mobile:data.mobile,address:data.address,note:data.note});
                        EditDialog = dg.open({
                            area: ['600px','auto'],
                            content: html
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });

        doc.on("click",".js-del",function(){
            var channelId = $(this).parents(".js-info-btn").data('channelId');
            dg.confirm("您确定要删除该渠道商吗?",function(){
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + "/share/channel/deleteChannelInfo",
                    data: {
                        id : channelId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("删除成功", function () {
                                window.location.href = BASE_PATH + "/share/channel/index";
                            });
                        } else {
                            dg.fail(result.message);
                        }
                    }
                });
            },function (){
                return false;
            });
        });

        function tableFill(url,tabletpl) {
            //表格渲染
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'tableDetail',
                //分页容器id，必需
                pageid: 'tablePage',
                //表格模板id，必需
                tplid: tabletpl,
                //扩展参数,可选
                data: {size: 12},
                //关联查询表单id，可选
                formid: 'formInfo',
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }

    });

});
