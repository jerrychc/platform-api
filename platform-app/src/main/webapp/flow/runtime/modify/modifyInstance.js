/**
 * Created by admin on 2018/1/2.
 */
/**
 * Created by dgh on 2017/11/24.
 * 流程数据初始化使用
 */
var variableDtoList = {};
define(['xljUtils', 'utils','actions'], function (xljUtils, utils,action) {

    var flDataDef;
    var nodeTypeObj = {
        'start':'1',
        'task':'2',
        'end':'3',
        'join':'4',
        'fork':'5',
        'connector':'6',
        'cc':'7'
    };


    //初始化按钮事件
    var initOperation = function () {
        $('#saveBtn').on('click',function () {

            //验证流程图完整性
            var validateResult = validateFlowChart();
            if(!validateResult.validateFlag){
                $.xljUtils.tip('blue',validateResult.errorMsg);
                return;
            }

            var nodesData = getAllNodesData();
            var acObj = nodesData.ac;
            $('#currentNodeId').empty();
            for(var item in acObj){
                var cell = acObj[item];
                var extra = cell.extra;
                if(extra&&extra!=''){
                    extra = JSON.parse(extra);
                }else{
                    extra = {
                        code:cell.nodeType+cell.mxObjectId.replace('mxCell#','_')
                    };
                }
                var code = extra.code;
                var id = cell.id;
                var optObj = $('<option value="'+id+'">'+code+'</option>');
                if(cell.acStatus=='2'&&(cell.nodeType=='task'||cell.nodeType=='cc')){
                    optObj = $('<option value="'+id+'" selected>'+code+'</option>');
                    $('#oldCurrentAcId').val(id);
                }
                if((cell.nodeType=='task'||cell.nodeType=='cc')&&cell.acStatus!=3){
                    $('#currentNodeId').append(optObj);
                }
            }
            $('#selectCurrentNodeModal').modal({show:true,backdrop:'static'});

        });

        $('#closeWinBtn').on('click',function () {
            window.open('','_self');
            window.close();
        });

        //确认流程当前节点
        $('#selectCurrentNodeBtn').on('click',function () {
            var hasReturnAc = $('#hasReturnAc').val();
            if(hasReturnAc=='true'){
                $.xljUtils.confirm('blue','调整环节后将丢失此表单之前发起流程的打回环节！确定调整吗？',function () {
                    var currentNodeId = $('#currentNodeId').val();
                    var oldCurrentAcId = $('#oldCurrentAcId').val();
                    saveModifyInstanceData(currentNodeId,oldCurrentAcId);
                    $('#selectCurrentNodeModal').modal('hide');
                },true);
            }else{
                var currentNodeId = $('#currentNodeId').val();
                var oldCurrentAcId = $('#oldCurrentAcId').val();
                saveModifyInstanceData(currentNodeId,oldCurrentAcId);
                $('#selectCurrentNodeModal').modal('hide');
            }

        });
    };

    //初始化流程实例数据
    var initInstanceDatas = function () {
        var instanceDataDef = new $.Deferred();
        //获取url参数
        var urlParams = $.xljUtils.getUrlParams();
        var instanceId = urlParams.instanceId;
        var instanceDataDef = new $.Deferred();

        $.ajax({
            type: "get",
            url: hostUrl + "flow/instance/getInstanceGraph/" + instanceId + "?entryType=true&time=" + new Date().getTime(),
            success: function (data) {
                if(data.success&&data.result){
                    var instance = data.result;
                    var data = {
                        instanceData: instance
                    };
                    instanceDataDef.resolve(data);
                }
            },
            error: function (xhr) {
                $.xljUtils.getError(xhr.status);
                instanceDataDef.resolve(null);
            }
        });
        return instanceDataDef.promise();
    };

    //验证流程图连接正确性
    var validateFlowChart = function () {
        var graph = utils.getGraphEditor();
        var parentNode = graph.getDefaultParent();
        var allCells = parentNode.children;
        var hasStartCell = false;
        var hasMoreStartCell = false;
        var hasEndCell = false;
        var hasMoreEndCell = false;
        var cellConnectorIsCorrect = true;
        var acBaseInfoIsCorrect = true;
        var errorMsg = '';
        if(allCells){
            for(var i in allCells){
                var cell = allCells[i];
                if(!cell.isEdge()){
                    try {
                        var acExtra = cell.extra;
                        acExtra = JSON.parse(acExtra);
                        if(acExtra&&!acExtra.sourceId){
                            continue;
                        }
                    }catch (e){

                    }


                    var nodeType = cell.nodeType;
                    if(nodeType=='start'){
                        if(hasStartCell){
                            hasMoreStartCell = true;
                            errorMsg = '只能有一个开始节点';
                            break;
                        }
                        hasStartCell = true;
                        var edges = cell.edges;
                        var isSourceCell = false;
                        //var isTargetCell = false;
                        for(var i in edges){
                            var edge = edges[i];
                            var source = edge.source;
                            if(source==cell){
                                isSourceCell = true;
                            }
                        }
                        if(!isSourceCell){
                            cellConnectorIsCorrect = false;
                            errorMsg = '开始节点未连线';
                            graph.clearSelection();
                            graph.setSelectionCell(cell);
                            break;
                        }
                    }
                    if(nodeType=='end'){
                        if(hasEndCell){
                            hasMoreEndCell = true;
                            errorMsg = '只能有一个结束节点';
                            break;
                        }
                        hasEndCell = true;
                        var edges = cell.edges;
                        var isTargetCell = false;
                        for(var i in edges){
                            var edge = edges[i];
                            var target = edge.target;
                            if(target==cell){
                                isTargetCell = true;
                            }
                        }
                        if(!isTargetCell){
                            cellConnectorIsCorrect = false;
                            errorMsg = '结束节点未连线';
                            graph.clearSelection();
                            graph.setSelectionCell(cell);
                            break;
                        }
                    }
                    if(nodeType!='start'&&nodeType!='end'){
                        var edges = cell.edges;
                        var isSourceCell = false;
                        var isTargetCell = false;
                        for(var i in edges){
                            var edge = edges[i];
                            var source = edge.source;
                            var target = edge.target;
                            if(source==cell){
                                isSourceCell = true;
                            }
                            if(target==cell){
                                isTargetCell = true;
                            }
                        }
                        if(!isSourceCell||!isTargetCell){
                            cellConnectorIsCorrect = false;
                            errorMsg = '任务、网关节点连线不完整';
                            graph.clearSelection();
                            graph.setSelectionCell(cell);
                            break;
                        }

                        //验证节点内容完整性
                        if(cell.isValidateNode=='true'){
                            if((nodeType=='task'||nodeType=='cc')&&!cell.extra){
                                acBaseInfoIsCorrect = false;
                                errorMsg = '流程环节基本信息填写不完整';
                                graph.clearSelection();
                                graph.setSelectionCell(cell);
                                break;
                            }else if(nodeType=='task'&&cell.extra&&cell.extra!=''){
                                try {
                                    var acObj = JSON.parse(cell.extra);
                                    var participant = cell.participant;
                                    if((!participant||participant=='')){
                                        acBaseInfoIsCorrect = false;
                                        errorMsg = '流程环节基本信息填写不完整';
                                        graph.clearSelection();
                                        graph.setSelectionCell(cell);
                                        break;
                                    }

                                }catch (e){
                                    console.error(e);
                                }

                            }
                        }

                    }
                }
            }

            if(!hasStartCell&&errorMsg==''){
                errorMsg = '没有开始节点';
            }
            if(!hasEndCell&&errorMsg==''){
                errorMsg = '没有结束节点';
            }

            return {validateFlag:hasStartCell&&!hasMoreStartCell&&!hasMoreEndCell&&hasEndCell&&cellConnectorIsCorrect&&acBaseInfoIsCorrect,errorMsg:errorMsg};
        }else{
            errorMsg = '没有流程图';
            return {validateFlag:false,errorMsg:errorMsg};
        }
    };

    //递归获取排序后的节点及连线信息
    var getSortedNodesData = function (startNode,nodeMap,sortIdArr,stepNodesMap) {

        startNode.nextAcIds = null;
        startNode.preAcIds = null;
        var edgeList = startNode.edges;
        var target;
        var source;
        if(edgeList&&edgeList.length>0){

            if(edgeList.length==1){

                source = edgeList[0].source;
                target = edgeList[0].target;
                if(source==startNode){
                    if(target){
                        if($.inArray(target.id,sortIdArr)>-1){
                            sortIdArr.splice($.inArray(target.id,sortIdArr),1);
                        }

                        sortIdArr.push(target.id);
                        if(!nodeMap[startNode.id].nextAcIds){
                            nodeMap[startNode.id].nextAcIds = '';
                        }
                        nodeMap[startNode.id].nextAcIds += target.id + ',';
                        nodeMap[target.id] = target;
                        getSortedNodesData(target,nodeMap,sortIdArr,stepNodesMap);
                    }
                }else{
                    if(!nodeMap[startNode.id].preAcIds){
                        nodeMap[startNode.id].preAcIds = '';
                    }
                    nodeMap[startNode.id].preAcIds += source.id  + ',';
                }

                stepNodesMap[edgeList[0].id] = edgeList[0];
            }else{
                for (var i = 0; i < edgeList.length; i++) {
                    var edge = edgeList[i];
                    stepNodesMap[edge.id] = edge;
                    if(edge.source == startNode){
                        target = edge.target;
                        source = edge.target;
                        if(target){
                            if($.inArray(target.id,sortIdArr)>-1){
                                sortIdArr.splice($.inArray(target.id,sortIdArr),1);
                            }
                            sortIdArr.push(target.id);
                            if(!nodeMap[startNode.id].nextAcIds){
                                nodeMap[startNode.id].nextAcIds = '';
                            }
                            nodeMap[startNode.id].nextAcIds += target.id + ',';
                            nodeMap[target.id] = target;
                            getSortedNodesData(target,nodeMap,sortIdArr,stepNodesMap);
                        }
                    }else{
                        var source = edge.source;
                        if(!nodeMap[startNode.id].preAcIds){
                            nodeMap[startNode.id].preAcIds = '';
                        }
                        nodeMap[startNode.id].preAcIds += source.id  + ',';
                    }
                }

            }
        }
    };

    //获取开始节点
    var getStartNode = function () {
        var graph = utils.getGraphEditor();
        var parentNode = graph.getDefaultParent();
        var allCells = parentNode.children;
        for(var i in allCells){
            var cell = allCells[i];
            try {
                var acExtra = cell.extra;
                acExtra = JSON.parse(acExtra);
                if(acExtra&&acExtra.sourceId){
                    if(!cell.isEdge()&&cell.nodeType=='start'){
                        return cell;
                    }
                }else if(acExtra&&!acExtra.sourceId){
                    if(!cell.isEdge()&&cell.nodeType=='start'){
                        $('#hasReturnAc').val('true');
                    }
                }
            }catch (e){

            }

        }
    };

    //从开始节点顺序获取所有节点及连线信息
    var getAllNodesData = function () {
        var startNode = getStartNode();

        //所有节点的id-节点map映射
        var allNodesMap = {};
        allNodesMap[startNode.id] = startNode;

        //排序数组，用于记录节点顺序
        var sortIdArr = [];
        sortIdArr.push(startNode.id);

        //所有连线的id-连线map映射
        var stepNodesMap = {};
        getSortedNodesData(startNode,allNodesMap,sortIdArr,stepNodesMap);

        for(var item in allNodesMap){
            allNodesMap[item].px = $.inArray(item,sortIdArr)+1;
        }
        return {ac:allNodesMap,step:stepNodesMap};
    };
    window.getAllNodesData = getAllNodesData;

    //获取实例保存数据
    var getInstanceDataForSave = function (instanceData) {

        var allNodesMap = getAllNodesData();
        var acArr = [];
        var stepArr = [];

        var acNodesMap = allNodesMap.ac;
        var approverList = [];
        var ccPersonList = [];
        for(var item in acNodesMap){
            var cell = acNodesMap[item];
            var acObj = {};
            var extra = cell.extra;

            if (extra && extra != '') {
                acObj = JSON.parse(extra);
            }
            //审核人
            var participant = cell.participant;
            if (participant && participant != '') {
                //acObj.participant = participant;
                var approverParticipantList = JSON.parse(participant);
                $.merge(approverList,approverParticipantList);
            }
            //抄送人
            var ccPerson = cell.ccPerson;
            if (ccPerson && ccPerson != '') {
                //acObj.ccPerson = ccPerson;
                $.merge(ccPersonList,JSON.parse(ccPerson));
            }
            acObj.id = cell.id;
            acObj.name = cell.value;
            acObj.fiId = instanceData.id;
            acObj.acType = nodeTypeObj[cell.nodeType];
            //acObj.nodeId = obj.mxObjectId.replace('mxCell#', '');
            acObj.x = cell.geometry.x;
            acObj.y = cell.geometry.y;
            acObj.width = cell.geometry.width;
            acObj.height = cell.geometry.height;
            acObj.height = cell.geometry.height;
            acObj.preAcIds = cell.preAcIds?cell.preAcIds.replace(/,$/,''):null;
            acObj.nextAcIds = cell.nextAcIds?cell.nextAcIds.replace(/,$/,''):null;
            acObj.px = cell.px;
            acArr.push(acObj);
        }

        var stepNodesMap = allNodesMap.step;
        for(var item in stepNodesMap){
            var cell = stepNodesMap[item];
            var stepObj = {};
            stepObj.id = cell.id;
            stepObj.name = cell.value;
            stepObj.sourceId = cell.source?cell.source.id:null;
            stepObj.targetId = cell.target?cell.target.id:null;
            stepObj.nodeId = cell.mxObjectId.replace('mxCell#', '');
            stepObj.fiId = instanceData.id;
            stepArr.push(stepObj);
        }

        var modifyInstanceData = {};
        modifyInstanceData.instanceId = instanceData.id;//实例id
        modifyInstanceData.acList = acArr;//节点列表
        modifyInstanceData.stepList = stepArr;//连线列表
        modifyInstanceData.accessibles = instanceData.accessibles&&instanceData.accessibles!=''?JSON.parse(instanceData.accessibles):null;//可阅人
        modifyInstanceData.approverList = approverList;//审批人
        modifyInstanceData.ccPersonList = ccPersonList;//抄送人
        return modifyInstanceData;
    };

    //保存修改后的实例数据
    var saveModifyInstanceData = function (currentAcId,oldCurrentAcId) {
        var instanceDataDef = window.getInstanceDataDef();
        $.when(instanceDataDef).done(function (instanceBaseData) {
            var instanceData = instanceBaseData.instanceData;
            var saveInstanceData =  getInstanceDataForSave(instanceData);
            saveInstanceData.currentAcId = currentAcId;
            saveInstanceData.oldCurrentAcId = oldCurrentAcId;

            $.ajax({
                url : hostUrl + "flow/instance/saveModifyInstance",
                data : JSON.stringify(saveInstanceData),
                type : 'POST',
                contentType : 'application/json',
                dataType : 'JSON',
                complete: function() {
                },
                success : function(data) {
                    if (data.success) {
                        $.xljUtils.tip('blue','流程实例修改成功！');
                        try {
                            window.opener.location.reload();
                        }finally {
                            window.open('','_self');
                            window.close();
                        }
                    }
                },
                error : function(xhr) {
                    $.xljUtils.getError(xhr.status);
                }
            });
        });

    };


    //获取实例业务变量
    var queryVariableList = function () {
        var urlParams = $.xljUtils.getUrlParams();
        var paramData = {delflag:"0", "fiId": urlParams.instanceId };
        $.ajax({ //发送更新的ajax请求
            type: "post",
            url: hostUrl+"flow/instanceVariable/queryList",
            dataType:"json",
            data: JSON.stringify(paramData),
            contentType: 'application/json;charset=utf-8',
            success: function(data){
                if(data.result){
                    var variableList = data.result;
                    for(var i =0;i<variableList.length;i++){
                        if(variableList[i].name == 'flow_business_company_id'){
                            variableDtoList.flow_business_company_id = variableList[i].val;
                        }else  if(variableList[i].name == 'flow_business_dept_id'){
                            variableDtoList.flow_business_dept_id = variableList[i].val;
                        }
                    }
                }
            }
        });
    }
    queryVariableList();

    return {
        initOperation:initOperation,
        initInstanceDatas:initInstanceDatas
    };
});
