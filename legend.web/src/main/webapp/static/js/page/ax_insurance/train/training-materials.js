/**
 * Created by sky on 16/9/18.
 */

$(function () {
    seajs.use('ajax');
    var Model = {
        checkPassword: function (pwd) {
            var url = BASE_PATH + '/shop/manager/check-password';
            return $.post(url, {password: pwd});
        },
        packageDetail: function (id) {
            var url = BASE_PATH + '/insurance/anxin/train/training-materials/detail';
            return $.get(url, {id: id});
        },
        packageList: function () {
            var url = BASE_PATH + '/insurance/anxin/train/training-materials/list';
            return $.get(url);
        }
    }, dgLoginId;

    /**
     * create by sky 2016 11 1
     */
    function loginDialog() {
        seajs.use('dialog', function (dg) {
            dgLoginId = dg.open({
                area: ["380px", "auto"],
                closeBtn: 0,
                shadeClose: false,
                end: function () {
                    $(".js-dialog-login-pwd").attr('type', 'text');
                },
                content: $('#passwordTpl').html()
            });
            $('.js-dialog-password-btn').data('url', location.href);
            $('.js-dialog-login-pwd').focus();
        });
    }
    /*** end 2016 11 1 ***/

    (function initialize() {
        /**
         * create by sky 2016 11 1
         */
        var isFirst = localStorage.getItem(location.pathname);
        var isLogin = sessionStorage.getItem('trainingMaterials');
        sessionStorage.removeItem('trainingMaterials');
        if (isFirst == null) {
            localStorage.setItem(location.pathname, 1);
            seajs.use('dialog', function (dg) {
                var msg = '由于安心保险方系统尚在验收，云修系统投保功能暂时只能开放【服务包培训资料】模块，请知晓！';
                dg.confirm(msg, function () {
                    if (isLogin == null) {
                        loginDialog();
                    }
                }, ['知道了']);
            });
        } else {
            if (isLogin == null) {
                loginDialog();
            }
        }
        /*** end 2016 11 1 ***/

        Model.packageList()
            .done(function (result) {
                if (result.success) {
                    seajs.use('art', function (tpl) {
                        var html = tpl('packageTagsTpl', result);
                        $('#packageTagsContainer').html(html);
                        var $tag = $('.js-package-tag');
                        if ($tag.length) {
                            $tag.eq(0).click();
                        }
                    });
                } else {
                    seajs.use('dialog', function (dg) {
                        dg.fail(result.errorMsg);
                    });
                }

            });
    })();

    $(document).on('click', '.js-package-tag', function () {
        var $this = $(this);
        var id = $(this).data('id');
        if ($this.hasClass('active') || id === '') {
            return;
        }

        Model.packageDetail(id)
            .success(function (result) {
                if (result.success) {
                    seajs.use('art', function (tpl) {
                        var html = tpl('packageDetailTpl', result);
                        $('#packageDetailContainer').html(html);
                    });
                    $('.js-package-tag.active').removeClass('active');
                    $this.addClass('active');
                } else {
                    seajs.use('dialog', function (dg) {
                        dg.fail(result.errorMsg);
                    });
                }
            });
    });

    $(document).on('click', '.js-more-data', function () {
        var $tagsBox = $('.js-tags-box'),
            hasMaxHeight = !!$tagsBox.hasClass('max-tags-height');

        if (hasMaxHeight) {
            $tagsBox.removeClass('max-tags-height');
            $(this).text('收起');
        } else {
            $tagsBox.addClass('max-tags-height');
            $(this).text('更多');
        }
    });

    /**
     * create by sky 2016 11 1
     */
    $(document)
        .off('click', '.js-dialog-password-btn')
        .on('click', '.js-dialog-password-btn', function () {
            var pwd = $('.js-dialog-login-pwd').val();

            if (pwd == null || pwd === '') {
                $('.js-dialog-login-err').text('密码不能为空');
                return;
            }

            Model.checkPassword(pwd)
                .success(function (result) {
                    if (result.success && result.data) {
                        seajs.use('dialog', function (dg) {
                            dg.close(dgLoginId);
                        });
                    } else {
                        $('.js-dialog-login-err').text(result.errorMsg);
                    }
                });
        });
    /*** end 2016 11 1 ***/
});
