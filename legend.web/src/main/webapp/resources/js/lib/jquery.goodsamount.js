/*
 * author: by weilu
 * function: 商品数量限制
 * date: 2015-07-08
 *
 *
 */
;(function($){
    $.fn.goodsamount  = function(option){
        var opts = $.extend({}, $.fn.goodsamount.defaults  , option);
        //iterate and reformat each matched element  
        return this.each(function(){
            $this = $(this);
            var $minusBtn = $this.find( '.'+opts.minusBtnClass );
            if( !$minusBtn ){
                console.error("cannot find minus button!");
            }
            var $addBtn = $this.find( '.'+opts.addBtnClass );
            if( !$addBtn ){
                console.error("cannot find add button!");
            }
            var inputerSelector = 'input' + ( opts.inputerClass ? '.'+opts.inputerClass : '' );
            $inputer = $this.find( inputerSelector );
            if( !$inputer ){
                console.error("cannot find inputer !");
            }
            if( opts.maxValue < opts.minValue ){
                opts.minValue = opts.maxValue;
            }
            $this.get(0).opts = opts;
            $.fn.goodsamount.updateValue.call( $this , Number($this.find("input").val()));
            $this.addClass('goodsamount');
            $this.on('click' , '.'+opts.addBtnClass , $.fn.goodsamount.addminusClicked);
            $this.on('click' , '.'+opts.minusBtnClass , $.fn.goodsamount.addminusClicked);
            $this.on('input' , inputerSelector , $.fn.goodsamount.valueInputed);
        });
    };
    //default config
    $.fn.goodsamount.defaults = {
        addBtnClass   :'add',
        minusBtnClass:'minus',
        btnDisableClass : 'gray',
        maxValue : 999999,
        minValue : 0,
        initValue : 0,
        maxAmount: 999999999,
        flag: false,
        inputerClass : null,
        valueChangedCallback:null
    };
    $.fn.goodsamount.addminusClicked = function(e){
        var $btn = $(this);
        var nextValue = 0 ;
        var $that = $btn.closest('.goodsamount');
        var opts = $that.get(0).opts;
        var curValue = $.fn.goodsamount.getCurrentValue.call($that);
        var $addBtn = $that.find( '.'+opts.addBtnClass );
        if($btn.hasClass( opts.addBtnClass) ){
            var $input = $(this).prev();
            //$(".tqnum input").each(function(i){
            //    if(opts.maxAmount < 999999999){
            //       if((Number($("#amountPrice").val()) + Number($(this).data("min")) * Number($(this).data('price'))) > opts.maxAmount){
            //           $(this).next().addClass(opts.btnDisableClass);
            //       }
            //    }
            //});
            nextValue = curValue + 1 ;
            var $de = $input.prev();
            $input.show();
            $de.css('display', 'inline-block');
            if(Number($input.val()) < 0){
                $input.val(0);
            }
            if(nextValue < opts.minValue){
                nextValue = opts.minValue;
            }
            if($(this).hasClass(opts.btnDisableClass)){
                return false;
            }
        }else if($btn.hasClass( opts.minusBtnClass) ){
            //$(".tqnum input").each(function(){
            //    if(opts.maxAmount < 999999999){
            //        console.log(Number($("#amountPrice").val()), (Number($(this).data("min")) * Number($(this).data('price'))), (Number($("#amountPrice").val()) + Number($(this).data("min")) * Number($(this).data('price'))) > opts.maxAmount);
            //
            //        if((Number($("#amountPrice").val()) + Number($(this).data("min")) * Number($(this).data('price'))) <= opts.maxAmount){
            //            $(this).next().removeClass(opts.btnDisableClass);
            //        }
            //    }
            //});
            nextValue = curValue - 1;
            var $input = $(this).next();
            if(nextValue < opts.minValue){
                nextValue = opts.initValue;
            }
        }
        $.fn.goodsamount.updateValue.call($that,nextValue);
        nextValue > 0 ? $inputer.addClass("onn") : $inputer.removeClass("onn");
    };
    $.fn.goodsamount.valueInputed= function(e){
        var $inputer = $(this);
        var value = parseInt($inputer.val());
        var $that = $inputer.closest('.goodsamount');
        var opts = $that.get(0).opts;
        var curValue = $.fn.goodsamount.getCurrentValue.call($that);
        if( value < 0 || isNaN(value)){
            value = 0;
        }
        if( value < opts.minValue){
            value = curValue < value ? opts.minValue : 0;
        }
        value > 0 ? $inputer.addClass("onn") : $inputer.removeClass("onn");
        $.fn.goodsamount.updateValue.call( $that , value);
    };

    $.fn.goodsamount.getCurrentValue = function(){
        var $that = this;
        var value = $that.get(0).value ;
        return value;
    };

    $.fn.goodsamount.updateValue = function(value){
        var $that = this;
        var opts = $that.get(0).opts;
        if( value === null ){
            return;
        }
        if( value === $this.value ){
            return;
        }
        var $minusBtn = $that.find( '.'+opts.minusBtnClass );
        var $addBtn = $that.find( '.'+opts.addBtnClass );
        $minusBtn.removeClass(opts.btnDisableClass);
        $addBtn.removeClass(opts.btnDisableClass);
        if( value >= opts.maxValue ){
            value = opts.maxValue;
            $addBtn.addClass( opts.btnDisableClass );
        }else if ( value < opts.minValue ){
            $minusBtn.addClass( opts.btnDisableClass );
        }
        if( opts.maxValue == 0 && opts.minValue == 0){
            $minusBtn.addClass(opts.btnDisableClass);
            $addBtn.addClass(opts.btnDisableClass);
        }
        $that.get(0).prevValue = $that.get(0).value;
        $that.get(0).value = value;
        $inputer = $that.find( 'input' + ( opts.inputerClass ? '.'+ opts.inputerClass : '' ) );
        $inputer.val(value);
        if( $that.get(0).prevValue !== null
            && opts.valueChangedCallback ){
            opts.valueChangedCallback.call( $that.get(0) , value );
        }
    };
})( window.jQuery );