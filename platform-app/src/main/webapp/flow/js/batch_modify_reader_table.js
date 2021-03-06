/**
 * @author: peter <zhengjj_2009@126.com>
 * @date: 2017-03-17
 */

/**
 * 此文件实现动态地绘制审批人、抄送人和可阅读人的表格, 其中审批人和可阅读人的数据表格
 * 对应的tableID关键字为one, 抄送人对应的tableID关键字为two
 */

//定义全局的变量
var selectOneTRID = "";
var selectTwoTRID = "";
var returnPopId = "";  
var returnPopName = "";
var senderArr = ["", "发起人直接领导", "发起人顶级部门领导"];//, "上一环节审批人直接领导", "上一环节审批人顶级部门领导"];
var typeItemArray = ["","人员","岗位","角色","相对参与人"];
var variableList; //表单变量列表

// --------------------  通用方法定义 begin -----------------------------

/**
 * 拓展Array的insert方法, 参数是index和item, 实现指定的位置插入item对象
 */
Array.prototype.insert = function (index, item){  
	this.splice(index, 0, item);  
};

/**
 * 该方法实现数组对象的比较,以对象的property为比较参数,在changeArraySort()方法使用到
 * @param property
 * @returns{Function}
 */
function compare(property){
   return function(a,b){
       var value1 = a[property];
       var value2 = b[property];
       return value1 - value2;
   }
}

/**
 * 实现将tempArray按照某个属性进行排序,然后重新对sort属性进行赋值,使sort为我们想要的数据
 * @param tempArray
 * @returns
 */
function changeArraySort(tempArray){
	var newArray = tempArray.sort(compare('sort'));
	for(var idx=0; idx<newArray.length; idx++){
		var item = newArray[idx];
		item.sort = idx+1;
	}
	return newArray;
}
//-----------通用方法定义 end ---------------------------

/**
 * 查询相关的表单数据列表
 */
function queryVariableList(busiObjectId){
	var paramData = new Object();
	paramData.flId = busiObjectId;//此参数暂时为1,等整个流程跑起来之后再修改为正确的值
	$.ajax({ //发送更新的ajax请求
	    type : "post",  
	    url : hostUrl+"flow/businessObjectVariable/queryBusiVariableListByTemlateId",    
	    dataType : "json",  
	    data : JSON.stringify(paramData),//将对象序列化成JSON字符串  ,
	    contentType : 'application/json;charset=utf-8', //设置请求头信息  
	    success : function(data){
	    	variableList = data.result;	
	    },  
	    error : function(data){  
	    	if(data.msg){
	    		alert(data.msg);
	    	}else{
	    		//alert("修改失败！");
	    	}
	    }  
	});
}
/**
 * 在blockIdx的表格中增加一列
 * @param blockIdx --one or two
 * @param itIdx -- 在第itIdx行插入数据
 */
function addReaderRow(blockIdx,itIdx){
	var tableDataList = getTableAllTRTDData(blockIdx);
	$.each(tableDataList,function(index,singleItem){//遍历mapList的数组数据
		singleItem.sort = singleItem.sort+1;
	});
	var newObj = new Object();
	newObj.blockIdx = blockIdx;
	//新增的对象默认: 角色-本公司
	newObj.sort = 100;
	newObj.dataType = defaultDataType;
	tableDataList.push(newObj);
	//将数组重新排序, 然后绘制表格
	tableDataList = changeArraySort(tableDataList);
	redrawTBodyOfTable(blockIdx, tableDataList);
	$('html').getNiceScroll().show().resize();       //重置纵向滚动条
}

/**
 * 重绘某个表格
 * @param blockIdx
 */
