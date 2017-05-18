/**
 * create by wjc 2015.05.27
 */

seajs.use(['artTemplate'], function (template) {
    template.helper("getFlag", function (str) {

		var imgsrc = ['tqdd','hhzxj','fsjyzqdd','cgjdd','cgddzxj','bpdd'],
			thisSrc = imgsrc[0]; //返回图片名称
		if(!str){
			thisSrc = imgsrc[0];
		}else if(str.indexOf('FINANCEHD') >= 0){
			thisSrc = imgsrc[4];
		}else if(str.indexOf('YXCGJHD') >= 0){
			thisSrc = imgsrc[3];
		}else if(str.indexOf('FUCHSHDYXDD') >= 0){
			thisSrc = imgsrc[2];
		}else if(str.indexOf('HHDD') >= 0){
			thisSrc = imgsrc[1];
		}else if(str.indexOf('SPRAY')>=0){
			thisSrc = imgsrc[5];
		}

		return thisSrc;
	});

    template.helper("$toString", function (data) {
        if (typeof data === "object") {
            return JSON.stringify(data);
        } else {
            return "";
        }
    });

    template.helper("$toFixed", function (num, u) {
        if (typeof num === "number") {
            u = u ? u : 0;
            return num.toFixed(u);
        } else {
            return 0;
        }
    });
});

$(function () {
    $(document).on("click", ".order_list_toolbars a", function () {
        $(this).addClass("current").siblings().removeClass("current");
        $("#status").attr("value", $(this).attr("id"));
        seajs.use("table", function (table) {
            table.fill("order_list_table");
        });
    });

    //一键入库弹窗索引
    var batch_dg = null;
    //签收入库
    $(document).on("click", ".warehouse_in", function () {
        var flag =$(this).parent().find('.flag').val();
        var uid = $(this).parent().find(".uid").val();
        var orderSn = $(this).parent().find(".orderSn").val();
        //若配件已存在直接跳转到采购入库页面
        if (flag =="false") {
            window.location.href = BASE_PATH + "/shop/warehouse/in/tqmall/stock?uid=" + uid + "&orderSn=" + orderSn + "&refer=orderlist";
            return;
        }
        //配件不存在,调一键入库
        var orderId = $(this).parent().find("input[name='orderId']").val();
        seajs.use(["dialog", "artTemplate", "ajax"], function (dg, at, ajax) {

            ajax.post({
                url: BASE_PATH + "/shop/buy/batch_insert_list",
                data: {orderIds: orderId},
                success: function (json) {
                    if (json.success) {
                        var box = $("<div></div>");//清空内容

                        $.each(json.data, function (i, e) {
                            var html = at('batch_in_warehouse_tpl', {item: e});
                            box.append(html);
                        });
                        submitGoods(box.find('.goods_opt'), uid, orderSn);

                    } else {
                        dg.info("订单商品列表获取数据失败", 3);
                    }
                }
            });
        });
    });

	$(document).on('click', '.js-confirm-receive', function () {
	    var data = $(this).data(), that = this;
	    seajs.use('dialog', function (dialog) {
	        dialog.confirm('确认收货', function () {
                dialog.close();

	            $.get(BASE_PATH + '/shop/buy/confirm_revice', data, function (json) {
	                if(json && json.success) {
	                    dialog.info('确认成功');

                        // 配货中的数目
                        var phz = +$('#PHZ b').text().replace(/[()]/g, '');
                        // 已签收的数目
                        var yqs = +$('#YQS b').text().replace(/[()]/g, '');

                        $('#PHZ b').text('(' + (phz - 1) + ')');
                        $('#YQS b').text('(' + (yqs + 1) + ')');
						$('.qxy_link.current').click();
	                }
	            })
	        });
	    })
	});

	//取消一键入库弹框
	$(document).on("click","#batch_in_warehouse .cancel",function(){
		seajs.use("dialog",function(dg){
			batch_dg && dg.close(batch_dg);
		});
	});
	//保存一键入库弹框
	$(document).on("click","#batch_in_warehouse .save",function(){

    });

    $(".banner").lunbo({
        interval: 4500
    });


    //换货清单弹窗
    $(document).on("click", ".order-pop", function () {
        var data = $(this).data('refundgoods');
        seajs.use(['dialog', 'artTemplate'], function (dg, tpl) {
            dg.dialog({
                html: tpl('refundGoodsTpl', {data: data})
            })
        });
    });

    // 签收入库, 获取数据
    function submitGoods(goodsList, uid, orderSn) {
        var data = [];
        var flag = true;
        seajs.use(["dialog","ajax","formData"],function(dg,ajax,fd){
            goodsList.each(function(index){
                var $this = $(this);

                //goodsAttrRelList
                var garl_dom = $("input[name='goodsAttrRelList']",$this);
                var garl = garl_dom.val();
                if(garl!=""&&garl!="null"){
                    garl = JSON.parse(garl);
                    for(var i=0;i<garl.length;i++){
                        var item = garl[i];
                        item.attrName = item.name;
                        delete item.name;
                        item.attrValue = item.value;
                        delete item.value;
                        garl[i] = item;
                    }
                    garl_dom.val(JSON.stringify(garl));
                }

                //goodsCarList
                var gcl_dom = $("input[name='goodsCarList']", $this);
                var gcl = gcl_dom.val();
                if (gcl != "" && gcl != "null") {
                    gcl = JSON.parse(gcl);
                    for (var i = 0; i < gcl.length; i++) {
                        var item = gcl[i];
                        item.carBrandId = item.id;
                        delete item.id;
                        item.carBrandName = item.name;
                        delete item.name;
                        gcl[i] = item;
                    }
                    gcl_dom.val(JSON.stringify(gcl));
                }

                data.push(fd.get2($this));
            });
            if (flag) {
                //验证通过，提交数据
                ajax.post({
                    url: BASE_PATH + "/shop/goods/batch_add_with_attr_car/ng",
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function (json) {
                        if (json.success) {
                                window.location.href = BASE_PATH + "/shop/warehouse/in/tqmall/stock?uid=" + uid + "&orderSn=" + orderSn + "&refer=orderlist";
                        }
                    }
                });
            }
        });
    }
});