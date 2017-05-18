<script>
    $(function () {
        var nav = typeof getAsideNav === "function" ? getAsideNav() : [],
            pathname = location.pathname,
            index = 0;
        //外层循环标签
        loop:
        for (var i = 0; i < nav.length; i++) {
            var item = nav[i].url;
            for (var j = 0; j < item.length; j++) {
                for (var k in item[j]) {
                    var reg1 = new RegExp(k + '$');
                    if (reg1.test(pathname)) {
                        break loop;
                    }
                    if (item[j][k].length > 0) {
                        for (var x = 0; x < item[j][k].length; x++) {
                            var reg2 = new RegExp(item[j][k][x] + '$');
                            if (reg2.test(pathname)) {
                                break loop;
                            }
                        }
                    }
                }
                index++;
            }
        }
        $('a','.aside-nav').eq(index).addClass('current');
    });
</script>