function redrawTBodyOfTable(blockIdx, dataObjList){
	var tableObj = $("#table_"+blockIdx); 
	var firstTr = tableObj.find('tbody'); 
	firstTr.html("");//清除掉tbody标签内的所有html元素
	
	//循环dataObjList的数据进行绘制
	$.each(dataObjList,function(index,item){//遍历mapList的数组数据
		var dataType = item.dataType;
		var blockIdx = item.blockIdx;
		var selectType = "onlyPerson";
		if(dataType==2){
			selectType = "post";
		}
		if(dataType==3){
			selectType = "org";
		}
		var itIdx = item.sort;
		//1-绘制TR标签--绘制第一列的数据
		var row = $("<tr class=\"td-average\" id='"+blockIdx+"_"+itIdx+"' onclick='singleSelectReaderTR(this)'></tr>");
		var td1Header = "<td id='"+blockIdx+"_"+itIdx+"01' style=\"width: 20%\">" +
				"<select class='form-control addInputWidth' id='type_"+blockIdx+"_"+itIdx+"'" +
				" name='type_"+blockIdx+"_"+itIdx+"' onchange=\"readerTypeSelectChange(this)\" >";
		var td1Middle = "";
		for(var i=0; i<dataTypeArray.length; i++){
			var typeId = i+1;
			if( dataType == typeId ){
				td1Middle +="<option value ='"+typeId+"' selected='true'>"+dataTypeArray[i]+"</option>";
			}else{
				td1Middle +="<option value ='"+typeId+"'>"+dataTypeArray[i]+"</option>";
			}
		}
		var td1 = $(td1Header+td1Middle+"</select></td>"); 
		row.append(td1);
		
		//2-绘制第2列的数据
		var td2Header = "<td id='"+blockIdx+"_"+itIdx+"_02' class=\"col_text\" style=\"width: 80%\"><div id='chooseType_"+blockIdx+"_"+itIdx+"'>";
		var idValue = item.idValue;
		var nameValue = item.nameValue;
		if(!idValue || "undefined" == idValue || "null" == idValue){
			idValue = "";  nameValue="";
		}
		var td2Middle ="<div class=\"input-group\" style=\"width:100%;\"><input type=\"text\" class=\"form-control\" id='dataNameValue_"+blockIdx+"_"+itIdx+"'" +
				" name='dataNameValue_"+blockIdx+"_"+itIdx+"' value='"+nameValue+"' placeholder=\"请选择...\" readonly=\"true\">" +
				" <input type=\"hidden\" id='dataIdValue_"+blockIdx+"_"+itIdx+"' name='dataIdValue_"+blockIdx+"_"+itIdx+"' value='"+idValue+"'>" +
				"<span class=\"input-group-addon w28 multiple-selector\" id=\"multiSelector_"+blockIdx+"_"+itIdx+"_"+dataType+"\" data-selectortype=\""+selectType+"\"" +
				" data-savecallback=\"multiSelectorCallback\" readonly=\"readonly\"><a href=\"javascript:void(0);\"" +
				" class=\"fa fa-ellipsis-h\" aria-hidden=\"true\"></a></span></div></div>";
		
		var td2 = $(td2Header + td2Middle +"</div></td>");
		row.append(td2);
		tableObj.append(row);
		//动态初始化选择器
		$(tableObj).xljMultipleSelectorUtil();
	});
}

/**
 * 选择某一行
 * @param obj
 */
function singleSelectReaderTR(obj){
	selectOneTRID = obj.id;
	var classText = "ui-row-ltr ui-state-highlight";
	$(obj).addClass(classText).siblings().removeClass(classText);
}

/**
 * 删除掉某一行
 * @param tableType
 */
function deleteReaderRow(tableType){
	$("#"+selectOneTRID).remove();
	selectOneTRID="";	
	$('html').getNiceScroll().show().resize();       //重置纵向滚动条
}


/**
 * 根据blockIdx的值获取表格的相关业务数据,放到对应的对象数组中
 * @param blockIdx--one or two
 */
