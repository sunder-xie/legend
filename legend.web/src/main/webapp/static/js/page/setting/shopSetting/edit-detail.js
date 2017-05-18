/*
 * create by zmx 2017/1/4
 * 门店信息页面
 */

$(function(){
   var doc = $(document);
    var _point = null;
    var _isCrm = false;

    $('.clockpicker').clockpicker();

    seajs.use(['check','date','dialog','formData','select','upload','art'],
        function(ck, dp, dg, formData, st, upload, art){

        dg.titleInit();
        //验证
        ck.init();

        //初始化人工数
        st.init({
            dom: '.js-head-count',
            showKey:'key',
            callback:function(key){
                $('input[name="headCount"]').val(key);
            }
        });

        var province =  $('input[name="province"]').val();
        var city =  $('input[name="city"]').val();
        var district =  $('input[name="district"]').val();

        //门店地址
        st.init({
            dom:'.js-province',
            url:BASE_PATH + '/dict/getSubRegionByPid?p_id=1',
            showKey:'id',
            showValue:'regionName',
            callback:function(showKey) {
                $('input[name="province"]').val(showKey);
                province = showKey;
                $('input[name="cityName"]').val('');
                $('input[name="city"]').val('');
                $('input[name="districtName"]').val('');
                $('input[name="district"]').val('');
                $('input[name="streetName"]').val('');
                $('input[name="street"]').val('');
                $('.city-box').find('.yqx-select-options').remove();
                $('.district-box').find('.yqx-select-options').remove();
                $('.street-box').find('.yqx-select-options').remove();
            }
        });

        //选择市
        st.init({
            dom: '.js-city',
            url: BASE_PATH + '/dict/getSubRegionByPid',
            showKey: "id",
            showValue: "regionName",
            params: function (){
                return {
                    p_id: province
                };
            },
            callback: function (showKey) {
                $('input[name="city"]').val(showKey);
                city = showKey;
                $('input[name="districtName"]').val('');
                $('input[name="district"]').val('');
                $('input[name="streetName"]').val('');
                $('input[name="street"]').val('');
                $('.district-box').find('.yqx-select-options').remove();
                $('.street-box').find('.yqx-select-options').remove();
            }
        });

        //选择区
        st.init({
            dom: '.js-district',
            url: BASE_PATH + '/dict/getSubRegionByPid',
            showKey: "id",
            showValue: "regionName",
            params: function (){
                return {
                    p_id: city
                }
            },
            callback: function (showKey) {
                $('input[name="district"]').val(showKey);
                district = showKey;
                $('input[name="streetName"]').val('');
                $('input[name="street"]').val('');
                $('.street-box').find('.yqx-select-options').remove();
            }
        });

        //选择街道
        st.init({
            dom:'.js-street',
            url: BASE_PATH + '/dict/getSubRegionByPid',
            params:function (){
                return {
                    p_id: district
                }
            },
            showKey: "id",
            showValue: "regionName",
            callback:function(showKey){
                $('input[name="street"]').val(showKey);
            }
        });
        var map = setMap();

        upload.init({
            url: BASE_PATH + '/index/oss/crm_upload_image',
            dom: '.js-file',
            callback: function (result) {
                var parent = $(this).parent();
                var type = parent.data('type');
                var html = '<div class="picture-box"><span class="close-i js-close"></span><img src="">';

                if(result.success && result.data) {
                    if (type == 's') {
                        parent.addClass('hide');
                        parent.find('input[name]').val(result.data.original);
                        html = '<div class="picture-box"><div class="img-box"><span class="close-i js-close"></span><img src=""></div></div>';
                    } else {
                        html = '<div class="picture-box">\
                        <div class="img-box"><span class="close-i js-close" data-target="1"></span><img src=""><div class="img_sort">\
                            <span class="left_sort js-img-sort" data-sort="left"></span>\
                            <span class="right_sort js-img-sort" data-sort="right"></span>\
                        </div></div>\
                        <div class="form-item"><input class="yqx-input yqx-input-small" placeholder="请输入名称"></div>\
                        ';
                    }

                    parent.before(
                        $(html)
                            .find('img').attr('src', result.data.original)
                            .end()
                    );
                }
            }
        });
        $('.js-save').click(function () {
            if(!ck.check()) {
                return;
            }

            var data = {
                shop: formData.get('#shop', true),
                customerJoinAudit: formData.get('#crmShop', true),
                isCrm:_isCrm
            };

            data.customerJoinAudit.customerFilePathList = getCustomerFile();

            $.ajax({
                type: 'post',
                url: BASE_PATH + '/shop/setting/save',
                data: JSON.stringify(data),
                dataType:'json',
                contentType: 'application/json',
                success: function (json) {
                    if(json.success) {
                        dg.success('保存成功',function(){
                            window.location.reload();
                        });
                    } else {
                        dg.fail(json.message || '保存失败');
                    }
                }
            });
        });

        $(document).on('click','.js-goBack',function(){
            util.goBack();
        });

        $('.js-file').click(function (e) {
            e.stopPropagation();
        });

        $('.js-img-add').click(function () {
            var type = $(this).data('type');

            if(type === 's'
                && $(this).parent().find('.picture-box').length > 1
            ) {
                dg.warn('仅能添加一张图片');
                return;
            }
            $(this).find('input[type="file"]').click();
        });

        doc.on('click','.js-issue',function(){
            if($(this).is(':checked')){
                _isCrm = true;
                $('.form-box2').show();

                if(_point) {
                    setTimeout(function () {
                        map.setCenter(_point);
                        _point = null;
                    }, 300);
                }
                loadBrand();
            }else{
                _isCrm = false;
                $('.form-box2').hide();
            }
        })
            .on('click.img', '.js-close', function () {
                var box = $(this).parents('.picture-box');
                $(this).parents('.show-grid')
                    .find('.js-img-add').removeClass('hide');

                if($(this).data('target') == 1 && box.data('id')) {
                    removeImg(box.data('id'), function (json) {
                        if(json.success) {
                            box.remove();
                        } else {
                            dg.fail(json.message || '删除失败');
                        }
                    });
                } else {
                    // 删除 input[hidden] value
                    box.parents('.show-grid').find('input[name="saImg"]').val('');

                    // 接待照片
                    box.remove();
                }
            })
            .on('click', '.js-img-sort', function () {
                var direction = $(this).data('sort');
                var ele = $(this).parents('.picture-box');
                var t;

                if(direction == 'right') {
                    t = ele.next('.picture-box').eq(0);
                    if(t.length) {
                        t.after(ele);
                    }
                } else {
                    t = ele.prev('.picture-box').eq(0);
                    if(t.length) {
                        t.before(ele);
                    }
                }
            })
            .on('click','.edit-bank', function () {
                location.href = BASE_PATH + "/shop/setting/finance/finance-detail-shop";
            });

        function getCustomerFile() {
            var ret = [];

            $('.customer-file-box').find('.picture-box').each(function (i) {
                ret.push({
                    orderIdx: i,
                    imgUrl: $(this).find('img').attr('src'),
                    remarks: $(this).find('input').val(),
                    id: $(this).data('id')
                });
            });

            return ret;
        }

        function setMap(){
            var data = {
                longitude:$('.js-longitude').val(),
                latitude:$('.js-latitude').val()
            };
            var map = new BMap.Map("map");         // 创建地图实例
            var point = new BMap.Point(data.longitude, data.latitude);  // 创建点坐标
            var marker = new BMap.Marker(point);
            var geoCon = new BMap.GeolocationControl();
            map.addControl(geoCon);

            // 启用地图拖拽
            map.enableDragging();
            map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM}));
            // 启用滚轮放大缩小
            map.enableScrollWheelZoom();
            // 缩放级别：4，国
            map.setMinZoom(4);

            if(!data.longitude) {
                new BMap.Geolocation().getCurrentPosition(function (data) {
                    $('.js-longitude').val(data.point.lng);
                    $('.js-latitude').val(data.point.lat);
                    marker = new BMap.Marker(data.point);

                    _point = data.point;
                    map.centerAndZoom(data.point, 15);                 // 初始化地图，设置中心点坐标和地图级别
                    map.addOverlay(marker);
                })
            } else {
                _point = point;
                map.centerAndZoom(point, 15);                 // 初始化地图，设置中心点坐标和地图级别
                map.addOverlay(marker);
            }

            map.addEventListener("click", function(e){
                $("input[name='longitude']").val(e.point.lng);
                $("input[name='latitude']").val(e.point.lat);

                map.removeOverlay(marker);

                marker = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat));        // 创建标注
                map.addOverlay(marker);
                marker.addEventListener("dragend", function(e){
                    $("input[name='longitude']").val(e.point.lng);
                    $("input[name='latitude']").val(e.point.lat);
                });
            });

            return map;
        }

        function removeImg(id, fn) {
            $.get(BASE_PATH + '/shop/setting/app/img-delete', {
                id: id
            }, fn)
        }

        /****专修品牌 start*****/
        //渲染专修品牌
        function loadBrand() {
            $.ajax({
                type: 'POST',
                url: BASE_PATH + '/shop/car_category/brand_letter',
                success: function (result) {
                    if (result.success != true) {
                        taoqi.error(result.errorMsg);
                        return false;
                    }
                    else {
                        var templateHtml = art('brandContentTpl', {'templateData': result.data});
                        $('#majorCarBrand').html(templateHtml);
                    }
                }
            });
        }

        //专修品牌
        doc.on("change", "#majorCarBrand", function () {
            var text = $("option:selected", $(this)).text();
            var id = $(this).val();

            if (id == "") {
                return;
            }

            var parent = $(this).parent();
            var majorCarBrandItem = $(".majorCarBrand_item", parent);
            var size = majorCarBrandItem.size();
            if (size >= 5) {
                dg.warn("最多只能添加5个品牌");
                return;
            }
            var bool = true;
            majorCarBrandItem.each(function () {
                var _id = $(this).data("id");
                if (id == _id) {
                    dg.warn("请勿重复添加品牌");
                    bool = false;
                    return;
                }
            });
            if (!bool) return;

            $(".field_box", parent).append(getCarBrandHtml(id, text));
            if (text != "") {
                writeStr();
            }
        });

        //删除专修品牌
        doc.on("click", ".majorCarBrand_item i", function () {
            var parent = $(this).parent();
            parent.fadeOut(function () {
                parent.remove();
                writeStr();
            });
        });

        var getCarBrandHtml = function (id, text) {
            var html = '<div class="majorCarBrand_item" data-id="' + id + '">' + text + '<i></i></div>';
            return html;
        }

        //拼接字符串
        var writeStr = function () {
            var majorCarBrand = $("input[name='majorCarBrand']");
            var str = "";
            $(".majorCarBrand_item").each(function () {
                var id = $(this).data("id");
                var text = $(this).text();
                str += id + ":" + text + ";";
            });
            majorCarBrand.val(str);
        }

        //专修品牌的回填。
        var majorCarBrand = $.trim($("input[name='majorCarBrand']").val());
        // majorCarBrand = "8:安驰;14:保斐利;7:阿斯顿马丁;";
        if (majorCarBrand && majorCarBrand != "null") {
            var arr = majorCarBrand.split(/;/g);
            var html = "";
            var fieldBox = $("#majorCarBrand").parent().find('.field_box').eq(0);
            for (var i = 0; i < arr.length; i++) {
                if (arr[i] == "") {
                    continue;
                }
                var temp = arr[i].split(/:/g);
                html += getCarBrandHtml(temp[0], temp[1])
            }
            fieldBox.append(html);
        }
        /****专修品牌 end*****/

    }); // seajs
});