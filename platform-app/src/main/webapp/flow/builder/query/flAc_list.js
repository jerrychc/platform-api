/**
 * Created by admin on 2018/1/25.
 */
$(function () {

    var uuidArr;
    //初始化UUID缓存数组
    var initUUIDs = function (count) {

        if(!count){
            count = 100;
        }

        $.ajax({
            type: 'get',
            url: hostUrl + "generator/getGuuids?count="+ count + "&_t=" + new Date().getTime(),
            async: false,
            success: function (data) {
                if (data.success) {
                    uuidArr = data.result;
                }
            }
        });
    };

    //获取UUID
    var getUUID = function (count) {
        if (uuidArr && uuidArr.length > 0) {
            return uuidArr.shift();
        } else {
            initUUIDs(count);
            return uuidArr.shift();
        }
    };

    var initBtnsEvent = function () {
        //展开/收起查询区域
        $('#collapseSearchSpan').unbind('click').on('click',function () {
            //$('#searchFormContainer').fadeToggle();
            if($('#searchFormContainer').is(':visible')){
                $('#searchFormContainer').hide();
                $(this).removeClass('fa-angle-down').addClass('fa-angle-up');
            }else{
                $('#searchFormContainer').show();
                $(this).removeClass('fa-angle-up').addClass('fa-angle-down');
            }
            $.xljUtils.resizeNestedGrid(null,false);
        });

        //展开高级查询区域
        $('#advanceSearchBtn').on('click',function () {
            if($(this).find('i').hasClass('fa-angle-down')){
                $('#exactSearchContainer').hide();
                $(this).find('span').text('高级查询');
                $(this).find('i').removeClass('fa-angle-down').addClass('fa-angle-up');
            }else{
                $('#exactSearchContainer').show();
                $(this).find('span').text('收起');
                $(this).find('i').removeClass('fa-angle-up').addClass('fa-angle-down');
            }
            $.xljUtils.resizeNestedGrid(null,false);
        });

        //查询条件重置
        $('#resetFormBtn').on('click',function () {
            $('#searchForm')[0].reset();
        });

        //查询按钮点击事件
        $('#searchFormBtn').on('click',function () {
            var formDatas = $('#searchForm').serializeArray();
            var searchData = {};
            var postData = $('#flAcListGrid').jqGrid('getGridParam','postData');
            for (var i in formDatas ){
                var eleNameValPair = formDatas[i];
                delete postData[eleNameValPair.name];
                if(eleNameValPair.value!=''){
                    searchData[eleNameValPair.name] = eleNameValPair.value;
                }
            }
            /*var participantIdArr = [];
            var participantId = searchData['participantId'];
            var participantRoleId = searchData['participantRoleId'];
            if(participantId){
                participantIdArr.push(participantId);
            }
            participantIdArr.push();
            if(participantRoleId){
                participantIdArr.push(participantRoleId);
            }

            searchData['participantId'] = participantIdArr.join(',');*/
            var queryData = searchData;
            $('#flAcListGrid').jqGrid('setGridParam', {postData: queryData,page:1}).trigger('reloadGrid');

        });

        //修改环节属性
        $('#updateAttrBtn').on('click',function () {
            var rowIds = $('#flAcListGrid').jqGrid('getGridParam','selarrrow');

            if(!rowIds||rowIds.length==0){
                $.xljUtils.tip('blue','请选择至少一个节点进行属性修改！');
                return;
            }
            $('#acAttrModal').modal({show: true, backdrop: 'static'});
        });

        //替换审批人按钮事件
        $('#replaceApproverBtn').on('click',function () {
            var rowIds = $('#flAcListGrid').jqGrid('getGridParam','selarrrow');

            if(!rowIds||rowIds.length==0){
                $.xljUtils.tip('blue','请选择至少一个节点进行审批人替换！');
                return;
            }
            $('#participantModal').modal({show: true, backdrop: 'static'});
        });
    };
    initBtnsEvent();

    //初始化业务系统
    var initSysAppList = function () {
        var appDef = new $.Deferred();
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
               if(data&&data.success){
                   appDef.resolve(data.result);
               }
            },
            error: function (data) {
                if (data.msg) {
                    $.xljUtils.tip('red', data.msg);
                } else {
                    $.xljUtils.tip('red', "查询业务系统的列表数据失败！");
                }
            }
        });

        return appDef.promise();
    };
    var appDef = initSysAppList();
    var drawAppList = function () {
        $.when(appDef).done(function (appList) {
            $('#appId').empty();
            $('#appId').append('<option value="">请选择</option>');
            for (var i = 0; i < appList.length; i++) {
                var obj = appList[i];
                var optionObj = $('<option ></option>');
                optionObj.attr('value',obj.id);
                optionObj.text(obj.name);
                $('#appId').append(optionObj);
            }
        });

        //
        $('#appId').on('change',function () {
            $("#busiObjectId").val("");
            $("#busiObjectName").val("");
            $("#flId").empty().append("<option value=''>请选择</option>");
        });
    };
    drawAppList();

    //查询流程模板列表
    var queryFlowViewList = function (paramId) {
        var postdata = { delflag: false };
        if(paramId){
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
                var busiObjectList = data.result;
                $("#flId").empty();
                $("#flId").append('<option value="">请选择</option>');
                if(busiObjectList){
                    $.each(busiObjectList, function(index, item){
                        $('#flId').append('<option value="'+item.id+'">'+item.name+'</option>');
                    });
                }

            },
            error: function (data) {
                if (data.msg) {
                    $.xljUtils.tip('red', data.msg);
                } else {
                    $.xljUtils.tip('red', "查询业务系统的列表数据失败！");
                }
            }
        });
    };

    //初始化业务对象列表
    var initBusinessObjectList = function () {
        $('#busiObjectName ~ div.input-group-addon').on('click',function () {
            $("#busiObjectId").val("");
            $("#busiObjectName").val("");
            $("#flId").empty().append("<option value=''>请选择</option>");
        });

        $('#busiObjectName,#busiObjectName ~ span.input-group-addon').on('click',function () {
            var appId = $('#appId').val();
            if(appId == ''){
                $.xljUtils.tip('blue','请先选择应用系统');
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
                targetId: 'busiObjectId', //选择的数据的ID存储input域
                targetName: 'busiObjectName', //选择的数据的Name存储input域
                gridColNames:['ID','名称'],
                gridColModel:[
                    {name : 'id',width : 100,align : "center",hidden : true},
                    {name : 'name',width : 90,align : "center"}
                ],
                saveCallback: function(selectData,selectArray,ele){
                    $('#flId').empty().append("<option value='-1'>请选择</option>");
                    var busiObjectId = $("#busiObjectId").val();
                    var busiObjectIdArr = busiObjectId!=''?busiObjectId.split(','):null;
                    if(busiObjectIdArr){
                        queryFlowViewList(busiObjectIdArr);
                    }
                }
            });
        });
    };
    initBusinessObjectList();

    var approveTypeMap ;
    //初始化审批类型
    var initApproveTypeList = function (){
        var approveTypeDef = new $.Deferred();
        var paramData = {delflag:false, status:true };
        $.ajax({ //发送更新的ajax请求
            type: "post",
            url: hostUrl+"flow/approveType/queryList",
            dataType:"json",
            async:false,
            data: JSON.stringify(paramData),
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function(data){
                $("#approveTypeId").empty().append('<option value="">请选择</option>');//首先清空select现在有的内容
                var resultList = data.result;
                var resultMap = {};
                $.each(resultList,function(index,item){//遍历mapList的数组数据
                    $("#approveTypeId").append("<option value='"+item.code+"'>"+item.name+"</option>");
                    resultMap[item.code] = item.name;
                });
                approveTypeDef.resolve(resultMap);
                approveTypeMap = resultMap;
            }
        });
        return approveTypeDef.promise();
    };
    var approveTypeDef = initApproveTypeList();

    //初始化参与人身份
    var initParticipantRoleList = function (userId) {
        console.info(userId);
    };
    
    //显示参与人身份选择框
    var showParticipantRoleContainer = function () {
        var userId = $('#participantId').val();
        if(userId==''){
            return;
        }
        $('#participantRoleModal').modal({show: true, backdrop: 'static'});
    };

    //参与人身份显示事件
    var initParticipantRoleShowEvent = function () {
        //加载参与人身份
        $('#participantRoleModal').on('shown.bs.modal',function () {
            var userId = $('#participantId').val();
            var postdata = {
                userId:userId
            };
            $.ajax({ //发送更新的ajax请求
                type: "post",
                url: hostUrl+"sys/authentication/queryUserPostRoleList",
                dataType: "json",
                async: false,
                data: JSON.stringify(postdata),
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                success: function (data) {
                    console.info(data);

                    if(data.success&&data.result){
                        var settings = {
                            data: {
                                simpleData: {
                                    enable: true,
                                    idKey: "id",
                                    pIdKey: "pId",
                                    rootPId: null
                                }
                            },
                            check:{
                                enable:true
                            },
                            callback: {
                                onDblClick:function (event, treeId, treeNode) {},
                                onExpand:function (event, treeId, treeNode) {
                                    $("#participantRoleTree").parent('div').getNiceScroll().show().resize();
                                },
                                onCollapse: function(){
                                    $("#participantRoleTree").parent('div').getNiceScroll().show().resize();
                                },
                                onNodeCreated:function (event,treeId,treeNode) {
                                    var participantRoleId = $('#participantRoleId').val();
                                    var participantRoleIdArr = participantRoleId.split(',');
                                    if($.inArray(treeNode.id,participantRoleIdArr)>-1){
                                        var zTreeObj = $.fn.zTree.getZTreeObj(treeId);
                                        zTreeObj.checkNode(treeNode, true, true);
                                    }
                                }
                            }
                        };

                        var treeNodes = data.result;

                        //本人
                        var currentUser = {
                            id:$('#participantId').val(),
                            name:'本人',
                            pId:null
                        };
                        treeNodes.push(currentUser);

                        var treeObj = $('#participantRoleTree');
                        var zTreeObj = $.fn.zTree.init(treeObj, settings, treeNodes);
                        setTimeout(function(){
                            $.xljUtils.addTreeScroll('treeContainer');
                            /*$('#participantRoleTree').niceScroll({
                                autohidemode: false,
                                cursorcolor: "#eee",
                                cursorwidth: "6px", // 滚动条的宽度，单位：便素
                                cursorborder: "1px solid #eee", // CSS方式定义滚动条边框
                                horizrailenabled: true, // nicescroll可以管理水平滚动
                                background: "#fff"
                            });*/
                            $.xljUtils.treeResizeFn();
                        },300);
                        var nodes = zTreeObj.getNodes();
                        //默认展开第一个节点
                        zTreeObj.expandNode(nodes[0], true, false, false,true);

                    }

                },
                error: function (data) {
                    if (data.msg) {
                        $.xljUtils.tip('red', data.msg);
                    } else {
                        $.xljUtils.tip('red', "查询业务系统的列表数据失败！");
                    }
                }
            });
        });

        //初始化参与人身份确定按钮
        $('#selectParticipantRoleBtn').on('click',function () {
            var treeObj = $.fn.zTree.getZTreeObj('participantRoleTree');
            var nodes = treeObj.getCheckedNodes(true);
            var participantId = $('#participantId').val();
            var participantRoles = [];
            var participantIdArr = [];
            for(var i in nodes){
                var node = nodes[i];
                participantRoles.push(node.name);
                if(node.id!='1'&&node.id!='2'&&node.id!='3'){
                    participantIdArr.push(node.id);
                }

            }
            $('#participantRoleId').val(participantIdArr.join(','));
            $('#participantRole').val(participantRoles.join(','));

            $('#participantRoleModal').modal('hide');
        });
    };

    //初始化对象类型更改事件
    var initParticipantCateEvent = function () {
        $('#participantCate').on('change',function () {
            $('#participantName').val('');
            $('#participantId').val('');
            $('#participantRole').val('');
            $('#participantRoleId').val('');
            var participantCateVal = $(this).val();
            $('body').off('click','**');
            $('#participantName,#participantName ~ span.input-group-addon').off('click').on('click',function () {
                //人员
                if(participantCateVal=='1'){
                    $(this).xljSingleSelector({
                        title:'选择人员',
                        selectorType:'person',
                        immediatelyShow:true,
                        targetId:'participantId',
                        targetName:'participantName',
                        saveCallback:function (selectData,ele) {
                            //initParticipantRoleList(selectData.id);
                            $('body').on('click','#participantRole,#participantRole ~ span.input-group-addon',showParticipantRoleContainer);
                        }
                    });
                }
                //岗位
                if(participantCateVal=='2'){
                    $(this).xljMultipleSelectorReset({
                        title: '选择岗位',
                        selectorType: 'post',
                        immediatelyShow:true,
                        targetId: 'participantId',
                        targetName: 'participantName',
                        treeParam: {},
                        saveCallback: function (selectedData, ele) {
                            //console.info(selectedData);
                            var participantNameObj = $(ele).parent('div').find(':input[name="participantName"]');
                            var participantIdObj = $(ele).parent('div').find(':input[name="participantId"]');

                            var participantName = '';
                            var participantId = '';
                            for (var i = 0; i < selectedData.length; i++) {
                                var obj = selectedData[i];
                                participantName += obj.prefixName+'/'+obj.name + ',';

                                participantId += obj.id + ',';

                            }
                            participantName = participantName.replace(/,$/,'');
                            participantNameObj.val(participantName);

                            participantId = participantId.replace(/,$/,'');
                            participantIdObj.val(participantId);

                        }

                    });

                }
                //标准岗位
                if(participantCateVal=='3'){
                    $(this).xljMultipleSelectorReset({
                        title: '选择标准岗位',
                        selectorType: 'role',
                        immediatelyShow:true,
                        targetId: 'participantId',
                        targetName: 'participantName',
                        treeParam: {type: '1'},
                        saveCallback: function (selectedData, ele) {
                            var participantNameObj = $(ele).parent('div').find(':input[name="participantName"]');
                            var participantIdObj = $(ele).parent('div').find(':input[name="participantId"]');

                            var participantName = '';
                            var participantId = '';
                            for (var i = 0; i < selectedData.length; i++) {
                                var obj = selectedData[i];
                                participantName += obj.prefixName+'/'+obj.name + ',';

                                participantId += obj.id + ',';

                            }
                            participantName = participantName.replace(/,$/,'');
                            participantNameObj.val(participantName);

                            participantId = participantId.replace(/,$/,'');
                            participantIdObj.val(participantId);

                        }

                    });
                }
                //角色
                if(participantCateVal=='5'){
                    $(this).xljMultipleSelectorReset({
                        title: '选择角色',
                        selectorType: 'role',
                        immediatelyShow:true,
                        targetId: 'participantId',
                        targetName: 'participantName',
                        treeParam: {type: false,roleCataStatus:true},
                        saveCallback: function (selectedData, ele) {
                            var participantNameObj = $(ele).parent('div').find(':input[name="participantName"]');
                            var participantIdObj = $(ele).parent('div').find(':input[name="participantId"]');

                            var participantName = '';
                            var participantId = '';
                            for (var i = 0; i < selectedData.length; i++) {
                                var obj = selectedData[i];
                                participantName += obj.prefixName+'/'+obj.name + ',';

                                participantId += obj.id + ',';

                            }
                            participantName = participantName.replace(/,$/,'');
                            participantNameObj.val(participantName);

                            participantId = participantId.replace(/,$/,'');
                            participantIdObj.val(participantId);

                        }

                    });
                }
            });
        });

        $('#participantName ~ div.input-group-addon').on('click',function () {
            $('#participantName').val('');
            $('#participantId').val('');

            $('#participantRole').val('');
            $('#participantRoleId').val('');
        });

        $('#participantRole ~ div.input-group-addon').on('click',function () {
            $('#participantRole').val('');
            $('#participantRoleId').val('');
        });

        /*$('#participantRole ~ div.input-group-addon').on('click',function () {
            $('#participantRole').val('');
        });*/
    };

    //初始化修改人点击事件
    var initModifyPersonEvent = function () {
        $('#modifyPersonName,#modifyPersonName ~ span.input-group-addon').on('click',function () {
            $(this).xljSingleSelector({
                title:'请选择模板修改人',
                selectorType:'person',
                immediatelyShow:true,
                targetId:'modifyPersonId',
                targetName:'modifyPersonName',
                saveCallback:function (selectData,ele) {
                }
            });
        });

        $('#modifyPersonName ~ div.input-group-addon').on('click',function () {
            $('#modifyPersonName').val('');
            $('#modifyPersonId').val('');
        });
    };

    //初始化修改时间点击事件
    var initUpdateTimeEleEvent = function () {
        //模板修改时间起
        $('#beginTime').on('click',function () {
            var maxDate = $('#endTime').val();
            WdatePicker({
                el: this,
                dateFmt: "yyyy-MM-dd",
                errDealMode:-1,
                maxDate:'#F{$dp.$D(\'endTime\')}'
            });
        });
        $('#beginTime').siblings('.input-group-addon').on('click',function () {
            var maxDate = $('#endTime').val();
            WdatePicker({
                el: 'beginTime',
                dateFmt: "yyyy-MM-dd",
                errDealMode:-1,
                maxDate:'#F{$dp.$D(\'endTime\')}'
            });
        });

        //模板修改时间止
        $('#endTime').on('click',function () {
            var minDate = $('#beginTime').val();
            WdatePicker({
                el: this,
                dateFmt: "yyyy-MM-dd",
                errDealMode:-1,
                minDate:'#F{$dp.$D(\'beginTime\')}'
            });
        });
        $('#endTime').siblings('.input-group-addon').on('click',function () {
            var minDate = $('#beginTime').val();
            WdatePicker({
                el: 'endTime',
                dateFmt: "yyyy-MM-dd",
                errDealMode:-1,
                minDate:'#F{$dp.$D(\'beginTime\')}'
            });
        });

    };

    //初始化审批人设置方式change事件
    var initIsAddLabelChangeEvent = function () {
        $('#isStart').attr('disabled','disabled');

        $('#isAddLabel').on('change',function () {
            var isAddLabelVal = $(this).val();
            if(isAddLabelVal=='1'){
                $('#isStart').removeAttr('disabled');
            }else{
                $('#isStart').val('');
                $('#isStart').attr('disabled','disabled');
                var postData = $('#flAcListGrid').jqGrid('getGridParam','postData');
                delete postData['isStart'];
            }
        });
    };
    

    //修改流程模板
    window.editFl = function editFl(flId,businessObjectId,acId) {
        //var rowData = $("#flAcListGrid").jqGrid('getRowData', flId);
        //var v_businessObjectId = rowData.businessObjectId;
        var url = hostUrl +'flow/builder/fl_template.html?flId=' + flId + '&businessObjectId='+businessObjectId+'&act=update';
        if(acId){
            url += '&acId='+acId;
        }
        url = encodeURI(url);
        window.open(url);
    };

    //初始化列表
    var initFlAcListGrid = function () {
        //格式化审批类型
        var approvalTypeFormat = function (cellvalue, options, rowObject) {
            var retVal = approveTypeMap[cellvalue];
            retVal = retVal?retVal:'';
            return retVal;
        };

        //格式化多岗策略及同岗多人策略
        var postMultiPersonFormat = function (cellvalue, options, rowObject) {
            if(cellvalue==1){
                return '抢占';
            }
            if(cellvalue==2){
                return '串行';
            }
            if(cellvalue==3){
                return '并行';
            }

            return cellvalue;
        };

        //格式化审批人为空及岗位为空策略
        var approvalPersonIsNullFormat = function (cellvalue, options, rowObject) {
            if(cellvalue=='1'){
                return '不允许发起';
            }

            if(cellvalue=='2'){
                return '允许发起，挂起';
            }

            if(cellvalue=='3'){
                return '允许发起，跳过，并显示该行';
            }

            if(cellvalue=='4'){
                return '允许发起，跳过，不显示该行';
            }

            return '';
        };

        //格式化审批人设置
        var isAddLabelFormat = function (cellvalue, options, rowObject) {
            return cellvalue?'流程发起时，由发起人指定审批人':'在模板环节中设置';
        };

        //格式化是否必须置定审批人
        var isStartFormat = function (cellvalue, options, rowObject) {
            var val =  cellvalue?'必须指定审批人，否则不允许发起流程':'可以不指定审批人，依旧可以发起流程';
            if(!rowObject.isAddLabel){
                val = '';
            }
            return val;
        };

        //格式化模板名称
        var flNameFormat = function (cellvalue, options, rowObject) {

            return '<a href="javascript:void(0);" id="'+options.rowId+'" style="font-weight: bold;color:#3c8dbc;" onclick="editFl(\''+rowObject.flId+'\',\''+rowObject.businessObjectId+'\')">'+cellvalue+'</a>';
        };

        //格式化节点名称
        var acNameFormat = function (cellvalue, options, rowObject) {

            return '<a href="javascript:void(0);" id="'+options.rowId+'" style="font-weight: bold;color:#3c8dbc;" onclick="editFl(\''+rowObject.flId+'\',\''+rowObject.businessObjectId+'\',\''+rowObject.acId+'\')">'+cellvalue+'</a>';
        };

        jQuery("#flAcListGrid").jqGrid({
            url: baseUrl + "flow/ac/queryModifyAcListByPage",
            ajaxGridOptions: {contentType: 'application/json'},
            mtype: "post",
            datatype: "json",
            jsonReader: {
                repeatitems: false
            },
            colNames: ['AC_ID','业务系统', '业务对象', '流程模板', '环节名称','审批类型','环节人员信息','审批人设置方式','必须指定审批人','岗位为空策略','审批人为空策略','多岗审批策略','同岗位多人审批策略','节点ID','应用ID','业务对象ID','模板ID','参与人ID'],
            colModel: [
                {name: 'id',  width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'appName', width: 200, align: "center",sortable: false},
                {name: 'businessObjectName', width: 300, align: "center",sortable: false},
                {name: 'flName', width: 300, align: "center", sortable: false,formatter:flNameFormat},
                {name: 'acName',  width: 100, align: "center", sortable: false,formatter:acNameFormat},
                {name: 'approveTypeId',  width: 100, align: "center", sortable: false ,formatter:approvalTypeFormat},
                {name: 'participantName',  width: 550, align: "center", sortable: false },
                {name: 'isAddLabel',  width: 100, align: "center", sortable: false ,formatter:isAddLabelFormat},
                {name: 'isStart',  width: 100, align: "center", sortable: false,formatter:isStartFormat },
                {name: 'postIsNull',  width: 100, align: "center", sortable: false ,formatter:approvalPersonIsNullFormat},
                {name: 'approvalPersonIsNull',  width: 100, align: "center", sortable: false,formatter:approvalPersonIsNullFormat },
                {name: 'approveStrategy',  width: 100, align: "center", sortable: false,formatter:postMultiPersonFormat },
                {name: 'postMultiPerson',  width: 100, align: "center", sortable: false,formatter:postMultiPersonFormat },
                {name: 'acId',  width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'appId',  width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'businessObjectId',  width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'flId',  width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'participantId',  width: 100, align: "center", sortable: false ,hidden:true}
            ],
            viewrecords: true,
            rownumbers: true,
            multiboxonly: true,
            multiselect: true,
            caption: "",
            rowNum: 20,//一页显示多少条
            rowList: [20, 50, 100, 200],//可供用户选择一页显示多少条
            pager: '#pagered',//表格页脚的占位符(一般是div)的id
            editurl: "",
            autowidth: false,
            rownumWidth:55,
            gridComplete: function (xhr) {
                $.xljUtils.addGridScroll();
                //$.xljUtils.gridResizeFn();
                setTimeout(function () {
                    $.xljUtils.resizeNestedGrid(null,false);
                },100);

            },
            loadComplete:function (xhr) {
                //console.info(xhr);
            },
            ondblClickRow: function (id) {

            }
        }).navGrid('#pagered', {add: false, edit: false, del: false, search: false, refresh: false});
    };

    //初始化属性修改form表单中的事件
    var initAcAttrUpdateFormEvents = function () {
        $('#isAddLabelForUpdate').on('change',function () {
            var isAddLabelVal = $(this).val();
            if(isAddLabelVal=='1'){
                $('#isStartForUpdate').parents('tr').show();
            }else{
                $('#isStartForUpdate').parents('tr').hide();
                $('#isStartForUpdate').val('');
            }
        });

        //保存节点属性
        $('#saveAcAttrBtn').on('click',function () {
            saveModifyAcAttr();
        });
    };

    //初始化替换的审批列表
    var initReplaceApproverListGrid = function () {

        //才与人类型格式化
        var participantTypeFormat = function (cellvalue, options, rowObject) {
            if(cellvalue=='1'){
                return '人员';
            }else if(cellvalue=='2'){
                return '岗位';
            }else if(cellvalue=='4'){
                return '相对参与人';
            }else if(cellvalue=='5'){
                return '角色';
            }else if(cellvalue=='3'){
                return '标准岗位';
            }else{
                return '';
            }
        };
        var participantTypeUnFormat = function (cellvalue, options, rowObject) {
            if(cellvalue=='人员'){
                return '1';
            }else if(cellvalue=='岗位'){
                return '2';
            }else if(cellvalue=='相对参与人'){
                return '4';
            }else if(cellvalue=='角色'){
                return '5';
            }else if(cellvalue=='标准岗位'){
                return '3';
            }else{
                return '';
            }
        };
        var scopeJson = {
            '51':'指定角色',
            '40':'发起人','41':'发起人直接领导','42':'发起人顶级部门领导','43':'上一环节审批人直接领导','44':'上一环节审批人顶级部门领导',
            '311':'本集团','312':'本公司','313':'本部门','314':'本项目','315':'本分期','317':'表单组织',
            '21':'指定岗位','22':'表单岗位',
            '11':'指定人员','12':'表单人员'};
        var participantScopeFormat = function (cellvalue, options, rowObject) {
            var ret = scopeJson[cellvalue];
            ret = ret?ret:'';
            return ret;
        };

        var unFormatScopeJson = {
            '指定角色':'51',
            '发起人':'40','发起人直接领导':'41','发起人顶级部门领导':'42','上一环节审批人直接领导':'43','上一环节审批人顶级部门领导':'44',
            '本集团':'311','本公司':'312','本部门':'313','本项目':'314','本分期':'315','表单组织':'317',
            '指定岗位':'21','表单岗位':'22',
            '指定人员':'11','表单人员':'12'};
        var participantScopeUnFormat = function (cellvalue, options, rowObject) {
            var ret = unFormatScopeJson[cellvalue];
            ret = ret?ret:'';
            return ret;
        };

        var paramData = {isInit:true};
        $('#_oldParticipantListGrid').jqGrid({
            url: baseUrl + "flow/participant/queryReplaceParticipantList",
            postData : paramData,
            datatype: "json",
            ajaxGridOptions: {contentType: 'application/json'},
            mtype: "post",
            jsonReader: {
                root : "result"
            },
            colModel: [
                //{name: 'id',  label:'ID',width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'type',  label:'环节参与者类型',width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'flId',  label:'模板ID',width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'acId',  label:'环节ID',width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'participantId',  label:'参与人ID',width: 100, align: "center", sortable: false ,hidden:true,key:true},
                {name: 'participantName',  label:'参与人名称',width: 100, align: "center", sortable: false },
                {name: 'participantType',  label:'参与人类型',width: 100, align: "center", sortable: false ,formatter:participantTypeFormat,unformat:participantTypeUnFormat},
                {name: 'participantScope',  label:'参与人范围',width: 100, align: "center", sortable: false,hidden:true},
                {name: 'paramValue',  label:'备用参数',width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'sort',  label:'排序号',width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'newParticipantId',  label:'新参与人ID',width: 100, align: "center", sortable: false ,hidden:true},
                {name: 'newParticipantName',  label:'新参与人名称',width: 100, align: "center", sortable: false },
                {name: 'newParticipantType',  label:'新参与人类型',width: 100, align: "center", sortable: false ,formatter:participantTypeFormat,unformat:participantTypeUnFormat},
                {name: 'newParticipantScope',  label:'新参与人范围',width: 100, align: "center", sortable: false,formatter:participantScopeFormat,unformat:participantScopeUnFormat},
                {name: 'newParamValue',  label:'新备用参数',width: 100, align: "center", sortable: false ,hidden:true}

            ],
            //forceFit:true,                                      //当为ture时，调整列宽度不会改变表格的宽度。
            //width: $('#_oldParticipantListGridContainer').parent('div').width()-10,
            //width:$("#lcbegin").width()-10,
            height:200,
            rownumbers:true,
            rowNum : -1,                                        //在grid上显示记录条数，这个参数是要被传递到后台，-1代表不翻页
            hoverrows:false,                                    //禁止mouse hovering
            //rownumWidth:55,
            gridComplete: function (xhr) {

                //$.xljUtils.gridResizeFn();
                setTimeout(function () {

                    $(window).resize();
                },1000);

            },
            loadComplete:function (xhr) {
                //console.info(xhr);
            },
            ondblClickRow: function (id) {

            }
        });

        //初始化新增审批人
        addRow('_approvePartnerTb');

        //替换审批人
        $('#replaceApprovePartnerBtn').on('click',function () {
            var rowId = $('#_oldParticipantListGrid').jqGrid('getGridParam','selrow');
            if(rowId){
                var partnerDatas = getFlPartnerData('_approvePartnerTb');
                var partner = partnerDatas[0];
                var newParticipantId = partner.participantId;
                var newParticipantName = partner.participantName;
                var newParticipantScope = partner.participantScope;
                var newParticipantType = partner.participantType;
                var newParamValue = partner.paramValue;
                var rowData = $('#_oldParticipantListGrid').jqGrid('getRowData',rowId);
                rowData['newParticipantId'] = newParticipantId;
                rowData['newParticipantName'] = newParticipantName;
                rowData['newParticipantScope'] = newParticipantScope;
                rowData['newParticipantType'] = newParticipantType;
                rowData['newParamValue'] = newParamValue;
                if(rowData.newParticipantId&&rowData.newParticipantId!=''){
                    $('#_oldParticipantListGrid').jqGrid('setRowData',rowId,rowData);
                }else{
                    $.xljUtils.tip('blue','请选择审批人！');
                }
            }
        });

        $('#addApprovePartnerBtn').on('click',function () {
            var rowId = $('#_oldParticipantListGrid').jqGrid('getGridParam','selrow');
            if(!rowId){
                var partnerDatas = getFlPartnerData('_approvePartnerTb');
                var partner = partnerDatas[0];
                var newParticipantId = partner.participantId;
                var newParticipantName = partner.participantName;
                var newParticipantScope = partner.participantScope;
                var newParticipantType = partner.participantType;
                var newParamValue = partner.paramValue;
                var rowData = {};
                rowId = getUUID();
                rowData['participantId'] = rowId;
                rowData['newParticipantId'] = newParticipantId;
                rowData['newParticipantName'] = newParticipantName;
                rowData['newParticipantScope'] = newParticipantScope;
                rowData['newParticipantType'] = newParticipantType;
                rowData['newParamValue'] = newParamValue;
                if(rowData.newParticipantId&&rowData.newParticipantId!=''){
                    $('#_oldParticipantListGrid').jqGrid('addRowData',rowId,rowData);
                }else{
                    $.xljUtils.tip('blue','请选择审批人！');
                }
            }
        });

        //清除选中的已替换的审批人
        $('#clearNewAddPartnerBtn').on('click',function () {
            var rowId = $('#_oldParticipantListGrid').jqGrid('getGridParam','selrow');
            var rowData = $('#_oldParticipantListGrid').jqGrid('getRowData',rowId);
            rowData['newParticipantId'] = '';
            rowData['newParticipantName'] = '';
            rowData['newParticipantScope'] = '';
            rowData['newParticipantType'] = '';
            rowData['newParamValue'] = '';
            $('#_oldParticipantListGrid').jqGrid('setRowData',rowId,rowData);
        });

        //保存替换后的审批人
        $('#saveParticipantBtn').on('click',function () {
            var rowDatas = $('#_oldParticipantListGrid').jqGrid('getRowData');
            var replacePartnerArr = [];
            var replacePartnerMap = {};
            $.each(rowDatas,function (i,rowData) {
                if(rowData['newParticipantType'] != ''){
                    replacePartnerArr.push(rowData);
                    replacePartnerMap[rowData['participantId']] = rowData;
                }
            });

            if(replacePartnerArr.length>0){

                var acIds = $('#flAcListGrid').jqGrid('getGridParam','selarrrow');
                var acFlIdmap = {};
                for(var i in acIds){
                    var rowData = $('#flAcListGrid').jqGrid('getRowData',acIds[i]);
                    acFlIdmap[rowData.acId]=rowData.flId;
                }
                var saveJson = {
                    acIds:acIds.join(','),
                    approverMap:replacePartnerMap,
                    acFlIdMap:acFlIdmap
                };
                $.ajax({ //发送更新的ajax请求
                    type: "post",
                    url: hostUrl+"flow/participant/saveReplaceApprover",
                    dataType:"json",
                    data: JSON.stringify(saveJson),
                    contentType: 'application/json;charset=utf-8', //设置请求头信息
                    success: function(data){
                        if(data.success){
                            $.xljUtils.tip('green','审批人替换成功！');
                            $('#flAcListGrid').jqGrid().trigger('reloadGrid');
                            $('#participantModal').modal('hide');
                        }else{
                            $.xljUtils.tip('red','替换审批人失败！');
                        }
                    },
                    error:function (xhr) {
                        $.xljUtils.tip('red','替换审批人失败！');
                    }
                });

            }else{
                $.xljUtils.tip('blue','没有任何审批人替换！');
                return;
            }

        });
    };

    var initPage = function () {
        initParticipantCateEvent();
        initModifyPersonEvent();
        initUpdateTimeEleEvent();
        initIsAddLabelChangeEvent();
        initParticipantRoleShowEvent();
        initFlAcListGrid();

        initAcAttrUpdateFormEvents();

        initReplaceApproverListGrid();

        //节点属性弹出框显示事件
        $('#acAttrModal').on('shown.bs.modal',function () {
            $('#updateAccAttrForm')[0].reset();
        });

        //替换审批人弹出框显示事件
        $('#participantModal').on('shown.bs.modal',function () {
            $('#_approvePartnerTb').find(':input[name="type"]').change();

            $('#_oldParticipantListGrid').jqGrid('setGridWidth',$('#_oldParticipantListGridContainer').width(), true);

            var rowIds = $('#flAcListGrid').jqGrid('getGridParam','selarrrow');
            var postData = $('#_oldParticipantListGrid').jqGrid('getGridParam','postData');
            delete postData.isInit;
            postData.acIds = rowIds.join(',');
            jQuery("#_oldParticipantListGrid").jqGrid('setGridParam', {postData: postData}).trigger('reloadGrid');

        });
    };
    initPage();

    //保存节点属性
    var saveModifyAcAttr = function () {
        var formDatas = $('#updateAccAttrForm').serializeArray();
        var updateData = {};
        var updateDataNameArr = [];
        $.each(formDatas,function (i,item) {
            var name = item.name;
            var value = item.value;
            if(value!=''){
                updateData[name] = value;
                updateDataNameArr.push(name);
            }
        });

        if(updateDataNameArr.length==0){
            $.xljUtils.tip('blue','没有任何一项属性进行修改，请修改一项属性！');
            return;
        }

        var rowIds = $('#flAcListGrid').jqGrid('getGridParam','selarrrow');
        var flIds = [];
        $.each(rowIds,function (i,rowId) {
            var rowData = $('#flAcListGrid').jqGrid('getRowData',rowId);
            var flId = rowData.flId;
            if($.inArray(flId,flIds)==-1){
                flIds.push(flId);
            }
        });
        var postData = {
            updateAttrData:updateData,
            updateAcIds:rowIds.join(','),
            updateFlIds:flIds.join(',')
        };

        $.ajax({
            type: 'POST',
            url: hostUrl + 'flow/ac/batchUpdateAcAttr',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(postData),
            success: function (json) {
                if (json.success) {
                    $('#acAttrModal').modal('hide');
                    $('#flAcListGrid').jqGrid().trigger("reloadGrid");
                    $.xljUtils.tip('green','属性修改成功！');
                }
            }
        });
    };

    window.gridForceFit = false;
});
