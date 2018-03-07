/**
 * @author: peter <zhengjj_2009@126.com>
 * @date: 2017-03-24
 */

/**
 * 此文件实现业务对象的业务变量的列表及相关操作
 */

var dataType;
var businessObjectId;
var lastSel_dataId;
var firstType;
var currentUserLoginName;
/**
 * 页面JS的执行入口处
 */
$(function() {

	//跨租户使用，暂时注释掉
	//initUserInfo();
	dataType = $.getUrlParam('dataType');
	disableDatetimepicker(dataType);
	firstType = $.getUrlParam('firstType');
	initTaskGridList();
    //根据dataType的值选择对应的下标
	if($("#queryType option[value='"+dataType+"']")[0]){
    	$("#queryType option[value='"+dataType+"']")[0].selected=true;
	}

	if($("#firstType option[value='"+firstType+"']")[0]){
    	$("#firstType option[value='"+firstType+"']")[0].selected=true;
	}

	$('#firstType').on('change',function () {
		$('#queryType option[value="-1"]')[0].selected=true;
		dataType = null;
	});

//	$('#queryType').on('change',function () {
//		$('#firstType option[value=""]')[0].selected=true;
//		firstType = null;
//	});
	initDatetimepicker();
	function ie8Match() {
		if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i) == "8.") {
			return true;
		}

		return false;
	}

	if(ie8Match()){
		$('.form-group').find('label').css('display','inline');
	}
	
	//删除我的发起任务
	$('#deleteFqTaskBtn').on('click',function () {
		var selRowIds = $('#jqgridList').jqGrid('getGridParam','selarrrow');

		if (!selRowIds || selRowIds.length == 0) {
			$.xljUtils.tip('blue', "请选择要删除的行！");
			return;
		}

		for (var i = 0; i < selRowIds.length; i++) {
			var selId = selRowIds[i];
			var rowData = $('#jqgridList').jqGrid('getRowData',selId);
			if(rowData.opType!='FQ'){
				$.xljUtils.tip('blue', "只能删除我的发起类型消息！");
				return;
			}
		}

		var tipText = "确定要删除这" + selRowIds.length + "条数据吗？";
		$.xljUtils.confirm('blue', tipText, function () {
			selRowIds = selRowIds.join(",");
			if (selRowIds && selRowIds != '') {
				$.ajax({
					url: hostUrl + "flow/sysNoticeMsg/deletePseudoBatchAndRecord/" + selRowIds,
					type: 'DELETE',
					dataType: 'JSON',
					success: function (resultData) {
						if (resultData && resultData.success) {
							$.xljUtils.tip('green', "数据删除成功！");
							$('#jqgridList').jqGrid().trigger("reloadGrid");
						} else {
							$.xljUtils.tip('red', resultData.msg);
						}
					}
				});
			}
		}, true);
	});
	
	//高级查询
	$("#changeRightIcon").on("click",function(e){
		$(this).toggleClass("col");
		var colFlag = $(this).hasClass("col");
		if(colFlag){
			$('#changeRightIcon').addClass('fa-angle-down');
			$('#changeRightIcon').removeClass('fa-angle-up');
			$("#xj-form-content_starter").hide();
		}else{
			$('#changeRightIcon').addClass('fa-angle-up');
			$('#changeRightIcon').removeClass('fa-angle-down');
			$("#xj-form-content_starter").show();
		}
		$.xljUtils.resizeNestedGrid();
		$.xljUtils.gridResizeFn();
	});

	//页面加载完毕后更改grid宽高
    $.xljUtils.resizeNestedGrid();
    $.xljUtils.addGridScroll();
});

function initUserInfo(){
	var userId = window.opener.edit_userId;
	var uBody = "sys/org/user/getMyInfo?time="+Math.random();
	var uAll = hostUrl + uBody;
	$.ajax({
		type:'get',
		url:uAll,
		dataType:'JSON',
		async:false,
		success: function(data) {
			currentUserLoginName = data.result.loginName;
		},error:function(XMLHttpRequest, textStatus, errorThrown){
			$.xljUtils.tip("red","获取用户请求失败");
		}
	});

}


