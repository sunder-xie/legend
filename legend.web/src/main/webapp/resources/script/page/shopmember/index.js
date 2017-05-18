seajs.use(["tab","downlist"],function(tab){
	tab.init();
});
$(function(){
	/** 切换tab
	 *  wjc 2015.6.5
	 */
	
	
	//控制选择支付方式的修改
	$("input[name='cashBo.payMethod']").on("focus",function () {
	    var $this = $(this);
	    $this.attr("readonly","readonly");  
	});

	$("input[name='cashBo.payMethod']").on("blur",function () {
	   // $('#settle1').hide().empty();
	    //结算方式消失时候的作用
	    var $this = $(this);
	    $this.removeAttr("readonly");
	    
	});
	
	
	//控制选择支付方式的修改
	$("input[name='payMethod']").on("focus",function () {
	    var $this = $(this);
	    $this.attr("readonly","readonly");  
	});

	$("input[name='payMethod']").on("blur",function () {
	   // $('#settle1').hide().empty();
	    //结算方式消失时候的作用
	    var $this = $(this);
	    $this.removeAttr("readonly");
	    
	});


	
	
	$(".rows_p").on("click","li",function () {   
		var minPoint = $(this).attr("min-point");
		var maxPoint = $(this).attr("max-point");

		if(!maxPoint || maxPoint == "null" || maxPoint == null){
			maxPoint = "";
		}

		if(!minPoint || minPoint == "null" || minPoint == null){
			minPoint = "";
		}

		$("#minPoint").val(minPoint);
		$("#maxPoint").val(maxPoint);
		
		$(".search_btn").trigger('click');

		//$("#minPoint").attr("value","");
		//$("#maxPoint").attr("value","");
	});
	
	//弹窗
	$(".addNewMember").click(function () {
		$("input[name='customerName']").val("");
		seajs.use(["dialog"],function(dg){
			dialog = dg.dialog({
				"dom":"#addNew"
			});
		});
	});

	//查询级别填充到tab项。
	function selectLevel(){
		$(".level_tab").remove();
		seajs.use(["ajax"],function(ajax){
			ajax.post({
	    		url : BASE_PATH + "/member/tabInfo",
	    		success : function(json){
	    			if(json.success){
	    				if(json.data && json.data.length > 0){
	    					for(var i=0;i<json.data.length;i++){
	    						var item  = json.data[i];
	    						var html = '<li class="level_tab" min-point="'+item.minPoint+'"  max-point="'+item.maxPoint+'"><a href="javascript:void(0);">'+item.levelName+'(<font class="red">'+item.count+'</font>)</a></li>';
	    						$(".rows_p ul").append(html);
	    					}
	    				}    				
	    			}
	    		}
	    	});
		});
	}
	selectLevel();

    //会员级别弹窗
    $(".addMemberLevel").click(function () {
        seajs.use(["dialog","ajax","artTemplate"],function(dg,ajax,template){
        	dialog = dg.dialog({
                "dom":"#addLevel"
            });
        	ajax.post({
        		url : BASE_PATH + "/member/level/list",
        		success : function(json){
        			if(json.success){
        				//回填上次级别设置的数据。
        				var html = "";
        				if(json.data && json.data.length > 0){
        					html = template.render("addLevelTpl",{"templateData":json.data});
        					$("#addLevel").html(html);
        				}else{
        					html = template.render("addLevelTpl",{"templateData":[0]});
        				}
        				$("#addLevel").html(html);
        			}
        		}
        	});
        });
    });
	
	//更改customerName
	$("#customerNameInput").change(function(){
		$("input[name='customerName']").val($("#customerNameInput").val());
	});

	//会员级别弹窗中的事件触发
	$(document).on("click", ".sub_row_ico", function() {
		$(this).parents(".form_item").remove();
	}).on("click", ".add_row_ico", function() {
		var $this = $(this),
			blankRow = $this.parents(".form_item").clone(true);
		blankRow.find("input[name!='imgUrl']").val("");
		$("input[name='imgUrl']").val("resources/images/normal-card.jpg");
		$(".img_preview",blankRow).attr("src","resources/images/normal-card.jpg");
		$(".pop_member_list").append(blankRow);
		$this.addClass("sub_row_ico").removeClass("add_row_ico");
	}).on("click", "#addLevel .save", function() {
		var scope = $(".pop_member_list");
		var data = {};
		var levelStr = [];

		seajs.use(["check","ajax","dialog"],function(c,ajax,dialog){
			if(!c.check()){
				return;
			}
			$("ul:gt(0)",scope).each(function(){
				var ulScope = $(this);
				var data = {};

				//级别名称
				var levelName = $.trim($("input[name='levelName']",ulScope).val());
				if(levelName != ""){
					data["levelName"]  = levelName;
				}
				//开始积分
				var minPoint = $.trim($("input[name='minPoint']",ulScope).val());
				if(minPoint != ""){
					data["minPoint"]  = minPoint;
				}
				//结束积分
				var maxPoint = $.trim($("input[name='maxPoint']",ulScope).val());
				if(maxPoint != ""){
					data["maxPoint"]  = maxPoint;
				}
				//级别图片地址
				var imgUrl = $.trim($("input[name='imgUrl']",ulScope).val());
				
				if(imgUrl != ""){
					data["imgUrl"] = imgUrl;
				}
				if(levelName != "" && minPoint !="" && maxPoint !=""){
					levelStr.push(data);
				}
			});
			levelStr = JSON.stringify(levelStr);
			data.levelStr = levelStr;
			ajax.post({
				url:BASE_PATH+"/member/level/add",
				data : data,
				success : function(json){
					if(json.success){
						dialog.info("级别设置成功",1,2,function(){
							selectLevel();
						},true);
						dialog.closeDialog("#addLevel");
					}else{
						dialog.info(json.errorMsg,3,2);
					}
				}
			});
		});
	}).on("click", ".qxy_file", function() {
		//图片上传
		var $this = $(this),
			$imgBox = $this.parents(".img_box");
		$this.fileupload({
			url: BASE_PATH + '/index/oss/upload_image_name',
			dataType: 'json',
			acceptFileTypes: /(\.|\/)(gif|jpe?g|bmp)$/i,
			autoUpload: false,
			add: function (e, data) {
				data.submit()
					.success(function (data, textStatus, jqXHR) {
						if (data.success) {
							var imgInfo = data.data;
							for (var fileName in imgInfo) {
								var imgPathObj = imgInfo[fileName];
								var thumb = imgPathObj['thumb'];
								$(".img_preview", $imgBox).prop("src", thumb);
								$(".img_hidden", $imgBox).val(thumb);
							}
						} else {
							//alert(data.errorMsg);
						}
					});
			}
		});
	});

	$("body").on("click",".urlBtn",function(){
		window.location.href = $(this).data("url");
	});
});

