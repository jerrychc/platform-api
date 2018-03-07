
var treeObj;
var rowData;//当前选中数据
var rowDataBefore;
$(function() {

	//生成左侧的菜单数
	getLeftTree();
    $("#searchName").inputPlaceholder();
    $("#searchName").keypress(function(e){
        var eCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (eCode == 13){
     	   searchDate();
        }
	 })
	/* $("#officeList").inputPlaceholder();
	 $("#officeList").keypress(function(e){
		 var eCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		 if (eCode == 13){
			 searchList();
		 }
	 });*/
	//初始化日期控件
	initDatetimepicker();
	//计算高度
	resizeHeight();
	$(window).resize(function() {
	    resizeHeight();
	});

});
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
/**
* 代理人的回调函数处理事件
* @param data
* @param ele
*/
function replacerCallback(data, ele){
	$("#startUserId").val(data.id);
	$("#startUserName").val(data.name);
}
//清空输入框
function emptyDataObject(dataIdText){
	$("#"+dataIdText+"Id").val("");
	$("#"+dataIdText+"Name").val("");
}
//清空选择框
function emptyDateObject(dataIdText){
	$("#"+dataIdText).val("");
}
//查询条件重置
function resetFrom() {
	$("#startUserId").val("");
	$("#startUserName").val("");
	$("#flowCode").val("");
	$("#flowName").val("");
	$("#startDeptName").val("");
	$("#templetName").val("");
	$("#sCreateDate").val("");
	$("#eCreateDate").val("");
}
/**
 * open静态页面
 */
function toDetail(){
	var ids=$('#flowInfoList').jqGrid('getGridParam','selarrrow');
	if(!ids||ids.length==0) {
		pop_tip_open("blue","请选择要查看的流程");
		return;
	}
	if(ids.length>1){
		pop_tip_open("blue","请选择一行！");
		return;
	}
	var rowData = $("#flowInfoList").jqGrid("getRowData",ids);
	window.open(rowData.url);
}
/**
 * 查询按钮的点击事件
 */
function doSearchAction(){
	//获取左树选中节点
	var treeObj = $.fn.zTree.getZTreeObj("_zTree");
	var nodes = treeObj.getSelectedNodes(true);//在提交表单之前将选中的checkbox收集
	var parameterData = {
		code:$("#flowCode").val(),
		name:$("#flowName").val(),
		startUserId:$("#startUserId").val(),
		startDeptName:$("#startDeptName").val(),
		startCompanyName:"",
		templetName:$("#templetName").val(),
		sCreateDate:$("#sCreateDate").val()!=""?$("#sCreateDate").val()+" 00:00:00":"",
		eCreateDate:$("#eCreateDate").val()!=""?$("#eCreateDate").val()+" 23:59:59":"",
		flTypeId:nodes.length>0?nodes[0].id:""
	};
	jQuery("#flowInfoList").jqGrid("setGridParam",{postData:parameterData,page:1}).trigger("reloadGrid");
}
//计算高度
function resizeHeight(){
	var w_h = $(window).height();
    //左侧  头部底部为60px  title类 为50px
	$(".slide-left .ztree-box").height((w_h-190)+"px");
	//右侧table
	$(".con-table .mytable").height((w_h-80)+"px");
/*	$.xljUtils.resizeNestedGrid();*/
}

/**
 * 查询大类树结构
 * null 参数代表查询所有的树结构，不带条件搜索
 */