function getTableAllTRTDData(blockIdx){
	var tempDataList = new Array();
	var trSort = 1;
	$("#tableTBody_"+blockIdx).find("tr").each(function(){
		var itemObj = new Object();
		var tdArr = $(this).children();
		var dataType = tdArr.eq(0).find("select").val();//人员类型
		var userTypeText = tdArr.eq(0).find("select").find("option:selected").text();
		itemObj.sort = trSort;
		itemObj.dataType = dataType;
		var div1Arr = tdArr.eq(1).children();
		var firstDiv = div1Arr[0];
		var firstIDText = firstDiv.id;////chooseType_reader_1
		//console.log("firstIDText="+firstIDText);
		//dataIdValue_reader_1
		var idKeyText = firstIDText.replace("chooseType_"+blockIdx, "dataIdValue_"+blockIdx);
		//dataNameValue_reader_1
		var nameKeyText = firstIDText.replace("chooseType_"+blockIdx, "dataNameValue_"+blockIdx);
		itemObj.idValue = $("#"+idKeyText).val();
		itemObj.nameValue = $("#"+nameKeyText).val();
		itemObj.blockIdx = blockIdx;
		trSort++;
		tempDataList.push(itemObj);
	});
	
	return tempDataList;	
}

/**
 * 第一列的用户类型下拉框的change事件处理方法
 * @param obj --select标签本身
 */
function readerTypeSelectChange(obj){
	var idText = obj.id;
	var selIndex = obj.options.selectedIndex;
	var selVal = obj.options[selIndex].value;
	//idText=userType_one_1
	var idItems = idText.split("_");
	var sortText = idItems[2];//1--tr的序号
	var itIdx = parseInt(sortText);
	var blockIdx = idItems[1];
	//根据选中的值对第二列做相应的调整
	var div2Obj = $("#table_"+blockIdx+" tr:eq("+(itIdx-1)+")>td:eq(1)>div"); 
	div2Obj.html("");//将第2列的DIV清空
	var selectType = "onlyPerson";
	if(selVal==2){
		selectType = "post";
	}
	if(selVal==3){
		selectType = "org";
	}
	var td2Middle ="<div class=\"input-group\" style=\"width:100%;\"><input type=\"text\"" +
	" class=\"form-control\" id='dataNameValue_"+blockIdx+"_"+itIdx+"'" +
	" name='dataNameValue_"+blockIdx+"_"+itIdx+"'   placeholder=\"请选择...\" readonly=\"true\">" +
	" <input type=\"hidden\" id='dataIdValue_"+blockIdx+"_"+itIdx+"' name='dataIdValue_"+blockIdx+"_"+itIdx+"' >" +
	"<span class=\"input-group-addon w28 multiple-selector\" id=\"multiSelector_"+blockIdx+"_"+itIdx+"_"+selVal+"\"" +
	" data-selectortype=\""+selectType+"\" data-savecallback=\"multiSelectorCallback\" readonly=\"readonly\">" +
	"<a href=\"javascript:void(0);\" class=\"fa fa-ellipsis-h\" aria-hidden=\"true\"></a></span></div></div>";
	div2Obj.html( td2Middle );
	 
	var tableObj = $("#table_"+blockIdx);
	//动态初始化选择器
	$(tableObj).xljMultipleSelectorUtil();
}


function senderChange(blockType , trSort, obj){
	//showResultTextToLabel(blockType);
}

/**
 * 单选框的回调函数处理事件
 * @param data
 * @param ele
 */
function singleSelectorCallback(data, ele){
	var idText = ele.id;
	var idItems = idText.split("_");
	var idName = idItems[0];
	var blockType = idItems[1];
	var trSort = idItems[2];
	var busiType = idItems[3];
	var showTextId = "",hidenTextId="";
	var showTextId = "",hidenTextId="";
	if(busiType=="316"){
		showTextId = "paramValueName_"+blockType+"_"+trSort;
		hidenTextId = "paramValue_"+blockType+"_"+trSort;
	}else{
		showTextId = "participantIdName_"+blockType+"_"+trSort;
		hidenTextId = "participantId_"+blockType+"_"+trSort;
	}
	$("#"+showTextId).val(data.name);
	$("#"+hidenTextId).val(data.id);
	//showResultTextToLabel(blockType);
}

/**
 * 多选框的回调函数的处理逻辑
 * @param data
 * @param ele
 */
