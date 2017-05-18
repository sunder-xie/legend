/**
 * Created by sky on 16/4/28.
 */

define(function(reuqire, exports, module) {

    var Audio = function (source) {
        this.source = source;
        this.init();
    };

    Audio.prototype = {
        init: function() {
            var self = this, elemId, $elem, i, source = '';
            if (self.source === '') throw new Error('source不能为空！');
            if (typeof self.source === 'string') self.source = [self.source];
            if (self.source instanceof Array) {
                for (i = 0; i < self.source.length; i++) {
                    source += '<source src="' + self.source[i] + '"/>';
                }
            } else {
                throw new Error('source必须是url字符串或者url字符串数组！');
            }
            elemId = '_au_' + Math.round(Math.random() * 1e7);
            $elem = $('<audio id="' + elemId + '">' + source + '</audio>').appendTo('body');
            self.elem = $elem.get(0);
        },
        play: function() {
            this.elem.play();
        }
    };

    module.exports.init = function (source) {
        return new Audio(source);
    };
});