function getLeftTree(){
	var setting = {

		/*async: {
			enable: true,//异步加载
			//请求地址，可用function动态获取
			url:hostUrl + "/ld/landrayFlowType/getFlTypeTree?t=" + (+new Date()),
			contentType:"application/x-www-form-urlencoded",//按照标准的 Form 格式提交参数
			autoParam:["id", "pId"],//提交的节点参数，可用“id=xx”取请求提交时的别名
			//otherParam:{"otherParam":"zTreeAsyncTest"},//提交的其他参数,json的形式
			dataType:"json",//返回数据类型
			type:"post",//请求方式
			dataFilter: null//数据过滤
		},*/
		async: {
			enable:true, //表示异步加载生效
			url: hostUrl + "/ld/landrayFlowType/getFlTypeTree?t=" + (+new Date()), // 异步加载时访问的页面
            data: {
                simpleData: {
                    idKey: "id",//使用简单必须标明的的节点对应字段
                    enable: true,
                    pIdKey: 'parentId',
                    rootPId : '151c3d6bb01bf4bf4cc0724413e987b1'
                },
                key: {

                }
            },
			autoParam: ["id"], // 异步加载时自动提交的父节点属性的参数
			/*otherParam:["ajaxMethod",'AnsyData'], //ajax请求时提交的参数
			type:'post',
			dataType:'json'*/
            dataFilter : function(treeId, parentNode, childNodes) {//这里由于本人设置的节点属性跟zTree不一致所以进行了过滤
                childNodes = childNodes.result;
                /**/if (!childNodes)
                    return null;
                for (var i = 0, l = childNodes.length; i < l; i++) {
                    if (childNodes[i].contentTypeId==0) {
                        //当主节点  下面还有父节点时自动将isParent=true
                        //这样点击父节点zTree会自动再加载其子节点的数据
                        childNodes[i].isParent = true;
                    }
                }
                return childNodes;
            }
		},
		callback: {
		    	beforeClick: function(treeId, treeNode, clickFlag) {
                    return true;
		    	},
		        onClick: loadContentChildByTypeId,
				onCollapse: function(){
					$.xljUtils.addTreeScroll();
					$.xljUtils.treeResizeFn();
				},
				onExpand: function(){
					$.xljUtils.addTreeScroll();
					$.xljUtils.treeResizeFn();
				},
			    asyncSuccess: function(event, treeId, treeNode, msg){
					$.xljUtils.addTreeScroll();
				}  //异步加载成功事件
		    }
		};
    $.fn.zTree.init($("#_zTree"), setting, [ {
        'id' : '0',
        "name" : '流程分类',
        'type' : 1,
        'isParent' : true
    } ]);
    /*$.ajax({
        type: "POST",
        url: hostUrl + "/ld/landrayFlowType/getFlTypeTree?t=" + (+new Date()),
        dataType: "json",
        contentType: 'application/json',
		data:JSON.stringify({id:null}),
        success: function (typeNodes) {
            var zNodes = typeNodes.result;
    		var inputVal=$("#searchName").getInputVal();
			if(inputVal){
				var resultId="";
				var dataArr=new Array();
			  for(var o in zNodes){
				  if((zNodes[o].name).indexOf(inputVal)>-1){
					  resultId+=zNodes[o].id+","
					  resultId+=zNodes[o].parentId+","
				  }
			  }
			  for(var o in zNodes){
				  if((resultId).indexOf(zNodes[o].id)>-1){
					  dataArr.push(zNodes[o]);
				  }
			  }
				zNodes=	dataArr;
			}
            recursionArray(zNodes);
            // treeObj = $.fn.zTree.init($("#_zTree"), setting, zNodes);
            $.fn.zTree.init($("#_zTree"), setting, [ {
                'id' : '151c3d6bb01bf4bf4cc0724413e987b1',
                "name" : '凯顿儿童美语ERP信息系统',
                'type' : 1,
                'isParent' : true
            } ]);
            //treeObj.expandAll(true);
            //默认加载第一个菜单的列表数据
            if(zNodes.length>0){
            	var firstChildNode = null;
            	/!*var nodes =  treeObj.transformToArray(treeObj.getNodes());
            	for(var i = 0, len = nodes.length;i<len;i++) {
            		if(!nodes[i].isParent) {
            			firstChildNode = nodes[i];
            			break;
            		}
            	}*!/
            	//treeObj.selectNode(firstChildNode);	//选中第一个节点
            	onClick("","",null);
            }else{
				onClick("","",null);
			}
			setTimeout(function(){
				$.xljUtils.addTreeScroll('ztree-box');
				$.xljUtils.treeResizeFn();
			},300);
        },
		error: function(e) {
			onClick("","",null);
		}
	});*/
    onClick("","",null);
}

/**
 * 默认进入只是目录页面的时候，加载左侧树第一个大类信息
 * 加载对应jqgrid数据列表
 */