function multiSelectorCallback(data, ele){
	var idText = ele.id;
	var idItems = idText.split("_");
	var idName = idItems[0];
	var blockType = idItems[1];
	var trSort = idItems[2];
	var dataType = idItems[3];
	/*//console.info("multiSelectorCallback   idText="+idText);
	//console.info(data);*/
	var showTextId = "",hidenTextId="";
	if(data.length>0){
		showTextId = "dataNameValue_"+blockType+"_"+trSort;
		hidenTextId = "dataIdValue_"+blockType+"_"+trSort;
		var nameText = "";
		var idText = "";
		for(var idx=0; idx<data.length; idx++){
			var dataItem = data[idx];
			if(dataType == "1"){
				nameText += dataItem.name+",";
				idText +=  dataItem.userId+",";
			}else{
				nameText += dataItem.name+",";
				idText +=  dataItem.id+",";
			}
		}
		$("#"+showTextId).val(nameText.substr(0, nameText.length-1));
		$("#"+hidenTextId).val(idText.substr(0, idText.length-1));
	}
} 

//----------------------------------------------------------------------------
var selectd_blockType = "";
var selectd_trSort;
var selectd_busiType;

function chooseOuterData(blockType,trSort,busiType){
	var paramData = new Object();
	selectd_blockType = blockType;
	selectd_trSort = trSort;
	selectd_busiType = busiType;
	var title = "", busiUrl = "";
	if("11"==busiType){
		title = "指定人员的选择"; 
		busiUrl = hostUrl+"sys/org/user/getUserTree";
		paramData.userStatus = 1;
		paramData.userDelFlag = 0;
		paramData.rootStatus = 1;
		paramData.rootDelFlag = 0;
		paramData.orgStatus = 1;
		paramData.orgDelFlag = 0;
/*sys/org/user/getUserTree
目录父ID>parentId
目录禁用状态>rootStatus:0禁用，1启用
目录删除状态>rootDelFlag:0正常，1删除
组织禁用状态>orgStatus:0禁用，1启用
组织删除状态>orgDelFlag:0正常，1删除
用户禁用状态>userStatus:0禁用，1启用
用户删除状态>userDelFlag:0正常，1删除
*/
	}else if("21"==busiType){
		title = "指定岗位的选择";
		busiUrl = hostUrl+"sys/org/post/getPostTree";
		paramData.rootStatus = 1;
		paramData.rootDelFlag = 0;
		paramData.orgStatus = 1;
		paramData.orgDelFlag = 0;
		paramData.postStatus = 1;
		paramData.postDelFlag = 0;	
/*/sys/org/post/getPostTree
目录父ID>parentId
目录禁用状态>rootStatus:0禁用，1启用
目录删除状态>rootDelFlag:0正常，1删除
组织禁用状态>orgStatus:0禁用，1启用
组织删除状态>orgDelFlag:0正常，1删除
岗位禁用状态>postStatus:0禁用，1启用
岗位删除状态>postDelFlag:0正常，1删除
*/
	}else if("31"==busiType){
		title = "标准角色的选择";
		busiUrl = hostUrl+"sys/org/roleCatalog/getRoleTree";
		paramData.roleCataStatus = 1;
		paramData.roleCataDelFlag = 0;
		paramData.status = 1;
		paramData.delflag = 0;
/*
 sys/org/roleCatalog/getRoleTree
目录禁用状态>roleCataStatus:0禁用，1启用
目录删除状态>roleCataDelFlag:0正常，1删除
角色禁用状态>status:0禁用，1启用
角色删除状态>delflag:0正常，1删除
是否查询角色>isRole:Y是，N否(默认为Y)
角色类型>type:0虚拟角色，1标准角色
*/
	}else if("316"==busiType){
		title = "指定组织的选择";
		busiUrl = hostUrl+"sys/org/root/getTree";
		paramData.rootStatus = 1;
		paramData.rootDelFlag = 0;
		paramData.orgStatus = 1;
		paramData.orgDelFlag = 0;
/*目录禁用状态>rootStatus:0禁用，1启用
目录删除状态>rootDelFlag:0正常，1删除
组织禁用状态>orgStatus:0禁用，1启用
组织删除状态>orgDelFlag:0正常，1删除
*/
	}
	$("#modal-title").html(title);//修改标题
	$("#modalWindow").modal("show");
	queryzTree(busiUrl, paramData);
}



