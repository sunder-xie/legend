<script>
    $(document).on('click', '.js-insurance-item', function () {
        var $this = $(this),
                model = $this.data('anxinModel'),
                link = $this.attr('href');
        if (model == null || model === "") {
            seajs.use('dialog', function(dg) {
                var dgId = dg.confirm('抱歉，您还没有报名参加保险售卖活动，如有疑问，请联系淘汽销售人员进行咨询', function () {
                    dg.close(dgId);
                }, ['知道了']);
            });
        } else {
            location.href = link;
        }
        return false;
    });
</script>