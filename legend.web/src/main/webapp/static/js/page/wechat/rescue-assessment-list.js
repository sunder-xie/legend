
$(function(){
    seajs.use(['table', 'dialog', 'select',  'layer'], function (table, dialog, select, layer) {
        // 下拉选择
        select.init({
            dom: '.js-status',
            showKey: 'key',
            showValue: 'name',
            data: [ {
                key: 0,
                name: '未处理'
            }, {
                key: 1,
                name: '已确认'
            }, {
                key: 2,
                name: '申请已取消'
            }],
            allSelect: true
        });
        $(document).on('click', '.list-tab', function () {
            var $this = $(this),
                type = $this.data('type');
            location.hash = 1;
            if(type == "1"){
                table.init({
                    //表格数据url，必需
                    url: BASE_PATH + '/shop/wechat/op/rescue-apply-list',
                    //表格数据目标填充id，必需
                    fillid: 'tableList',
                    //分页容器id，必需
                    pageid: 'paging',
                    //表格模板id，必需
                    tplid: 'rescue-table-tpl',
                    //关联查询表单id，可选
                    formid: 'searchForm',
                    isfirstfill: true,
                    callback: function(){
                        $this.addClass('current-tab').siblings('.list-tab').removeClass('current-tab');
                    }
                });

            }else{
                table.init({
                    //表格数据url，必需
                    url: BASE_PATH + '/shop/wechat/op/assessment-apply-list',
                    //表格数据目标填充id，必需
                    fillid: 'tableList',
                    //分页容器id，必需
                    pageid: 'paging',
                    //表格模板id，必需
                    tplid: 'assessment-table-tpl',
                    //关联查询表单id，可选
                    formid: 'searchForm',
                    isfirstfill: true,
                    callback: function(){
                        $this.addClass('current-tab').siblings('.list-tab').removeClass('current-tab');

                        $.each($('.car-photos-group'), function(){
                            var id = $(this).attr('id');
                            layer.photos({
                                photos: '#'+id,
                                closeBtn: true,
                                area: 'auto',
                                maxWidth: 450
                            });
                        })
                    }
                });
            }

        })
        $('.current-tab').trigger('click');

        $(document)
            .on('click','.js-view-map',function(){
                $('#map-bounce').show();
                $('.bounce-bg').height($(document).height());
                if(!oMap.initialized){
                    oMap.init();
                }
                var longitude = $(this).data('longitude');
                var latitude = $(this).data('latitude');
                if(longitude && latitude) {
                    oMap.update(longitude, latitude);
                }
            })
            .on('click', '.js-map-close, .bounce-bg', function(){
                $('#map-bounce').hide();
            })
            .on('click', '.js-confirm-rescue' , function(){
                sendAjax($(this), '/shop/wechat/op/confirm-rescue?id=', '已确认')
            })
            .on('click', '.js-cancel-rescue' , function(){
                sendAjax($(this), '/shop/wechat/op/cancel-rescue?id=', '申请已取消');
            })
            .on('click', '.js-confirm-assessment', function(){
                sendAjax($(this), '/shop/wechat/op/confirm-assessment?assessmentId=', '已确认');
            })
            .on('click', '.js-cancel-assessment' , function(){
                sendAjax($(this), '/shop/wechat/op/cancel-assessment?assessmentId=', '申请已取消')
            });

        function sendAjax($this, url, status){
            if($this.hasClass('disable')) return;
            var id = $this.data('id');
            $.ajax({
                url: BASE_PATH + url + id,
                type:'POST',
                success: function (json) {
                    if (json && json.success) {
                        dialog.success('操作成功');
                        $this.addClass('disable').text(status).siblings().remove();
                        $this.closest('tr').prev().find('.processStatus').text(status);
                    } else {
                        dialog.fail(json.errorMsg || '请求失败');
                    }
                },
                error: function () {
                    dialog.fail('请求失败');
                }
            })
        }
    });

    $(document)
        .on('click', '.js-is-fold', function(){
            var $this = $(this),
                text;
            $this.closest('tr').next().find('.apply-detail').slideToggle('slow');
            text = ($this.text()=="展开"? "收起": "展开");
            $this.text(text);
        });

    var oMap = function(){
        var map = null,  marker = null, initialized = false, point;
        function init(){
            map = new BMap.Map("map");
            point = new BMap.Point(116.404, 39.915);
            map.centerAndZoom(point, 15);

            marker = new BMap.Marker(point);
            map.addOverlay(marker);

            map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_LARGE}));
            map.enableScrollWheelZoom(true);
            initialized = true;
        }
        function update(longitude, latitude){
            point = new BMap.Point(longitude, latitude);
            map.setCenter(point);
            map.removeOverlay(marker);
            marker = new BMap.Marker(point);
            map.addOverlay(marker);
        }
        return {
            init: init,
            update: update,
            initialized: initialized
        }

    }();


})

