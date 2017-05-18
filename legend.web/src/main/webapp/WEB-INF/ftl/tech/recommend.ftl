<div class="recommend-box">
    <p class="recommend_tit">设备推荐</p>
    <ul class="recommend_list">
    </ul>
</div>

<script type="text/html" id="recommendProductTpl">
    <%for(var i = 0; i < json.length; i++) {%>
    <%var item = json[i];%>
    <li>
        <a href="<%=item.tqmallUrl%>" target="_blank">
            <div class="rItem_l">
                <img src="<%=item.pic%>"/>
            </div>
            <div class="rItem_r">
                <p class="goods_name"><%=item.name%></p>
            </div>
        </a>
    </li>
    <%}%>
</script>

<script charset="utf-8" type="text/javascript">
    // dom加载完毕后 触发渲染事件
    $(".recommend_list").ready(function () {
        $.ajax({
            type: 'GET',
            url: BASE_PATH + "/portal/tech_center/recommendproducts",
            data: {}
        }).done(function (json) {
            if (json['success']) {
                var html = template("recommendProductTpl", {json: json["data"]});
                $('.recommend_list').append(html);
            } else {
                $('.recommend_list').html('');
            }
        });
    });
</script>