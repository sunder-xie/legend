$(function () {
    if (typeof FileReader == 'undefined') return layer.msg('您的游览器不支持文件上传');
    var mWidth = null, mHheight = null, mSize = 10 * 1024 * 1024, url = '/legend/index/oss/upload_image', imgList = new Array(8);
    $('.up-img-btn').on('click', function () {
        $('.up-img-hide').data('index', $(this).index('.up-img-btn')).trigger('click');
    });
    $('.submit-btn').on('click', function () {
        var urlList = '', index = 0, url = '/legend/insurance/anxin/flow/mobile/submit-upload';
        if ($(this).prop('disabled')) {
            return;
        } else {
            $(this).prop('disabled', true);
        }
        if (imgList.every(function (v, i) {
                index += 1;
                if (!v || !v.length) {
                    return $('.up-img-btn').eq(i).addClass('need-up'), false;
                } else {
                    return true;
                }
            })) {
            if (index != 8) return $(this).prop('disabled', false), alert('必须上传8张照片才可以提交');
            index = 0;
            urlList = imgList.join(',').replace(/,/g, function (v, i, l) {
                index += 1;
                return (index == 1 || index == 3 || index == 8) ? ';' : v;
            });
            $.get(url, {
                orderSn: $('.orderSn').val(),
                urls: urlList
            }).done(function (res) {
                if (res.success) {
                    alert('提交成功');
                    close();
                }
            })
        } else {

        }
    });
    $(document)
        .on('change.file', '.up-img-hide', function (e) {
            var odlUrl = '/legend/static/img/page/ax_insurance/up-demo-1.png';
            var _this = this, fr, index = $(this).data('index'), file;
            if (!this.files.length) {
                return;
            } else {
                file = this.files[0];
            }
            if(!file.type.match(/image\/(png|jpeg)/g)) return layer.msg('上传文件类型错误');
            if (this.files[0].size > mSize) return layer.msg('上传文件过大');
            fr = new FileReader();
            fr.onload = function (file) {
                var img = new Image(), form = new FormData();
                img.onload = function () {
                    if (mWidth && this.width > mWidth) {
                        return layer.msg('图片宽度超过{0}限制'.format(mWidth));
                    }
                    if (mHheight && this.height > mHheight) {
                        return layer.msg('图片高度超过{0}限制'.format(mHheight));
                    }
                    $('.load-img').eq(index)[0].src = img.src;
                    $('.up-img-btn').eq(index).val('重新上传');
                };
                img.src = file.target.result;
                form.append('img-' + index, $(_this)[0].files[0]);
                $.ajax({
                    url: url,
                    type: 'post',
                    data: form,
                    processData: false,
                    contentType: false
                }).done(function (res) {
                    if (res.state == 'SUCCESS') {
                        imgList[index] = res.url;
                        $(_this).attr('value', '');
                        clearFile($(_this));
                    } else {
                        alert(res.errorMsg);
                        $('.load-img').eq(index)[0].src = odlUrl.replace(/\d/, index+1);
                    }
                })
            };
            fr.readAsDataURL(this.files[0]);
        });

    function clearFile($target) {
        $target.after($target.clone().val(''));
        $target.remove();
    }
    function close() {
        if(typeof WeixinJSBridge != 'undefined') {
            WeixinJSBridge.invoke('closeWindow',{},function(res){
            });
        }
    }
})