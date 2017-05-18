define(function (require, exports, module) {
    var _handler = {};

    var dragged;

    document.addEventListener('dragstart', function (event) {
        var handler = getStartHandler(event.target, getClassNames(_handler));
        if (handler[0]) {
            dragged = handler[1];

            // make it half transparent
            event.target.style.opacity = .5;
            typeof handler == 'function' && handler.call(event.target);
        }
    });

    document.addEventListener('dragend', function (event) {
        var handler = getStartHandler(event.target, getClassNames(_handler));
        if (handler) {
            // reset the transparency
            event.target.style.opacity = '';
        }
    });

    /* events fired on the drop targets */
    document.addEventListener('dragover', function (event) {
        // prevent default to allow drop
        event.preventDefault();
    });

    document.addEventListener('drop', function (event) {
        var fn = null;
        // prevent default action (open as link for some elements)
        event.preventDefault();
        // 处理移动的元素 dragged 和目标元素 event.target
        if ( (fn = getDropHandler(event.target, _handler) )) {
            typeof fn == 'function' && fn.call(event.target, dragged);
        }

    });

    function getStartHandler(ele, classNames) {
        var $e = $(ele);
        var ret = null, className = '',
            target = null, bool = false;

        classNames.some(function (t) {
            if($e.hasClass(t)) {
                target = $e;
                bool = true;
            } else if( (target = $e.parents('.' + t)) && target.length ) {
                bool = true;
            }
            
            if(bool) {
                ret = _handler[t];
                return true;
            }
        });

        return [ret ? ret.fn[0] : null, target];
    }

    function getClassNames(obj) {
        var ret = [];
        for(var key in obj) {
            if(obj.hasOwnProperty(key)) {
                ret.push(key);
            }
        }

        return ret;
    }

    function getDropHandler(ele, handler) {
        var $e = $(ele), t;
        for(var key in handler) {
            t = handler[key].dropZone;
            if($e.hasClass(t) || $e.parents('.' + t).length ) {
                return handler[key].fn[1];
            }
        }
    }

    function checkArgs(className, dropZoneClassName, dragStart, drop) {
        var str = [];
        if (className == null || className == '') {
            str.push('need className');
        }
        if (dropZoneClassName == null || dropZoneClassName == '') {
            str.push('need dropZoneClassName');
        }
        if (drop == null || typeof drop != 'function') {
            str.push('need drop function');
        }

        return str.join(',');
    }

    module.exports = function (className, dropZoneClassName, dragStart, drop) {
        var obj = null;
        if(typeof className == 'object') {
            obj = className;
            className = obj.className;
            dropZoneClassName = obj.dropZoneClassName;
            dragStart = obj.dragStart;
            drop = obj.drop;
        }
        if(!drop) {
            drop = dragStart;
            dragStart = function () {};
        }
        var t = checkArgs(className, dropZoneClassName, dragStart, drop);

        if(t != '') {
            console.error(t);
        }

        _handler[className] = {
            fn: [dragStart, drop],
            dropZone: dropZoneClassName
        };
    };
});