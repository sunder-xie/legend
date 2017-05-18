//  
//create closure  
//  
//  
;(function($){
    $.fn.addminusinputer  = function(option){
        var opts = $.extend({}, $.fn.addminusinputer.defaults  , option);
        //iterate and reformat each matched element  
        return this.each(function(){
            $this = $(this);  
            var $minusBtn = $this.find( '.'+opts.minusBtnClass );
            if( !$minusBtn ){
                console.error("cannot find descrease button!");
            }
            var $addBtn = $this.find( '.'+opts.addBtnClass );
            if( !$addBtn ){
                console.error("cannot find increase button!");
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
            $.fn.addminusinputer.updateValue.call( $this , opts.initValue);
            $this.addClass('addminusinputer');

            $this.on('click' , '.'+opts.addBtnClass , $.fn.addminusinputer.addminusTaped);
            $this.on('click' , '.'+opts.minusBtnClass , $.fn.addminusinputer.addminusTaped);
            $this.on('input' , inputerSelector , $.fn.addminusinputer.valueInputed);
        });
    };
    //default config
    $.fn.addminusinputer.defaults = {
        addBtnClass   :'add',
        minusBtnClass:'minus',
        btnDisableClass : 'gray',
        maxValue : 999999,
        minValue : 0,
        initValue : 0,
        inputerClass : null,
        valueChangedCallback:null
    };
    $.fn.addminusinputer.addminusTaped = function(){
        var $btn = $(this);
        var nextValue = 0 ;
        var $that = $btn.closest('.addminusinputer');
        var opts = $that.get(0).opts;
        var curValue = $.fn.addminusinputer.getCurrentValue.call($that);

        if($btn.hasClass( opts.addBtnClass) ){
            nextValue = curValue + 1 ;
            var $input = $(this).prev();
            var $de = $input.prev();
            $input.show();
            $de.css('display', 'inline-block');
            if($input.val() < 0){
                $input.val(0);
            }
            if($(this).hasClass(opts.btnDisableClass)){
                taoqi.error("超过限购数量");
                return false;
            }
        }
        else if($btn.hasClass( opts.minusBtnClass) ){
            nextValue = curValue - 1;
            var $input = $(this).next();
            if(nextValue == opts.minValue){
                nextValue = opts.minValue;
            }
        }
        $.fn.addminusinputer.updateValue.call($that,nextValue);
    };  
    $.fn.addminusinputer.valueInputed= function(e){
        var $inputer = $(this);
        var value = parseInt($inputer.val());
        var $that = $inputer.closest('.addminusinputer');
        if( value < 0 || isNaN(value)){
            value = 0;
        }
        $.fn.addminusinputer.updateValue.call( $that , value);
    };

    $.fn.addminusinputer.getCurrentValue = function(){
        var $that = this;
        var value = $that.get(0).value ;
        return value;
    };

    $.fn.addminusinputer.updateValue = function(value){ 
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
        }else if ( value <= opts.minValue ){
            value = opts.minValue;
            //$minusBtn.addClass( opts.btnDisableClass );
        }
        if( opts.maxValue == 0 && opts.minValue == 0){
            //$minusBtn.addClass(opts.btnDisableClass);
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

