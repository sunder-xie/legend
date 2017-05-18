/**
 * Created by lihaizhong on 2015/2/10.
 */
$(function () {
    //相册效果渲染
    if ($('#galleria') && $('#galleria').length > 0) {
        Galleria.loadTheme(BASE_PATH + '/resources/js/lib/galleria/themes/classic/galleria.classic.js');
        Galleria.configure({
            transition: 'fade',
            //imageCrop: true,
            lightbox: true,
            autoplay: 3000
        });
        Galleria.run('#galleria');
    }
});