/**
 * 获取通用表格的可直接提交的数据,
 * @param tableKey: tableId的关键字 one/two
 * @param flId: 模板ID
 * @param acId: 环节ID
 * @param readFlag: 是否为可阅读人
 */
function getSubmitDataListofCommonTable(tableKey, flId, acId, readFlag){
	var tempList = getTableAllTRTDData(tableKey);
	var dataList = new Array();
	$.each(tempList,function(index,item){//遍历mapList的数组数据
		var participant = new Object();
		////type flId acId paramValue  sort
		participant.flId = flId;
		if(readFlag==true){//可阅读人不需要设置acId
			participant.type = 3;
		}else{//审批人和抄送人需要设置acId
			participant.type = item.type;
			participant.acId = acId;
		}
		participant.sort = item.sort;
		if(item.paramValue && item.paramValue.length >=1){
			participant.paramValue = item.paramValue;
		}
		if(item.participantId && item.participantId.length >=1){
			participant.participantId = item.participantId;
		}
		//participantId  participantType  participantScope  
		participant.participantType = item.participantType;
		participant.participantScope = item.participantScope;
		dataList.push(participant);
	});
	return dataList;
}

/**
 * 实现在表格下方显示该表格所有的选择项的组合文本描述
 * @param blockType
 */
function showResultTextToLabel(blockType){
	var dataList = getTableAllTRTDData(blockType);
	var showDataText = "";
	$.each(dataList,function(index,item){//遍历mapList的数组数据
		var userType = item.participantType;
		var userTypeText = "";
		var scopeText = getParticipantScopeText(item.participantScope);
		if(userType == "1"){
			userTypeText = item.participantIdName+"/"+scopeText+"(人员)";
		}else if(userType == "2"){
			userTypeText = item.participantIdName+"/"+scopeText+"(岗位)";;
		}else if(userType == 3){
			var scope = item.participantScope;
			var roleHeader = item.participantIdName;
			if(scope == "316" || scope == "317"){
				roleHeader = item.participantIdName+"/"+item.paramValueName;
			}
			userTypeText = roleHeader +"/"+scopeText+"(角色)";
		}else if(userType == 4){
			var senderText = getParticipantScopeText(item.participantScope);
			userTypeText = senderText+"(相对参与人)";
		}
		showDataText += userTypeText+";<br> ";
	});
	/*北京公司/成本部/张三（人员）;<br/>北京公司/财务经理（岗位）;<br/>成本经理/本公司（角色）；<br/>
	成本经理/指定组织/北京公司（角色）；<br/>成本经理/单据组织/北京公司（角色）；<br/>
	经办人（表单人员）；<br/>部门会计（表单岗位）<br/>*/
	$("#showResultText_"+blockType).html(showDataText);
	
}

/**
 * 根据chooseType返回对应的中文业务术语
 * @param chooseType
 * @returns
 */
function getParticipantScopeText(chooseType){
	var numValues = ["11","12","21","22",
	                 "31","311","312","313","314","315","316","317"
	                 ,"41","42","43","44"];
	
	var typeNames = ["指定人员","表单人员","指定岗位","表单岗位"
	                 ,"指定角色","本集团","本公司","本部门","本项目","本分期","指定组织","表单组织"
	                 ,"发起人直接领导","发起人顶级部门领导","上一环节审批人直接领导","上一环节审批人顶级部门领导"];
	var idx = jQuery.inArray(chooseType+"", numValues);
	return typeNames[idx];
}

//----------------------- 模态弹出框部分  begin ----------------------------------
/**
 * 将模态弹出框选中的数据传回给页面,并关闭模态弹出层
 */
function submitAndCloseModelWindow(){
	var showTextId = "",hidenTextId="";
	if(selectd_busiType=="316"){
		showTextId = "paramValueName_"+selectd_blockType+"_"+selectd_trSort;
		hidenTextId = "paramValue_"+selectd_blockType+"_"+selectd_trSort;
	}else{
		showTextId = "participantIdName_"+selectd_blockType+"_"+selectd_trSort;
		hidenTextId = "participantId_"+selectd_blockType+"_"+selectd_trSort;
	}
	$("#"+showTextId).val(returnPopName);
	$("#"+hidenTextId).val(returnPopId);
	returnPopId = "";
	returnPopName = "";
	closeModelWindow();
	showResultTextToLabel(selectd_blockType);
}

