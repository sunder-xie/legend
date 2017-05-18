/**
 * Created by huage on 2017/3/31.
 */
$(function () {
    seajs.use(['dialog','check'],function (dg,ck) {
        //点击确认提交按钮
        ck.init();
        $(document).on('click','.confirm-btn',function () {
            var data = {};
            var license_number = $.trim($('#license-number').val());
            //验证是否填写车牌
            if(!license_number){
                dg.warn('请填写车牌号，未取得车牌可备注"新车”');
                return false;
            }
            if(!ck.check()){
                return;
            }
            var pattern = /新车/g;
            if(pattern.test(license_number)){
                data.vehicleSn = '新车';
            }else{
                data.vehicleSn = license_number;
            }
            //验证商业险信息和交强险信息
            var flag = checkInfo(data);
            if(!flag){
                return false;
            }
            $.ajax({
                url:BASE_PATH+'/insurance/offline/entering/save',
                data:data,
                success:function (result) {
                    if(result.success){
                        dg.success('为了保证结算对账的准确性，后台人员会对保单进行审核，' +
                            '审核进度可进入已录入保单列表页进行查询');
                        setTimeout(function () {
                            document.location.reload();
                        },4000);
                    }else{
                        dg.fail(result.message)
                    }
                }
            })
        }).on('click','.cancel-btn',function () {
            window.location.href = BASE_PATH+'/insurance/offline/list';
        });
        
        
        //验证商业险信息和交强险信息 方法
        function checkInfo(data) {
                var all_Arr = [],
                    jq_must = [],
                    sy_must = [],
                    selective_jq = [],
                    selective_sy = [],
                    msg,
                    flag=true;
            $('.must-input,.selective-input').each(function () {
                var $this = $(this),
                    val = $.trim($this.val());
                if(!val){
                    return;
                }
                all_Arr.push($this.val());
                if($this.hasClass('must-input')){
                    if($this.hasClass('sy-must') && val){
                        sy_must.push(val);
                    }
                    if($this.hasClass('jq-must')){
                        jq_must.push(val);
                    }
                }else{
                    if($this.hasClass('selective-jq')){
                        selective_jq.push(val);
                    }
                    if($this.hasClass('selective-sy')){
                        selective_sy.push(val);
                    }
                }
            });
                msg = '商业险和交强险至少完善其中一项';
                if(all_Arr.length<1){
                    info_Prompt(msg);
                    flag = false;
                    return false;
                }

                if(jq_must.length>=1 && jq_must.length<2 ||jq_must.length<1&&selective_jq>0){
                    msg = '交强险的必填项未填写完整';
                    info_Prompt(msg);
                    flag = false;
                    return false;
                }else if(jq_must.length<1){
                    msg = '商业险的必填项未填写完整';
                    if(sy_must.length<1 && selective_sy.length>0){
                        info_Prompt(msg);
                        flag = false;
                        return false;
                    }
                }else{
                    msg = '商业险的必填项未填写完整';
                    if(sy_must.length<1 && selective_sy.length>0){
                        info_Prompt(msg);
                        flag = false;
                        return false;
                    }
                    getInputVal('.Info-wrap-jq',data)
                }
                if(sy_must.length>=1){
                    getInputVal('.Info-wrap-sy',data)
                }
                return flag;
        }
        
        //信息提示
        function info_Prompt(msg) {
                dg.confirm('抱歉，'+msg+'，请完善必填项后再提交',function () {
                },['我知道了']);
        }
        //拿到对应一个表内的多有input框里面的值；
        function getInputVal(Info_wrap,data) {
            $(Info_wrap).find('input').each(function () {
                var $this = $(this),
                    val = $this.val(),
                    data_val = $this.data('valname');
                if(val){
                    data[data_val] = $this.val();
                }
            })
        }
    })
});
