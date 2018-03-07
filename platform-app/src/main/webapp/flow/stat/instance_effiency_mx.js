
var statGrid;
var appId="-1", busiObjectId="-1";

$(function() {
	queryAppSystemList();
	initStatGridList();
	$.xljUtils.resizeNestedGrid();
	$.xljUtils.addGridScroll();
	initDatetimepicker();
	initNowTime();
});

/**
 * 导出
 */
function exportData(){
	var searchType=$("#searchType").val();
	if(searchType==2 && $("#approverId").val()!=""){
		$("#orgId2").val("");
	}else if(searchType==1 && $("#orgId2").val()!=""){
		$("#approverId").val("");
	}else{
		$("#orgId2").val("");
		$("#approverId").val("");
	}
//	var paramText = "?startDate1="+startDate1+"&endDate1="+endDate1+"&appId="+appId
//			+"&flowId="+flowId+"&busiObjectId="+busiObjectId+"&approverIds="+approverIds
//			+"&orgIds="+orgIds+"&holdTime="+holdTime+"&hfTask="+hfTask+"&workDay="+workDay;
	var url = hostUrl+"flow/instanceStat/exportInstanceEfficiencyMx"; 
    $('#statForm').attr('action', url); 
    $('#statForm').submit(); 
}

//初始化日期控件
function initDatetimepicker(){
    $('.form_datetime').datetimepicker({
        language:  'zh-CN',
        format: 'custom',
        customFormat:'yyyy-MM-dd HH:mm:ss',
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
        showMeridian: 1,
        showSecond:true
    });
}

/**
 * 初始化列表
 */
function initStatGridList(){
	statGrid = $("#statGridList");
	var paramData = {busiObjectId:-1};
	var urlText = hostUrl+"/flow/instanceStat/statInstanceEfficiencyMxPage";
	var colModel = [ 
        {name:'instanceStatus',  label:'流程状态',  width:"60", align:"center", sortable:false ,formatter: statusFormatter},
        {name:'flName',  label:'流程模板名称', width:"100", align:"left" , sortable:false },
        {name:'instanceName',  label:'流程实例名称', width:"100", align:"left" , sortable:false },
        {name:'appType',  label:'业务系统',  width:"60", align:"left", sortable:false},
        {name:'businessObjectName',  label:'业务对象',  width:"100", align:"left", sortable:false }, 
        {name:'holdTime',  label:'停留时长',  width:"60", align:"center", sortable:false },
        {name:'taskStartTime',  label:'到达时间',  width:"140", align:"center", sortable:false },
        {name:'taskEndTime',  label:'处理时间',  width:"140", align:"center", sortable:false },
        {name:'approverName',  label:'处理人',  width:"60", align:"left", sortable:false },
        {name:'approverDeptName',  label:'处理人所在部门',  width:"100", align:"left", sortable:false },
        {name:'approverCompanyName',  label:'处理人所在公司',  width:"100", align:"left", sortable:false },
        {name:'starterName',  label:'发起人',  width:"60", align:"left", sortable:false },
        {name:'starterDeptName',  label:'发起人所在部门',  width:"100", align:"left", sortable:false },
        {name:'starterCompanyName',  label:'发起人所在公司',  width:"100", align:"left", sortable:false }
	];
	initSingleGrid(statGrid, paramData, urlText, colModel);
}

function initSingleGrid(itemGrid, postParam, urlText, colModel){
	itemGrid.jqGrid( {//创建jqGrid组件
        url : urlText, 
        postData : postParam,
        datatype : "json", 
        ajaxGridOptions: { contentType: 'application/json;charset=utf-8' },
        mtype : "post", 
        jsonReader: {
            repeatitems: false
        },
        colModel : colModel,    
        pager: '#statGridListPager',//定义翻页用的导航栏，必须是有效的html元素            
        rowNum: 20,//在grid上显示记录条数，这个参数是要被传递到后台
        rownumWidth:45,
        rowList:  [20, 50, 100, 200], //可供用户选择一页显示多少条
        sortname : 'id',//默认的排序列
        sortorder : "desc",//排序方式,可选desc,asc
        rownumbers: true,
        viewrecords : true, //定义是否要显示总记录数
       	//multiselect:true,//定义是否可以多选
       	gridComplete: function() {//当表格所有数据都加载完成
       		$('.ui-state-default.ui-jqgrid-hdiv').css({'overflow':'hidden','margin-top':'8px'});
       		$.xljUtils.addGridScroll();
			$.xljUtils.gridResizeFn();
	    },
        loadError: function(xhr, status, error){
        	
        }
    });
}

/**
 * 查询按钮的点击事件
 */
