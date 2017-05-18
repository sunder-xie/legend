/**
 * Created by majian on 16/3/16.
 */
$(document).on("click", '.delete_member_btn', function(e){
    e.stopPropagation();
    var $this = $(this),
        $pare = $this.parents('.card'),
        id = $this.siblings('.memberId').val(),
        count, layerId;

    seajs.use(['ajax', 'dialog'], function(ajax, dg) {
        dg.confirm("确认删除?", function() {
            count = $pare.find('.expenseCount').text();

           /* if(count>0) {
                dg.info('消费次数大于0不能删除!');
                return false;
            }*/

            layerId = dg.load('删除中...');

            $.ajax({
                type : "POST",
                url : BASE_PATH + "/member/delete",
                data : {id : id},
                dataType : "json",
                success : function (data) {
                    dg.close(layerId);

                    if (data.success == true) {
                        dg.info('删除成功', 1);
                        $pare.remove();
                    }else {
                        dg.info(data.errorMsg, 3);
                    }
                }
            });
        });
    });
});