/**
 * 直接关闭模态弹出层
 */
function closeModelWindow(){
	$("#modalWindow").modal("hide");
}
//------------------------ 模态弹出框部分  end ----------------------------------

//----------------------- zTree 配置部分  begin ----------------------------------
//zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）

//ztree的setting参数
var setting ={
		view:{
			dblClickExpand: false,  
			showLine: true,  
			selectedMulti: true,
			//fontCss: getFont,
			fontCss: getFontCss,
			nameIsHTML: true
		},  
		edit:{  
			enable: true,
			showRemoveBtn:false,
			showRenameBtn:false,
			drag:{  
				autoExpandTrigger: true,  
				prev: null,  
				inner: null,  
				next: null,
				isCopy: false,
				isMove: false
			}
		 },  
		data:{
			keep:{
				leaf: true,
				parent: true
			},
			simpleData:{
				enable: true
			}
		},
		callback:{  
	        onClick: zTreeOnClick, //点击节点事件
	        beforeDrag: null, //拖拽前：捕获节点被拖拽之前的事件回调函数，并且根据返回值确定是否允许开启拖拽操作  
	        beforeDrop: null, //拖拽中：捕获节点操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作  
	        beforeDragOpen: null, //拖拽到的目标节点是否展开：用于捕获拖拽节点移动到折叠状态的父节点后，即将自动展开该父节点之前的事件回调函数，并且根据返回值确定是否允许自动展开操作  
	        onDrag: null, //捕获节点被拖拽的事件回调函数  
	        onDrop: null, //捕获节点拖拽操作结束的事件回调函数  
	        onExpand: null //捕获节点被展开的事件回调函数  
		}  
};

/**
 * 重写getFontCss的函数
 * @param treeId
 * @param treeNode
 * @returns
 */
function getFontCss(treeId, treeNode){
	return (!!treeNode.highlight) ?{color:"#A60000", "font-weight":"bold"} :{color:"#333", "font-weight":"normal"};
}

/**
 * 递归树匹配节点icon
 * @param arr
 */
function recursionArray(arr){
  for(var i in arr){
  	if(arr[i].pId == 0){
          arr[i].icon = "../../sys/css/zTreeStyle/img/diy/main.png";
    }else{
          arr[i].icon = "../../sys/css/zTreeStyle/img/diy/1_open.png";
    } 
  }
}

/**
 * 处理节点点击事件
 * @param event
 * @param treeId
 * @param treeNode
 */
function zTreeOnClick(event, treeId, treeNode){
	returnPopId = treeNode.id;
	returnPopName = treeNode.name;
}

/**
 * 隐藏或显示Tree
 */
$('.sidebar-toggle').click(function(){
    if($('body').hasClass('sidebar-collapse')){
        $('.collapse_btn_container').css('borderBottom','1px solid #ddd').children('span').show();
        $('#zTreeId').css('display','block');
        $(this).children('span').removeClass('glyphicon-menu-right').addClass('glyphicon-menu-left');
    }else{
        $('.collapse_btn_container').css('borderBottom','0px').children('span').hide();
        $('#zTreeId').css('display','none');
        $(this).children('span').removeClass('glyphicon-menu-left').addClass('glyphicon-menu-right');
    }
});


var key;
var lastValue = "", nodeList = [], fontCss ={};

/**
 * 获取业务对象树的通用方法
 * @param busiUrl
 */