function doSearchAction(){
	var postData = statGrid.jqGrid("getGridParam", "postData");
	for(var key in postData){
		delete postData[key];
	}
	
	var paramData = {};
	if($("#startDate1").val()==null || $("#startDate1").val()==""){
		pop_tip_open('blue', "请选择开始时间！");
		return;
	}
	paramData.startDate1 = $("#startDate1").val();
	paramData.endDate1 = $("#endDate1").val();
	paramData.appId = $("#appId2").val();
	paramData.flowId = $("#flowId2").val();
	paramData.busiObjectId = $("#busiObjectId2").val();
	var searchType=$("#searchType").val();
	if(searchType==2 && $("#approverId").val()!=""){
		paramData.approverIds = $("#approverId").val();
	}else if(searchType==1 && $("#orgId2").val()!=""){
		paramData.orgIds = $("#orgId2").val();
	}
	paramData.holdTime = $("#holdTime").val();
	paramData.hfTask = -1;
	var hfTaskIsChecked = $('#hfTask').is(":checked"); 
	var workDayIsChecked = 0;//$('#workDay').is(":checked"); 
	if(hfTaskIsChecked){
		paramData.hfTask = 1;
	}
	if(workDayIsChecked){
		paramData.workDay = 1;
	}
	
	statGrid.jqGrid('setGridParam', {datatype: 'json', postData: paramData,page:1}).trigger("reloadGrid");

}

/******************************************************************************/
/**
 * 查询所有业务系统的静态数据
 */
function queryAppSystemList() {
    var postdata = {
    	appDelFlag: "0",
        appStatus: "1"	
    };
    $.ajax({ //发送更新的ajax请求
        type: "post",
        url: hostUrl+"sys/res/appSystem/queryList",
        dataType: "json",
        async: true,
        data: JSON.stringify(postdata),
        contentType: 'application/json;charset=utf-8', //设置请求头信息
        success: function (data) {
        	sysAppList = data.result;
        	if(sysAppList && sysAppList.length>0){
        		fristAppId = sysAppList[0].id;
        	}
        	$("#appId2").empty();
        	$.each(sysAppList, function(index, item){
        		$("#appId2").append("<option value='"+item.id+"'>"+item.name+"</option>");
        	});
        	$("#appId2").append("<option value='-1'  selected='selected'>全部</option>");
//        	doSearchAction();
        	init_xljSingleArraySelector('2');
        },
        error: function (data) {
            if (data.msg) {
                pop_tip_open('red', data.msg);
            } else {
                pop_tip_open('red', "查询业务系统的列表数据失败！");
            }
        }
    });//end-for $.ajax({
}

function init_xljSingleArraySelector(type){
	$('#tree_0'+type).off('click').on('click',function(){
		if($('#appId'+type).val()=='-1'){
			$.xljUtils.tip('blue','请先选择应用系统');
			return;
		}
		$(this).xljMultipleSelector({
			title:'选择业务对象',
			selectorType:'businessObject',
			immediatelyShow: true,
			treeUrl: hostUrl+'flow/businessObject/getCategoryTreeBySystemApp?time='+Math.random(),
			treeParam:{"appDelFlag":false,"menuDelFlag":false,"delflag":false},
			treeSettings:{
				data:{
					simpleData:{
						enable:true,
						idKey:'id',
						pIdKey:'parentId',
						rootPId:null
					}
				}
			},
			targetId: 'busiObjectId'+type, //选择的数据的ID存储input域
			targetName: 'busiObjectName'+type, //选择的数据的Name存储input域
			gridColNames:['ID','名称'],
			gridColModel:[
				{name : 'id',width : 100,align : "center",hidden : true},
				{name : 'name',width : 90,align : "center"}
			],
			saveCallback: function(selectData,selectArray,ele){
				$("#flowId"+type).empty().append("<option value='-1'>请选择</option>");
				var busiObjectId = $("#busiObjectId"+type).val();
				queryFlowViewList(busiObjectId.split(','),type);
			}
		});
	});

}

/**
 *  查询某个busiObjectId下所有的模板视图数据
 */
function queryFlowViewList(paramId,type) {
	var postdata = { delflag: false };
	if(paramId != "-1"){
		postdata.businessObjectIdList = paramId;
	}
    
    $.ajax({ //发送更新的ajax请求
        type: "post",
        url: hostUrl+"flow/fl/queryViewList",
        dataType: "json",
        async: false,
        data: JSON.stringify(postdata),
        contentType: 'application/json;charset=utf-8', //设置请求头信息
        success: function (data) {
        	busiObjectList = data.result;
        	$("#flowId"+type).empty();
        	$("#flowId"+type).append("<option value='-1'>请选择</option>");
			if(busiObjectList){
				$.each(busiObjectList, function(index, item){
					$("#flowId"+type).append("<option value='"+item.id+"'>"+item.name+"</option>");
				});
			}

        },
        error: function (data) {
            if (data.msg) {
                pop_tip_open('red', data.msg);
            } else {
                pop_tip_open('red', "查询业务系统的列表数据失败！");
            }
        }
    });
}

