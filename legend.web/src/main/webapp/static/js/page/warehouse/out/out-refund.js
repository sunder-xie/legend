/*
* 出库退货
* zmx  2016/08/30
*/

$(function () {
    seajs.use([
            'dialog',
            'select',
            'formData',
            'check'
        ], function (dg,st,fd,ck) {
        dg.titleInit();
        ck.init();
        //领料人
        st.init({
            dom: '.js-picking',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        //删除
        $(document).on('click','.js-delete-btn',function(){
           var del = $(this).parents('tr');
           dg.confirm('确定要删除吗？',function(){
               del.remove();
               totalMoney();
           })
        });

        //返回
        $(document).on('click','.js-return',function(){
            util.goBack();
        });

        //保存其他入库
        $(document).on('click', '.js-refund', function () {

            var msg = checkGoods();
            var result = ck.check();
            if (!result) {
                return;
            }
            if (msg.num) {
                dg.warn(msg.num);
                return;
            }
            var warehouseOut = fd.get('.js-out'),
                warehouseOutDetail = [];
            $.extend(warehouseOut, fd.get('.js-comment'));

            $('.js-detail').each(function () {
                warehouseOutDetail.push(fd.get($(this)));
            });

            if (!warehouseOutDetail.length) {
                dg.warn('退货配件不能为空');
                return;
            }
            $.ajax({
                url: BASE_PATH + "/shop/warehouse/out/out-refund/save",
                dataType: 'json',
                data: {
                    warehouseOutBo: JSON.stringify(warehouseOut),
                    warehouseOutDetailBoList: JSON.stringify(warehouseOutDetail)
                },
                type: 'POST',
                success: function (data) {
                    if (data.success) {
                        dg.success("退货成功",function () {
                            window.location.href = BASE_PATH + "/shop/warehouse/out/out-detail?id=" + data.data + "&refer=out-refund";
                        });
                    } else {
                        dg.fail(data.errorMsg);
                    }
                }
            });
        });

        $(document).on('blur','input[name="goodsCount"]',function(){
            var result = ck.check('.js-detail');
            if (!result) {
                return;
            }
            //价格计算
            totalMoney();
        });
        $(document).on('blur','input[name="salePrice"]',function(){
            var result = ck.check('.js-detail');
            if (!result) {
                return;
            }
            //价格计算
            totalMoney();
        })

        //价格计算
        totalMoney();

    })
});


function checkGoods() {
    var str, t = [];

    $('.yqx-table .js-detail').toArray().forEach(function (e, i) {
        // 退货数量是负的
        if ($(e).find('input[name=goodsCount]').val() < (-1 * $(e).find('.js-goods-real').data('count') )) {
            t.push(i + 1);
        }
    });

    if (t.length)
        str = '第' + t.join(',') + '行配件 出库数量不能大于可退数量,请修改';
    return {num: str};
}

//价格计算
function totalMoney(){
    var accessoriesTotal = 0,
        costTotal = 0;
    $('.js-detail').each(function(){
        var salePrice = $(this).find('input[name="salePrice"]').val(),
            goodsCount = $(this).find('input[name="goodsCount"]').val(),
            costPrice = $(this).find('.cost-price').text();

        accessoriesTotal += ( Number(salePrice) * Number(goodsCount) );
        costTotal += ( Number(costPrice) * Number(goodsCount) );
    });
    $('.accessories-total').text(accessoriesTotal.toFixed(2));
    $('.cost-total').text(costTotal.toFixed(2));
}
