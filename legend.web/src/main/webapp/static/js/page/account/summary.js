$(function () {
    var _baseUrl = BASE_PATH + '/account';

    seajs.use(['select', 'dialog', 'table', 'formData', 'art'],
        function(select, dialog, table, form, art) {
            var _table = {
                member: {},
                combo: {}
            };

            art.helper('Date', Date);

            dialog.titleInit();
            select.init({
                pleaseSelect: true,
                dom: '.js-card-type',
                canInput: true,
                url: _baseUrl + '/card_info_pair/list'
            });

            select.init({
                pleaseSelect: true,
                dom: '.js-combo-type',
                url: _baseUrl + '/combo_info_pair/list',
                canInput: true,
                callback: function () {
                    $('.js-combo-service').val('')
                        .next('input').val('')
                        .end().parent()
                        .find('.yqx-select-options').remove();
                }
            });

            select.init({
                pleaseSelect: true,
                canInput: true,
                dom: '.js-combo-service',
                params: function () {
                    return {
                        comboId: $('#comboForm input[name="comboInfoId"]').val()
                    }
                },
                url: _baseUrl + '/combo_service_pair/list'
            });

            ['member', 'combo'].forEach(function (e) {
                _table[e].send = table.init({
                    url: _baseUrl + '/' + e + '/page',
                    pageid: e + 'Page',
                    fillid: e + 'Fill',
                    formid: e + 'Form',
                    tplid : e + 'Tpl',
                    isfirstfill: !(e === 'combo')
                });

                if(e !== 'combo') {
                    getSummary(e);
                }
            });

            // 导出
            // 密码登录导出
            exportSecurity.tip({'title':'导出报表信息'});
            exportSecurity.confirm({
                dom: '.js-excel',
                title: '客户管理-卡券汇总',
                callback: function(json){
                    var desc = $(this).data('desc');
                    var data = form.get('#' + desc + 'Form');
                    var url = _baseUrl + '/' + desc + '/export';

                    $(document.body).append(
                        $('<iframe>').attr('src', url + '?' + $.param(data))
                    );
                }
            });

            // 排序
            $('.js-sort').click(function () {
                var data = $(this).data();
                var form = $(this).parents('.aside-main').find('.form');
                var status, direction;

                // 判断状态
                switch(data.status) {
                    case 'up':
                        status = 'down';
                        direction = 'DESC';
                        break;
                    case 'down':
                        status = '';
                        direction = '';
                        break;
                    default:
                        status = 'up';
                        direction = 'ASC';
                }
                // 重置其他排序的状态
                $(this).parents('tr').find('.sort-i').removeClass('up down')
                    .parent().data('status', '');

                // 设置目前排序的状态
                $(this).find('.sort-i').toggleClass('up', status == 'up')
                    .toggleClass('down', status == 'down')
                    .end().data('status', status);

                form.find('input[name="sort"]').val(status ? data.key + ',' + direction : '');

                form.find('.js-search-btn').trigger('click.table');
            });

            $('.js-tab').click(function () {
                var selector = $(this).data('target');
                var desc = $(this).data('desc');

                $('.current-tab').toggleClass('current-tab', false);
                $('.list-tab[data-desc="' + desc + '"]').toggleClass('current-tab', true);

                $('.current-main').toggleClass('current-main', true)
                    .toggleClass('hide', true);

                $(selector).toggleClass('current-main', true)
                    .toggleClass('hide', false);

                if (desc == 'combo' && _table.combo.state !== false) {
                    _table.combo.send(1);
                    getSummary('combo');
                    _table.combo.state = false;
                }
            });

            $('.js-search-btn').click(getSummary);
            $('.js-reset').click(function () {
                $(this).parents('.form')
                    .find('input').val('')
                    .end().find('.js-search-btn').click();
            });

            function getSummary(desc) {
                desc = typeof desc == 'string' ? desc : $(this).data('desc');

                $.get(_baseUrl + '/' + desc + '/summary',
                    form.get($(this).parents('.form')),
                    function (json) {
                        if(json.success && json.data) {
                            for(var i in json.data) {
                                $('.' + i).text(json.data[i] ? json.data[i].priceFormat() : 0);
                            }
                        } else {
                            dialog.fail(json.errorMsg || '获取总计失败');
                        }
                    });
            }
        }); //seajs end
});