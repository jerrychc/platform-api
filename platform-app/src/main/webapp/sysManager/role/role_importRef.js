/**
 * 引入组织机构
 * @author shiyong
 */
var zTreeObj;

//树 搜索名称参数
var lastValue = "", nodeList = [], fontCss = {};

//zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
var setting = {
    view: {
        fontCss: getFontCss
    },
    edit: {
        enable: false,
        showRemoveBtn: false,
        showRenameBtn: false
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        onDblClick: zTreeOnDblClick,
        onCollapse: function(){
            $.xljUtils.treeResizeFn();
        },
        onExpand: function(){
            $.xljUtils.treeResizeFn();
        }
    }
};
/**
 * 引用类型格式化
 * @param cellvalue
 * @param options
 * @param rowObject
 * @returns {String}
 */
function refTypeFmatter (cellvalue, options, rowObject) {
    if(cellvalue == "1"){
        return "人员";
    }else if(cellvalue == "2"){
        return "岗位";
    }
}


var newrowid = 0;
/**
 * 双击组织机构树
 * @param event
 * @param treeId
 * @param treeNode
 * @returns {Boolean}
 */
function zTreeOnDblClick(event, treeId, treeNode) {
//	treeNode=zTreeObj.getSelectedNodes();
    if(!(treeNode.type == 'user' || treeNode.type == 'post')){
        var ztreeObj = $.fn.zTree.getZTreeObj(treeId);
        var childNodes = ztreeObj.transformToArray(treeNode);
        var rowDatas = [];
        //var existIds = $("#listRef").jqGrid('getDataIDs');
        var existIds = [];
        var obj=$("#listRef").jqGrid("getRowData");
        jQuery(obj).each(function(){
            if(this.targetType=='岗位'){
                existIds.push(this.postId);
            }
        });
        $.each(childNodes,function (i,childNode) {
            if(childNode.type=='post'){
                var dataRow = {
                    //id:'JQGRID_' + childNode.id,
                    postId:childNode.postId,
                    targetName:childNode.prefixName
                };
                dataRow.targetType='2';
                var isHave = existIds.indexOf(dataRow.postId) ;
                if(isHave == -1){
                    rowDatas.push(dataRow);
                }
            }
        });
        $("#listRef").jqGrid("addRowData", "id", rowDatas);
        return false;
    }
    var isExistUser = false ;
    var isExistPost = false ;
    var obj=$("#listRef").jqGrid("getRowData");
    jQuery(obj).each(function(){
        if(treeNode.type=='user'){
            if(this.targetType=='人员' && this.userId == treeNode.id ){
                if(treeNode.postId && this.postId != treeNode.postId){
                    return true;
                }
                isExistUser =  true;
                return false;
            }
        }else if(treeNode.type=='post'){
            if(this.targetType=='岗位' && this.postId == treeNode.id){
                isExistPost =  true;
                return false;
            }
        }
    });

    if(isExistUser){
        pop_tip_open("blue","该岗位人员已存在，请重新选择");
        return false;
    }
    if(isExistPost){
        pop_tip_open("blue","该岗位已存在，请重新选择");
        return false;
    }

    newrowid = newrowid+1;
    var dataRow = {
        postId:treeNode.postId,
        targetName:treeNode.name,
        belongOrgName:treeNode.belongOrgName
    };
    if(treeNode.type=='user'){
        dataRow.targetType='1';
        dataRow.userId=treeNode.id;

    }else if(treeNode.type=='post'){
        dataRow.targetType='2';
    }
    //将新添加的行插入到第一列
    $("#listRef").jqGrid("addRowData", newrowid, dataRow, "first");
    $.xljUtils.addGridScroll();
    $.xljUtils.gridResizeFn();
};

/**
 * 初始化已选组织
 */
