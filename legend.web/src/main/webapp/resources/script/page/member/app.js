$(function(){

	if($("#isFail").val() == "true"){
		$("input,select").attr("disabled", "disabled");
		$(".app_info").remove();
		seajs.use(["dialog"], function(dg){
			dg.info("该店铺信息未保存在CRM中.",5)
		});
	}
	$(".clockpicker").clockpicker();

	var upload = function(btn,type,text) {
		seajs.use(["upload", "dialog"], function (a, dg) {
			a();
			var layerIndex = null;
			$(btn).uploadifive({
				buttonClass: "img_add_btn",
				width: 120,
				height: 120,
				buttonText: '',
				uploadScript: BASE_PATH + '/index/oss/crm_upload_image',
				onSelect: function (obj) {
					layerIndex = dg.load("图片正在上传中...");
				},
				onUploadComplete: function (file, data) {
					layerIndex != null && dg.close(layerIndex);
					data = eval('(' + data + ')');
					if (data.success) {
						var scope = $(btn).parents(".img_box");
						var imgInfo = data.data;
						var html = $(".img_clone",scope).clone();
						$(".img_cont",scope).prepend(html);
						if(type == 1){
							$(".img_item_sa:eq(0)",scope).removeClass("hide").removeClass("img_clone");
							$(".img_item_sa:eq(0) img",scope).attr("src",imgInfo.original);
							$(".img_add_btn",scope).hide();
						} else {
							$(".img_item:eq(0)",scope).removeClass("hide").removeClass("img_clone");
							$(".img_item:eq(0) img",scope).attr("src",imgInfo.original);
	                // getImgList();
						}
					}
				}
			});
		});
	}

	upload(".file_btn_xx");
	upload(".reception_file_btn",1);

	var getImgList = function(){
        var arr = [];
		var orderIdx = 0;
        $(".img_item").each(function(){
        	var $this = $(this);
        	var customerJoinAuditId = $("input[name='id']").val();
        	if(!$this.hasClass('.img_clone') && $this.find(".file_btn").size()<1){
        		var src = $this.find("img").attr("src");
        		var name = $this.find("input").val();
        		var id = $this.data("id");
        		var obj;
        		if(id!=undefined){
        			obj = {
	        			"imgUrl" : src,
	        			"remarks" : name,
	        			"id" : id,
	        			"customerJoinAuditId" : customerJoinAuditId
	        		}
        		}else{
        			obj = {
	        			"imgUrl" : src,
	        			"remarks" : name,
	        			"customerJoinAuditId" : customerJoinAuditId
	        		}
        		}
				obj.orderIdx = orderIdx++;
        		if(src != null && src!=""){
        			arr.push(obj);
        		}
        	}
        });
        return arr;
	}

	var getSaImg = function(){

		var saArr = [];
		$(".img_item_sa").each(function(){
			var $this = $(this);
			if(!$this.hasClass('.img_clone')){
				var src = $this.find("img").attr("src")
				if(src!=undefined && src!=""){
					saArr.push(src);
				} else {
					saArr.push("");
				}
			}
		});
		return saArr;
	}

	$(".app_info").click(function(){
		var arr = getImgList();
		//组装saImg
		var saArr = getSaImg();
		seajs.use(["ajax","formData","dialog","check"],function(ajax,formData,dialog,check){
			var datas = formData.get2("#app_info");
			if(arr.length == 0){
				$("input[name='customerFilePathList']").val("");
			}else{
				datas["customerFilePathList"] = arr;
			}

			if(saArr.length == 0){
				$("input[name='saImg']").val("");
			}else{
				datas["saImg"] = saArr[0];
			}

			delete datas.remarks;
			delete datas.companyFormalName;
			//表单全局验证
			var result = check.check("#app_info");
			if(!result){
				return;
			}
            //判断地址经纬度是否发生变化做出提醒
            var _lng = $("input[name='longitude']").val();
            var _lat = $("input[name='latitude']").val();


            var sendData = function(){
                ajax.post({
                    url: BASE_PATH + "/shop/member/app/save",
                    contentType: "application/json",
                    data : JSON.stringify(datas),
                    success : function(json){
                        if(json.success){
                            dialog.info("保存成功",1,1,function(){
                            	history.go(0);
                            },true);
                        }else{
                            dialog.info(json.errorMsg,1,1,null,false);
                        }
                    }
                });
            }

            if(oldPoint.lng!=_lng || oldPoint.lat != _lat){
                dialog.confirm("门店坐标发生变化，是否确认保存？",function(){
                    sendData();
                },null);
            }else{
                sendData();
            }
		});
	});

	$("body").on("click",".img_del_btn",function(){
		var parent = $(this).parents(".img_item");
		if(parent.length == 0){
			parent = $(this).parents(".img_item_sa");
			var box = parent.parents(".img_box").parent();
			box.find(".img_add_btn").show();
		}
		var id = parent.data("id");
		if(id!=undefined){
			seajs.use(["ajax","dialog"],function(ajax,dialog){
				ajax.post({
					url:BASE_PATH + "/shop/member/app/delete",
					data:{id:id},
					success:function(json){
						if(json.success){
							dialog.info("图片删除成功",1);
							parent.remove();
						}else{
							dialog.info("图片删除失败",1);
						}
					}
				});
			});
		}else{

			parent.remove();
		}
	}).on("mouseover", ".img_item_inner", function(){
		if($(this).find("div").size() == 0) {
			$(this).find("img").after($("#sortDiv").html());
		} else {
			$(this).find("div").eq(0).show();
		}
	}).on("mouseout", ".img_item_inner", function(){
		$(this).find("div").eq(0).hide();
	}).on("click", ".left_sort", function() {
		var $e = $(this).parents(".img_item");
		var $prev = $e.prev(".img_item");
		if($prev.size() == 0) {
			var $last = $e.nextAll(".img_item").eq(-3);
			console.log($last);
			if($last != $e) {
				$e.insertAfter($last);
			}
		} else {
			$e.insertBefore($prev);
		}
	}).on("click", ".right_sort", function(){
		var $e = $(this).parents(".img_item");
		var $after = $e.next(".img_item:has(div.img_item_inner)").not(".hide");
		console.log($after);
		if($after.size() == 0) {
			var $first = $e.prevAll(".img_item").last();
			if($first != $e) {
				$e.insertBefore($first);
			}
		} else {
			$e.insertAfter($after);
		}
	});


	var map = new BMap.Map("map"); 
	var point = new BMap.Point(116.404, 39.915);
	var lng = $("input[name='longitude']").val();
	var lat = $("input[name='latitude']").val();
    var oldPoint = {"lng":lng,"lat":lat};
	var marker;
	if(lng!="" && lat!=""){
		point = new BMap.Point(Number(lng), Number(lat));
		marker = new BMap.Marker(new BMap.Point(Number(lng), Number(lat)));        // 创建标注    
		map.addOverlay(marker);
	}
	map.centerAndZoom(point, 11); 
	
	map.addEventListener("click", function(e){ 
		$("input[name='longitude']").val(e.point.lng);
		$("input[name='latitude']").val(e.point.lat);
		// var a = (e.point.lng + ", " + e.point.lat); 
		// alert (a);
		map.removeOverlay(marker);     

		marker = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat));        // 创建标注    
		map.addOverlay(marker); 
		marker.enableDragging();
		map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM}))
		map.enableScrollWheelZoom(true);
		marker.addEventListener("dragend", function(e){
		 	$("input[name='longitude']").val(e.point.lng);
			$("input[name='latitude']").val(e.point.lat);  
		});     
	});
	// map.enableScrollWheelZoom();
	//经纬度转百度
	// var url="http://api.map.baidu.com/geoconv/v1/?coords=x,y&from=1&to=5&ak=cSGY6d9xfcvZdNIdGA38Y9zZ"
	// $.get(url,function(result){
	// 		result.x;
	// 		result.y; //百度的
	// });
	//百度转地址
	// var url="http://api.map.baidu.com/geocoder/v2/?ak=cSGY6d9xfcvZdNIdGA38Y9zZ&callback=renderReverse&location=39.983424,116.322987&output=json"



	var getHtml = function(id,text){
		var html = '<div class="majorCarBrand_item" data-id="'+id+'">'+text+'<i></i></div>';
		return html;
	}

	//拼接字符串
	var writeStr = function(){
		var majorCarBrand = $("input[name='majorCarBrand']");
		var str = "";
		$(".majorCarBrand_item").each(function(){
			var id = $(this).data("id");
			var text = $(this).text();
			str += id+":"+text+";";
		});
		majorCarBrand.val(str);
	}

	//专修品牌 
	$(document).on("change","#majorCarBrand",function(){
		var text = $("option:selected",$(this)).text();
		var id = $(this).val();
		
		if(id == ""){
			return;
		}

		// 放品牌的容器
		var nextFormItem = $(this).parents(".form_item").next();
		var majorCarBrandItem = $(".majorCarBrand_item",nextFormItem);
		var size = majorCarBrandItem.size();
		if(size >= 5){
			seajs.use("dialog",function(dg){
				dg.info("最多只能添加5个品牌",3);
			});
			return;
		}
		var bool = true;
		majorCarBrandItem.each(function(){
			var _id = $(this).data("id");
			if(id == _id){
				seajs.use("dialog",function(dg){
					dg.info("请勿重复添加品牌",3);
				});
				bool = false;
				return;
			}
		});
		if(!bool) return;
		$(".field_box",nextFormItem).append(getHtml(id,text));
		if(text != ""){
			writeStr();
		}
	});

	//删除专修品牌
	$(document).on("click",".majorCarBrand_item i",function(){
		var parent = $(this).parent();
		parent.fadeOut(function(){
			parent.remove();
			writeStr();
		});
	});

	//专修品牌的回填。
	(function(){
		var majorCarBrand = $.trim($("input[name='majorCarBrand']").val());
		// majorCarBrand = "8:安驰;14:保斐利;7:阿斯顿马丁;";
		if(majorCarBrand!="" && majorCarBrand!=null && majorCarBrand!="null"){
			var arr = majorCarBrand.split(/;/g);
			var html = "";
			var nextFormItem = $("#majorCarBrand").parents(".form_item").next();
			for(var i=0;i<arr.length;i++){
				if(arr[i]==""){
					continue;
				}
				var temp = arr[i].split(/:/g);
				html += getHtml(temp[0],temp[1])
			}
			$(".field_box",nextFormItem).append(html);
		} 
	})();
});

window.onload=function(){
	if($("#saImgStr").val() != "" ){
		var saImg = $("#saImg");
		var scope = saImg.parents(".img_box");
		var html = $(".img_clone",scope).clone();
		$(".img_cont",scope).prepend(html);
		var src = $("#saImgStr").val();
		$(".img_item_sa:eq(0)",scope).removeClass("hide").removeClass("img_clone");
		$(".img_item_sa:eq(0) img",scope).attr("src", src);
		$(".img_add_btn",scope).hide();
	}
}