function queryzTree(busiUrl, paramData){
	$.ajax({
	  type: "post",
	  url:busiUrl,
	  dataType:"json",
	  contentType: "application/json;charset=utf-8",
	  data: JSON.stringify(paramData),
	  success: function(json){
	      var zNodes = json.result;
	      recursionArray(zNodes);
	      zTreeObj = $.fn.zTree.init($("#zTreeId"), setting, zNodes);
	     
	      //-----对二级和三级节点自动展开----begin----
	      var treeObj = $.fn.zTree.getZTreeObj("zTreeId");
          var nodes = treeObj.getNodes();
          for (var i = 0; i < nodes.length; i++){ //设置节点展开
              treeObj.expandNode(nodes[i], true, false, true);//展开二级节点
              var pNode = nodes[i];
              if(pNode.isParent){
            	  var subnodes = pNode.children;
            	  for(var j=0; j<subnodes.length; j++){
            		  treeObj.expandNode(subnodes[j], true, false, true);//展开三级节点
            	  }
              }
          }// //-----对二级和三级节点自动展开----end----
          
          //------------------处理输入框关键字--------------------------------
          key = $("#key");
          key.bind("focus", focusKey).bind("blur", blurKey)
			.bind("propertychange", searchNode).bind("input", searchNode);
	  }
	})
}

/**
 * focusKey事件的处理方法
 * @param e
 */
function focusKey(e){
	if (key.hasClass("empty")){
		key.removeClass("empty");
	}
}

/**
 * blurKey事件的处理方法
 * @param e
 */
function blurKey(e){
	if (key.get(0).value === ""){
		key.addClass("empty");
	}
}

/**
 * searchNode事件的处理方法
 * @param e
 */
function searchNode(e){
	var zTree = $.fn.zTree.getZTreeObj("zTreeId");
	var keyType = "name";
	var value = $.trim(key.get(0).value);
	if (lastValue === value) return;
	lastValue = value;
	if (value === "") return;
	updateNodes(false);
	
	nodeList = zTree.getNodesByParamFuzzy(keyType, value);
	for(var i=0;i<nodeList.length;i++){
		var node=nodeList[i];
		var parentNode=node.getParentNode();
		if(parentNode && !parentNode.open){
			zTree.expandNode(parentNode,true,false,false,false);
		}
	}
	updateNodes(true);
}

/**
 * 对节点进行高亮显示
 * @param highlight
 */
