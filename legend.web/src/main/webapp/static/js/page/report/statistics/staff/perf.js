/**
 *
 * Created by ende on 2016/10/9.
 */
$(function () {
    var isYBD = $('#isYBD').val() == 'true';
    // 规则详情的定时器
    var _detailTimer = null;
    // 提成方式
    var _method = {};

    if(util.getPara('hasBack') == 'true') {
        addBackBtn();
    }
    // 视图的切换
    // 包括 tab 切换和查看详情\设置
    function View(prev, next) {
        this.prev = prev ? [prev] : [];
        this.current = next;
    }

    View.prototype.set = function (next, padClass, preClass) {
        var len = this.prev.length;
        if(next == this.current) {
            return false;
        }
        if(len >= 2) {
            this.prev.shift();
        }
        this.prev.push(this.current);
        this.current = next;

        len = this.prev.length;

        $(this.prev[len - 1]).addClass('hide').removeClass(preClass);
        $(next).removeClass('hide').addClass(padClass);

        return true;
    };

    // 返回，主要用于查看详情、查看工单明细的返回
    View.prototype.back = function () {
        var prev = this.prev.pop();

        if(prev) {
            $(prev).removeClass('hide');
            $(this.current).addClass('hide');

            this.current = prev;
        }
    };

    seajs.use([
        'ajax',
        'dialog',
        'table',
        'formData',
        'art',
        'check',
        'date'], function (ajax,
                           dialog,
                           table,
                           formData,
                           art,
                           check,
                           date) {
        // 当前 tab 的时间 'yyyy-MM'
        var month = $('.summary .js-date').val();
        var
            isFinishSetting = hasDefaultSetting(month),
            i, t, name;
        // 值为 css selector
        var view = new View(null, '.summary');
        var tableObj = {
            summary: {
                url: BASE_PATH + '/shop/report/staff/perf/collect',
                send: null
            },
            gather: {
                url: BASE_PATH + '/shop/report/gather/staff/perf/add_point_info'
            },
            repair: {
                url: BASE_PATH + '/shop/report/staff/perf/repair_collect'
            },
            sale: {
                url: BASE_PATH + '/shop/report/staff/perf/sale_collect'
            },
            service: {
                url: BASE_PATH + '/shop/report/staff/perf/sa_collect'
            },
            serviceOrderDetail: {
                url: BASE_PATH + '/shop/report/staff/perf/sa_order'
            },
            saleGoodsDetail: {
                url: BASE_PATH + '/shop/report/staff/perf/sale_goods'
            },
            saleOrderDetail: {
                url: BASE_PATH + '/shop/report/staff/perf/sale_order'
            },
            repairServiceDetail: {
                url: BASE_PATH + '/shop/report/staff/perf/repair_service'
            },
            repairOrderDetail: {
                url: BASE_PATH + '/shop/report/staff/perf/repair_order'
            }
        };
        var _last;
        // 加点提成详情
        var _gatherDetail = {};
        var tplName;

        check.init();
        dialog.titleInit();

        // 工单详细的总计
        // 获取汇总的数据来填充
        art.helper('getTotal', getTotal);
        art.helper('getPercentageMethod', function (desc) {
            return _method[desc];
        });

        if(isYBD) {
            tableObj.summary.url = BASE_PATH + '/shop/report/gather/staff/perf/collect_boss';
        }

        for(name in tableObj) {
            t = tableObj[name];
            // 详细页
            i = name.indexOf('Detail') > -1;

            tplName = i ? name + 'Tpl' : name + 'CollectTpl';
            if(name == 'saleGoodsDetail') {
                i = false;
                tplName = name + 'Tpl';
            }

            t.send = table.init({
                url: t.url,
                fillid: name + 'Fill',
                formid: name + 'Form',
                tplid: tplName,
                pageid: name + 'Page',
                ajax: {
                    type: i ? 'post' : 'get'
                },
                dataType: i ? 'json' : null,
                callback: setTheadPrefType.bind(null, '#' + name + 'Fill'),
                isfirstfill: false
            });
        }

        date.datePicker('.js-date', {
            dateFmt: 'yyyy-MM',
            maxDate: 'yyyy-MM'
        });

        // 配件 添加按钮
        addGoodsInit({
            dom: '.js-get-goods',
            callback: setTableData.bind(null, 'goods')
        });

        // 选择服务
        getService({
            dom: '.js-get-service',
            callback: setTableData.bind(null, 'service')
        });

        // 是否已经有默认设置了
        if(!isFinishSetting || util.getPara('setting') == 'true') {
            if(!isFinishSetting) {
                dialog.warn('请先设置提成规则');
            }

            $('.perf-content').addClass('hide');
            $('.setting-main').removeClass('hide');
            $('.setting-box.number-1').removeClass('hide').addClass('current-setting');
            if(util.getPara('hasBack') !== 'true') {
                $('.js-setting-back').hide();
            }
        } else {
            tableObj.summary.send(1);
        }

        $('.js-setting-back').click(function () {
            if(util.getPara('setting') == 'true') {
                location.href = BASE_PATH + '/marketing/gather/rule';
            } else if(util.getPara('hasBack') == 'true' && isFinishSetting == false) {
                util.goBack();
            } else {
                $('.navigator li').removeClass('complete active current')
                    .eq(0).addClass('current');

                $('.current-setting')
                    .removeClass('current-setting').addClass('hide');

                $('.setting-main').addClass('hide');
                $('.perf-content').removeClass('hide');
            }
        });

        // 业绩设置的下一步
        $('.setting-box .js-rule-next').on('click', function () {
            var target = $(this).data('target'),
                current = $(this).data('current'),
                box = $(this).parents('.setting-box');

            if(current) {
                if(!check.check(box)) {
                    return;
                }
            }

            // 不是最后一步的时候
            if (target) {
                $('.current-setting')
                    .removeClass('current-setting').addClass('hide');
                $(target).removeClass('hide').addClass('current-setting');

                if(target !== '.setting-box.setting-gather') {
                    $('.navigator li.active').removeClass('active')
                        .addClass('complete').append('<i class="angle"></i>');
                    $('.setting-main .navigator li.current')
                        .removeClass('current').addClass('active')
                        .next().addClass('current');
                }
            } else {
                if( submitSetting() ) {
                    if(util.getPara('setting') == 'true') {
                        location.href= BASE_PATH + '/marketing/gather/rule';
                    } else {
                        $('.js-setting-back').click();

                        isFinishSetting = true;
                        refreshCollectTable();

                        _last = null;
                    }
                }
            }
        });

        // 加点提成的完成设置
        $('.js-complete-gather').click(function () {
            var target = $(this).data('target'),
                current = $(this).data('current'),
                box = $(this).parents('.setting-box');

            if(current) {
                if(!check.check(box)) {
                    return;
                }
                submitSetting();
                if(util.getPara('setting') == 'true') {
                    location.href= BASE_PATH + '/marketing/gather/rule';
                } else {
                    $('.perf-content').removeClass('hide');
                    $('.setting-main').addClass('hide');
                    refreshCollectTable();
                }

            }
        });

        // 工单金额满...的 checkbox
        $('.js-order-rule').on('change', function () {
            $(this).next('i').toggleClass('must', this.checked);

            if(this.checked) {
                $(this).parents('li').find('.yqx-input')
                    .prop('readonly', false)
                    .attr('data-v-type', 'required | price');
            } else {
                $(this).parents('li').find('.yqx-input')
                    .prop('readonly', true)
                    .val('')
                    .removeAttr('data-v-type', 'required | price');
            }
        });

        // 规则详情的hover
        $('.js-rule-detail').hover(function () {
            var that = this, url;
            var box = $('.rule-detail-tips');

            box.remove();
            if(_last == month) {
                box.eq(0).appendTo( $(this) ).show();

                if(_detailTimer) {
                    clearTimeout(_detailTimer);
                    _detailTimer = null;
                }
                return;
            }

            if(isYBD) {
                url = BASE_PATH + '/shop/report/gather/staff/perf/get_all_config'
            }

            _last = month;
            $.get(url || BASE_PATH + '/shop/report/staff/perf/get_all_config',
                {
                    month: month
                },
                function (result) {
                    if(result.success) {
                        var html = art('ruleDetailTpl', {
                            data: convertConfig(result.data, month)
                        });

                        $(that).append(html);

                        var box = $(that).find('.rule-detail-tips');
                        var width = box[0].offsetWidth,
                            t;

                        if(width > 418) {
                            width = 418;
                        }

                        box.css({
                            'word-break': 'break-all',
                            width: width
                        });
                        t = -20 + width + box.offset().left - document.body.clientWidth;

                        if(t > 0) {
                            box.css({
                                right: t
                            });

                            box.find('.angle').css('right', -t);
                        }
                    } else {
                        dialog.fail(result.message || '获取当月绩效规则失败');
                    }
            });
        }, function () {
            _detailTimer = setTimeout(function () {
                $('.rule-detail-tips').hide();
            }, 300);
        });

        $('.tag-control').click(function () {
            $(this).parent().find('.selected')
                .removeClass('selected');

            $(this).addClass('selected');
        });

        // 重新设定规则
        $('.js-reset-setting').on('click', function () {
            var t;
            $('.perf-content').addClass('hide');
            $('.setting-main').removeClass('hide');

            if(isYBD) {
                t = $('.setting-main .setting-gather');
            } else {
                t = $('.setting-main .setting-service');
            }
            t.removeClass('hide')
                .addClass('current-setting');
        });

        // tab标签组
        var tabMap = {
            'summary': '管理报表—绩效管理绩效汇总',
            'repair': '管理报表—绩效管理维修业绩',
            'sale': '管理报表—绩效管理销售业绩',
            'service': '管理报表—绩效管理服务顾问业绩'
        };

        // tab 切换
        $('.js-tab-item').on('click', function () {
            var desc = $(this).data('desc'),
                $current = $('.tab-item.current-item');
            var target = $(this).data('target');

            exportSecurity["exportBrief"] = tabMap[desc] || "管理报表—绩效管理绩效汇总";

            if ($current[0] == this) {
                return;
            }

            $('.rule-detail-tips').hide();
            // 当前 tab 的时间
            // 因为详情页切换的时候较难获取
            month = $(target).find('.js-date').val();

            if (!isFinishSetting) {
                dialog.warn('请先完成业绩规则设置');
                return;
            }
            // tab 隐藏
            $current.removeClass('current-item');
            // tab 显示
            $(this).addClass('current-item');

            view.set(target);

            // 显示表格 & 获取数据
            if (tableObj[desc] && tableObj[desc].send) {
                tableObj[desc].send(1);
            }
        });

        $('.js-search-btn').click(function () {
            month = $(this).parents('.form').find('input.js-date').val();
            // 加点提成时
            if(isYBD && $('.current-item').data('desc') == 'gather') {
                hasDefaultSetting(month, true, function (json) {
                    $('.gather').find('.empty-table').toggleClass('hide', json.data)
                        .end().find('.yqx-table').toggleClass('hide', !json.data)
                        .end().find('.export-box').toggleClass('hide', !json.data);
                });
            }
        });

        // 查看工单明细
        $('.js-order-detail').on('click', function () {
            var target = this.dataset.target;
            var current = this.dataset.current,
                data = formData.get(current);

            data.target = target;
            data.empName = $(current).find('.empName').text();

            view.set(target);

            showDetail($(target), null, data);

            setQuickLook(view.quickLookData.map(function (e) {
                var t = $.extend({}, e);
                t.target = target;
                return t;
            }), target, data.desc);
        });

        // 返回
        $('.js-back').click(function () {
            view.back();
        });
        // 删除
        $('.yqx-table').on('click', '.js-del', function () {
            $(this).parents('tr').remove();
        })
        // 查看详情
        .on('click', '.js-see-detail', function () {
            var prev = this.dataset.current;
            var target = $( $(this).attr('data-target') );
            var date = $(prev).find('input[name="dateStr"]').val();
            var data = $(this).data();

            view.set( target);

            showDetail(target, date, this.dataset);
            view.quickLookData = setQuickLook( $(this).parents('.yqx-table'), target, data.desc, this.dataset);
        })
            // 选中效果，及校验切换
            .on('click', '.js-tag-control', function(e) {
                var tr = $(this).parents('tr'),
                    t = $(this).find('input[data-type]').data('type');

                if( $(this).hasClass('selected') ) {
                    return;
                }
                tr.find('.selected.tag-control')
                    .removeClass('selected')
                    .end().find('input[data-type]')
                    .prop('disabled', true)
                    // 清空数据
                    .val('');

                $(this).addClass('selected')
                    .find('input[data-type]')
                    .prop('disabled', false)
                    .attr('data-v-type', t)
                    .focus();
            });

        $(document).on('click', '.js-quick-look', function () {
            var data = this.dataset;
            showDetail($(data.target), month, data);
        })
            .on('click', '.js-gather-look', function () {
                var id = $(this).data('userId');

                if(!$(this).hasClass('selected')) {
                    $('.js-gather-look.selected').removeClass('selected');
                    $(this).addClass('selected');

                    $('.gather-detail')
                        .find('input[name="empId"]').val(id)
                        .end().find('input[name="userId"]').val(id);

                    refreshGatherDetail(
                        $('#gatherFill').find('.js-see-detail[data-user-id="' + id + '"]').data()
                    );
                }
            });
        // 导出
        // 密码登录导出
        exportSecurity.tip({'title':'导出报表信息'});
        exportSecurity.confirm({
            dom: '.js-excel',
            title: '管理报表—绩效管理绩效汇总',
            callback: function () {
                var desc = $(this).find('.icon-signout').data('desc');
                var id = $(this).parents('.detail-box')
                    .find('input[name=empId]').val();
                var isGather = false;
                var t;

                var url = BASE_PATH + '/shop/report/staff/perf/' + desc + '/export';

                if(isYBD) {
                    switch (desc) {
                        case 'collect':
                            isGather = true;
                            t = '/export_achievement_summary';
                            break;
                        case 'export_staff_commission':
                            isGather = true;
                            id = $('.gather-detail input[name=empId]').val();
                            break;
                        case 'export_commission_summary':
                            isGather = true;
                            break;

                    }

                    if(isGather) {
                        t = t || ('/' + desc);
                        url = BASE_PATH + '/shop/report/gather/staff/perf' + t
                            + '?monthStr=' + month;
                    }
                }

                excelExport(url, !isGather ? month : undefined, id);
            }
        });

        function setTableData(desc, data) {
            var box = $('#' + desc + 'SettingTable');
            var index = 0;
            var ids = {};

            box.find('.data-tr').each(function () {
                ids[$(this).data('id')] = true;
                index += 1;
            });

            if(ids[data.id]) {
                dialog.warn('服务/配件已添加，请不要重复添加');
                return;
            }

            var goodsHtml = art(desc + "Tpl", {json: [data], index: index});
            box.append(goodsHtml);
        }

        function showDetail($target, date, data) {
            var t = $.camelCase(data.target).replace('.', '');

            date = date || data.dateStr;

            $target.find('.month').text( new Date(date).getMonth() + 1 + '月' )
                .end().find('.empName').text(data.empName)
                // 设置 input[name]
                .end().find('input[name=empId]').val(data.empId)
                .end().find('input[name=dateStr]').val( date.length === 10 ? date.slice(0, -3) : date );

            if(data.desc === 'gather') {
                $target.find('input[name="userId"]').val(data.userId)
                    .end().find('input[name="empId"]').val(data.userId);

                refreshGatherDetail(data);
            } else {
                tableObj[t].send && tableObj[t].send(1);
            }
        }

        function refreshGatherDetail(data) {
            var t = 'newCustomer';

            if(!_gatherDetail.newCustomer) {
                _gatherDetail.newCustomer = table.init({
                    formid: 'gatherDetailForm2',
                    url: BASE_PATH + '/shop/report/gather/staff/perf/new_customer',
                    tplid: t + 'Tpl',
                    ajax: {
                        type: 'post'
                    },
                    dataType: 'json',
                    callback: setTheadPrefType.bind(null, '#' + t + 'Fill'),
                    fillid: t + 'Fill',
                    pageid: t + 'Page'
                });
            } else {
                _gatherDetail.newCustomer(1);
            }

            if(!_gatherDetail.saleStart) {
                t = 'saleStart';
                _gatherDetail.saleStart = table.init({
                    formid: 'gatherDetailForm',
                    url: BASE_PATH + '/shop/report/gather/staff/perf/sale_star',
                    tplid: t + 'Tpl',
                    fillid: t + 'Fill',
                    pageid: t + 'Page'
                });
            } else {
                _gatherDetail.saleStart(1);
            }

            if(!_gatherDetail.businessBelone) {
                t = 'businessBelone';
                _gatherDetail.businessBelone = table.init({
                    formid: 'gatherDetailForm2',
                    url: BASE_PATH + '/shop/report/gather/staff/perf/business_belone',
                    tplid: t + 'Tpl',
                    fillid: t + 'Fill',
                    ajax: {
                        type: 'post'
                    },
                    dataType: 'json',
                    callback: setTheadPrefType.bind(null, '#' + t + 'Fill'),
                    pageid: t + 'Page'
                });
            } else {
                _gatherDetail.businessBelone(1);
            }

            if(!_gatherDetail.pie) {
                _gatherDetail.pie = setGatherPieECharts(data);
            } else {
                setGatherPieECharts(data, _gatherDetail.pie)
            }

        }

        // 提交设置
        function submitSetting() {
            // 连接两份数据
            var url = BASE_PATH + '/shop/report/staff/perf/submit_config';
            var ret = true;
            var t = getAllSetting(isYBD);

            if(isYBD) {
                url = BASE_PATH + '/shop/report/gather/staff/perf/submit_config'
            }

            $.ajax({
                url: url,
                type: 'post',
                contentType: 'application/json',
                async: false,
                data: JSON.stringify(t),
                success: function (json) {
                    ret = json.success;
                    if(json.success) {
                        isFinishSetting = true;
                        dialog.success('配置保存成功');
                    } else {
                        dialog.fail(json.errorMsg || '保存失败');
                    }
                }
            });

            return ret;
        }

        function hasDefaultSetting(month, async, fn) {
            var ret = false, t = month;
            var url = BASE_PATH + '/shop/report/staff/perf/check_config';

            if(isYBD) {
                url = BASE_PATH + '/shop/report/gather/staff/perf/check_config'
            } else {
                t = null;
            }

            $.ajax({
                url: url,
                data: {
                    month: t
                },
                async: async || false,
                success: function (json) {
                    if( (ret = json.data) ) {
                        getAllConfig(month, isYBD);
                    }
                    if(fn && typeof fn == 'function') {
                        fn(json);
                    }
                }
            });

            return ret;
        }

        function getGatherSettingData(box) {
            var gather = {},
                data = {
                    gatherPerfConfigVO: gather
                },
                ret = {
                    performanceConfigVOs: [
                        data
                    ],
                    type: 1
                };

            data.percentageMethod = box.find('.js-percent-method.selected').data('value');

            $.extend(gather, formData.get(box));

            data.percentageRate = gather.percentageRate || 0;
            data.percentageType = 0;
            data.configType = 3;
            delete gather.percentageRate;

            // 销售之星：统计方式
            gather.salePercentageType = box.find('.js-reward-type.selected').data('value');
            gather.saleRewardCycle = box.find('.js-cycle.selected').data('value');

            gather.isContainWashCarOrder = box.find('.js-carwash').prop('checked') ? 0 : 1;
            gather.isUseOrderMinAmount = box.find('.js-order-rule').prop('checked') ? 1 : 0;

            return ret;
        }

        function getAllSetting(isYBD) {
            var ret = getSettingData('service', '.setting-service');

            ret = ret.concat( getSettingData('goods', '.setting-goods') );
            ret = ret.concat( getSettingData('sa-service', '.setting-sa-service') );

            if(isYBD) {
                var gatherData = getGatherSettingData($('.setting-box.setting-gather'));
                gatherData.performanceConfigVOs = gatherData.performanceConfigVOs.concat(ret);
                gatherData.type = 0;

                ret = gatherData;
            }

            return ret;
        }

        // 获取设置数据
        function getSettingData(desc, selector) {
            var ret = [],
                configType,
                $box = $(selector),
                defaultRate = $box.find('input[name=percentageRate]').eq(0).val() || 0;

            if(desc == 'service' ) {
                configType = 0;
            } else if(desc == 'goods') {
                configType = 1;
            } else if(desc == 'sa-service') {
                // 服务顾问
                configType = 2
            } else {
                return;
            }
            // 默认比例
            ret.push({
                percentageRate: defaultRate,
                percentageType: 0
            });

            if( $box.find('.js-percent-method.selected').length ) {
                ret[0].percentageMethod = $box.find('.js-percent-method.selected').data('value');
            }

            $box.find('.yqx-table .data-tr').each(function () {
                var data = formData.get( $(this) );

                // 按比例还是按单数
                if($(this).find('.js-by-rate.selected').length) {
                    // 按比例
                    data.percentageType = 1;
                    delete data.percentageAmount;
                } else {
                    data.percentageType = 2;
                    delete data.percentageRate;
                }

                ret.push(data);
            });

            ret.forEach(function (e) {
                e.configType = configType;
            });

            ret.percentageMethod = $box.find('.js-percent-method.selected').data('value');

            return ret;
        }

        // 导出
        function excelExport(url, dateStr, empId) {
            var iframe = $('<iframe class="hide"/>');
            var src = url + (dateStr ? '?dateStr=' + dateStr : '');

            if(empId !== undefined) {
                src += '&empId=' + empId;
            }
            iframe.attr('src', src)
                .appendTo(document.body);
        }

        // 获取快速查看的列表数据
        function getQuickLookData($table) {
            var ret = [];

            $table.find('.js-see-detail').each(function () {
                ret.push(
                    this.dataset
                );
            });

            return ret;
        }

        // 快速查看
        function setQuickLook($table, target, desc, curr) {
            // 传入的是 jquery 对象，还是数据
            var data = $table instanceof jQuery ? getQuickLookData($table) : $table;
            var tplId = desc === 'gather' ? 'gatherQuickLookTpl' : 'quickLookTpl';

            var html = art(tplId, {data: data});

            if(desc === 'gather') {
                $(target).find('.quick-look-box .look-list').html(html)
                    .find('li[data-user-id="' + curr.userId + '"]').addClass('selected');
            } else
                $(target).find('.quick-look .name-list').html(html);

            return data;
        }

        // 获取规则设置的数据并填充
        function getAllConfig(month, isYBD) {
            var url = isYBD ? BASE_PATH + '/shop/report/gather/staff/perf/get_all_config'
                : BASE_PATH + '/shop/report/staff/perf/get_all_config';

            $.ajax({
                url: url,
                data: {
                    month: month
                },
                // 不要loading
                global: false,
                success: function (result) {
                    var data = result.data;
                    var t;
                    if (result.success) {
                        t = convertGatherConfig(data);

                        setConfig('.setting-service', t.service, 'service');
                        setConfig('.setting-goods', t.goods, 'goods');
                        setConfig('.setting-sa-service', t.sa, 'sa');

                        if(t.gather) {
                            setGatherConfig($('.setting-gather'), t.gather);
                        }
                    } else {
                        dialog.fail(result.message);
                    }
                }
            });
        }

        // 填充加点提成的规则
        function setGatherConfig(box, data) {
            box
                .find('.js-percent-method.selected').removeClass('selected')
                .end()
                .find('.js-percent-method[data-value="' + data.percentageMethod + '"]').addClass('selected');

            box.find('input[name="percentageRate"]').val(data.percentageRate);

            for (var name in data.gatherPerfConfigVO) {
                t = data.gatherPerfConfigVO[name];

                if (name == 'isContainWashCarOrder') {
                    box.find('.js-carwash').prop('checked', t == 0);
                } else if (name == 'isUseOrderMinAmount') {
                    box.find('.js-order-rule').prop('checked', t ? true : false)
                        .trigger('change');
                } else if (name == 'salePercentageType') {
                    box.find('.js-reward-type.selected').removeClass('selected');
                    box.find('.js-reward-type[data-value="' + t + '"]').addClass('selected');
                } else if (name == 'saleRewardCycle') {
                    box.find('.js-cycle.selected').removeClass('selected');
                    box.find('.js-cycle[data-value="' + t + '"]').addClass('selected');
                } else {
                    box.find('input[name="' + name + '"]').val(t);
                }
            }
        }

        // 填充除加点提成以外的规则
        function setConfig(selector, data, desc) {
            var $box = $(selector);

            data = data.filter(function (e) {
                // 默认的比例
                if (e.percentageType == 0) {
                    $box.find('input[name=percentageRate]').eq(0)
                        .val(e.percentageRate);

                    $box
                        .find('.js-percent-method.selected').removeClass('selected')
                        .end()
                        .find('.js-percent-method[data-value="' + e.percentageMethod + '"]').addClass('selected');

                    _method[desc] = e.percentageMethod;
                    return false;
                }
                return true;
            });

            if (desc && $('#' + desc + 'Tpl').length) {
                // 和添加服务、添加配件共用模板
                var html = art(desc + 'Tpl', {json: data});

                $('#' + desc + 'SettingTable').append(html);
            }
        }
        // 返回的数据进行分类
        function convertGatherConfig(data) {
            var ret = {
                goods: [],
                service: [],
                sa: []
            };

            for(var i in data) {
                if(data[i].configType == 3) {
                    ret.gather = data[i];
                } else if(data[i].configType == 2) {
                    ret.sa.push( data[i] );
                } else if(data[i].configType == 1) {
                    ret.goods.push( data[i] );
                } else if(data[i].configType == 0) {
                    ret.service.push( data[i] );
                }
            }

            return ret;
        }

        // 转换数据，绩效规则详情用
        function convertConfig(data, month) {
            var ret = {
                month: month
            };
            var t;

            for(var i in data) {
                t = data[i];
                if(t.configType == 0) {
                    if(t.percentageType == 0) {
                        ret.servicePer = t.percentageRate;
                        ret.serviceMethod = t.percentageMethod;
                    } else {
                        if(!ret.serviceRules) {
                           ret.serviceRules = [];
                        }
                        ret.serviceRules.push({
                            name: t.relName,
                            value: t.percentageRate >= 0 && t.percentageRate !== null && !t.percentageAmount
                                ? t.percentageRate + '%'
                                : t.percentageAmount + '元/单'
                        });
                    }
                } else if(t.configType == 1) {
                    if(t.percentageType == 0) {
                        ret.goodsPer = t.percentageRate;
                        ret.goodsMethod = t.percentageMethod;
                    } else {
                        if(!ret.goodsRules) {
                            ret.goodsRules = [];
                        }
                        ret.goodsRules.push({
                            name: t.relName,
                            value: t.percentageRate && t.percentageRate !== null && !t.percentageAmount
                                ? t.percentageRate + '%'
                                : t.percentageAmount + '元/' + t.measureUnit
                        });
                    }
                } else if(t.configType == 2) {
                    ret.servicerPer = t.percentageRate;
                    ret.servicerMethod = t.percentageMethod;
                } else if(t.configType == 3) {
                    ret.gather = t;
                }
            }

            return ret;
        }

        // 加点提成饼图的数据
        function convertGatherPie(data) {
            var ret = {
                name: [
                    '业绩归属奖励',
                    '新客户到店奖励',
                    '销售之星奖励'
                ],
                arr: [
                    {value: data.businessBelone, name: '业绩归属奖励'},
                    {value: data.newCustomer, name: '新客户到店奖励'},
                    {value: data.saleStart, name: '销售之星奖励'}
                ]
            };

            ret.total = data.saleStar + data.newCustomer + data.businessBelone;

            return ret;
        }

        // 加点提成的饼图
        function setGatherPieECharts(data, chart) {
            var box = $('#gatherCount');

            if (!chart) {
                chart = echarts.init(box[0]);
            }

            box.parent()
                .find('.business i').html('&yen;' + data.businessBelone)
                .end().find('.customer i').html('&yen;' + data.newCustomer)
                .end().find('.star i').html('&yen;' + data.saleStart)
                .end().find('strong').html(
                 data.userName + '：<i class="money-font">&yen;' + (+data.saleStart).Jia(+data.newCustomer).Jia(+data.businessBelone)
                    +'</i>'
            );

            data = convertGatherPie(data);

            var option = {
                title: {
                    text: '加点提成',
                    left: 40,
                    top: 64,
                    textStyle: {
                        fontSize: 14
                    },
                    show: true
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                series: [
                    {
                        name: '加点提成',
                        type: 'pie',
                        center: [75, 75],
                        radius: [75, 50],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data: data.arr
                    }
                ],
                color: ['#607b0a', '#fa482c', '#222d49']
            };

            chart.setOption(option);
            return chart;
        }

        // 获取汇总的数据作为总计
        function getTotal(desc, empId) {
            var ret, box;

            switch(desc) {
                case 'repair':
                    box = $('#repairFill');
                    break;
                case 'sale':
                    box = $('#saleFill');
                    break;
                case 'sa':
                    box = $('#serviceFill');
                    break;
            }
            ret = box.find('.js-see-detail[data-emp-id="' + empId + '"]')
                .data();

            return ret || {};
        }

        // 显示毛利还是显示营业额/金额
        function setTheadPrefType(selector) {
            var count = 0, t, thead;
            var table = $(selector).parents('table');

            $(selector).find('input.perf-type').each(function () {
                var type;
                if((type = this.value)) {
                    var index = $(this).parents('tr').find('td').index( $(this).parent() );
                    var t, text;

                    var thead = table.find('thead');

                    if(thead.find('th').eq(index).length == 0) {
                        t = table.find('tbody tr').eq(0).find('td').eq(index);
                    } else {
                        t = table.find('th').eq(index);
                    }

                    text = t.text();
                    if(type == '毛利') {
                        t.data('origin', text);
                        text = text
                            .replace('金额', type)
                            .replace('营业额', type);
                    } else {
                        text = t.data('origin');
                    }

                    if(text) {
                        t.text(text);
                    }
                }

                count ++;
            });

            if(!count) {
                table.find('th')
                    .add( table.find('tbody tr').eq(0).find('td') )
                    .each(function () {
                    var t = $(this).data('origin');

                    if(t) {
                        $(this).text(t);
                    }
                });
            }
        }

        function refreshCollectTable() {
            var desc = $('.current-item').data('desc');
            // 重新发送请求，刷新汇总数据
            // 显示表格 & 获取数据
            if (tableObj[desc] && tableObj[desc].send) {
                tableObj[desc].send(1);
            }
        }
    }); // seajs

    function addBackBtn() {
        var html = '<button class="yqx-btn-1 yqx-btn js-go-back fr" style="margin-top: -7px;">返回</button>';

        $('.perf-content .headline').css('margin-bottom', 20).append(html);

        $('.js-go-back').click(util.goBack);
    }
});
