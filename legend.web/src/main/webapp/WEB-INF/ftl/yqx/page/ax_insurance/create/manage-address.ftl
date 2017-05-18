<!--管理常用地址 页面-->
<style type="text/css">
    #manage-address {
        display: none;
    }

    .layui-layer-content {
        height: auto !important;
    }

    .manage-address-box {
        padding: 40px 60px;
        position: relative;
    }

    .manage-btn-style {
        width: 110px;
        height: 35px;
        border: #f55d5e;
        border-radius: 3px;
        background-color: #fa7b7c;
        color: #FFFFFF;
        line-height: 35px;
        text-align: center;
        display: inline-block;
        cursor: pointer;
        margin-bottom: 20px;
    }
    .manage-table,.no-data-div{
        min-width: 400px;
        min-height: 200px;
        border: none;
    }

    .no-data-div{
        line-height: 200px;
        text-align: center;
        font-size: 14px;
        border-bottom: 1px solid #d1d1d1;
        margin-bottom: 10px;
    }

    .manage-table td{
        padding: 10px 15px;
    }

    .remove-btn-style{
        width: 110px;
        height: 35px;
        background-color: #f9f9f9;
        border: #d2d2d2 1px solid;
        border-radius: 3px;
        line-height: 33px;
        text-align: center;
        font-size: 14px;
        color: #666666;
        float: right;
        cursor: pointer;
    }

    .save-btn {
        margin-left: 45%;

    }
    .edit-address-btn{
        margin: 10px 0 10px 40px;
    }
    .remove-address-btn{
        margin: 10px 40px 10px 0;
    }
    .add-address-btn{
        margin-left: 35%;
    }
    .manage-address-box .min-width{
        min-width: 690px;
    }
</style>
<div id="manage-address">
    <div class="manage-address-box">
        <div class="margin min-width">
            <span class="all_inf_w label-must">收件人姓名</span>
            <input type="text" class="all_input receiverName">
            <span class="all_inf_w label-must">手机号</span>
            <input type="text" class="all_input receiverPhone">
        </div>
        <div class="margin">
            <span class="all_inf_w label-must">收件人地址</span>
            <div class="prevK">
                <div class="prev receiverProvince">请选择省</div><div class="zj_btn s_up choose_btn"></div>
                <ul class="prevUl dis"></ul>
            </div>
            <div class="cityK">
                <div class="city receiverCity">请选择市</div><div class="zj_btn s_up choose_btn"></div>
                <ul class="cityUl dis"></ul>
            </div>
            <div class="areaK">
                <div class="area receiverArea">请选择区</div><div class="zj_btn s_up choose_btn"></div>
                <ul class="areaUl dis"></ul>
            </div>
        </div>
        <div class="margin">
            <span class="all_inf_w label-must"></span>
            <input class="detail_addr receiverAddr" placeholder="请输入具体地址">
        </div>
    </div>
    <div class="save-btn manage-btn-style">保存</div>

</div>
<script type="text/html" id="manageAddTpl">
    <%if(success && data){%>
        <%if(data.length<=0){%>
            <div class="no-data-div">暂无常用地址,请添加</div>
            <div class="manage-btn-style add-address-btn">添加新地址</div>
        <%}else{%>
        <table class="manage-table">
            <%for(var i = 0;i< data.length;i++){%>
           <tr data-id="<%= data[i].id%>" class="choose-tr">
               <td><%= data[i].receiverName%></td>
               <td><%= data[i].receiverPhone%></td>
               <td><%= data[i].receiverProvince%></td>
               <td><%= data[i].receiverCity%></td>
               <td><%= data[i].receiverArea%></td>
               <td><%= data[i].receiverAddr%></td>
           </tr>
            <%}%>
        </table>
        <div class="manage-btn-style edit-address-btn">编辑地址</div>
        <div class="remove-btn-style remove-address-btn">删除地址</div>
        <%}%>
    <%}else{%>
        <div class="no-data-div">暂无常用地址,请添加</div>
        <div class="manage-btn-style add-address-btn">添加新地址</div>
    <%}%>
</script>
<script type="application/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/create/manage-address.js?6862083e942214ac50a2df51bca4ddda"></script>

