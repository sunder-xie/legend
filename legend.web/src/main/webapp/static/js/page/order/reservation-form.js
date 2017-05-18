$(function(){
    var $document = $(document);

    //tab初始化
    $(".js-tab a").first().addClass('hover');
    $(".tabcon").eq(0).show();

    $document

        //服务项目添加行
        .on('click','.js-addbtn',function(){
            var inlist = '"<tr>'
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small serves-name" value="" placeholder="请输入"></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small serves-type" value="" placeholder=""></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small work-money" value="" placeholder=""></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small work-hour" value="" placeholder=""></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small money" value="" placeholder="" readonly=""></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small seres-remarks" value="" placeholder=""></div></td>';
            inlist += '<td><a href="javascript:;" class="dellist-btn js-delbtn">删除</a></td>';
            inlist += '</tr>"';

            $(".serves-box").append(inlist);
        })

        //服务项目删除行
        .on('click','.js-delbtn',function(){
            var $this = $(this);
            $this.parents('tr').remove();
        })


        //配件物料添加行
        .on('click','.js-goods-addbtn',function(){
            var inlist = '"<tr>'
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small part-number" value="" placeholder="请输入"></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small goods-name" value="" placeholder=""></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small goods-models" value="" placeholder=""></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small goods-sale" value="" placeholder=""></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small number" value="" placeholder=""><span class="fa icon-small unittop">单位</span></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small price" value="" placeholder="" readonly=""></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small favorable-price" value="" placeholder=""></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small stock" value="" placeholder="" readonly=""></div></td>';
            inlist += '<td><div class="form-item"><input type="text" name="" class="yqx-input yqx-input-small salesperson" value="" placeholder=""></div></td>';
            inlist += '<td><a href="javascript:;" class="dellist-btn js-delbtn">删除</a></td>';
            inlist += '</tr>"';

            $(".goods-box").append(inlist);
        })

        //配件物料删除行
        .on('click','.js-goods-delbtn',function(){
            var $this = $(this);
            $this.parents('tr').remove();
        })

        //tab切换
        .on('click','.js-tab a',function(){
            var $this = $(this),
                $tabcon = $('.tabcon');

            $this.addClass('hover').siblings().removeClass('hover');
            $tabcon.eq($this.index()).show().siblings('.tabcon').hide();

        })
})