function initJqGridOrg(refStatus){
    var ubody = "sys/org/roleUser/queryRoleRefListByRoleId";
    var uall = hostUrl+ubody;
    var roleId=window.opener.importOrgNode.id;
    //创建jqGrid组件
    var jqGridorg = jQuery("#listRef").jqGrid(
        {
            url: uall,
            ajaxGridOptions: { contentType: 'application/json' },
            mtype : "POST",
            contentType : "application/json",
            postData:{"roleId":roleId,"refStatus":refStatus},
            datatype : "json",
            jsonReader : {
                // root:"result"
                root:function(data){
                    data = data.result;
                    for(var i =0;i<data.length;i++){
                        var rowdata = data[i];
                        if(!rowdata.postId){
                            var arr = rowdata.targetName.split("/");
                            var userName = arr[arr.length-1];
                            var orgName = rowdata.targetName.substring(0,(rowdata.targetName.length-userName.length-1));
                            rowdata.targetName = userName;
                            rowdata.belongOrgName = orgName;
                        }
                    }
                    return data;
                }
            },
            rownumbers: true,
            colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
                {name : 'id',label : '序号',width : 55,align : "center",hidden : true},
                {name : 'userId',label : '用户Id',width : 55,align : "center",hidden : true},
                {name : 'postId',label : '岗位Id',width : 55,align : "center",hidden : true},
                {name : 'targetType',label : '对象类型',width : 180,align : "center",formatter:refTypeFmatter},
                {name : 'targetName',label : '对象',width : 180,align : "center"},
                {name : 'belongOrgName',label : '对象',width : 180,align : "center",hidden : true}
            ],
            rowNum : -1,//一页显示多少条
            sortname : 'id',//初始化的时候排序的字段
            sortorder : "desc",//排序方式,可选desc,asc
//            mtype : "POST",//向后台请求数据的ajax的类型。可选post,get
            ondblClickRow:function(rowid,iRow){
                $("#listRef").jqGrid("delRowData", rowid);
                $.xljUtils.addGridScroll();
                $.xljUtils.gridResizeFn();
            },
            gridComplete: function() {//当表格所有数据都加载完成，
                /*var objs = $(this).jqGrid("getRowData");
                for(var i = 0;i<objs.length;i++){
                    var rowdata = objs[i];
                    if(!rowdata.postId){
                        var arr = rowdata.targetName.split("/");
                        var userName = arr[arr.length-1];
                        var orgName = rowdata.targetName.substring(0,(rowdata.targetName.length-userName.length-1));
                        $("#"+rowdata.id).find("td[aria-describedby='listRef_targetName']").html(userName);
                        $("#"+rowdata.id).find("td[aria-describedby='listRef_targetName']").attr("title",orgName);
                    }
                }*/
                $("#listRef > tbody tr").each(function(){
                    var orgName = $(this).find("td[aria-describedby='listRef_belongOrgName']").html();
                    $(this).find("td[aria-describedby='listRef_targetName']").attr("title",orgName)
                });
                $.xljUtils.addGridScroll();
                $.xljUtils.gridResizeFn();
            },


            viewrecords : true
        });
}

/*
 * 树搜索方法
 */

function focusKey(e) {
    if (key.hasClass("empty")) {
        key.removeClass("empty");
    }
}
function blurKey(e) {
    if (key.get(0).value === "") {
        key.addClass("empty");
    }
}

function clickRadio(e) {
    /*lastValue = "";
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    var value = $.trim(key.get(0).value);
    //如果搜索框内无内容，不进行搜索，展开所有节点
    if(value == ""){
        zTree.expandAll(true);
        setTimeout(function(){
            $.xljUtils.treeResizeFn();
        },300);
    }else{
        searchNode();
    }*/
    var searchKeys = ['loginName', 'name'];
    $.xljUtils._searchTreeBtnEvent(key,zTreeObj, searchKeys);
//	$.xljUtils._searchTreeBtnEvent(key,zTreeObj);
}
function searchNode(e) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
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
function updateNodes(highlight) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    for( var i=0, l=nodeList.length; i<l; i++) {
        nodeList[i].highlight = highlight;
        zTree.updateNode(nodeList[i]);
    }
}
function getFontCss(treeId, treeNode) {
    /*return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};*/
    return (treeNode.highlight&&treeNode.highlight=='true') ?
        {'font-family': 'Verdana, Arial, Helvetica, AppleGothic, sans-serif','font-style':'italic', "font-weight":"bold"} :
        {color:"#333", "font-weight":"normal",'font-style':'normal'} | (treeNode.status&&treeNode.status=='0') ?
            {'font-family': 'Verdana, Arial, Helvetica, AppleGothic, sans-serif','font-style':'normal', 'color':'#CD0000'} :
            {color:"#333", "font-weight":"normal",'font-style':'normal'};
}
function filter(node) {
    return !node.isParent && node.isFirstNode;
}