function changeSel(that){
	if(that.value==1){
		$("#org-sel").show();
		$("#person-sel").hide();
	}else if(that.value==2){
		$("#org-sel").hide();
		$("#person-sel").show();
	}else{
		$("#org-sel").hide();
		$("#person-sel").hide();
	}
}

function emptyApproverObject(type){
	if(type==1){
		$("#approverId").val("");
		$("#approver").val("");
	}else if(type==2){
		$("#busiObjectId2").val("");
		$("#busiObjectName2").val("");
	}
}

function orgCallback(data, ele){
	if(data && data.length>0){
		var orgName="";
		var orgId="";
		for(var idx=0; idx<data.length; idx++){
			orgName+= data[idx].name+",";
			orgId+= data[idx].id+",";
		}
		orgName = orgName.substr(0, orgName.length-1);
		orgId = orgId.substr(0, orgId.length-1);
		$("#orgName").val(orgName);
		$("#orgId").val(orgId);
	}
}

function emptyOrgObject(type){
	$("#orgName"+type).val("");
	$("#orgId"+type).val("");
}

function approverCallback(data, ele){
	if(data && data.length>0){
		var itemName="";
		var itemId="";
		for(var idx=0; idx<data.length; idx++){
			var userId = data[idx].userId;
			userId = (!userId||userId=='')?data[idx].id:userId;
			itemName+= data[idx].name+",";
			itemId+= userId+",";
		}
		itemName = itemName.substr(0, itemName.length-1);
		itemId = itemId.substr(0, itemId.length-1);
		$("#approver").val(itemName);
		$("#approverId").val(itemId);
	}
}

function appIdChange(type, that){
	if(that.value){
		appId = that.value;
	}else{
		appId=that;
		$("#appId"+type).val(appId);
	}
	$("#busiObjectName"+type).val("");  
	$("#busiObjectId"+type).val("");  
	$("#flowId"+type).empty().append("<option value='-1'>请选择</option>");
	$('#tree_0'+type).off('click').on('click',function(){
		if($('#appId'+type).val()==-1){
			$.xljUtils.tip('blue','请先选择应用系统！');
			return;
		}
		$(this).xljMultipleSelectorReset({
			title:'选择业务对象',
			selectorType:'businessObject',
			immediatelyShow: true,
			treeUrl: hostUrl+'flow/businessObject/getCategoryTreeBySystemApp?time='+Math.random(),
			treeParam:{"appId":appId,"appDelFlag":false,"menuDelFlag":false,"delflag":false},
			treeSettings:{
				data:{
					simpleData:{
						enable:true,
						idKey:'id',
						pIdKey:'parentId',
						rootPId:null
					}
				}
			},
			targetId: 'busiObjectId'+type, //选择的数据的ID存储input域
			targetName: 'busiObjectName'+type, //选择的数据的Name存储input域
			gridColNames:['ID','名称'],
			gridColModel:[
				{name : 'id',width : 100,align : "center",hidden : true},
				{name : 'name',width : 90,align : "center"}
			],
			saveCallback: function(selectData,selectArray,ele){
				$("#flowId"+type).empty().append("<option value='-1'>请选择</option>");
				var busiObjectId = $("#busiObjectId"+type).val();
				queryFlowViewList(busiObjectId.split(','),type);
			}
		});
	});
}

function clearCondition(){
	queryAppSystemList();
	emptyOrgObject('2');
	emptyApproverObject(1);
	emptyApproverObject(2);
	$("#startDate1").val("");
	$("#endDate1").val("");
	$("#searchType").val("-1");
	$("#holdTime").val("");
	$("#flowId2").val("-1");
	$("input[name='workDay']").prop("checked", false);
	$("input[name='hfTask']").prop("checked", false);
	initNowTime();
}

/**
 * 初始化当前时间
 */
function initNowTime(){
	var myDate = new Date();
	//获取当前年
	var year=myDate.getFullYear();
	//获取当前月
	var month=myDate.getMonth()+1;
	//获取当前日
	var date=myDate.getDate(); 
	var h=myDate.getHours();       //获取当前小时数(0-23)
	var m=myDate.getMinutes();     //获取当前分钟数(0-59)
	var s=myDate.getSeconds();  

	var now=year+'-'+p(month)+"-01"+" "+"00:00";
	$("#startDate1").val(now);
}

/**
 * 获取当前时间
 */
function p(s) {
    return s < 10 ? '0' + s: s;
}

/**
 * 状态格式化
 */
function statusFormatter(cellvalue, options, rowObject){
	if(cellvalue=="1"){
		return "运行中";
	}else if(cellvalue=="2"){
		return "已完成";
	}else if(cellvalue=="3"){
		return "撤回";
	}else if(cellvalue=="4"){
		return "打回";
	}else if(cellvalue=="7"){
		return "作废";
	}else if(cellvalue=="9"){
		return "挂起";
	}
	return "";
}
