var type= $.xljUtils.getUrlParam('type');
var regInput=true;
$(function () {
	pageInit();
	if(type=="add"){
		$("#reverfile").show();
		$("#commonToolsTitle").html("图标管理-新增");
		document.title="图标管理-新增";
	}else{
		$("#reverfile").show();
		$("#commonToolsTitle").html("图标管理-修改");
		document.title="图标管理-修改";
	}
	$("#saveBtn").on('click',function(){
		$("#commonTools").attr("data-validate-success","saveForm('over')");
		$("#commonTools").submit();
	});

	$("#saveAndCreateBtn").on('click',function(){
		$("#commonTools").attr("data-validate-success","saveForm('continue')");
		$("#commonTools").submit();
	});
});
function newFile(target) {
	//判断文件类型
	var filePath = target.value;
	if(filePath!=""){
		var fileType=(filePath.substr(filePath.lastIndexOf("."))).toLowerCase();
		if(fileType!=".jpg"&&fileType!=".gif"&&fileType!=".jpeg"&& fileType!=".png"){
			pop_tip_open("blue","您上传图片的类型不符合(.jpg|.jpeg|.gif|.png)！");
			clearPic();
			return false;
		}
	}

	document.getElementById('newImg').setAttribute('width',"80px");
	document.getElementById('newImg').setAttribute('hight',"80px");
	$('#newImg').css('margin-top',"20px");
	$('#newImg').css('margin-bottom',"-10px");
	try{
		var windowURL = window.URL || window.webkitURL;
		var loadImg = windowURL.createObjectURL(document.getElementById('photoPic').files[0]);
		document.getElementById('newImg').setAttribute('src',loadImg);
	}catch(e){
		document.getElementById('newImg').setAttribute('src',"../../common/img/default.png");
		var div = document.getElementById('newImg');
		document.getElementById('file').select();
		document.getElementById('file').blur();
		top.parent.document.body.focus();
		var src = document.selection.createRange().text;
		div.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
		div.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = src;
	}
	$('#newImg').next().html("");
	$('#newImg').next().append("<a href='javascript:void(0)' onclick='clearPic()'>删除</a>");
	$("#isDelPic").val("1");
	$('#newImg').show();

	$("#file_name").val($("#photoPic").val());  //将 #file 的值赋给 #file_name
}
function clearPic(){
	$("#newImg").removeAttr("width");
	$("#newImg").removeAttr("height");
	$('#newImg').next().html("");
	$("#isDelPic").val("0");
	$('#newImg').hide();
	$('#photoPic').replaceWith('<input type="file" id="photoPic"  onchange="newFile(this)" name="pic" multiple="true" class="" accept="image/*"/>');
	$("#file_name").val("");
}
/**
 * author:liuf
 * describe:加载完毕后执行
 * param: null
 */
function pageInit(){
	if(type=="add"){
		//隐藏预览图片dom
		$('#newImg').hide();

		var uuid=getuuid();
		$("#sysCommonId").val(uuid);
	}else{
		var id = $.xljUtils.getUrlParam('id');
		getCommonToolsData(id)
	}
}
/**
 * author:liuf
 * describe:获取uuid
 * param: null
 */
function getuuid(){
	$.ajax({
		beforeSend:function(){
			var guuid="";
		},
		type:'get',
		async:false,
		url:hostUrl+'generator/getGuuid?time='+Math.random(),
		success: function(data) {
			if(data.success){
				guuid=data.result;
			}else{
				pop_tip_open("red",data.msg);
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.xljUtils.getError(jqXHR.status);
		},
		complete:function(){
		}
	});
	return guuid;
}
/**
 * author:liuf
 * describe:编辑回显数据
 * param: null
 */
function getCommonToolsData(id){
	$.ajax({
		url:hostUrl+"sys/iconTools/get/"+id+'?time='+Math.random(),
		type:'get',
		success: function(data) {
			if(data.success){
				var sysCommonTools=data.result;
				$("input[name='id']").val(sysCommonTools.id);
				$("input[name='name']").val(sysCommonTools.name);
				$("input[name='code']").val(sysCommonTools.code);
				$("#file_name").val(sysCommonTools.fullName);
				if(sysCommonTools.url){
					document.getElementById('newImg').setAttribute('src',sysCommonTools.url);
					document.getElementById('newImg').setAttribute('width',"80px");
					document.getElementById('newImg').setAttribute('hight',"80px");
					$('#newImg').css('margin-top',"20px");
					$('#newImg').css('margin-bottom',"-10px");
					$('#newImg').next().append("<a href='javascript:void(0)' onclick='clearPic()'>删除</a>");
					$("#isDelPic").val("1");
				}else{
					//隐藏预览图片dom
					$('#newImg').hide();
				}
			}else{
				pop_tip_open("red",data.msg);
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.xljUtils.getError(jqXHR.status);
		}
	});
}

function regData(ele){
	var placeholder=$(ele).attr("placeholder");
	var specialKey ="#$%\\'\\\\^*\"\+[]【】{}";
	var inputval=$(ele).val();
	if((specialKey.indexOf(inputval)>-1&&(inputval!=""))||inputval.indexOf("&lt;")>-1||inputval.indexOf("'")>-1||inputval.indexOf("&gt;")>-1){
		pop_tip_open("blue",placeholder+"不允许输入特殊字符");
		$(ele).val("");
		regInput=false;
		return;
	}
	var dataLength=$(ele).val().length;
	var dataMaxLength=$(ele).attr("data-maxlength");
	if(parseInt(dataLength)>parseInt(dataMaxLength)){
		pop_tip_open("blue",placeholder+"输入超长,请重新输入");
		$(ele).val("");
		regInput=false;
		return;
	}else{
		regInput=true;
	}
}
/**
 * author:liuf
 * describe:新增 修改数据
 * param: null
 */
function saveForm(btType){
	var  url = hostUrl+"sys/iconTools/save";
	if(type=="add"){
	}else{
		url = hostUrl+"sys/iconTools/update";
	}
	var ajax_option={
		url:url,//form 的action
		type:"post",  //form 的method方法
		//beforeSubmit:checkUppro,  //在表达提交前执行的验证函数
		contentType: "application/x-www-form-urlencoded; charset=utf-8",   //设置编码集
		success:function(data){  //表单提交成功后执行的函数
			formSubmitCallBack(data,btType);
		}
	}
	$('#commonTools').ajaxSubmit(ajax_option);
}
/**
 * form表单提交回掉方法
 */
function formSubmitCallBack(resultData,op){
	resultData = JSON.parse(resultData);
	if(resultData) {
		var commonToolsId = $('#sysCommonId').val();
		var successFlag = resultData.success;
		var result = resultData.result;
		var msg = resultData.msg;
		if(successFlag) {
			if(op=="over"){
				window.opener.location="javascript:_logoList.reloadGrid('"+commonToolsId+"');";
				window.opener=null;
				window.close();
			}else if(op=="continue"){
				window.opener.location="javascript:_logoList.reloadGrid('"+commonToolsId+"');";
				//window.opener=null;
				clearPic();
				$("#commonTools")[0].reset();
				var uuid=getuuid();
				$("#sysCommonId").val(uuid);
				type="add";
			}
		}else {
			pop_tip_open("red",resultData.msg);
		}
	}
}
/**
 * author:liuf
 * describe:关闭页面
 * param: null
 */
function closed(){
	window.close();
}