/**
 * 在jqgridList标签上绑定jqgrid表格，并实现获取数据
 */
function initTaskGridList(){
	var postData = {};
	if(dataType){
		postData.dataType = dataType;

	}
	if (firstType) {
		postData.firstType = firstType;
	}
    jQuery("#jqgridList").jqGrid(//创建jqGrid组件
        {
            url : hostUrl+"flow/sysNoticeMsg/searchDataByKeyword",
            postData : postData, //,  sidx:"send_date", sord:"desc"},
            datatype : "json",
            ajaxGridOptions: { contentType: 'application/json;charset=utf-8' },
            mtype : "post",
            multiboxonly: true,
    		multiselect: true,
            //jsonReader : { root:"result" }, 
            jsonReader: { 
//            	repeatitems: false 
            	root: function (obj) {
					var result = obj.rows;
					for(var row in result){
						var rowData = result[row];
						var essence="";
						if(rowData.operationTip){
							try {
								var extendInfoData = JSON.parse(rowData.operationTip);
								if(extendInfoData.remainder>0){
									essence = "<img class='xb' title='已回复人员："+extendInfoData.replyNames+"\n未回复人员："+extendInfoData.remainderNames+"' src='../../../common/img/xb_no.png'>";
								}else{
									essence = "<img class='xb' title='已回复人员："+extendInfoData.replyNames+"\n未回复人员："+extendInfoData.remainderNames+"' src='../../../common/img/xb_yes.png'>";
								}
								rowData.title=rowData.title+essence;
							} catch (e) {
								continue;
							}
						}
					}
					return result;
				},
                repeatitems: false
            },
            colModel : [
                {name:'id',      label:'ID', align:"left", hidden:true},
                {name:'serverDate', label:'serverDate', align:"left",hidden:true},
                {name:'sendDate', label:'sendDate', align:"left",hidden:true},
                {name:'msgType', label:' ', width:30, align:"center",formatter: msgTypeFormatter},
                {name:'title',   label:'流程标题', width:300, align:"left", formatter: titleFormatter},
				{name:'source',   label:'来源', width:50, align:"center" , formatter: appCodeFormatter},
				{name:'opType',  label:'查询分类', width:50, align:"center", formatter: opTypeFormatter,unformat:opTypeUnformatter },
                {name:'approver',label:'当前审批人',width:100, align:"center",sortable:false},
                {name:'sendDate',label:'到达时间',width:100, align:"left"},
				{name:'dealDate',label:'完成时间',width:100, align:"left"},
                {name:'hourSum',  label:'停留(小时)', width:60, align:"center", formatter:hourSumFormatter}
            ],
            rowNum : -1,//在grid上显示记录条数，这个参数是要被传递到后台
            sortname : 'sendDate',//默认的排序列
            sortorder : "desc",//排序方式,可选desc,asc
            viewrecords : true, //定义是否要显示总记录数
            pager: '#jqgridPager',//定义翻页用的导航栏，必须是有效的html元素            
            rowNum: 20,//在grid上显示记录条数，这个参数是要被传递到后台
            rowList:  [20, 50, 100, 200], //可供用户选择一页显示多少条
			rownumWidth:48,
            rownumbers: true,
            onSelectRow: function(rowid, status) {//被选中的状态
            	lastSel_dataId = rowid;
            },
            loadComplete : function(xhr) {
    			$.xljUtils.addGridScroll();
    			$.xljUtils.gridResizeFn();
    			$(".ui-jqgrid .ui-jqgrid-hbox").css("marginTop","4px");
    		},
            loadError: function(xhr, status, error){

            }
    });
}

