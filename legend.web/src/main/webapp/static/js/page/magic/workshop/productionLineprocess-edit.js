/*
 *  zmx 2016/08/18
 *  生产线管理
 */

$(function () {
    var doc = $(document);

    seajs.use([
        'select',
        'dialog',
        'art'
    ],function(st,dg,at){

        //判断是哪一条生产线
        function type(){
            var selectType = $('.js-type-select').data('type');
            if(selectType == '1'){
                $('.js-type-select').val('快修线');
            }else if( selectType == '2'){
                $('.js-type-select').val('事故线');
            }else if( selectType == '3'){
                $('.js-type-select').val('快喷线');
            }
            else if( selectType == '4'){
                $('.js-type-select').val('小钣金事故线');
            }
        }
        type();


        //获取checkbox的状态（页面初始化时的状态）
        function checkboxSelect(){
            var arr=[];
            $(':checkbox').each(function(){

                if($(this).prop('checked')==true){
                    arr.push(1);
                }
                else{
                    arr.push(0);
                }
            });
            return arr;
        }
        var oldCheck = checkboxSelect();

        var newCheck = [];

        //保存
        doc.on('click','.js-save',function(){

            //获取checkbox的状态（是否做了更改）
            newCheck = checkboxSelect();
            var on=true;
            for(var i=0;i<$(':checkbox').length;i++){
                if(oldCheck[i]!==newCheck[i]){
                    on=false;
                    break;
                }
            }
            if(on){

                var lineNameTemp  = $('input[name="lineName"]').val();
                var formData = {
                    id:$('input[name="lineId"]').val(),
                    lineName:lineNameTemp,
                    remark:$('input[name="remark"]').val(),
                    isReScheduling:false
                };
                var processVOList = [];
                $('.table-list').each(function(){
                    var workTime = $(this).find('.time-hour').val(),
                        id = $(this).find('.id').val(),
                        lineName=lineNameTemp,
                        checked = $(this).find('input[type="checkbox"]'),
                        isDeleted = $(this).find('.isDeleted').val();

                    if( !(checked.is(':checked')) ){
                        isDeleted='Y';
                    }else{
                        isDeleted='N';
                    }
                    processVOList.push({
                        id:id,
                        workTime:workTime,
                        lineName:lineName,
                        isDeleted:isDeleted
                    });
                });

                formData['processVOList'] = processVOList;

                $.ajax({
                    type:'post',
                    url:BASE_PATH + '/workshop/productionline/update',
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
            }else{
                dg.confirm('生产线发生变动，请重新排班',function(){
                    var formData = {
                        id:$('input[name="lineId"]').val(),
                        lineName:$('input[name="lineName"]').val(),
                        remark:$('input[name="remark"]').val(),
                        isReScheduling:true
                    };
                    var processVOList = [];
                    $('.table-list').each(function(){
                        var workTime = $(this).find('.time-hour').val(),
                            id = $(this).find('.id').val(),
                            lineName=$('input[name="lineName"]').val(),
                            checked = $(this).find('input[type="checkbox"]'),
                            isDeleted = $(this).find('.isDeleted').val();

                        if( !(checked.is(':checked')) ){
                            isDeleted='Y';
                        }else{
                            isDeleted='N';
                        }
                        processVOList.push({
                            id:id,
                            workTime:workTime,
                            lineName:lineName,
                            isDeleted:isDeleted
                        });
                    });

                    formData['processVOList'] = processVOList;
                    $.ajax({
                        type:'post',
                        url:BASE_PATH + '/workshop/productionline/update',
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
            }

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


        //返回按钮
        doc.on('click','.js-return',function(){
           util.goBack();
        });

    });
});