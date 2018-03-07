/**
 * Created by admin on 2017/7/23.
 */
$(function () {
    var baseUrl = '/platform-app/';
    var urlParams = $.xljUtils.getUrlParams();

    $('#treeTitle').text('新闻中心');
    $('.xj-form-title').text('新闻中心');

    $('.xj-form-header').show();
    $('.news-container').css({'padding-top':'70px'});

    /**
     * 初始化新闻/知识列表
     */
    function initContentRowTypeGrid() {
        jQuery("#contentRowGrid").jqGrid({
            url: hostUrl+'/ld/landrayNewsInfo/page',
            ajaxGridOptions: { contentType: 'application/json' },
            mtype : "POST",
            contentType : "application/json",
            datatype : "json",
            postData:{name:"",newsType:my.dataPar.newsTypes[my.dataPar.newsTypeKey]},
            autowidth:true,
            multiselect:true,
            autowidth:true,
            rownumbers:true,
            jsonReader : {
                repeatitems : false
            },
            colModel : [
                {name : 'id',label : 'id',hidden:true,align : "center"},
                {name : 'url',label : '静态页面url',hidden:true,align : "center"},
                {name : 'newsType',label : '新闻类型',hidden:true,align : "center"},
                {name : 'code',label : '编码',hidden:true,align : "center"},
                {name : 'name',label : '主题',align : "center"},
                {name : 'importance',label : '文档重要度',align : "center"},
                {name : 'createPersonName',label : '创建者',align : "center"},
                {name : 'startDate',label : '发布时间',align : "center"},
                {name : 'updateDate',label : '修改时间',align : "center"},
                {name : 'crt',label : '点击率',align : "center"}
            ],
            rowNum : 20,//一页显示多少条
            rowList : [ 20,50,100,200],//可供用户选择一页显示多少条
            pager : '#pagered',//表格页脚的占位符(一般是div)的id
            ondblClickRow:function(rowid){
                var rowData = $("#contentRowGrid").jqGrid("getRowData",rowid);
                window.open(rowData.url);
            },
            onSelectRow: function (rowid) {
                my.dataPar.rowData = $('#contentRowGrid').jqGrid('getRowData',rowid);
            },
            onCellSelect: function(){
                if(my.dataPar.rowDataBefore!=null&&my.dataPar.rowDataBefore!='undefined'){
                    //重新选择行时清除上一次选中行的样式
                    $('#contentRowGrid '+'#'+my.dataPar.rowDataBefore.id).find("td").removeClass("ui-state-highlight");
                }
            },
            gridComplete: function () {
                $.xljUtils.addGridScroll();
                my.dataPar.rowDataBefore = my.dataPar.rowData;
                if(my.dataPar.rowDataBefore!=null&&my.dataPar.rowDataBefore!='undefined'){
                    //添加回显选中行样式
                    $('#contentRowGrid').setSelection(my.dataPar.rowDataBefore.id,true);
                    $('#contentRowGrid '+'#'+my.dataPar.rowDataBefore.id).find("td").addClass("ui-state-highlight");
                }
            },
            viewrecords : true,
            loadError:function(xhr,status,error){
                //异常处理
                if(xhr.status==404){
                    pop_tip_open("red","请求url有误！");
                    return;
                }
                if(xhr.status==405){
                    pop_tip_open("red","请求方法有误！");
                    return;
                }
                pop_tip_open("red","网络异常,请联系管理员！");
            },
            loadComplete:function(xhr){
                if(!xhr.success){
                    if(xhr.code=="50000"||xhr.code=="50001"||xhr.code=="50003"){
                        pop_tip_open("red",xhr.msg);
                        return;
                    }
                    if(xhr.code=="50002"){
                        pop_tip_open("blue",xhr.msg);
                        return;
                    }
                    pop_tip_open("red","查询数据失败！");
                }else{
                    //success
                }
            }

        });
    }
    initContentRowTypeGrid();


    /**
     * 加载左侧知识目录树
     */
    function initContentChildTree() {
        $.ajax({
            type: "POST",
            url: hostUrl + 'oa/content/contentChild/queryTreeList?time=' + Math.random(),
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify({delflag: false,contentTypeId:urlParams.contentTypeId,contentType:urlParams.contentType}),
            success: function (typeNodes) {
                var zNodes = typeNodes.result;
                if (zNodes == null || zNodes.length == 0) {
                    return;
                }
                $("#contentTitle").html( zNodes[0].name);

                var setting = {
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: 'id',
                            pIdKey: 'pid',
                            rootPId: null
                        }
                    },
                    edit: {
                        enable: false
                    },
                    callback: {
                        onClick: function (event,treeId,treeNode,clickFlag) {
                            var  postData = $("#contentRowGrid").jqGrid('getGridParam','postData');
                            if(postData != undefined){
                                delete postData['extendAttarFields'];
                            }
                            if(postData != undefined){
                                delete postData['fuzzyQueryFields'];
                            }

                            if(!postData) {
                                postData = {};
                            }
                            if(treeNode.contentChildId){
                                var childeNodeIds = new Array();
                                var nodeList = $.fn.zTree.getZTreeObj(treeId).transformToArray(treeNode);

                                for(var i = 0, len = nodeList.length;i<len;i++) {
                                    var childNodeId = nodeList[i].id;
                                    if(childNodeId){
                                        childeNodeIds.push(childNodeId);
                                    }
                                }
                                if(childeNodeIds.length > 0){
                                    postData.contentChildId = childeNodeIds.join(',');
                                }else{
                                    postData.contentChildId = null;
                                }
                            }else{
                                delete postData['contentChildId'];
                            }
                            postData.contentTypeId = treeNode.contentTypeId;
                            postData.loadState = "tree_click";
                            $("#contentRowGrid").jqGrid('setGridParam', {
                                postData: postData,
                                page:1
                            }).trigger('reloadGrid');
                        },
                        onExpand:function (event, treeId, treeNode) {
                            $.xljUtils.treeResizeFn();
                        },
                        onCollapse: function(){
                            $.xljUtils.treeResizeFn();
                        }
                    }

                };

                $.fn.zTree.init($("#contentTree"), setting, zNodes);
                var zTreeObj = $.fn.zTree.getZTreeObj('contentTree');
                var nodes = zTreeObj.getNodes();
                //默认展开第一个节点
                zTreeObj.expandNode(nodes[0], true, false, false, false);
                zTreeObj.selectNode(nodes[0]);
                zTreeObj.setting.callback.onClick(null, zTreeObj.setting.treeId, nodes[0]);
                setTimeout(function(){
                    $.xljUtils.addTreeScroll();
                    $.xljUtils.treeResizeFn();
                },300);
            }
        });
    }
    initContentChildTree();
    //计算高度
    function resizeTreeHeight() {
        var headerHeight = 0;
        if($('header').is(":visible") ){
            headerHeight = 70;
        }
        var height = window.innerHeight;//$(window).height()
        if(!window.innerHeight){
            height = Math.max(document.body.clientHeight,document.documentElement.clientHeight);
        }
        $('.ztree-box').height(height-$('.org-title').outerHeight()-$('.searchBox:visible').outerHeight()-38-headerHeight);
    }
    resizeTreeHeight();

    /**
     * open新闻静态页面
     */
    function toDetail(){
        var ids=$('#list2').jqGrid('getGridParam','selarrrow');
        if(!ids||ids.length==0) {
            pop_tip_open("blue","请选择要修改的规则类型行");
            return;
        }
        if(ids.length>1){
            pop_tip_open("blue","请选择一行！");
            return;
        }
        this.dataPar.rowData = $('#list2').jqGrid('getRowData',ids);
        var rowData = $("#list2").jqGrid("getRowData",ids);
        window.open(rowData.url);
    }

    $(window).resize(function () {
        resizeTreeHeight();
        $.xljUtils.resizeNestedGrid();
    });

});
