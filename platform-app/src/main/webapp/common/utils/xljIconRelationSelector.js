/**
 * erp_cloud_platform xljSingleSelector Created by dingguanghuai on 2017/4/2.
 * @author dingguanghuai
 * @date 2017/4/2
 */
;;(function ($,window,document,undefined) {
    var IconRelationSelector = function (ele, opts) {
        this.$element = ele;
        this.defaults = {
            targetId:null,//选择的数据的ID存储input域ID
            targetName:null,//选择的数据的Name存储input域ID
            targetCode:null,//选择数据的编码存储input域ID
            saveCallback:function (selectedData,ele) {}
        };
        this.options = $.extend({},this.defaults, opts);
    };

    IconRelationSelector.prototype = {
        modalContainer:null,
        isOneLoad:false,
        _init:function () {
            var $ele = $(this.$element);

            //modal最外层div
            var modalContainer = this._createModalContainer();
            this.modalContainer = modalContainer;
            //modal内容div
            var modalContentContainer = this._createModalContentContainer();
            modalContainer.append(modalContentContainer);
            //modal头部div
            var modalHeaderContainer = this._createModalHeaderContainer();
            modalContentContainer.find('.modal-content').append(modalHeaderContainer);
            //modal body div
            var modalBodyContainer = this._createModalBodyContainer();
            modalContentContainer.find('.modal-content').append(modalBodyContainer);

            //modal body
            var bodyRightContainer = this._createBodyRightContainer();
            this.rightContainer = bodyRightContainer;
            modalBodyContainer.append(bodyRightContainer);
            //orgGrid头部 div
            var gridHeader = this._createGridHeader();
            bodyRightContainer.append(gridHeader);
            //orgGrid容器div
            var gridContainer = this._createGridContainer();
            bodyRightContainer.append(gridContainer);

            //orgGrid 的table
            var gridObj = this._createGrid();
            gridObj.find("table").attr('id','_relationFlowGrid'+new Date().getTime());
            gridContainer.append(gridObj);

            var orgGridFooter = this._createGridFooter();
      /*      orgGridFooter.attr('id','_relationFlowGridFooter'+new Date().getTime());*/
            gridContainer.append(orgGridFooter);
            if($(document.body).find("._iconToolsModal").length>0){
                this.isOneLoad = true;
                modalContainer = $(document.body).find("._iconToolsModal");
            }
            $(document.body).append(modalContainer);

            $ele.on('click', function () {
                modalContainer.modal({show: true, backdrop: 'static'});
            });

            //取消按钮事件
            modalHeaderContainer.find('button.modal-cancel').on('click',function () {
                //gridObj.jqGrid('clearGridData');
                modalContainer.modal('hide');
            });

            bodyRightContainer.find('input').on('keypress',function (event) {
                var inputVal = $(this).val();
                if (event.keyCode == 13) {
                    gridObj.jqGrid('setGridParam', {postData: {code:inputVal,name:inputVal}}).trigger('reloadGrid');
                }
            });

            bodyRightContainer.find('button').on('click',function (event) {
                var inputVal = $(bodyRightContainer.find('input')[0]).val();
                //if (event.keyCode == 13) {
                    gridObj.jqGrid('setGridParam', {postData: {code:inputVal,name:inputVal}}).trigger('reloadGrid');
                //}
            });

            this._initSizeAndData(gridContainer,modalContainer,gridObj);

            this._initGrid(gridObj,orgGridFooter);

            var that = this;
            modalHeaderContainer.find('.modal-save').off('click').on('click',function () {
                that._saveSelectData(gridObj,modalContainer);
            });


        },
        _createModalContainer: function () {
            var modalContainer = $('<div class="_iconToolsModal modal fade" tabindex="-1" role="dialog" aria-labelledby="_iconGridModalLabel"></div>');
            return modalContainer;
        },
        _createModalContentContainer: function () {
            var modalContentContainer = $('<div class="modal-dialog modal-grid" role="document"><div class="modal-content"></div></div>');
            return modalContentContainer;
        },
        _createModalHeaderContainer: function () {
            var modalHeaderContainer = $('<div class="modal-header">' +
                '   <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
                '   <h5 class="modal-title modal-jz" id="_iconGridModalLabel">选择图标</h5>' +
                '   <button type="button" class="btn btn-sm blue pull-right mr20 modal-cancel">取消</button>' +
                '   <button type="button" class="btn btn-sm blue pull-right mr20 modal-save">确定</button>' +
                '</div>');
            return modalHeaderContainer;
        },
        _createModalBodyContainer:function () {
            var modalBodyContainer = $('<div class="modal-body clearfix"></div>');
            return modalBodyContainer;
        },
        _createBodyRightContainer:function () {
            var bodyRightContainer = $('<div class="changeWidth rm-pad" style="width: 100%;" ></div>');
            return bodyRightContainer;
        },
        _createGridHeader:function () {
            var orgGridHeader = $('<div class="modal_title">' +
                '   <i class="fa fa-list" aria-hidden="true"></i>' +
                '   <span>图标列表</span>' +
                '   <input type="text" class="form-control" placeholder="编码/名称">'+
                '   <button type="button" class="btn btn-sm btnMsearch rm-pad">'+
                '      <i class="fa fa-search" aria-hidden="true"></i>'+
                '   </button>'+
                '</div>');
            return orgGridHeader;
        },
        _createGridContainer:function () {
            var orgGridContainer = $('<div id="_orgGridContainer"></div>');
            return orgGridContainer;
        },
        _createGrid:function () {
            var orgGrid = $('<table id="_iconToolsGridList"></table>');
            return orgGrid;
        },
        _createGridFooter:function(){
        	/*<div id="_orgGridPage1"></div>*/
            var orgGridFooter = $('<div id="_orgGridPage1"></div>');
            return orgGridFooter;
        },

        /**
         * 初始化容器大小和数据
         * @param orgTreeContainer ztree父级容器
         * @param orgGridContainer grid父级容器
         * @param orgGridModal 模态框jq对象
         * @param orgGridObj grid jq对象
         * @param orgTreeObj ztree jq对象
         */
        _initSizeAndData: function (gridContainer,modalContainer,gridObj) {
            var that = this;
            gridContainer.css({
                height:'360px',
                overflow:'hidden'
            });

            modalContainer.on('hide.bs.modal',function () {
                gridContainer.find('.ui-jqgrid-bdiv').getNiceScroll().hide();
            });

            modalContainer.on('shown.bs.modal',function () {
                //modalContainer.find('div.ui-jqgrid-view').css({'padding-top':'8px'});

                gridObj.jqGrid().setGridHeight(gridContainer.height()-60);
                gridObj.jqGrid().setGridWidth(gridContainer.width()-2);

               // $.xljUtils.addTreeScroll('selector-model-tree');
                $.xljUtils.addGridScroll();
                $('.ui-jqgrid-bdiv').getNiceScroll().show().resize();
                modalContainer.css({position:'fixed'});
            });
        },
        /**
         * 初始化grid
         * @param gridObj
         * @param colNames
         * @param colModel
         * @private
         */
        _initGrid:function (gridObj,orgGridFooter) {
            var that = this;
            if(!that.isOneLoad) {
                gridObj.jqGrid({
                    url: hostUrl + "sys/iconTools/page?_t=" + new Date().getTime(),
                    ajaxGridOptions: {contentType: 'application/json'},
                    mtype: "post",
                    datatype: "json",
                    jsonReader: {
                        repeatitems: false
                        //root: 'result'
                    },
                    postData: {keyword: ''},
                    colModel: [
                        {name: 'id', index: 'id', width: 55, hidden: true},
                        {name: 'code', index: '编码'},
                        {name: 'name', index: '名称'},
                        {
                            name: 'url', label: '图标', align: "center", formatter: function (icon) {
                            if (icon) {
                                return '<img src="' + icon + '" style="width:32px;height:30px">';
                            } else {
                                return "";
                            }
                        }
                        }
                    ],
                    viewrecords: true,
                    rownumbers: true,
                    multiboxonly: true,
                    multiselect: false,
                    caption: "",
                    rowNum: 20,//一页显示多少条
                    rowList: [20, 50, 100, 200],//可供用户选择一页显示多少条
                    pager: '#_orgGridPage1',//表格页脚的占位符(一般是div)的id
                    editurl: "",
                    autowidth: true,
                    ondblClickRow: function (rowid, iRow, iCol, e) {
                        /*var unsearchIds = gridObj.data("searchIds");
                         if(!unsearchIds){
                         unsearchIds = [];
                         }
                         if (unsearchIds.indexOf(rowid)!=-1){
                         unsearchIds.splice(unsearchIds.indexOf(rowid),1);
                         }

                         var searchIds = gridObj.data("unsearchIds");
                         if(!searchIds){
                         searchIds = [];
                         }
                         if (searchIds.indexOf(rowid)!=-1){
                         searchIds.splice(searchIds.indexOf(rowid),1);
                         }
                         gridObj.jqGrid('delRowData',rowid);*/
                    },
                    gridComplete: function () {
                        that.modalContainer.find('.ui-jqgrid-htable').css({'background-color':'#acd7ff;'});
                        $('.ui-jqgrid-bdiv').getNiceScroll().show().resize();
                    },
                    loadComplete: function (xhr) {
                        console.info(xhr);
                    }
                });
            }else{
                gridObj.jqGrid().trigger("reloadGrid");
            }
        },
        /**
         * 保存选择数据
         * @param gridObj
         * @param $ele
         * @param modalContainer
         * @private
         */
        _saveSelectData:function (gridObj,modalContainer) {
            var that = this;
            var id = gridObj.jqGrid('getGridParam', 'selrow');
            if(id.length<0) {
                $.xljUtils.tip('blue','无选择数据！');
                return;
            }

            var rowDatas = [];
            var targetId = '';
            var targetName = '';
            var targetCode = '';
            var rowData = gridObj.jqGrid('getRowData',id);
            targetId += rowData.id+',';
            targetName += rowData.name+',';
            targetCode += rowData.flCode+',';
            rowDatas.push(rowData);

            targetId = targetId.lastIndexOf(',')>-1?targetId.substring(0,targetId.lastIndexOf(',')):'';
            targetName = targetName.lastIndexOf(',')>-1?targetName.substring(0,targetName.lastIndexOf(',')):'';
            targetCode = targetCode.lastIndexOf(',')>-1?targetCode.substring(0,targetCode.lastIndexOf(',')):'';
            if(that.options.targetId&&$.trim(that.options.targetId)!=''){
                var targetIdInputObj = $('#'+that.options.targetId);
                if(targetIdInputObj.length>0){
                    targetIdInputObj.val(targetId);
                    targetIdInputObj.attr('title',targetId);
                }
            }

            if(that.options.targetName&&$.trim(that.options.targetName)!=''){
                var targetNameInputObj = $('#'+that.options.targetName);
                if(targetNameInputObj.length>0){
                    targetNameInputObj.val(targetName);
                    targetNameInputObj.attr('title',targetName);

                }
            }

            that.options.saveCallback(rowDatas,that.$element);

            modalContainer.modal('hide');
        }
    };

    $.fn.extend({
        xljIconRelationSelector:function (options) {
            if(!options){
                return IconRelationSelector;
            }
            var opts = options;
            return this.each(function (index,ele) {
                var eleId = $(ele).attr('id');
                if(!eleId||eleId==''){
                    eleId = 'singleEle_'+new Date().getTime();
                    $(ele).attr('id',eleId);
                }
                var relationSelector = $(document.body).data(eleId);
                if(!relationSelector){
                    relationSelector = new IconRelationSelector(this,opts);
                    $(document.body).data(eleId,relationSelector);
                }
                relationSelector._init();
                return relationSelector;
            });
        },
        xljIconRelationSelectorUtil:function () {
            var eles = $(this).find('.flow-relation-selector');
            $.each(eles,function () {
                $(this).attr('readonly','readonly');

                var opts = {};

                var targetId = $(this).attr('data-targetId');
                if(targetId){
                    opts.targetId = targetId;
                }
                var targetName = $(this).attr('data-targetName');
                if(targetName){
                    opts.targetName = targetName;
                }

                var saveCallback = $(this).attr('data-saveCallback');
                if(saveCallback&&saveCallback!=''){
                    try {
                        if($.isFunction(eval(saveCallback))){
                            opts.saveCallback = eval(saveCallback);
                        }
                    }catch(e){
                        console.error(e);
                    }
                }
                $(this).xljIconRelationSelector(opts);
            });
        }
    });

})(jQuery,window,document);

$(function () {
    var eles = $('.flow-relation-selector');
    $.each(eles,function () {
        $(this).attr('readonly','readonly');

        var opts = {};

        var targetId = $(this).attr('data-targetId');
        if(targetId&&targetId!=''){
            opts.targetId = targetId;
        }
        var targetName = $(this).attr('data-targetName');
        if(targetName&&targetName!=''){
            opts.targetName = targetName;
        }

        var saveCallback = $(this).attr('data-saveCallback');
        if(saveCallback&&saveCallback!=''){
            try {
                if($.isFunction(eval(saveCallback))){
                    opts.saveCallback = eval(saveCallback);
                }
            }catch(e){
                console.error(e);
            }
        }
        $(this).xljIconRelationSelector(opts);
    });
});