$(function () {
    // 查询重置
    $('.js-reset').on('click', function () {
        var inputs = $('.content input');

        inputs.each(function () {
            $(this).val('');
        });

        $(this).parents('.content')
            .find('.js-search-btn').click();
    });

    // 列表字段设置选择框 显示
    $('.js-list-option').click(function () {
        var target = $(this).parents('.content')
            .find($(this).data('target'));

        if (target.hasClass('hide')) {
            plusTominus(this);
            target.removeClass('hide');
        } else {
            minusToplus(this);
            target.addClass('hide');
        }
    });

    // '+' -> '-'
    function plusTominus(e) {
        $(e).find('.icon-plus')
            .removeClass('icon-plus')
            .addClass('icon-minus');
    }

    // '-' -> '+'
    function minusToplus(e) {
        $(e).find('.icon-minus')
            .removeClass('icon-minus')
            .addClass('icon-plus');
    }
});

// 列表字段设置
// desc: array,标示
// prefix: string,localStorage 存储的字段前缀
function TableDisplay(desc, prefix) {
    var data = this.data = {};

    this.prefix = prefix || '';

    this.getData(data, [].concat(desc));

    return this;
}

// 设置单个的
TableDisplay.prototype.setData = function (desc) {
    localStorage[this.prefix + desc] = JSON.stringify(this.data[ desc ]);
};

// desc: array
TableDisplay.prototype.getData = function (data, desc) {
    for (var i in desc) {
        data[desc[i]] = JSON.parse(
            localStorage[ this.prefix + desc[i]] || '{}'
        );
    }
};

// 列表字段设置初始化
// 搜索的是 ".`desc`-content .dropdown"
// 需要放在 table.init callback 里
TableDisplay.prototype.tableDisplayInit = function (desc, fn) {
    var i, data;
    // desc 表示的是 localStorage 的 key
    // 不包括前缀
    for(var t in desc) {
        data = this.data[ desc[ t ] ];
        for (i in data) {
            // 字段不显示的时候
            if (!data[i]) {
                fn && fn.call(null, i, false);

                $('.' + desc[t] + '-content .dropdown')
                    .find('input[data-ref="' + i + '"]')
                    .removeAttr('checked');
                $('.' + desc[t] + '-content table')
                    .find('.' + i).addClass('hide');
            }
        }
    }
};

// 单选框的点击
TableDisplay.prototype.checkBoxEvent = function (t, event) {
    var ele = event.target;
    var desc = $(ele).parents('.dropdown-menu').data('desc'),
        field = $(ele).data('ref');
    var table = $('.' + desc + '-content').find('table');

    t.data[desc][field] = t.changeTableCol.call(ele, table);

    // 保存
    t.setData(desc);

    event.stopImmediatePropagation();
};

/* 显示隐藏对应的列 */
TableDisplay.prototype.changeTableCol = function (table) {
    var $this = $(this),
        ref = $this.data('ref'),
        $ref = table ? table.find('.' + ref) : $('.' + ref),
        checked = $this.prop('checked');

    checked ? $ref.removeClass('hide') : $ref.addClass('hide');
    return checked;
};

function getDateStr(AddDayCount) {
    var dd = new Date(),
        y, m, d;

    dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
    y = dd.getFullYear();
    m = dd.getMonth() + 1;//获取当前月份的日期
    d = dd.getDate();

    m = m < 10 ? "0" + m : m;
    d = d < 10 ? "0" + d : d;

    return y + "-" + m + "-" + d;
}
