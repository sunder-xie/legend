<script type="text/html" id="commonDialogTpl">
    <style>
        .dg-scroller {
            position: relative;
            height: 405px;
            margin-right: -10px;
            padding: 10px 20px 10px 10px;
            overflow: scroll;
        }

        .dg-protocol-box {
            line-height: 1.4;
        }

        .dg-protocol-box h1 {
            font-size: 16px;
            font-weight: 700;
            color: #333;
        }

        .dg-protocol-box h2 {
            font-size: 14px;
        }

        .dg-protocol-box h3 {
            margin: 5px 0;
            font-size: 14px;
            font-weight: 700;
        }

        .dg-protocol-box h4 {
            margin-left: 20px;
        }

        .dg-protocol-box ol ol {
            margin-left: 20px;
        }

        .list-styled {
            padding-left: 20px;
        }

        .list-styled > li {
            list-style: decimal;
        }
    </style>
    <div class="yqx-dialog">
        <div class="yqx-dialog-header">
            <h1 class="yqx-dialog-headline">{0}</h1>
        </div>
        <div class="yqx-dialog-body">
            <div class="dg-scroller">{1}</div>
        </div>
    </div>
</script>