function titleFormatter(cellvalue, options, rowObject) {
	 var hourSumText = rowObject.hourSum;
     var hourSum = parseInt(hourSumText);
     var showHourText = hourSum+"小时";
     /*if(hourSum>24){
     	var daySum = parseInt(hourSum / 24);
     	var leftHour = hourSum % 24;
     	if(leftHour>0){
     		showHourText = daySum+"天"+leftHour+"小时";
     	}else{
     		showHourText = daySum+"天";
     	}
    }*/
    var opType = rowObject.opType;
    var showText = "";
    /*if(opType=="DB" || opType=="DY"){
    	showText = "停留   "+showHourText+"  " + rowObject.title ;
    }else{
    	showText = rowObject.title ;
    }*/
	showText = rowObject.title ;
    return "<a id='" +options.rowId+ "_a' href='javaScript:void(0);' style='font-weight: bold;color:#3c8dbc;' onclick='taskView(\""+ decodeURI(rowObject.url) +"\",\""+opType+"\");'>"+showText+"</a>";
}

function taskView(urlText, opType){
    var tendCode = getTextUrlParams(urlText).tendCode;
	var flag = checkLogin(tendCode);
	if(!flag){
		//return window.location.href='/platform-app/login.html?_s='+$.xljUtils.getUrlParams()._s+'&_time='+Math.random();
        if(tendCode){
            window.open('/platform-app/login.html?tendCode='+tendCode+'&_time='+Math.random(),'_self');
        }else{
            window.open('/platform-app/login.html?_time='+Math.random(),'_self');
        }

		return;
	}
    var initText = hostUrl+urlText+"&source="+opType+"&time="+Math.random();

	if(urlText && urlText.indexOf("http")==0){
		initText = urlText+"&source="+opType+"&time="+Math.random();
	}
	var url = initText;

	openWin(url);
}
function checkLogin(tendCode) {
    var flag = true;
    var url = hostUrl + 'sys/thirdPartyAuthentication/checkLogin?_time=' + new Date().getTime();
	//跨租户消息问题，暂时注释掉
	/*if(currentUserLoginName){
		url = url +'&loginName='+currentUserLoginName;
	}
    if(tendCode){
        url += '&tendCode='+tendCode + '&_s='+tendCode;
    }*/
    $.ajax({
        type: 'GET',
        url: url,
        dataType: 'JSON',
        async: false,
        success: function (resultData) {
            flag = resultData.success;
        },
        error: function (xhr) {
            flag = false;
        }
    });
    return flag;
}

function getTextUrlParams(url) {
	var tendCodeParam = url.substring(url.indexOf('?'));
	tendCodeParam = tendCodeParam.replace('?', '').replace(/&/g, '","');
	tendCodeParam = tendCodeParam.replace(/=/g, '":"');
	if (tendCodeParam != "") {
		try{
			tendCodeParam = JSON.parse('{"' + tendCodeParam + '"}');
		}catch(e){}
	}

	return tendCodeParam;
}
/**
 * 兼容性时间创建方法
 */
function newDate(dateStr) {
	var array = dateStr.match(/\d+/g);
	dateStr = array[0] + '/' + array[1] + '/' + array[2];
	var date = new Date(dateStr);
	date.setHours(parseInt(array[3]), parseInt(array[4]), parseInt(array[5]));
	return date;
}
function calculateTimeOffset(newTime, oldTime){
    var hourSum = (newTime-oldTime)/1000/60/60;
    var hourText = hourSum+"";
    var index = hourText.indexOf(".");
    return hourText.substr(0,index+2);
}

