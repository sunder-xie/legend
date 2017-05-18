/**
 * Created by sky on 2017/1/10.
 */

define(function (require, exports, module) {
    var tpl = require('art');
    var dg = require('dialog');
    require('ajax');

    var extend = $.extend;

    var idReg = /^#\w+$/,
        tvBranch = 'yunui-tv-branch',
        tvNode = 'yunui-tv-node',
        tvNodeOpened = 'yunui-tv-opened',
        tvNodeClosed = 'yunui-tv-closed',
        tvNodeLastLevel = 'yunui-tv-last-level',
        tvNodeSelected = 'yunui-tv-selected',
        tvItem = 'yunui-tv-item',
        tvItemContainer = 'yunui-tv-item-container';

    function noop() {
    }

    function print(debug) {
        return function () {
            var msgs = Array.prototype.slice.call(arguments);
            debug && window.console.debug.apply(window.console, msgs);
        }
    }

    function createElement(el, cls) {
        var $$el = document.createElement(el);
        $$el.className = cls;
        return $$el;
    }

    TreeView.util = {
        hasClass: function (dom, cls) {
            return $(dom).hasClass(cls);
        },
        addClass: function (dom, cls) {
            $(dom).addClass(cls);
        },
        removeClass: function (dom, cls) {
            $(dom).removeClass(cls);
        },
        replaceClass: function (dom, srcCls, replaceCls) {
            $(dom).removeClass(srcCls)
                .addClass(replaceCls);
        },
        isPlainType: function (o, t) {
            return Object.prototype.toString.call(o).toLowerCase() === '[object ' + t + ']';
        }
    };

    function TreeView(options) {
        this.opts = extend({}, TreeView.default, TreeView.global, options);
        this._init();
    }

    TreeView.prototype = {
        _init: function () {
            var self = this,
                wrapDom = self.opts.wrap,
                level = self.opts.level,
                showLevel = self.opts.showLevel,
                indent = self.opts.indent;
            // 处理待填充DOM节点
            if (idReg.test(wrapDom)) {
                self.wrapId = wrapDom;
                wrapDom = document.getElementById(wrapDom.substr(1));
            } else if (!(typeof wrapDom === 'object' && wrapDom.nodeType === 1)) {
                wrapDom = document.body;
                self.wrapId = 'body';
            } else {
                self.wrapId = '#' + wrapDom.id;
            }
            self.wrapDom = wrapDom;

            self.opts.level = isNaN(level) ? TreeView.default.level : parseInt(level);

            (/^(?:(?:[1-9]\d+)|\d)$/.test(indent)) || (self.opts.indent = TreeView.default.indent);

            if (typeof self.opts.nodeKey !== 'string') {
                self.opts.nodeKey = '';
            }

            self.opts.showLevel = isNaN(showLevel) ? TreeView.default.showLevel :
                showLevel > self.opts.level ? self.opts.level : parseInt(showLevel);

            self.print = print(self.opts.debug);
        },
        _getData: function () {
            var self = this,
                dfd,
                ajaxDfd = this.opts.ajaxDfd;
            if (ajaxDfd) {
                ajaxDfd.done(function (result) {
                    if (result.success) {
                        return self.opts.data = result.data;
                    } else {
                        dg.fail(result.errorMsg);
                        return null;
                    }
                })
            } else {
                dfd = $.Deferred();
                dfd.resolve(self.opts.data);
                return dfd;
            }
        },
        showSwitch: function (level, $$item) {
            var $item = $($$item),
                $subBranch = $item.siblings('.' + tvBranch);

            if ($subBranch.is(':hidden')) {
                // 显示当前节点的最近下级分支
                $subBranch.show();
            } else {
                // 隐藏当前节点的所有下级分支
                $item.parent().find('.' + tvBranch).hide();
            }

        },
        _eventFn: function () {
            var self = this;
            return {
                current: function (e) {
                    var $$this = this,
                        $$node = $$this.parentNode,
                        $$root = self.wrapDom,
                        level = $$node.dataset['level'],
                        isNext;

                    level = isNaN(level) ? '' : level >>> 0;

                    // 状态切换
                    TreeView.util.removeClass($$root.querySelectorAll('.' + tvNode + '.' + tvNodeSelected), tvNodeSelected);
                    TreeView.util.addClass($$node, tvNodeSelected);

                    // 回调函数
                    isNext = self.opts.selectedCallback.call(self, e, level, $$this);
                    
                    if (isNext !== false) {
                        // 视图切换
                        self.showSwitch(level, $$this);
                        // 图标更新
                        self._updateIcon($$node.parentNode);
                    }

                    // 防止节点冒泡
                    return false;
                }
            };
        },
        _bindEvent: function () {
            $(document)
                .off('click.tv', this.wrapId + '.yunui-tv-item', this._eventFn().current)
                .on('click.tv', this.wrapId + ' .yunui-tv-item', this._eventFn().current);
        },
        _createBranch: function (level) {
            var cls = [tvBranch, tvBranch + '-' + level, this.opts.showIcon || ''];
            return createElement('ul', cls.join(' '));
        },
        _createNode: function () {
            return createElement('li', tvNode);
        },
        /**
         * 创建操作节点
         * @param level
         * @returns {*}
         * @private
         */
        _createItem: function (level) {
            var cls = [tvItem, tvItem + '-' + level];
            return createElement('div', cls.join(' '));
        },
        /**
         * 创建模板容器 .*-container
         * @returns {*}
         * @private
         */
        _createItemContainer: function () {
            return createElement('div', tvItemContainer);
        },
        /**
         * 构建模板
         * @param data
         * @param level
         * @returns {*}
         * @private
         */
        _buildTemplate: function (data, level) {
            var self = this,
                template = self.opts.template,
                $$tplNode;

            if (typeof template !== 'string') {
                return '';
            } else if (idReg.test(template)) { // 判断是否是DOM ID
                $$tplNode = document.getElementById(template.substr(1));
                // $$tplNode 是DOM元素
                if ($$tplNode && $$tplNode.nodeType === 1) {
                    template = $$tplNode.innerHTML;
                } else {
                    return template;
                }
            }

            return tpl.compile(template)({
                templateData: data,
                level: level
            });
        },
        /**
         * 构建节点容器<li>
         * @param index
         * @param data
         * @param level
         * @returns {*}
         * @private
         */
        _buildNode: function (data, index, level) {
            var self = this,
                $$nodeItem,
                $$node,
                $$container,
                $$content,
                $$item,
                next = true,
                nextData,
                printHead = 'node: ' + level + ',' + index + ' ';

            self.print(printHead + '执行创建容器&lt;li&gt;前的回调函数...');
            // 节点容器构建前的回调
            nextData = self.opts.beforeCreateNodeCallback.call(self, index, level, data);

            self.print(printHead + '开始处理数据。处理前的数据：' + data);
            // 处理回调返回的数据
            if (TreeView.util.isPlainType(nextData, 'object')) {
                if (nextData.data === undefined) {
                    nextData.data = data;
                }
                if (nextData.nodeKey === undefined) {
                    nextData.nodeKey = self.opts.nodeKey;
                }
            } else if (nextData === false) {
                next = false;
            } else {
                nextData = {
                    data: data,
                    nodeKey: self.opts.nodeKey
                };
            }
            self.print(printHead + '处理数据完成。处理后的数据：' + JSON.stringify(nextData));

            self.print(printHead + '节点容器开始构建...');
            // 构建模板
            $$content = self._buildTemplate(nextData.data, level);
            self.print(printHead + '模板编译完成：' + $$content);
            // 创建模板容器
            $$container = self._createItemContainer();
            $$container.innerHTML = $$content;
            // 创建节点
            $$item = self._createItem(level);
            $$item.appendChild($$container);
            // 创建节点容器
            $$node = self._createNode();
            $$node.appendChild($$item);
            $$node.dataset['level'] = level;
            // 递归下级容器列表
            if (next) {
                $$nodeItem = self._buildBranch(nextData.data[nextData.nodeKey], level + 1);
                $$nodeItem && $$node.appendChild($$nodeItem);
            }
            self.print(printHead + '执行创建节点容器后的回调函数...');
            // 节点容器构建后的回调
            self.opts.afterCreateNodeCallback.call(self, index, level, $$node);
            self.print(printHead + ' 构建节点容器完成。', $$node);
            return $$node;
        },
        /**
         * 构建容器列表<ul>
         * @param data
         * @param level
         * @param $$branch
         * @returns {null}
         * @private
         */
        _buildBranch: function (data, level) {
            var self = this,
                $$branch,
                $$node,
                i,
                printHead = 'branch: ' + level + ' ';

            TreeView.util.isPlainType(data, 'object') && (data = [data]);
            if (!(data instanceof Array && data.length)) {
                return;
            }

            if (self.opts.level != 0 && level > self.opts.level) {
                return;
            }

            self.print(printHead + '容器列表开始构建...');
            $$branch = self._createBranch(level);
            $$branch.style.display = level <= self.opts.showLevel ? 'block' : 'none';
            level - 1 && ($$branch.style.marginLeft = self.opts.indent + 'px');

            for (i = 0; i < data.length; i++) {
                $$node = self._buildNode(data[i], i, level);
                $$branch.appendChild($$node);
                $$node = null;
            }
            self.print(printHead + '执行创建容器列表后的回调函数...');
            // 树枝构建后的回调
            self.opts.afterCreateBranchCallback.call(self, level, $$branch);
            self.print(printHead + '容器列表构建完成。');
            return i === 0 ? null : $$branch;
        },
        /**
         * 更新节点图标
         * @param tree
         */
        _updateIcon: function (tree) {
            var self = this;

            if (!(typeof self.opts.showIcon === 'string' && self.opts.showIcon !== '')) {
                return;
            }

            self.print('图标开始更新...');
            $(tree || self.wrapDom).find('.' + tvNode).each(function () {
                var $$this = this,
                    level = this.dataset['level'],
                    $$subBranch;

                level = isNaN(level) ? '' : level >>> 0;
                TreeView.util.removeClass($$this, [tvNodeClosed, tvNodeOpened, tvNodeLastLevel].join(' '));
                if (self.opts.level !== 0 && self.opts.level === level) {
                    TreeView.util.addClass($$this, tvNodeLastLevel);
                } else if (($$subBranch = $$this.querySelector('.' + tvBranch)) && $$subBranch.style.display === 'none') {
                    TreeView.util.addClass($$this, tvNodeClosed);
                } else {
                    TreeView.util.addClass($$this, tvNodeOpened);
                }
            });
            self.print('图标更新完成。');
        },
        /**
         * 构建树并添加到指定DOM元素上
         */
        buildTree: function () {
            var self = this;
            self.print('获取TreeView数据信息');
            self._getData()
                .done(function (data) {
                    var wholeTree;
                    if (data !== null) {
                        self.print('TreeView开始构建...');
                        wholeTree = self._buildBranch(data, 1);
                        if (wholeTree) {
                            self._updateIcon(wholeTree);
                            self.wrapDom.appendChild(wholeTree);
                            self._bindEvent();
                        }
                        self.print('TreeView构建完成后的回调函数...');
                        self.opts.finishedCallback.call(self, wholeTree);
                        self.print('TreeView构建完成。', wholeTree);
                    }
                });
            return this;
        },
        /**
         * 通过静态数据更新TreeView树
         * @param data
         */
        updateTree: function (data) {
            var self = this;
            self.print('TreeView数据更新...');
            self.opts.data = data;
            self.opts.ajaxDfd = null;
            self.buildTree();
            return self;
        },
        /**
         * 添加节点容器
         * @param el
         * @param data
         */
        addNode: function (el, data) {
            var self = this,
                $item = $(el).closest('.' + tvItem),
                $branch = $item.siblings('.' + tvBranch).eq(0),
                $$node,
                $$branch,
                level;

            self.print('节点容器添加...');
            if ($branch.length) {
                level = $branch.closest('.' + tvNode).data('level');
                level = level >>> 0;
                $$node = self._buildNode(data, -1, ++level);
                $branch.append($$node);
                self._updateIcon($branch[0]);
            } else {
                $$node = $item.closest('.' + tvNode);
                level = $$node.data('level');
                level = level >>> 0;
                $$branch = self._buildBranch(data, ++level);
                $$node.append($$branch);
                $$branch.style.display = 'block';
                self._updateIcon($$node.parentNode);
            }
            return this;
        },
        /**
         * 删除树叶
         * @param el
         */
        removeNode: function (el) {
            $(el).closest('.' + tvNode).remove();
            return this;
        },
        /**
         * 销毁实例化树
         */
        destroy: function () {
            this.wrapDom.innerHTML = '';
            delete TreeView.manage[this.manageId];
        }
    };

    // TreeView 管理
    TreeView.manage = {};

    // TreeView 默认设置
    TreeView.default = {
        // 开启后可以打印出构建日志
        debug: false,
        // 渲染节点的id或者DOM元素
        // {DOM ID | DOM Object}
        // 必填
        wrap: null,
        // ajax Deferred接口
        // {deferred}
        // 与data必填其一
        ajaxDfd: null,
        // 静态数据
        // {Array|Object}
        // 与ajaxDfd必填其一
        data: null,
        // 模板，可以使用id或者html片段
        // {DOM ID | DOM String}
        // 必填
        template: null,
        // 每层缩进的值，单位：px
        // {Number}
        indent: 20,
        // 树的最大层级，0为不限制层级
        // {Number}
        // 选填
        level: 0,
        // 生成节点的下级节点列表（根据`data`中的`[nodeKey]`属性依次遍历）
        // {String}
        // 选填
        nodeKey: 'data',
        // 初始化展示的层级
        // {Number}
        // 选填
        showLevel: 1,
        // 是否显示左侧的小图标
        // {DOM CLASS}
        // 选填
        showIcon: 'yunui-tv-icon',
        // 节点容器构建完成前的回调函数，主要用来处理渲染数据的回调函数
        // @param idx 当前渲染的节点index
        // @param level 当前渲染的层级
        // @param data 即将渲染未处理的数据
        // @return {{data: 已处理过的数据, nodeKey: 下一个遍历的节点}|Boolean}
        // 返回为全等于false时，表示停止向下遍历
        beforeCreateNodeCallback: noop,
        // 节点容器构建完成后的回调函数
        // @param idx 当前渲染的节点index
        // @param level 当前渲染的层级
        // @param data 渲染的数据
        // @param el 当前渲染的元素
        // @return {}
        afterCreateNodeCallback: noop,
        // 容器列表构建后的回调函数
        // @param level 当前渲染的层级
        // @param el 当前渲染的元素
        afterCreateBranchCallback: noop,
        // 点击选中节点后的操作
        // @param e Event对象
        // @param level 当前元素所在层级
        // @param el 选中的当前元素
        selectedCallback: noop,
        // TreeView构建完成的回调函数
        // @param tree 返回整颗TreeView
        finishedCallback: noop
    };

    // TreeView 全局设置
    TreeView.global = {};

    module.exports = {
        config: function (options) {
            TreeView.global = extend({}, TreeView.global, options);
        },
        init: function (options) {
            var tv = new TreeView(options);
            tv.manageId = new Date().getTime() + parseInt(Math.random() * 100);
            TreeView.manage[tv.manageId] = tv;
            return tv;
        },
        destroy: function (manageId) {
            var tv;
            if (!manageId) {
                return;
            }
            tv = TreeView.manage[manageId];
            tv.destroy();
        }
    }
});
