/*
 *  zmx 2016/05/13
 *  股东首页
 */

$(function(){
    seajs.use([
        'table',
        'select',
        'art',
        'dialog'
    ],function(tb,st,at,dg){
        var listUrl = BASE_PATH + '/share/partner/getPartnerPage',
            doc = $(document);
        var tabletpl = 'tableTpl',
            searchData;
        //状态下拉列表
        st.init({
            dom: '.js-downlist',
            url: BASE_PATH + '/share/partner/getPartnerStatus',
            showKey: "code",
            showValue: "name"
        });

        //编辑弹窗
        var EditDialog = null;
        doc.on('click','.js-edit',function(){
            var partnerId = $(this).parents(".js-info-btn").data('partnerId');
            $.ajax({
                type: 'get',
                url: BASE_PATH + "/share/partner/getPartnerInfo",
                data: {
                    id : partnerId
                },
                success: function (result) {
                    if (result.success) {
                        var data = result.data;
                        var html = at('editTpl',{partnerId:data.id,name:data.name,rate:data.rate,contactName:data.contactName,mobile:data.mobile,
                            address:data.address,note:data.note});
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
        doc.on('click','.js-back',function(){
            dg.close(EditDialog);
        });

        doc.on('click','.js-save',function(){
            var id = $(this).data('partnerId');
            var rate = $("#rate").val();
            var contactName = $("#contactName").val();
            var mobile = $("#mobile").val();
            var address = $("#address").val();
            var note = $("#note").val();
            if (rate == "") {   //TODO 只能输入数字校验
                dg.warn("结算比例不能为空！");
                return false;
            }
            var data={};
            data["id"] = id;
            data["rate"] = rate;
            data["contactName"] = contactName;
            data["mobile"] = mobile;
            data["address"] = address;
            data["note"] = note;
            $.ajax({
                type: 'post',
                url: BASE_PATH + "/share/partner/updatePartnerInfo",
                contentType:'application/json',
                data: JSON.stringify(data),
                success: function (result) {
                    if (result.success) {
                        dg.close(EditDialog);
                        dg.success("保存成功", function () {
                            $('.js-search-btn').trigger('click');
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });

        //退出弹窗
        var ExitDialog = null;
        doc.on('click','.js-exit',function(){
            var partnerId = $(this).parents(".js-info-btn").data('partnerId');
            var html = at('exitTpl',{partnerId: partnerId});
            ExitDialog=dg.open({
                area:['500px','auto'],
                content:html
            });
        })
        doc.on('click','.js-close',function(){
            dg.close(ExitDialog);
        });
        //退出弹窗-确认
        doc.on('click','.js-sub',function(){
            var partnerId = $(this).data('partnerId');

            var quitReason = $("#quitReason").val();
            if (quitReason == "") {
                dg.warn("请选择退出的原因！");
                return false;
            }
            $.ajax({
                type: 'post',
                url: BASE_PATH + "/share/partner/quitPartner",
                data: {
                    id: partnerId,
                    reason: quitReason
                },
                success: function (result) {
                    if (result.success) {
                        dg.close(ExitDialog);
                        dg.success("操作成功", function () {
                            $('.js-search-btn').trigger('click');
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });

        doc.on('click', '.js-search-btn', function() {
            var name = $('#search_name').val(),
                partnerStatus = $('#search_partnerStatus').val();

            // 选择模板
            switch (partnerStatus) {
                case "0": tabletpl = 'tableTpl';break;
                case "1": tabletpl = 'tableTpl';break;
                case "2": tabletpl = 'tableTplJoin';break;
            }
            // 初始化分页
            searchData = {page: 1,size:12};
            renderContent(name, partnerStatus);
        });

        doc.on("click",".js-join",function(){
            var partnerId = $(this).parents(".js-info-btn").data('partnerId');
            dg.confirm("您确定要添加该股东吗?",function(){
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + "/share/partner/joinPartner",
                    data: {
                        id : partnerId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("添加成功", function () {
                                $('.js-search-btn').trigger('click');
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

        //结算比例输入校验
        doc.on('input', '.js-rate-input', function () {
            var val = $(this).val();

            val = checkFloat(val, 4);
            if(val !== '.')
                $(this).val(val);
            else {
                val = 0;
            }
        });

        function checkFloat(num, digit) {
            num = num + '';
            num = num.replace('。', '.');
            var index = num.indexOf('.'), reg = /^[0-9]*\.{0,1}[0-9]*$/;

            if(digit == 0 && index > 0) {
                return num.slice(0, index);
            }

            if(! +num && !reg.test(num)) {
                num = '';
            }
            if(num.slice(index+1).length > digit && index >= 0) {
                num = num.slice(0, index + digit + 1);
            }
            return num;
        }

        var Model = {
            // 查询数据接口
            search: function(name, partnerStatus, success) {

                name && (searchData['name'] = name);
                partnerStatus && (searchData['partnerStatus'] = partnerStatus);

                $.ajax({
                    url: listUrl,
                    data: searchData,
                    dataType: "json",
                    success: function(result) {
                        success && success(result);
                    }
                });
            }
        };

        // 渲染页面
        function renderContent(name, partnerStatus) {
            Model.search(name, partnerStatus, function(result) {
                var html;
                if(result.success) {
                    html = at(tabletpl, {json:result});
                    $('#tableDetail').html(html);

                    // 分页模块
                    $.paging({
                        dom : $(".qxy_page"),
                        itemSize : result.data.totalElements,
                        pageCount : result.data.totalPages,
                        current : result.data.number + 1,
                        backFn: function(p) {
                            searchData['page'] = p;
                            renderContent(name, partnerStatus);
                        }
                    });
                } else {
                    layer.msg(result.message);
                }
            });
        }
        $('.js-search-btn').trigger('click');


    });

});