function opTypeFormatter(cellvalue, options, rowObject) {
	if(cellvalue == "DB"){
    	return "待审";
    }else if(cellvalue == "DY"){
    	return "待阅";
    }else if(cellvalue == "YB"){
    	return "已办";
    }else if(cellvalue == "YY"){
    	return "已阅";
    }else if(cellvalue == "FQ"){
    	return "我的发起";
    }
    return "暂无";
}
function hourSumFormatter(cellvalue, options, rowObject) {
	if(cellvalue > 0){
		return cellvalue;
	}
	return "";
}
function opTypeUnformatter(cellvalue, options, rowObject) {
	if(cellvalue == "待审"){
		return "DB";
	}else if(cellvalue == "待阅"){
		return "DY";
	}else if(cellvalue == "已办"){
		return  "YB";
	}else if(cellvalue == "已阅"){
		return "YY";
	}else if(cellvalue == "我的发起"){
		return "FQ";
	}
	return "暂无";
}
function msgTypeFormatter(cellvalue, options, rowObject) {
    if(rowObject.opType == "DY"|| rowObject.opType== "YY"){
        return "<i class='glyphicon glyphicon-bell' style='color: green'></i>";
    }else if(rowObject.opType == "DB"|| rowObject.opType== "YB") {
        return "<i class='glyphicon glyphicon-bell' style='color: red'></i>";
    }
    return "";
}
function appCodeFormatter(cellvalue, options, rowObject) {
		switch(cellvalue){
			case 'flow':
				return '巨洲云';
			case 'OA':
				return '领臣地产';
			case 'LLOA':
				return '蓝凌OA';
			/*case 'PT':
				return '平台';
			case 'HR':
				return '人力资源';
			case 'AD':
				return '考勤系统';
			case 'PL':
				return '计划管理';
			case 'EX':
				return '费用管理';
			case 'LI':
				return '土地投资';
			case 'QU':
				return '质量管理';
			case 'FM':
				return '资金系统';
			case 'SA':
				return '销售管理';
			case 'CO':
				return '成本管理';
			case 'PU':
				return '招标采购';
			case 'SUPPLIER':
				return '供方门户';
			case 'EOA':
				return 'EOA';

			case 'CW':
				return '财务系统';*/
			default:
				return '其它';
		}
}
/**
 * 置为已办
 */
function setYB() {
	var selRowIds = $("#jqgridList").jqGrid("getGridParam", "selarrrow");
	if(selRowIds && selRowIds.length >0){
		for (var i = 0; i < selRowIds.length; i++) {
			var selId = selRowIds[i];
			var rowData = $('#jqgridList').jqGrid('getRowData',selId);
			if(rowData.opType!='DB' && rowData.opType!='DY'){
				$.xljUtils.tip("blue","只能操作待阅或待办的数据");
				return;
			}
		}
		selRowIds = selRowIds.join(",");
		if (selRowIds && selRowIds != '') {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				url: hostUrl + "flow/sysNoticeMsg/updateStatusOfNoticeMsgBatch?_t="+new Date().getTime() ,
				data:JSON.stringify({'id':selRowIds,'manualSet':1}),
				dataType: "json",
				success: function (result) {
					if(result&&result.success){
						$.xljUtils.tip('green',"消息处理成功！");
						$("#jqgridList").jqGrid().trigger('reloadGrid');
					}
				},
				error: function (xhr, textStatus, errorThrown) {
					$.xljUtils.tip("red", "服务异常,请联系管理员！");
				}
			});
		}
	}else{
		$.xljUtils.tip("blue","请选择要操作的数据");
	}

}
/**
 * 刷新JqGrid的表格数据，子窗口调用是opener.refreshJqGridData();
 */