function payDlg($this){
	var $chargeForm = $("#chargeForm");
	$chargeForm.find("input").val('');
	$1dynamic = $chargeForm.find("select:eq(0)").parents('.form_item').find('.field_box a');
	$1dynamic.removeClass('qxy_del_icon').addClass('qxy_add_icon');
	$chargeForm.find("select:gt(0)").each(function(i,e){
		$(e).parents('.form_item').remove();
	});
	$chargeForm.find("select:eq(0)").find("option:eq(0)").attr("selected",true);

	var id = $($this).attr('rel_id');
	var amount = $($this).attr('rel_amount');
	$("#memberId").val(id);
	$("#amount").html(amount+"元");
	seajs.use(["dialog"],function(dg){
		dialog = dg.dialog({
			"dom":"#payDlg"
		});
	});
}
function charge(){
    /*
    * modify by sky 2016-04-11
    * 充值提交 start
    */
    seajs.use(["ajax", "formData", "dialog", "check"], function (ajax, fd, dg, check) {
        var id_form = 'chargeForm',
            $form = $('#' + id_form),
            // 获取表单数据
            _data = fd.get(id_form),
            _check = check.check(id_form),
            url = $form.attr('action'),
            serviceSuiteStr, i, tmp;

        if (!_check) {
            return;
        }

        /* form特殊处理 */
        serviceSuiteStr = (_data&&_data.serviceSuiteStr)&&JSON.parse(_data.serviceSuiteStr);
        $form.find('select[name="id"]').each(function(i, v) {
			var name = $(this).find('option:selected').text();
            if (serviceSuiteStr[i]) {
				serviceSuiteStr[i]['name'] = name;
			}
        });
        _data['serviceSuiteStr'] = JSON.stringify(serviceSuiteStr);

        ajax.post({
            url: url,
            data: _data,
            contentType: 'application/x-www-form-urlencoded',
            loadShow: true,
            loadText: '正在保存信息中...',
            success: function (result) {
                if (result.success) {
                    dg.info("操作成功", 1, 2, function () {
                        $("#payDlg").find("input").val('');
                        seajs.use('table', function (table) {
                            table.fill("chain_member_table");
                        });
                        layer.closeAll();
                    }, true);
                } else {
                    dg.info(result.errorMsg, 3);
                }
            }
        })
    });
    /* 充值提交 end */
}

function closeDlg(){
	$("#payDlg").find("input").val('');
	layer.closeAll();
}