function onClick(e,treeId,treeNode){
	var parameterData = {
			userId:"e8aa6c00ee1a42358fd2de9fc91ed2ac",
			code:$("#flowCode").val(),
			name:$("#flowName").val(),
			startUserId:$("#startUserId").val(),
			startDeptName:$("#startDeptName").val(),
			startCompanyName:"",
			templetName:$("#templetName").val(),
			sCreateDate:$("#sCreateDate").val()!=""?$("#sCreateDate").val()+" 00:00:00":"",
			eCreateDate:$("#eCreateDate").val()!=""?$("#eCreateDate").val()+" 23:59:59":"",
			flTypeId:""
	};
	if(null!=treeNode){
		parameterData.flTypeId = treeNode.id;
	}
	jQuery("#flowInfoList").jqGrid({
    	url : hostUrl + "/ld/landrayFlowInstance/page",
        ajaxGridOptions: { contentType: 'application/json', aync:true },
        mtype : "post",
        datatype : "json",
        contentType : "application/json",
        postData:parameterData ,
        jsonReader : {
        	repeatitems: false
        },
        colModel : [
             {name : 'id',label : 'id', align:"center",hidden : true},
			 {name : 'url',label : 'url', align:"center",hidden : true},
			 {name : 'name',label : '主题', align:"center"},
			 {name : 'code',label : '申请单编号', align:"center",defaultValue:"eeee"},
             {name : 'oaRealName',label : '申请人', align:"center"},
             {name : 'startDate',label : '发布时间',align:"center"},
             {name : 'createDate',label : '创建时间',align:"center"}
        ],
        rowNum : 20,//一页显示多少条
        rowList : [ 20, 50, 100,200 ],//可供用户选择一页显示多少条
        autowidth:true,
        pager : '#pagered',//表格页脚的占位符(一般是div)的id
        rownumbers:true,
		ondblClickRow:function(rowid){
			var rowData = $("#flowInfoList").jqGrid("getRowData",rowid);
			window.open(rowData.url);
		},
        onCellSelect: function(){
        	if(rowDataBefore!=null&&rowDataBefore!='undefined'){
        		//重新选择行时清除上一次选中行的样式
        		$('#flowInfoList'+'#'+rowDataBefore.id).find("td").removeClass("ui-state-highlight");
        	}
        },
        onSelectRow: function () {
        	var rowId=$('#flowInfoList').jqGrid("getGridParam","selrow");
		      rowData = $('#flowInfoList').jqGrid('getRowData',rowId);
        },
        viewrecords : true,
//        multiboxonly : true,
        multiselect: true,
        gridComplete: function() {
        	$.xljUtils.addGridScroll();
			$.xljUtils.gridResizeFn();
			rowDataBefore = rowData;
            if(rowDataBefore!=null&&rowDataBefore!='undefined'){
            	//添加回显选中行样式
            	$('#flowInfoList').setSelection(rowDataBefore.id,true);
            	$('#flowInfoList'+'#'+rowDataBefore.id).find("td").addClass("ui-state-highlight");
            }
        }
	});
	$.xljUtils.resizeNestedGrid();
}

/**
 * 单击左侧菜单事件
 * 加载对应jqgrid数据列表
 */

function loadContentChildByTypeId(e,treeId,treeNode){
	var parameterData = {
		userId:"e8aa6c00ee1a42358fd2de9fc91ed2ac",
		code:$("#flowCode").val(),
		name:$("#flowName").val(),
		startUserId:$("#startUserId").val(),
		startDeptName:$("#startDeptName").val(),
		startCompanyName:"",
		templetName:$("#templetName").val(),
		sCreateDate:$("#sCreateDate").val(),
		eCreateDate:$("#eCreateDate").val(),
		flTypeId:treeNode.id
	};
	jQuery("#flowInfoList").jqGrid("setGridParam",{postData:parameterData,page:1}).trigger("reloadGrid");
}

//树增加样式
function recursionArray(arr) {
    for(var i in arr) {
    	if(arr[i].pId==""){
    		arr[i].iconSkin = "diy-company";
    	}else{
			arr[i].iconSkin = "diy-officetype";
			if(arr[i].children&&arr[i].children.length > 0) {
				that._recursionArray(arr[i].children);
			}
		}
    }
};
function reloadGrid(id){
	 pop_tip_open("green","数据操作成功！");
	 rowData={id:id};
	 $('#flowInfoList').jqGrid().trigger("reloadGrid");
}
/**
 * 查看调整记录
 */
function seeRecordList(){
	var ids= jQuery("#flowInfoList").jqGrid('getGridParam', 'selarrrow');
	if(ids.length!=1){
		pop_tip_open("blue","请选择一条记录!");
		return false;
	}
	var rowData = $('#flowInfoList').jqGrid('getRowData',ids[0]);
	window.open(rowData.url);
}

function searchDate(){
    var treeObj = $.fn.zTree.getZTreeObj("_zTree");
    var value = $('#searchName').val();
    var nodeList = treeObj.getNodesByParamFuzzy('name', value);
    var nodes = treeObj.getNodes();
    treeObj.hideNodes(nodes[0].children);
    treeObj.showNodes(nodeList);
}

