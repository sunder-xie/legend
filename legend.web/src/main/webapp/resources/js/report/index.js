/**
 * Created by sky on 16/4/12.
 * 营业日报
 */

function defDate(offset) {
    var date;

    if (offset) {
        date = new Date(Date.now() + offset * 24 * 60 * 60 * 1000);
    } else {
        date = new Date();
    }

    return [
        date.getFullYear(),
        date.getMonth() + 1,
        date.getDate()
    ].join('-');
}
document.getElementById('time').value = defDate();

$(function () {
    var $document = $(document);

    var Model = {
        search: function (time, success) {
            var shopId = $("#shopId").val();
            var url = BASE_PATH + '/shop/businessDaily/getInfo/' + shopId + '/' + time,
                loadding;
            var downloadurl=BASE_PATH + '/shop/businessDaily/exportexcel/' + shopId + '/' + time;
            $("#exportBtn").attr("href", downloadurl);
            $.ajax({
                url: url,
                dataType: 'json',
                beforeSend: function () {
                    loadding = taoqi.loading();
                },
                success: function (result) {
                    (typeof success === 'function') && success(result);
                },
                complete: function () {
                    taoqi.close(loadding);
                }
            });
        },
        main: function(success) {
            var url = BASE_PATH + "/report/get_config/daily";
            $.ajax({
                url: url,
                dataType: 'json',
                success:function(result) {
                    if (result.success) {
                        var datas = result.data;
                        for(var i = 0; i<datas.length;i++){
                            var data = datas[i];
                            var display = data.display;
                            if(!display){
                                $(".dropdown-menu input[data-ref="+data.field+"]").click();
                            }
                        }
                        success && success(result);
                    } else {
                        taoqi.info(result.errorMsg, 2, 3);
                    }
                }
            });
        }
    };

    /* 报表展示 start */
    function displayReport() {
        var $this = $(this),
            ref = $this.data('ref'),
            $ref = $('#' + ref),
            checked = $this.prop('checked');

        checked ? $ref.show() : $ref.hide();
    }

    /* 报表展示 end */

    /* 表单展示事件 start */
    $document.on('change', '.dropdown-menu input[type=checkbox]', function () {
        displayReport.call(this);
        var datas = [];
        //组装配置bean传给后台
        $(".dropdown-menu input[type=checkbox]").each(function () {
            var $this = $(this)
            data = {
                "field": $this.data('ref'),
                "name": $this.parent().text(),
                "display": $this.is(':checked')
            }
            datas.push(data);
        });
        var url = BASE_PATH + "/report/save_config/daily";
        $.ajax({
            url: url,
            dataType: 'json',
            data: {
                confValue: JSON.stringify(datas)
            },
            success: function (result) {
            }
        });
    });
    /* 表单展示事件 end */

    /* 查询 start */
    $document.on('click', '#searchBtn', function () {
        var $this = $(this),
            time = $('#time').val();

        if (!time) {
            layer.msg('请选择日期！', 2, 3);
            return;
        }

        $this.prop('disabled', true);
        Model.search(time, function (result) {
            if (result.success) {
                var html = template('mainTpl', result);
                $('#content').html(html);
                $('.dropdown-menu input[type=checkbox]').each(function () {
                    displayReport.call(this);
                });
            } else {
                layer.msg(result.errorMsg || '查询失败！', 2, 3);
            }
            $this.prop('disabled', false);
        })
    });

    $document.on('click', '.js-qs-item', function () {
        // 偏移（天）
        var offset = $(this).data('offset');
        if (offset != null) {
            $('#time').val(defDate(offset));
            $('#searchBtn').trigger('click');
        }
    });
    /* 查询 end */

    /* 左侧导航栏 start */
    // 新版报表全部展开
    $('.js-new-nav').find('.js-nav-list').show(500);
    $document.on('click', '.js-nav-title', function () {
        $(this).siblings('.js-nav-list').toggle(500);
    });
    /* 左侧导航栏 end */
    /* 列表项目根据配置更改 start */
    Model.main(function() {
        $("#searchBtn").click();
    });
    /* 列表项目根据配置更改 end */
    //弹框
    $document.on('click','.js-dialog',function(){
             $.layer({
                type: 1,
                title: false,
                area: ['auto', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000'],
                shadeClose: false,
                bgcolor: '#fff',
                closeBtn: [1, true], //去掉默认关闭按钮
                shift: 'top',
                page: {
                    html: $('#ins-dialog').html()
                }
            })

    })
});
