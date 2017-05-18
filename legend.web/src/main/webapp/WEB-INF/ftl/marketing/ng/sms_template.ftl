<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/marketing/sms_template.css?2d79e2655004ab86a895bf6fa71c453d"/>
<div class="wrapper">
<#include "marketing/ng/left_nav.ftl"/>
    <div class="right">
        <div class="head clearfix">
            <h3 class="Z-title">
                客户营销 >
                <a href="${BASE_PATH}/marketing/ng/center/sms"> 短信充值</a> > <i>模板列表</i>
                <button class="yqx-btn yqx-btn-small yqx-btn-1 fr" id="J-save">保存</button>
            </h3>
        </div>
        <div class="panel">
            <div class="panel-body">
                <p class="row"><span class="label">车主预约单提醒：</span><input class="input input-default w_670" type="text" name="1" value="${template1.content}" data-id="${template1.id}" /></p>
                <p class="row"><span class="label">保养到期提醒：</span><input class="input input-default w_670" type="text" name="2" value="${template2.content}" data-id="${template2.id}" /></p>
                <p class="row"><span class="label">保险到期提醒：</span><input class="input input-default w_670" type="text" name="3" value="${template3.content}" data-id="${template3.id}" /></p>
                <p class="row"><span class="label">年检到期提醒：</span><input class="input input-default w_670" type="text" name="4" value="${template4.content}" data-id="${template4.id}" /></p>
                <p class="row"><span class="label">回访到期提醒：</span><input class="input input-default w_670" type="text" name="5" value="${template5.content}" data-id="${template5.id}" /></p>
                <p class="row"><span class="label">生日提醒：</span><input class="input input-default w_670" type="text" name="6" value="${template6.content}" data-id="${template6.id}" /></p>
                <p class="row"><span class="label">流失客户提醒：</span><input class="input input-default w_670" type="text" name="7" value="${template7.content}" data-id="${template7.id}" /></p>
                <p class="row"><span class="label">休眠客户：</span><input class="input input-default w_670" type="text" name="8" value="${template8.content}" data-id="${template8.id}" /></p>
                <p class="row"><span class="label">活跃客户：</span><input class="input input-default w_670" type="text" name="9" value="${template9.content}" data-id="${template9.id}" /></p>
                <p class="row"><span class="label">默认：</span><input class="input input-default w_670" type="text" name="10" value="${template10.content}" data-id="${template10.id}" /></p>
                <div class="row">
                <#list otherList as other>
                    <p class="row other-row otherRow"><span class="label" content="其他短信模板："></span><input class="input input-default w_670" type="text" name="0" value="${other.content}" data-id="${other.id}" /><button class="btn-del ml-10" type="button"></button></p>
                </#list>
                <#if otherList?size lt 10>
                    <p class="row other-row otherRow"><span class="label" content="其他短信模板："></span><input class="input input-default w_670" type="text" name="0" /><button class="btn-add ml-10" type="button"></button></p>
                </#if>
                </div>
                </div>
        </div>
    </div>
</div>
<script>
    $(function() {
        var $doc = $(document);

        // 事件绑定
        $doc
                // 保存模板信息
                .on("click", '#J-save', function() {
                    var list=[],
                        url = BASE_PATH + "/marketing/ng/sms/template/update",
                        i, $temp;
                    // 遍历必传项
//                    for(i =1;i<8;i++){
//                        $temp = $("[name='"+i+"']");
//                        if($temp != ""){
//                            var data = {
//                                id: $temp.data("id"),
//                                templateType: i,
//                                content: $temp.val()
//                            };
//                            list.push(data);
//                        }
//                    }

                    $('input[type=text]').each(function() {
                        var $this = $(this),
                            type = $this.attr("name"),
                            id = $this.data("id"),
                            content = $this.val(),
                            data;

                        if(content !== "") {
                            data = {
                                id: (id === undefined? null : id),
                                templateType: type,
                                content: content
                            };
                            list.push(data);
                        }
                    });

                    $.ajax({
                        url:  url,
                        type: "POST",
                        data: JSON.stringify(list),
                        contentType: "application/json",
                        dataType: "json",
                        success: function(result) {
                            if(result.success) {
                                layer.msg(result.data, 3, 1);
                            } else {
                                layer.msg(result.errorMsg, 3, 3);
                            }
                        }
                    });
                })
                // 返回上一页
                .on("click", '#J-back', function() {
                    window.history.go(-1);
                })
                // 删除行
                .on("click", '.btn-del', function() {
                    var $this = $(this),
                        maxLength = 10,
                        len = $(".otherRow").size();

                    $this.parent().remove();
                    if(len == maxLength) {
                        $(".otherRow:last-child").find(".btn-del")
                                .removeClass("btn-del").addClass("btn-add");
                    }
                })
                // 添加行
                .on("click", '.btn-add', function() {
                    var $this = $(this),
                        maxLength = 10,
                        len = $(".otherRow").size(),
                        clone;
                    if(len == maxLength-1) {
                        $this.removeClass("btn-add").addClass("btn-del");
                    }

                    if(len >= maxLength) return false;

                    clone = $this.parents(".otherRow").clone(true);
                    clone.find(".btn-add").removeClass("btn-add").addClass("btn-del");
                    $this.siblings("input[type=text]").val("");
                    $this.parent().before(clone);
                })

    });
</script>
<#include "layout/footer.ftl" >