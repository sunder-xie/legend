<#--
zmx 2016-08-12
天安保险车牌下拉框。

 -->



<style>
    .yqx-dl-no-data {text-align: center;}
    .yqx-dl-no-data > a{display:inline-block;margin:4px auto;background:#9fc527;padding: 5px;color:#fff;border-radius:3px;}
</style>
<script type="text/html" id="carLicenceTpl">
    <%if(templateData){%>
    <ul class="yqx-downlist-content js-downlist-content" data-tpl-ref="car-licence-tpl">
        <%for(var i=0;i<templateData.length;i++){%>
        <%var item=templateData[i];%>
        <li class="js-downlist-item">
            <span title="<%=item.license%>" style="width:100%"><%=item.license%></span>
        </li>
        <%}%>
    </ul>
    <%}%>
</script>
<script>
    $(function(){
        var $doc = $(document),
                elems = 'input[name="orderInfo.carLicense"],input[name="license"],input[name="plateNumber"]',
                elemsCopy = '';

        $doc
                .on('keyup', elems, function () {
                    elemsCopy = $(this).val();
                })
                .on('click', '.yqx-dl-no-data > a', function () {
                    var url = BASE_PATH + '/shop/customer/edit';
                    window.location.href = url + '?license=' + elemsCopy;
                    return false;
                });
    });
</script>