function updateNodes(highlight){
	var zTree = $.fn.zTree.getZTreeObj("zTreeId");
	for( var i=0, l=nodeList.length; i<l; i++){
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}
//----------------------- zTree 配置部分  end ----------------------------------

//---------------------  回写显示部分通用的逻辑   begin -----------------------------
var userKey = "user";
var userArray = new Array();
var postKey = "post";
var postArray = new Array();
var roleKey = "role";
var roleArray = new Array();
var orgKey = "org";
var orgArray = new Array();
var formValueIdArray = new Array();//表单变量的ID数组
var formValueNameArray = new Array();//表单变量的name数组
var resDataIdArray = new Array();//指定资源的ID数组
var resDataNameArray = new Array();//指定资源的name数组

/**
 * 获取表单变量的数据列表
 */
function queryFormValueList(busiObjectId){
	var paramData ={delflag:false, businessObjectId: busiObjectId}; //{ delflag:false };
	$.ajax({ //发送更新的ajax请求
	    type: "POST",  
	    url: hostUrl+"flow/businessObjectVariable/queryList",    
	    dataType:"json",  
	    async: false,
	    data:  JSON.stringify(paramData),//此处必须JSON.stringify(paramData)
	    contentType: 'application/json;charset=utf-8', //设置请求头信息  
	    success: function(data){
	    	variableList = data.result;
	    	
	    	//先清空数组的数据,然后压入查询到的数据
	    	formValueIdArray.length = 0;
	    	formValueNameArray.length = 0;
	    	$.each(variableList,function(index, item){//遍历mapList的数组数据
	    		formValueIdArray.push(item.id); 
	    		formValueNameArray.push(item.name);
	    	});//$.each(appList	
	    },  
	    error: function(data){  
	    	if(data.msg){
	    		alert(data.msg);
	    	}else{
	    		alert("查询业务变量列表数据的接口异常");
	    	}
	    }  
	});//end-for $.ajax({
}

/**
 * 根据给定的参数查询,查询对应的资源数据列表
 * @param paramData：给定的参数
 */
function queryResDataList(paramData){
	var postdata={ paramData:paramData };
	$.ajax({ //发送更新的ajax请求
	    type: "POST",  
	    url: hostUrl+"sys/org/orgnazation/queryResListByIds",    
	    dataType:"json",  
	    async: false,
	    data:  JSON.stringify(postdata),//此处必须JSON.stringify(paramData)
	    contentType: 'application/json;charset=utf-8', //设置请求头信息  
	    success: function(data){
	    	var resDataList = data.result;
	    	//先清空数组的数据, 然后压入查询到的数据    
	    	resDataIdArray.length = 0;
	    	resDataNameArray.length = 0;  
	    	$.each(resDataList,function(index, item){//遍历mapList的数组数据
	    		resDataIdArray.push(item.id); 
	    		resDataNameArray.push(item.name);
	    	});//$.each(appList	
	    },  
	    error: function(data){  
	    	if(data.msg){
	    		alert(data.msg);
	    	}else{
	    		alert("查询资源数据的接口异常");
	    	}
	    }  
	});//end-for $.ajax({
}

/**
 * 为查询资源数据准备查询参数(user/post/role/org)
 * @param personList: 获取到的参与人数据
 */
function prepareParamDataForResData(personList){
	for(var idx=0; idx<personList.length; idx++){
		var item = personList[idx];
		var scope = item.participantScope;
		scope = scope+"";
		if(scope == "11"){//指定人员
			userArray.push(item.participantId);
		}else if(scope == "21"){//指定岗位
			postArray.push(item.participantId);
		}else if(scope.startWith("31")){//选择了角色
			roleArray.push(item.participantId);
			if(scope=="316"){
				orgArray.push(item.paramValue);
			}
		}
	}
	var typeArray = [userKey, postKey, roleKey, orgKey];
	var idArray = [userArray, postArray, roleArray, orgArray];
	var paramArray = new Array();
	for(var i=0; i<typeArray.length; i++){
		var idItemArray = idArray[i];
		if(idItemArray.length>0){
			var obj = new Object();
			obj.type = typeArray[i];
			obj.ids = idArray[i];
			paramArray.push(obj);
		}
	}
	return paramArray;
}

/**
 *  对查询数据进行后处理加工,以便进行Table TR TD的展示
 *  此方法是table tr td中通用的
 * @param resultList 
 * @returns 返回经加工处理的数据列表
 */
function getDataListAfterPostDataProcess(resultList, businessObjectId){
	if(!resultList || resultList.length==0){//如果为空或没有数据,则不进行处理
		return;
	}
	var paramArray = prepareParamDataForResData(resultList);//先准备查询的数据
	queryFormValueList(businessObjectId);
	queryResDataList(paramArray);
	$.each(resultList,function(index,item){//遍历resultList的数组数据
 	   item.participantTypeText = typeItemArray[item.participantType];
 	   item.participantScopeName = getParticipantScopeText(item.participantScope);
 	   var scope = item.participantScope;
 	   if(scope == "11" || scope == "21" || scope.startWith("31")){
 		   item.participantIdName = getResDataText(item.participantId);
 		   if(scope == "316"){
 			  item.paramValueName = getResDataText(item.paramValue);
 		   }
 	   }else if(scope == "12" || scope == "22"){
 		   item.participantIdName = getFormValueText(item.participantId);
 	   }else if(scope == "317"){
 		   item.paramValueName = getFormValueText(item.paramValue);
 	   }
 	   if(item.type=="1" || item.type =="3"){
 		  item.blockIdx ="one";
 	   }else{
 		  item.blockIdx ="two";
 	   }
 	});	
	return resultList;
}

/**
 * 根据表单的ID获得对应的ID值
 * @param formValueId
 * @returns{String}
 */
function getFormValueText(formValueId){
	var idx = jQuery.inArray(formValueId, formValueIdArray);
	var retText = formValueNameArray[idx];
	if(!retText){
		retText = "暂无";
	}
	return retText;
}

/**
 * 根据资源数组的ID,获取对应的Name值
 * @param resDataId
 * @returns ID对应的Name值
 */
function getResDataText(resDataId){
	var idx = jQuery.inArray(resDataId, resDataIdArray);
	var retText = resDataNameArray[idx];
	if(!retText){
		retText = "暂无";
	}
	return retText;
}
//----------------------  回写显示部分通用的逻辑   end -----------------------------