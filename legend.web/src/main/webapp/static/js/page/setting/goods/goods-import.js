/*
 * create by zmx 2016/11/30
 * 导入数据页面样式
 */
$(function(){
    var doc = $(document);

    doc.on('change','#fileBtn',function(){
        var val = $(this).val();
        $('.js-file-url').val(val);
        isImport();
    });

    function isImport(){
        var url = $('.js-file-url').val();
        if(url != ''){
            $('.js-confirm-import').prop('disabled',false)
        }
    }

    doc.on('click','.js-confirm-import',function(){
        var formData = new FormData();
        formData.append('excelFile', $('#fileBtn').get(0).files[0]);
        var formUrl = $(this).data("url");
        seajs.use(['dialog','art'],function(dg,at){
            $.ajax({
                type:'post',
                url:formUrl,
                beforeSend:function(){
                    dg.warn('导入可能需要3-5分钟,请您耐心等待...')
                },
                data:formData,
                cache: false,
                contentType: false,
                processData: false,
                dataType: 'json',
                success: function(result){
                    if(result.success){
                        var html = at('resultListTpl',{json:result});
                        $('#resultBox').html(html)
                    } else {
                        dg.fail(result.message);
                        $('#resultBox').html('');
                    }
                },
                error:function(){
                    dg.fail("文件不存在，请检查是否被删除,请重新选择文件");
                }
            });
        });
    });

    doc.on('click','.js-return',function(){
        util.goBack();
    })
});