function showLog(str) {
    if (!log) log = $("#log");
    log.append("<li class='"+className+"'>"+str+"</li>");
    if(log.children("li").length > 8) {
        log.get(0).removeChild(log.children("li")[0]);
    }
}
function getTime() {
    var now= new Date(),
        h=now.getHours(),
        m=now.getMinutes(),
        s=now.getSeconds(),
        ms=now.getMilliseconds();
    return (h+":"+m+":"+s+ " " +ms);
}

function setTrigger() {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.setting.edit.drag.autoExpandTrigger = $("#callbackTrigger").attr("checked");
}



/**
 * 递归设置树的图片样式
 */
function recursionArray(arr) {
    //所属的分类 diy-group 目录 diy-company 集团和公司;diy-program 项目和分期;diy-department 部门;
    for(var i in arr) {
        if(/['"#$%&\^*]/.test(arr[i].name)){
            arr[i].name=$.xljUtils.htmlDecode(arr[i].name);
        }
        if(arr[i].type == "zb" || arr[i].type == "company") {
            arr[i].iconSkin = "diy-company";
        }else if(arr[i].type == "dept" ) {
            arr[i].iconSkin = "diy-department";
        }else if(arr[i].type == "group" ) {
            arr[i].iconSkin = "diy-program";
        }else if(arr[i].type == "branch" ) {
            arr[i].iconSkin = "diy-program";
        }else if(arr[i].type == "cata" ) {
            arr[i].iconSkin = "diy-group";
        }else if(arr[i].type == "post" ) {
            arr[i].iconSkin = "diy-post";
        } else if(arr[i].type == "user" ) {
            arr[i].iconSkin = "diy-member";
        }
    }
};
/**
 * 递归设置树的图片样式
 */
function _recursionArray(arr) {
    //所属的分类 diy-group 目录 diy-company 集团和公司;diy-program 项目和分期;diy-department 部门;
    for(var i in arr) {
        if(/['"#$%&\^*]/.test(arr[i].name)){
            arr[i].name=$.xljUtils.htmlDecode(arr[i].name);
        }
        if(arr[i].type == "zb" || arr[i].type == "company") {
            arr[i].iconSkin = "diy-company";
            if(arr[i].children.length > 0) {
                _recursionArray(arr[i].children);
            }
        }else if(arr[i].type == "dept" ) {
            arr[i].iconSkin = "diy-department";
            if(arr[i].children.length > 0) {
                _recursionArray(arr[i].children);
            }
        }else if(arr[i].type == "group" ) {
            arr[i].iconSkin = "diy-program";
            if(arr[i].children.length > 0) {
                _recursionArray(arr[i].children);
            }
        }else if(arr[i].type == "branch" ) {
            arr[i].iconSkin = "diy-program";
            if(arr[i].children.length > 0) {
                _recursionArray(arr[i].children);
            }
        }else if(arr[i].type == "cata" ) {
            arr[i].iconSkin = "diy-group";
            if(arr[i].children.length > 0) {
                _recursionArray(arr[i].children);
            }
        }else if(arr[i].type == "user" ) {
            arr[i].iconSkin = "diy-member";
        }
    }
};
var key;
/**
 * 获取组织机构树
 */
function getOrgTree(refStatus) {
    var time0 = new Date();
    // var urlBody = "sys/org/user/getUserTree";
   var urlBody = "sys/org/roleUser/selectUserOrgTree";
    var urlAll = hostUrl + urlBody;
    var ajaxData = {"rootDelFlag":false,"orgDelFlag":false,"delflag":false,"userStatus":true,"userDelFlag":false};
    $.ajax({
        type:'POST',
        url:urlAll,
        dataType:'json',
        contentType:'application/json',
        data:JSON.stringify(ajaxData),
        success: function(json) {
            var zNodes = json.result;
            var time1 = new Date();
            recursionArray(zNodes);
            var time2 = new Date();
            zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            var time3 = new Date();
            var nodes = zTreeObj.getNodes();
            //默认展开第一个节点
            zTreeObj.expandNode(nodes[0], true, false, false,false);
            var time4 = new Date();
            key = $("#key");
            var searchKeys = ['loginName', 'name'];
            $.xljUtils._searchTreeInputEvent(key,zTreeObj,searchKeys);

            setTimeout(function(){
                $.xljUtils.addTreeScroll('ztree-box');
                $.xljUtils.treeResizeFn();
            },300);
        }
    })
}
/**
 * 获取岗位树
 */
function getPostTree(refStatus) {
    var time0 = new Date();
//	var urlBody = "sys/org/user/getUserTree";
    var urlBody = "sys/org/roleUser/selectUserPostTree";
    var urlAll = hostUrl + urlBody;
    var ajaxData = {"refStatus":refStatus};
    $.ajax({
        type:'POST',
        url:urlAll,
        dataType:'json',
        contentType:'application/json',
        data:JSON.stringify(ajaxData),
        success: function(json) {
            var zNodes = json.result;
            var time1 = new Date();
            recursionArray(zNodes);
            var time2 = new Date();
            zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            var time3 = new Date();
            var nodes = zTreeObj.getNodes();
            //默认展开第一个节点
            zTreeObj.expandNode(nodes[0], true, false, false,false);
            var time4 = new Date();
            key = $("#key");
            var searchKeys = ['loginName', 'name'];
            $.xljUtils._searchTreeInputEvent(key,zTreeObj,searchKeys);

            setTimeout(function(){
                $.xljUtils.addTreeScroll('ztree-box');
                $.xljUtils.treeResizeFn();
            },300);
        }
    })
}


/**
 * 保存引入
 */
function savePost() {
    var uBody = "sys/org/roleUser/saveBatchRoleUserPost";
    var uAll = hostUrl + uBody;
    var obj=$("#listRef").jqGrid("getRowData");
    var roleId=window.opener.importOrgNode.id;
    var list=[];
    var i=0;
    jQuery(obj).each(function(){
        var id = this.id;
        var reg = new RegExp('^JQGRID_');
        if(reg.test(id)){
            id='';
        }
        var savedata={
            roleId:window.opener.importOrgNode.id,
            id:id,
            userId:this.userId,
            postId:this.postId,
            roleId:roleId,
            targetType:this.targetType=='人员'?1:2
        };
        list[i]=savedata;
        i++;
    });
    console.log(list);
    var jsondata={
        list:list,
        roleId:roleId,
        refStatus:refStatus
    };
    $.ajax({
        type:'POST',
        url:uAll,
        async: false,
        dataType:'json',
        contentType:'application/json',
        data:JSON.stringify(jsondata),
        success: function(json) {
            if(json.success == true){
                var queryDataPost={
                    "roleId":window.opener.importOrgNode.id
                };
                window.opener.jqGridRef.jqGrid("setGridParam", { postData: queryDataPost }).trigger("reloadGrid");
                window.close();
            }else{
                pop_tip_open("red",json.msg);
            }
        }
    })
}

var refStatus;
$(function(){
    /*
     *
     * miying add
     * */
    //计算高度
    resizeHeight();
    function resizeHeight(){
        var w_h = $(window).height();
        //左侧  头部底部为60px  title类 为50px
        $(".slide-left .ztree-box").height((w_h-150)+"px");
        //右侧table
        //$(".con-table .mytable").height(w_h-90+"px");
    }
    //grid 自适应宽度
    $(window).resize(function(){
        resizeHeight();
        //$("#listRole").setGridWidth($('.mytable').width()-2,true);
        //$.xljUtils.gridResizeFn();
    });
//引用类型
    refStatus = $.xljUtils.getUrlParam('refStatus');
    if(!refStatus){
        refStatus = '0';//默认引入人员
        $("title").html("引入人员");
        //初始化组织机构树
        getOrgTree();
    }else{
        if(refStatus == '0'){
            $("title").html("引入人员");
            //初始化组织机构树
            getOrgTree();
        }else {
            $("title").html("引入岗位");
            //初始化岗位树
            getPostTree(refStatus);
        }
    }
    //初始化已选组织
    initJqGridOrg(refStatus);
    //页面加载完毕后更改grid宽高
    $.xljUtils.resizeNestedGrid();
});