function refreshJqGridData(){
	var keyword = $("#keyword").val();
	var receiverId = $("#receiverId").val();
	var queryType = $("#queryType").val();
//	var timeType = $("#timeType").val();
	var startDate = $("#startDate1_1").val();
	var endDate = $("#endDate1_1").val();
	var finishStartDate = $("#startDate2_2").val();
	var finishendDate = $("#endDate2_2").val();
	var firstType = $('#firstType').val();
	var resourceType = $('#resourceType').val();
	var postData = $('#jqgridList').jqGrid('getGridParam','postData');

	var sidx = '';
	var sord = '';

	if(postData){
		for(var item in postData){
			delete postData[item];
		}
	}
	if(startDate){
		startDate+=" 00:00:00";
	}
	if(endDate){
		endDate+=" 23:59:59";
	}
	if(finishStartDate){
		finishStartDate+=" 00:00:00";
	}
	if(finishendDate){
		finishendDate+=" 23:59:59";
	}
	postData = {dataType: queryType, keyword:keyword,receiverId:receiverId, startDate: startDate
			, endDate: endDate,finishStartDate:finishStartDate,finishendDate:finishendDate,appCode:resourceType};
	if(firstType&&firstType!=''){
		postData.firstType = firstType;
	}

	if(queryType&&queryType!=''){
		postData.dataType = queryType;
	}
	if(queryType=="HAVE_DONE"){
		sidx="dealDate";
		sord="desc";
	}else{
		sidx="sendDate";
		sord="desc";
	}

	var sendbegin = startDate;
	sendbegin = new Date(sendbegin.replace(/-/g,'/'));
	var sendend = endDate;
	sendend = new Date(sendend.replace(/-/g,'/'));
	if(sendbegin>sendend){
		$.xljUtils.tip('blue','到达时间的开始时间不能大于结束时间！');
		return;
	}

	var dealbegin = finishStartDate;
	dealbegin = new Date(dealbegin.replace(/-/g,'/'));
	var dealend = finishendDate;
	dealend = new Date(dealend.replace(/-/g,'/'));
	if(dealbegin>dealend){
		$.xljUtils.tip('blue','完成时间的开始时间不能大于结束时间！');
		return;
	}



	$("#jqgridList").jqGrid('setGridParam',{
	      datatype:'json',
	      postData:postData,
		  sortname : sidx,//默认的排序列
		  sortorder : sord,//排序方式,可选desc,asc
		  page:1
	}).trigger("reloadGrid");
	if(queryType=="FQ"){
		$("#deleteFqTaskBtn").show();
	}else{
		$("#deleteFqTaskBtn").hide();
	}
	setTimeout(function () {
		$.xljUtils.addGridScroll();
	},1500);
}

/**
 * 修改业务变量的处理事件
 */
function modifyItem(){
	if(!lastSel_dataId){
		pop_tip_open("blue","请选择一个业务变量!");
		return;
	}
	var urlText = baseUrl+"flow/runtime/businessObjectVariable/businessObjectVariable_edit.html?systemId=";
	urlText = urlText+systemId+"&busiObjectId="+businessObjectId+"&id="+lastSel_dataId;
	openWin(urlText);
}

/**
 * 关闭当前的子窗口
 */
function closeMe(){
	lastSel_rowId = "";
	window.opener=null;
	window.open('','_self');
	window.close();
}

/**
 * 发起人回调
 * @param data
 * @param ele
 */
function receiverCallback(data, ele){
	if(data && data.length>0){
		var loginName="";
		var name="";
		for(var idx=0; idx<data.length; idx++){
			if(loginName!=""){
				loginName+=",";
			}
			if(name!=""){
				name+=",";
			}
			loginName+=data[idx].loginName;
			name+=data[idx].name;
		}
		$("#receiver").val(name);
		$("#receiverId").val(loginName);
	}
}

//初始化日期控件
function initDatetimepicker(){
  $('.form_datetime').datetimepicker({
	  language: 'zh-CN', //语言
	  format: 'yyyy-mm-dd',//显示格式  HH:ii:ss
	  minView: "month",//设置只显示到月份
	  initialDate: new Date(),//初始化当前日期
	  autoclose: true,//选中自动关闭
	  todayBtn: true//显示今日按钮 
  });
}

function emptyDateObject(dateIdText){
	$("#"+dateIdText).val("");
}

function disableDatetimepicker(v){
    if(v == "DB" || v == "DY"){


        $("#startDate2_2").attr("disabled", true);

         $("#endDate2_2").attr("disabled", true);
		$("#finishDate").hide();

    }else {
        $("#startDate2_2").attr("disabled", false);

         $("#endDate2_2").attr("disabled", false);

		$("#finishDate").show();
    }


}

