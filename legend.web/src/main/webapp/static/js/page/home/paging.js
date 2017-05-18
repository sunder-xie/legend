Paging.prototype.next = function (next) {
    var t = next;
    if (next == this.current || next <= 0 || !next) {
        return;
    }
    if (t > this.total) {
        t = this.total;
    }

    this.update(t);
    this.updateCurrent(t);
};

Paging.prototype.updateCurrent = function (next) {
    $(self.dom).find('.page-current')
        .removeClass('page-current');

    $(self.dom).find('.yqx-page-num').each(function () {
        var t = $(this).text();
        if (t == next) {
            $(this).addClass('page-current');
        }
    })
};

Paging.prototype.update = function (num) {
    var t = num > this.total ? this.total : num;

    this.send && this.send(t);
    this.dataUpdate(t);
    this.render();
};

Paging.prototype.dataUpdate = function (next) {
    var i = 1;
    var t = next / this.show, num;
    var a = Math.ceil(this.current / this.show);
    var jump = false, data = this.data;

    if (this.show > this.total) {
        this.show = this.total;
    }
    for (i in data) {
        if (next == data[i]) {
            jump = true;
            break;
        }
    }

    if (jump) {
        this.current = next;
        return;
    }

    i = 1;
    if (t >= a) {
        num = (t>>0) * this.show + 1;
        i = 1;
        while (i <= this.show) {
            if(num <= this.total)
                data[i++] = num++;
            else
                delete data[i++];
        }
    } else {
        while (i <= this.show) {
            data[i] = i++;
        }
    }
    this.current = next;
};

Paging.prototype.render = function () {
    var str = this.html.head + this.html.prev;
    var i = 1, t;

    while (i <= this.show) {
        t = this.data[i];
        if (t && t > 0) {
            if (t == this.current) {
                str += '<div class="page-current yqx-page-num js-page">' + t + '</div>';
            } else
                str += '<div class="yqx-page-num js-page">' + t + '</div>';
        }
        i++;
    }

    str += this.html.next + this.html.foot;
    $(this.dom).html(str);

    this.updateButtonStatus(this.current);
};

Paging.prototype.updateButtonStatus = function (current) {
    var prev = $('.yqx-page .page-prev'),
        next = $('.yqx-page .page-next'),
        go   = $('.yqx-page .page-go');
    if(current == 1) {
        prev.addClass('disabled');
    }
    if(current == this.total) {
        next.addClass('disabled');
    }
    if (this.total > this.show && current < this.total) {
        next.removeClass('disabled');
    }
    if (this.total > this.show && current > 1) {
        prev.removeClass('disabled');
    }

    if(this.total == 1) {
        go.addClass('disabled');
    } else {
        go.removeClass('disabled');
    }
};

Paging.prototype.html = {
    head: '<div class="js-page-head page-prev"><i class="fa icon-double-angle-left"></i></div>',
    prev: '<div class="js-page-prev page-prev"><i class="fa icon-angle-left"></i></div>',
    next: '<div class="js-page-next page-next"><i class="fa icon-angle-right"></i></div>',
    foot: '<div class="js-page-foot page-next"><i class="fa icon-double-angle-right"></i></div>'
    + '<div class="page-jump"><div>跳转至</div><div class="page-input"><input class="js-page-input"></div>'
    + '<div class="page-go js-page-go">GO</div>'
};

Paging.prototype.domInit = function () {
    var $doc = $(document);
    var self = this;

    $doc.off('click.page')
        .on('click.page', '.js-page-head', function () {
            if ($(this).hasClass('disabled')) {
                return;
            }
            self.next(1);
        });

    $doc.on('click.page', '.js-page-prev', function () {
        if ($(this).hasClass('disabled')) {
            return;
        }
        self.next(self.current - 1);
    });

    $doc.on('click.page', '.js-page-next', function () {
        if ($(this).hasClass('disabled')) {
            return;
        }
        self.next(self.current + 1);
    });

    $doc.on('click.page', '.js-page-foot', function () {
        if ($(this).hasClass('disabled')) {
            return;
        }
        self.next(self.total);
    });

    $doc.on('click.page', '.js-page-foot', function () {
        if ($(this).hasClass('disabled')) {
            return;
        }
        self.next(self.total);
    });

    $doc.on('click.page', '.js-page-go', function () {
        self.next($('.yqx-page .js-page-input').val());
    });

    $doc.on('click.page', '.js-page', function (e) {
        if ($(this).hasClass('disabled')) {
            return;
        }
        self.next($(this).text());

        e.stopImmediatePropagation();
    });
};


function Paging(total, fn, show, dom) {
    var self = this;
    this.total = total <= 0 ? 1 : total;
    this.show = show || 4;

    this.current = 1;
    this.data = {};
    this.send = typeof fn == 'function' ? fn : null;
    this.dom = dom;

    this.dataUpdate(1);
    this.render();

    this.domInit();

    return this;
}

