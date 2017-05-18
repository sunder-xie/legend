/*
 *  zmx 2016/08/18
 *  生产线管理
 */

$(function () {
    var doc = $(document);

    seajs.use([
        'art',
        'select',
        'dialog'
    ],function(at,st,dg){
        //类型下拉列表
        st.init({
            dom: '.js-type-select',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '1',
                value: '快修线'
            },{
                key: '2',
                value: '事故线'
            },{
                key: '3',
                value: '快喷线'
            },{
                key: '4',
                value: '小钣金事故线'
            }],
            callback:function(showKey){
                $('.show-key').val(showKey);
                $.ajax({
                    type:"get",
                    url:BASE_PATH + "/workshop/productionline/processList?type="+showKey,
                    success:function(result){
                        if(result.success){
                            //表格数据
                            var html = at('TableTpl',{json:result});
                            $('#tbaleCon').html(html);
                            if(showKey == '2'){
                                $('.processName').each(function(){
                                    var processName = $.trim($(this).text());
                                    if(processName == '钣金'){
                                        $(this).parents('.table-list').find('.js-check').attr('disabled',true);
                                        $(this).parents('.table-list').find('.time-hour').attr('disabled',true);
                                    }
                                });
                            }
                        }else{
                            dg.fail(err.message);
                        }
                    }
                })
            }
        });

        //返回按钮
        doc.on('click','.js-goback',function(){
           util.goBack();
        });


        //保存数据
        doc.on('click','.js-save',function(){
            var formData = {
                lineName:$('input[name="lineName"]').val(),
                type:$('.show-key').val(),
                remark:$('input[name="remark"]').val()
            };
            var processVOList = [];
            $('.table-list').each(function(){
                var name = $(this).find('.name').val(),
                    barCode = $(this).find('.barCode').text(),
                    workTime = $(this).find('.time-hour').val(),
                    processId = $(this).find('.processId').val(),
                    processSort = $(this).find('.processSort').val(),
                    processName = $(this).find('.processName').text(),
                    checked = $(this).find('input[type="checkbox"]'),
                    isDeleted = $(this).find('.isDeleted').val();

                    if( !(checked.is(':checked')) ){
                        isDeleted='Y';
                    }
                processVOList.push({
                    name:name,
                    barCode:barCode,
                    workTime:workTime,
                    processId:processId,
                    processSort:processSort,
                    processName:processName,
                    isDeleted:isDeleted
                });
            });


            formData['processVOList'] = processVOList;

            $.ajax({
                type:'post',
                url:BASE_PATH + '/workshop/productionline/add',
                data: JSON.stringify(formData),
                contentType: 'application/json',
                success:function(result){
                    if(result.success){
                        dg.success('保存成功');
                        window.location.href=BASE_PATH + '/workshop/productionline/productionline-list';
                    }else{
                        dg.fail(result.message);
                    }
                }
            })
        });


        //工牌卡样式
        doc.on('click','.js-card-pic',function(){
            var cardName = $(this).parents('tr').find('.barCode').text();
            var cardUrl = BASE_PATH + '/static/img/page/magic/'+ cardName + '.jpg';
            var html = at('cardPic',{cardUrl:cardUrl});
            dg.open({
                area: ['420px', '324px'],
                content:html
            });
        });
    });
});