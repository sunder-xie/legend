<html>
<head>
    <meta name="viewport" content="width=device-width, minimum-scale=0.1">
    <link rel="stylesheet" href="${BASE_PATH}/resources/css/technology_center/book_preview_full.css"/>
</head>
<body style="margin: 0px;">

<div class="pdf-box" >
    <button type="button" class="prev_img-btn prev-btn"></button><#--
            -->
    <div class="pdf-content" id="content">

    </div>
    <script type="text/html" id="contentTemplate">
        <%for(var index in templateData){%>
        <%item = templateData[index]%>
        <img src="<%=item.pic%>" alt="<%=item.title%>"/>
        <%}%>
    </script>
<#--
-->
    <button type="button" class="next_img-btn next-btn"></button>
</div>
<input type="hidden" name="search_bookId" id="search_bookId" value="${book.id}"/>
<input type="hidden" name="page" id="page" value="${page?default(1)}"/>
</body>
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/legend_common.js?5c2ee7d21ab23dfa1a36c733be4eccd3"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.js"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.form.js"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.json.min.js"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/layer/layer.min.js?20547d53fa61b28dcf6645462ebc8907"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/Dao.js?91adfa36bf24157d8750feee19c20436"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/artTemplate/artTemplate.js"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/artTemplate/artTemplate-plugin.js"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/popup.js?3a084ca1d606e27e6547c63f5ba59424"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/book/book_info.js?0c57056f741fe8441354fb8d290af6c6"></script>
</html>