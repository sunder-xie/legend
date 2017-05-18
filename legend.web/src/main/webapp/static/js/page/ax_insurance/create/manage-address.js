/**
 * Created by huage on 2017/4/26.
 */
seajs.use(["dialog", "art"], function (dg, at) {
    var manageAdd,
        data,
        manage_address_list,
        manage_address_edit;
    // 获取常用地址列表
    query_list();
    var $doc = $(document);
    //点击 管理常用地址
    $doc.on('click', '.manage-address', function () {
       if(manageAdd){
           manage_address_list =  dg.open({
               title:'我的常用地址',
               content: manageAdd
           });
       }else{
            dg.fail('读取常用地址失败')
       }



    });

    //点击 编辑地址
    $doc.on('click','.edit-address-btn',function () {
        dg.close(manage_address_list);
        manage_address_edit = dg.open({
            title: '编辑常用地址',
            content: $('#manage-address').html()
        });
        $('.layui-layer-content').find('.save-btn').addClass('update-btn');
        getReceiveAddressFill(data);
    });

    //点击  添加地址
    $doc.on('click','.add-address-btn',function () {
        dg.close(manage_address_list);
        manage_address_edit = dg.open({
            title: '添加常用地址',
            content: $('#manage-address').html()
        });
        $('.layui-layer-content').find('.save-btn').addClass('create-btn');
        getReceiveAddressFill(data);
    });

    //点击 删除地址
    $doc.on('click','.remove-address-btn',function () {
        var addressId = $('.choose-tr').data('id');             //被选中的地址id
        $.ajax({
            url:BASE_PATH + '/insurance/anxin/address/delete',
            data:{
                addressId:addressId
            },
            success:function (result_remove) {
                if(result_remove.success){
                    dg.success('删除成功');
                    query_list(true)
                }else{
                    dg.fail(result_remove.message)
                }
            }
        });

    });

    //点击  保存
    $doc.on('click','.save-btn',function () {
        var url,
            addressId = $('.manage-address-box .receiverName').data('addressid');
        if($(this).hasClass('create-btn')){
            url= BASE_PATH + '/insurance/anxin/address/create';
        }else{
            url= BASE_PATH + '/insurance/anxin/address/update';
        }
        save_data(url,addressId);
    });

    //将获取到的信息填充到页面上
    function getReceiveAddressFill() {
        var receiver = {
            receiverName : '',
            receiverPhone :'',
            receiverProvince: '请选择省',
            receiverProvinceCode:'',
            receiverCity:'请选择市',
            receiverCityCode:'',
            receiverArea: '请选择区',
            receiverAreaCode : '',
            receiverAddr:'',
            id:''
        };
        if(!data){
            var dataAll = [];
            dataAll.push(receiver);
            data = dataAll[0];
        }
        $('.all_inf1 .receiverName').val(data.receiverName);
        $('.all_inf1 .receiverPhone').val(data.receiverPhone);
        $('.manage-address-box .receiverPhone').val(data.receiverPhone);
        $('.manage-address-box .receiverName').val(data.receiverName).data('addressid',data.id);
        $('.receiverProvince').text(data.receiverProvince).data('code', data.receiverProvinceCode);
        $('.receiverCity').text(data.receiverCity).data('code', data.receiverCityCode);
        $('.receiverArea').text(data.receiverArea).data('code', data.receiverAreaCode);
        $('.receiverAddr').val(data.receiverAddr)

    }

      // 查询列表 的方法
    function query_list(mask) {
        $.ajax({
            url: BASE_PATH + '/insurance/anxin/address/getFormReceiveAddressList',
            success: function (query_result) {
                if (query_result.success) {
                    data = query_result.data[0];
                    getReceiveAddressFill(data);
                    manageAdd = at("manageAddTpl", query_result);
                    if(mask){
                        dg.close(manage_address_edit);
                        dg.close(manage_address_list);
                        manage_address_list = dg.open({
                            title:'我的常用地址',
                            content: manageAdd
                        });
                    }
                } else {
                    dg.fail(query_result.message)
                }
            }
        });
    }

    //保存  方法
    function save_data(url,addressId) {
        var flag_data;
        var manage_address = $('.layui-layer-content'),
            receiver_Name = manage_address.find('.receiverName').val(),
            receiver_Phone = manage_address.find('.receiverPhone').val();
        if(!receiver_Name){
            dg.warn('请填写收件人姓名');
            return false;
        }
        if(!receiver_Phone){
            dg.warn('请填写收件人手机号');
            return false
        }
        flag_data = Insurance.shipping_Address(dg,manage_address);
        if(!flag_data){
            return false;
        }
        flag_data['receiverName'] = receiver_Name;
        flag_data['receiverPhone'] = receiver_Phone;
        if(addressId){
            flag_data['id'] = addressId;
        }
        $.ajax({
            url:url,
            type: "POST",
            data:flag_data,
            success:function (add_result) {
                if(add_result.success){
                    dg.success('保存成功');
                    query_list(true);
                }else{
                    dg.fail(add_result.message)
                }
            }
        })
    }
});
