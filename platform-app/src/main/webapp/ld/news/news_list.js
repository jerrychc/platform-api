/**
 * @author lY
 * @CREATEDATE 2017/03/22
 */
(function(window,document){
    var _newsList = {
    	ns : "_newsList",
    	dataPar:{
    		rowData:null,
    		rowDataBefore:null,
			newsTypes : {
				0 : "分子公司公告",
				1 : "集团公告",
				2 : "集团动态"
			},
			newsTypeKey:null
    	},
    	/**
    	 * 加载页面数据
    	 */
    	loadPageData:function(){
    		var my = this;
    		jQuery("#list2").jqGrid({
    	        url: hostUrl+'/ld/landrayNewsInfo/page',
    	        ajaxGridOptions: { contentType: 'application/json' },
    	        mtype : "POST",  
    	        contentType : "application/json",  
    	        datatype : "json",
    	        postData:{name:"",newsType:my.dataPar.newsTypeKey},
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
    	        pager : '#pager2',//表格页脚的占位符(一般是div)的id
    	        ondblClickRow:function(rowid){
					var rowData = $("#list2").jqGrid("getRowData",rowid);
					window.open(rowData.url);
    	        },
    	        onSelectRow: function (rowid) {
        		    my.dataPar.rowData = $('#list2').jqGrid('getRowData',rowid);
                },
    	        onCellSelect: function(){
                	if(my.dataPar.rowDataBefore!=null&&my.dataPar.rowDataBefore!='undefined'){
                		//重新选择行时清除上一次选中行的样式
                		$('#list2 '+'#'+my.dataPar.rowDataBefore.id).find("td").removeClass("ui-state-highlight"); 
                	}
                },
                gridComplete: function () {
                	$.xljUtils.addGridScroll();
                	my.dataPar.rowDataBefore = my.dataPar.rowData;
                    if(my.dataPar.rowDataBefore!=null&&my.dataPar.rowDataBefore!='undefined'){
                    	//添加回显选中行样式
                    	$('#list2').setSelection(my.dataPar.rowDataBefore.id,true);
                    	$('#list2 '+'#'+my.dataPar.rowDataBefore.id).find("td").addClass("ui-state-highlight"); 
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
    	},
    	/**
    	 * open新闻静态页面
    	 */
		toDetail:function(){
    		var ids=$('#list2').jqGrid('getGridParam','selarrrow');
    		if(!ids||ids.length==0) {
    			pop_tip_open("blue","请选择要查看的新闻");
    			return;
    		}
    		if(ids.length>1){
    			pop_tip_open("blue","请选择一行！");
    			return;
    		}
    		this.dataPar.rowData = $('#list2').jqGrid('getRowData',ids);
			var rowData = $("#list2").jqGrid("getRowData",ids);
    		window.open(rowData.url);
    	},
    	reloadGrid:function(id){
    		var my = this;
    		if(null!=id&&""!=id){
    			my.dataPar.rowData = {id:id};
    		}
    		$('#list2').jqGrid().trigger("reloadGrid");
    	},
    	/**
    	 * 装载过滤查询的条件
    	 */
    	searchData:function(){
			var my = this;
    		var corname=$("#corname").val();
			var createCompanyName = $('#createCompanyName').val();
    	 	jQuery("#list2").jqGrid("setGridParam",{postData:{name:corname,newsType:my.dataPar.newsTypeKey,createCompanyName:createCompanyName},page:1}).trigger("reloadGrid");
    	},
    	/**
    	 * 渲染grid数据样式
    	 */
    	addCellAttr:function (rowId, val, rowObject, cm, rdata) {
    	    if(rowObject.status == "0" ){
    	        return "style='color:red'";
    	    }
    	},
    	/**
    	 * 模糊查询支持回车事件
    	 */
    	bindSearchData:function(){
    		var my = this;
    		$("#corname").keyup(function(event){
    			if(event.keyCode ==13){
    				my.searchData();
    			}
    		});
			//获取页面参数并赋值
			my.dataPar.newsTypeKey = $.xljUtils.getUrlParam('newsType');
			//赋值页面标题
			$('#newsTitle').html(my.dataPar.newsTypes[my.dataPar.newsTypeKey]);
			if(my.dataPar.newsTypeKey==2){
				$('.resetwidth').show();
			}else{
				$('.resetwidth').hide();
			}
    	},
    	/**
    	 * 页面初始化
    	 */
    	pageInit:function(){
    		//支持回车事件
    		this.bindSearchData();
    		//初始化数据
    		this.loadPageData();
    		//页面加载完毕后更改grid宽高
    	    $.xljUtils.resizeNestedGrid();
    	}
    };
    $(_newsList.pageInit());
    window[_newsList.ns] = _newsList;
})(window,document);