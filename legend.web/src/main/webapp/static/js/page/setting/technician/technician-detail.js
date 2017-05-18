/*
 * create by zmx 2017/1/5
 * 技师认证
 */

$(function(){
    var doc = $(document);
    seajs.use([
        'check',
        'select',
        'formData',
        'dialog',
        'art'
    ],function(ck,st,fd,dg,at){
        //验证
        ck.init();
        //删除擅长修理项目
        doc.on('click','.js-remove-tips',function(){
            $(this).remove();
        });
        //返回
        doc.on('click','.js-goBack',function(){
            util.goBack(BASE_PATH+"/shop/setting/user-info/user-info");
        });

        ['.js-education', '.js-seniority', '.js-level'].forEach(function (e) {
            st.init({
                dom: e
            });

            var target = $(e).parent().find('dd[selected]');

            $(e).val($.trim(target.text()))
                .next('input').val(target.data('key'));
        });

        //擅长修理
        $.ajax({
            url: BASE_PATH + "/shop/car_category/brand_letter",
            success: function (json) {
                if (json.success) {
                    //回填上次级别设置的数据。
                    var html = at("allBrandTpl", {"templateData": json.data});
                    $("#allBrand").html(html);
                    handleChoosenSelect("#allBrand");
                }
            }
        });

        var getHtml = function(id,text){
            html = '<div class="majorCarBrand_item" data-id="'+id+'">'+text+'<i></i></div>';
            return html;
        };

        //拼接字符串
        var writeStr = function(){
            var majorCarBrand = $("input[name='majorCarBrand']");
            var str = "";
            $(".majorCarBrand_item").each(function(){
                var id = $(this).data("id");
                var text = $(this).text();
                str += id+":"+text+";";
            });
            majorCarBrand.val(str);
        };

        var resetStatus = function(obj){
            obj.val("");
            obj.chosen('destroy');
            handleChoosenSelect(obj);
        };

        $(document).on("change","#allBrand",function(){
            var text = $("option:selected",$(this)).text();
            var id = $(this).val();

            if(id == ""){
                return;
            }

            // 放品牌的容器
            var nextFormItem = $(this).parent();
            var majorCarBrandItem = $(".majorCarBrand_item",nextFormItem);
            var size = majorCarBrandItem.size();
            if(size >= 3){
                dg.fail("最多只能添加3个品牌");
                resetStatus($(this));
                return;
            }
            var bool = true;
            majorCarBrandItem.each(function(){
                var _id = $(this).data("id");
                if(id == _id){
                    dg.fail("请勿重复添加品牌");
                    bool = false;
                    return;
                }
            });
            if(!bool) {
                resetStatus($(this));
                return;
            }
            nextFormItem.append(getHtml(id,text));

            if(text != ""){
                writeStr();
            }
            resetStatus($(this));
        });

        //删除专修品牌
        $(document).on("click",".majorCarBrand_item i",function(){
            var parent = $(this).parent();
            parent.fadeOut(function(){
                parent.remove();
                writeStr();
            });
        });

        //专修品牌的回填。
        (function(){
            var majorCarBrand = $.trim($("#adeptRepair").val());
            if(majorCarBrand!="" && majorCarBrand!=null && majorCarBrand!="null"){
                var arr = majorCarBrand.split(/;/g);
                var html = "";
                var nextFormItem = $("#adeptRepair").parent();
                for(var i=0;i<arr.length;i++){
                    if(arr[i]==""){
                        continue;
                    }
                    var temp = arr[i].split(/:/g);
                    html += getHtml(temp[0],temp[1])
                }
                nextFormItem.append(html);
            }
        })();

        //搜索插件
        function handleChoosenSelect(selectorName, width) {
            $(selectorName).each(function () {
                if ($(this).find('option').length > 1) {
                    $(this).chosen({
                        search_contains: true,
                        display_width: width, //默认是220px
                        allow_single_deselect: $(this).attr("data-with-diselect") === "1" ? true : false,
                        no_results_text: '没有搜索到结果'
                    });
                }
            });
        }

        doc.on('click','.js-save',function(){
            if(!ck.check()){
                return false;
            }
            var data = fd.get('#formData',true);
            var shopManager = {
                name: data.name,
                mobile: data.mobile,
                identityCard: data.identityCard,
                gender: data.gender,
                education: data.education,
                graduateSchool: data.graduateSchool
            };
            var technician = {
                seniority: data.seniority,
                adeptRepair: data.majorCarBrand,
                technicianLevel: data.technicianLevel
            };
            var technicianBO = {
                technician:technician,
                shopManager:shopManager
            };

            $.ajax({
                type:'POST',
                url:BASE_PATH + '/shop/setting/technician/save',
                data: JSON.stringify(technicianBO),
                dataType: 'json',
                contentType: "application/json",
                success:function(result){
                    if(result.success){
                        dg.success('保存成功')
                    }else{
                        dg.fail(result.message)
                    }
                }
            })
        });
    })
});