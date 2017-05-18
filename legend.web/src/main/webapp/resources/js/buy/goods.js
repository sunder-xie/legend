/**
 * Created by Sky on 2015/7/1.
 * version: 1.0
 */

/* 数量加、减、输入 */
/*
*
* class命名(默认)：
* ipt1: input输入框
* btn1: 加
* btn2: 减
* total_number: total总数
* amount: amount总金额
* reduce_price: 减免的金额
*
* data全部写在input输入框中
* price: 价格
* min: 最小值
* max： 最大值
* alreadybuynum： 已经购买的量
*
* */
function operate() {

    var $input, $add, $reduce, $total, $amount,  $reduce_price, selectors, callback, oldNum = 0;

    if(arguments.length > 0) {
        if( isFunction(arguments[0]) ) {
            callback = arguments[0];
        } else {
            selectors = arguments[0];
            callback = arguments[1];
        }
    }
    if( isObject(selectors) ) {
        $input = $(selectors.input || ".J-number");
        $add = $(selectors.add || ".add");
        $reduce = $(selectors.reduce || ".minus");
        $total = $(selectors.total || ".tixin");
        $amount = $(selectors.amount || ".amount");
        $reduce_price = $(selectors.reduce || ".reduce");
    } else {
        $input = $(".J-number");
        $add = $(".add");
        $reduce = $(".minus");
        $total = $(".tixin");
        $amount = $(".amount");
        $reduce_price = $(".reduce");
    }

    $add.on("click", function() {
        var $n = $(this).prev(),
            num = Number($n.val());
        num++;
        $n.val( compare($n, num) );
        calculate();
    });

    $reduce.on("click", function() {

        var $n = $(this).next(),
            num = Number($n.val());
        num--;
        $n.val( compare($n, num) );
        calculate();
    });

    $input.on("focus", function() {
       oldNum = $(this).val();
    });

    $input.on("input", function() {

        var $this = $(this),
            num = Number($this.val()),
            reg = /^\d+$/;
        if(!reg.test(num)) {
            num = oldNum;
            $this.val(num);
        } else {
            //num = num + 0; 数字回0以及清除前置0
            num = compare($this, num + 0);
            oldNum = num;
            $this.val(num);
            calculate();
        }
    });

    function compare($n, num) {

        var min = $n.data("min") || 0,
            max = $n.data("max") || 9999,
            bought = $n.data("bought") || 0,
            remain = $n.data("remain"),
            max = $n.data("max"),
            rest = max - bought;
        if(rest <= 0){
            rest = 0;
        }
        if(remain === ""){
            remain = max;
        }
        max = (!isNaN(rest) && rest < remain) ? rest : remain;
        if(num < min) {
            num = min;
        } else if(num > max) {
            num = max;
        }
        return num;
    }

    function calculate() { //总计

        var amount = 0, total = 0, reduce_price = 0;
        $input.each(function() {

            var $this = $(this),
                num = Number($this.val()),
                price = $this.data("price") || 0,
                orlprice = $this.data('orlprice') || 0;
            total += num;
            amount += num * price;
            reduce_price += num * orlprice;
        });
        if( isFunction(callback) ) {
            callback($input);
        }
        $total.text(total);
        $amount.text(amount.toFixed(2));
        $reduce_price.text((reduce_price - amount).toFixed(2));
        if (total > 0) {
            $total.text(total).show();
        } else {
            $total.text(0).hide();
        }
    }
}

function isObject(o) {
    return !!(o && typeof o == "object");
}

function isFunction(o) {
    return Object.prototype.toString.apply(o) === "[object Function]";
}
