/**
 * Created by Administrator on 2017/11/22.
 */

(function(window,document){
    var customFill = {
        baseUrl : '/',
        hostUrl : '/platform-app/',
        search:window.location.search,
        id:"",
        type:"add",
        get_title:"",
        customFormId:"",
        modal:"",
        code:"",
        appId:"",
        isHasAttach:false,
        dataValueForSave:{
            operatorId:"",
            operateDepartmentId:"",
            operateCompanyId:"",
            operateProjectId:"",
            operateQiId:"",
            status: "",
            tendId: "",
            updatePersonId: "",
            vbusinesstype: "",//业务类型
            createCompanyId: "",
            createOrgId: "",
            createPersonId: "",
            createCompanyName: "",
            createDate: "",
            createOrgName: "",
            createPersonName: "",
            operateCompanyName: "",
            operateDepartmentName: "",
            operateProjectName: "",
            operateQiName: "",
            operatorName: "",
            updateDate: "",
            updatePersonName: "",
            formNumber:""
        },
        mapping:{
            "Operator":"operatorId",
            "OperateCompany":"operateCompanyId",
            "OperateProject":"operateProjectId",
            "OperateQi":"operateQiId",
            "OperateDepartment":"operateDepartmentId",
            "CreateOrg":"createOrgId"
        },
        sel_mapping:{
            "Operator":"user",
            "OperateCompany":"company",
            "OperateProject":"group",
            "OperateQi":"branch",
            "OperateDepartment":"dept",
            "CreateOrg":"post",
            "InnerUsers":"user"
        },
        stageData:{},
        registeredFlowVar:[],
        trEle:{},
        formValueJson:{},
        formTitleCmpId:"",
        findTitleFlag:false,
        formTitle:"",
        /**
         * 绑定事件
         */
        bind_event:function(){//事件绑定


            //附件删除
            $(".attachment-box .del").on("click",function(e){
                e.stopPropagation();
                $(this).parents("li").remove();
            });

            //展开折叠面板
            $(".con-tab .icon").on("click",function(){
                if($(this).hasClass("fa-angle-down")){
                    $(this).removeClass("fa-angle-down");
                    $(this).addClass("fa-angle-up");
                }else{
                    $(this).removeClass("fa-angle-up");
                    $(this).addClass("fa-angle-down");
                }
                $(this).parents(".con-tab").next().toggle();
            });
            //关闭窗口
            $(".cancel").on("click", function () {
                if(window.close) window.close();
                window.location.href = "start_home.html";
                //window.history.back();
            });
            //暂存
            $(".tmpsave").on("click", function () {
                if (_.isEmpty(customFill.stageData)) {
                    dialog_tip('页面未加载完成，请稍等');
                    return false;
                }
                //else {
                //    if($(".error-border").length>0){
                //        dialog_tip('文本框组件不能输入引号和回车，多行文本不能输入引号');
                //        return false;
                //    }
                //}
                customFill.onClickSubmitOrder({mode: 'TEMP_SAVE'});
            });
            //发起审批
            $(".approve").on("click", function () {
                var self = customFill;
                if (_.isEmpty(self.stageData)) {
                    dialog_tip('页面未加载完成，不能发起流程');
                    return false;
                }

                    //验证
                if(!$("#note").val()){
                    dialog_tip('请填写处理意见');
                    return false;
                }
                $("#fill-tbody input").each(function(i,n){
                    inputValidateFn(n);
                });

                //验证
                $("#fill-tbody textarea").each(function(i,n){
                    textareaValidateFn(n);
                });

                if($(".error-border").length>0){
                    //dialog_tip('有未填写正确的栏目，不能发起流程');
                    return false;
                }
                self.loadingShowStyle("请稍后...");

                var code = customFill.code;
                var customFormId = customFill.customFormId;
                var id = customFill.id;
                var url=  "../approve_start.html"
                    + "?businessObjectCode=" + code
                    + "&businessId=" + id
                    + "&customFormId=" + customFormId
                    + "&time=" + new Date().getTime();
                customFill.onClickSubmitOrder({
                    mode: 'START_FLOW',
                    url: url
                });

            });


            //附件展开折叠
            $("body").delegate(".custom-add-btn", "click", function(event){
                $(".attachAddFiels").toggleClass("collapse");
                $(this).next().toggle();
            });
            $("body").delegate(".fileinput-button", "click", function(e){
                e.stopPropagation();
            });


            //提示框样例
            var dialog_tip = this.dialog_tip = function (text,fn){
                $(document).dialog({
                    type : 'notice',
                    infoText: text,
                    autoClose: 2000,
                    position: 'top',  // center: 居中; bottom: 底部
                    onClosed: function(){
                        if(fn){
                            fn();
                        }
                    }
                });
            }
            var inputValidateFn = function(that,p){
                $(that).removeClass("error-border");
                var val = $(that).val();
                var p_node = $(that).parent();
                var cmpName = $(p_node).attr("data-cmpName");
                //
                if(($(p_node).attr("isRequired")=="TRUE" && cmpName!="Attach") || (cmpName == 'Operator' || cmpName == 'OperateCompany')){
                    if (_.isEmpty(val)) {
                        $(that).addClass("error-border");
                        dialog_tip("请检查某些可编辑的必填项，不能为空");
                        return false;
                    }
                }
                //特殊字符验证
                if (!_.isEmpty(val)) {
                    if (/[\n|\r|\\]+/.test(val)) {
                        $(that).addClass("error-border");
                        dialog_tip("必须输入除回车，\\外的字符数字");
                        return false;
                    }
                }
                if ( cmpName != 'Attach' && $(p_node).attr("inputDataType")) {
                    val = val.trim();
                    // 去掉两边的空格，再进行校验
                    if (!_.isEmpty(val)) {
                        var errorMsg = "";
                        if (!_.isUndefined(val)) {
                            var  dataType = $(p_node).attr("inputDataType");
                            var express = {
                                // 'STRING': /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/,
                                'STRING': /^[^<>]+$/,
                                'INT': /^[0-9]*$/,
                                'FLOAT': /^[0-9]+(\.[0-9]{1,2})?$/,
                                'DATETIME': /^(\d{4})(-|\/)(\d{2})(-|\/)(\d{2})/
                            };
                            var msgs = {
                                'STRING': '字符类型：请输入除\', \"，回车，<, >, \\外的字符数字',
                                'INT': '整数类型：只可输入整数',
                                'FLOAT': '浮点类型：可输入带小数点的数字',
                                'DATETIME': '日期类型：可输入如2017-01-01的日期类字符',
                                'BOOLEAN': '布尔类型：可输入true, false'
                            };
                            _.map(express, function(v, k){
                                if (dataType != 'STRING' && dataType == k && !v.test(val)) {
                                    errorMsg = msgs[dataType];
                                } else if (dataType == 'STRING' && (!express[dataType].test(val) || /[\n|\r|\\]+/.test(val))) {
                                    // 若是字符串，则继续匹配，不包含引号和回车
                                    errorMsg = msgs[dataType];
                                } else if (dataType == 'BOOLEAN' && ['true', 'false'].indexOf(_.toLower(val)) == -1) {
                                    errorMsg = msgs[dataType];
                                }
                            });
                            if(errorMsg){
                                $(that).addClass("error-border");
                                dialog_tip(errorMsg);
                                return false;
                            }
                        }
                    }
                }

            };
            //验证部分
            //input,
            $("body").delegate("#fill-tbody input[type=text]", "change", function(e){
                inputValidateFn(this);
                var val = $(this).val();
                if(val) $(this).next().css("display","inline-block");else $(this).next().hide();
                $(this).parent().attr("data-cmpValue",val);
            });
            //时间框
            $("body").delegate("input[class='inputDate']", "change", function(e){
                var val = $(this).val();
                if(val) $(this).next().css("display","inline-block");else $(this).next().hide();
                $(this).parent().attr("data-cmpValue",val);
            });
            var textareaValidateFn = function(that){
                $(that).removeClass("error-border");
                var val = $(that).val() || $(that).text();
                var p_node = $(that).parent();
                if($(p_node).attr("isRequired")=="TRUE"){
                    if (_.isEmpty(val)) {
                        $(that).addClass("error-border");
                        dialog_tip("请检查某些可编辑的必填项，不能为空");
                        return false;
                    }
                }
                if (!_.isEmpty(val)) {
                    if (/[\\]+/.test(val)) {
                        $(that).addClass("error-border");
                        dialog_tip("必须输入除\\外的字符数字");
                        return;
                    }
                    val = val.trim().replace(/<br\/?>/g, '\n');
                    if (!_.isUndefined(val) && /[\\]+/.test(val)) {
                        $(that).addClass("error-border");
                        dialog_tip("多行文本：可输入除 \', \",  \\ 外的字符");
                        return;
                    }
                    //不能输入表情符
                    if (/\uD83C[\uDF00-\uDFFF]|\uD83D[\uDC00-\uDE4F]/.test(val)) {
                        $(that).addClass("error-border");
                        dialog_tip("必须输入除表情符外的字符数字");
                        return;
                    }
                }
                val = val.replace(/\n/g, '<br/>');
                $(that).parent().attr("data-cmpValue",val);
            };
            //多行文本框
            $("body").delegate("#fill-tbody textarea", "blur", function(e){
                var val = $(this).val();
                if(val) $(this).next().css("display","inline-block");else $(this).next().hide();
                textareaValidateFn(this);
            });
            //select 选择
            $("body").delegate("#fill-tbody select", "change", function(e){
                $(this).parent().attr("data-cmpValue",$(this).val());
                $(this).parent().attr("data-cmpValueIds",$(this).val());
                $(this).parent().attr("data-cmpValueShowName",$(this).find("option:selected").text());
            });
            // radio
            $("body").delegate("#fill-tbody input[type='radio']", "click", function(e){
                $(this).parent().parent().attr("data-cmpValue",$(this).val());
                $(this).parent().parent().attr("data-cmpValueIds",$(this).val());
                $(this).parent().parent().attr("data-cmpValueShowName",$(this).next().html());
                e.stopPropagation();
            });
            $("body").delegate("textarea#note", "blur", function(e){
                var val = $(this).val();
                val = val.replace(/\n/g, '<br/>');
                $(this).attr("data-cmpValue",val);
            });
            //删除
            $(document).on("click",".titleSpan,.dateSpan,.deleteSpan",function(e){
                $(this).prev().val("");
                $(this).prev().text("");
                $(this).prev().parent().attr("data-cmpValue","");
                $(this).hide();
            });

            $(document).on("click",".noteSpan",function(e){
                $(this).prev().val("");
                $(this).prev().text("");
            });
            //checkbox
            $("body").delegate("#fill-tbody input[type='checkbox']", "click", function(e){
                var cmp_box = $(this).parent().parent();
                var vals = cmp_box.attr("data-cmpValue");
                var vnames = cmp_box.attr("data-cmpValueShowName");
                if(!vals || vals=="undefined") {vals = [];vnames = [];}
                else{vals = vals.split(",");vnames = vnames.split(",");}
                if ($(this).is(':checked')){
                    vals.push($(this).val());
                    vnames.push($(this).next().html());
                }else{
                    vals = _.remove(vals, function(n) {
                        return n == $(this).val();
                    });
                    vnames= _.remove(vnames, function(n) {
                        return n == $(this).next().html();
                    });
                }
                cmp_box.attr("data-cmpValue",vals.join(","));
                cmp_box.attr("data-cmpValueIds",vals.join(","));
                cmp_box.attr("data-cmpValueShowName",vnames.join(","));
                e.stopPropagation();
            });
        },
        loadingShowStyle:function(text){
            $("body").css("overflow","hidden");
            var scrollTop = $(window).scrollTop();
            var top =  $(window).height()/2;
            $(".loading").show();
            $(".loading").css("top",scrollTop+"px");
            $(".loading .load-box").css("top",top+"px");
            $(".loading .load-box .textWaiting").html(text);
        },
        loadingHideStyle:function(){
            $("body").css("overflow","auto");
            $(".loading").hide();
        },
        getUrlParam:function(name){
            var reg = new RegExp("(^|&)"+ name + "=([^&]*)(&|$)");
            var r = decodeURI(this.search).substr(1).match(reg);
            if (r!=null ){
                return unescape(r[2]);
            }
            return null;
        },
        // 过滤特殊字符
        filterSpecStr:function(strSrc) {
            if (typeof(strSrc) == 'string' && !_.isEmpty(strSrc)) {
                var specStrSrc = strSrc.replace(/\r|\n/g, '');
                specStrSrc = specStrSrc.replace(/\t/g, '    ');
                return specStrSrc;
            }
            return '';
        },
        //从页面获取dom数据
        getFilledDataFromDom:function(){
            var filledData = {};
            $('[data-cmpId]').map(function(i, node){
                var cmpName = node.getAttribute('data-cmpName');
            if (cmpName != 'Box.layout') {
                var cmpId = node.getAttribute('data-cmpId');

                // 转换单双字符
                var cmpValue = node.getAttribute('data-cmpValue') || '';
                var cmpValueIds = node.getAttribute('data-cmpValueIds') || '';
                var cmpValueShowName = node.getAttribute('data-cmpValueShowName') || '';
                cmpValue = cmpValue.replace(/\n/g, '<br/>');
                cmpValueShowName = cmpValueShowName.replace(/\n/g, '<br/>');
                // 转换单双字符
                if (_.isEmpty(cmpValueShowName)) {
                    cmpValueShowName = cmpValue;
                }
                if (cmpName != 'LabelForm') {
                    switch (cmpName) {
                        // 针对部分内部组件做特殊处理
                        case "Departments":
                        case "Organization":
                        case "InnerCompany":
                        case "InnerUsers":
                        case "OperateDepartment":
                            filledData[cmpId] = {
                                "cmpName":cmpName,
                                "cmpValue":cmpValue,
                                "cmpValueIds":cmpValueIds,
                                "cmpValueShowName":cmpValueShowName
                            };
                            break;

                        case "LegalPerson":
                        case "CustomArchive":
                            filledData[cmpId] = {
                                "cmpName":cmpName,
                                "cmpValue": cmpValueIds,
                                "cmpValueShowName": cmpValue,
                            };
                            break;

                        default:
                            filledData[cmpId] = {
                                "cmpName":cmpName,
                                "cmpValue":cmpValue,
                                "cmpValueShowName":cmpValueShowName
                            };
                            break;
                    }
                }
            }
        });
            //特殊处理  意见
            if($("#note").val()){
                var noteVal = $("#note").attr("data-cmpValue");
                noteVal = noteVal.replace(/\n/g, '<br/>');
                filledData["exp_remark"] = {
                    "cmpName":"remark",
                    "cmpValue":noteVal,
                    "cmpValueShowName":noteVal
                }
            }
            //标题
            if($("title").html()){
                filledData["exp_titleName"] = {
                    "cmpName":"titleName",
                    "cmpValue":$("title").html(),
                    "cmpValueShowName":$("title").html()
                }
            }

            return filledData;
        },
        flattenStageData:function(stageData){
            var invalidCmps = ['LabelForm'];
            var allCmpsValueInStageData = {};
            stageData.map(function(lineData) {
                lineData['row'].map(function(lineDatum) {
                    if (_.size(lineDatum) > 0 && _.indexOf(invalidCmps, lineDatum[0]['cmpName']) == -1) {
                        var cmpId = lineDatum[0].cmpId;
                        allCmpsValueInStageData[cmpId] = lineDatum[0];
                     }
                });
             });
            return allCmpsValueInStageData;
        },
        flattenStageLayoutData:function(stageData){
            // 排除标签组件
            var invalidCmps = ['LabelForm'];
            var allCmpsValueInLayoutData = {};
            stageData.map(function(lineData){
                var rowDispState = lineData['display']? lineData['display'] : 'VISIBLE';
                lineData['row'].map(function(lineDatum){
                    if (_.size(lineDatum) > 0 && _.indexOf(invalidCmps, lineDatum[0]['cmpName']) == -1) {
                        var cmpId = lineDatum[0].cmpId;
                        allCmpsValueInLayoutData[cmpId] = rowDispState;
                     }
                });
             });
            return allCmpsValueInLayoutData;
        },
        //获取数据
        getValueJson:function(filledData){

            var stageData = this.stageData;
            // 获得组件数据
            var allCmpsValueInStageData = this.flattenStageData(stageData);
            // 获得行布局数据
            var allCmpsDispStateInStageData = this.flattenStageLayoutData(stageData);

            var mobileValues = [];
            $('[data-cmpId]').map(function(i, node){
                var cmpName = node.getAttribute('data-cmpName');
            var cmpId = node.getAttribute('data-cmpId');
            if (allCmpsDispStateInStageData[cmpId] == 'VISIBLE') {
                var cmpNode = allCmpsValueInStageData[cmpId];
                var showColName = (cmpNode && cmpNode.showColName)? cmpNode.showColName : '';
                // cmpValueShowName 用来显示value对应的汉字
                var cmpValue = filledData[cmpId]? filledData[cmpId].cmpValueShowName : '';

                var value = {name: '', value: ''};
                if (!_.isEmpty(showColName)) {
                    value['name'] = showColName;
                    value['value'] = cmpValue;
                    value['type'] = cmpName;
                    mobileValues.push(value);
                }
            }
        });
            return mobileValues;
        },
        //获取经办等数据
        getOperatorData:function(){
            var operatorData = {
                operatorId: '',
                operatorName: '',

                operateDepartmentId: '',
                operateDepartmentName: '',

                operateCompanyId: '',
                operateCompanyName: '',

                operateProjectId: '',
                operateProjectName: '',

                operateQiId: '',
                operateQiName: '',

                createOrgId: '',
                createOrgName: '',
            };
            $('[data-cmpId]').map(function(i, node){
                var cmpName = node.getAttribute('data-cmpName');
            if (cmpName != 'Box.layout') {
                switch (cmpName) {
                    // 针对部分内部组件做特殊处理
                    case 'Operator':
                        operatorData.operatorId = node.getAttribute('data-cmpValueIds') || '';
                        operatorData.operatorName = node.getAttribute('data-cmpValue') || '';
                        break;
                    case "OperateDepartment":
                        operatorData.operateDepartmentId  = node.getAttribute('data-cmpValueIds') || '';
                        operatorData.operateDepartmentName = node.getAttribute('data-cmpValue') || '';
                        break;

                    case "OperateQi":
                        operatorData.operateQiId  = node.getAttribute('data-cmpValueIds') || '';
                        operatorData.operateQiName = node.getAttribute('data-cmpValue') || '';
                        break;
                    case "OperateCompany":
                        operatorData.operateCompanyId  = node.getAttribute('data-cmpValueIds') || '';
                        operatorData.operateCompanyName = node.getAttribute('data-cmpValue') || '';
                        break;
                    case "OperateProject":
                    {
                        operatorData.operateProjectId  = node.getAttribute('data-cmpValueIds') || '';
                        operatorData.operateProjectName = node.getAttribute('data-cmpValue') || '';
                        break;
                    }

                    case "CreateOrg":
                        operatorData.createOrgId  = node.getAttribute('data-cmpValueIds') || '';
                        operatorData.createOrgName = node.getAttribute('data-cmpValue') || '';
                        break;

                    default:
                        break;

                }
            }
        });
            return operatorData;
        },

        //保存方法
        onClickSubmitOrder:function(option){
            var self = this;
            var filledData = this.getFilledDataFromDom();
            var mobileValueJson = this.getValueJson(filledData);
            var formFlowVariableValue = this.getRegisteredFlowVar(filledData);
            var operatorData = this.getOperatorData();
            var remark = $("#note").val();
            remark = remark.replace(/\n/g, '<br/>');
            var formTitle = $("input[type='text'][isTitle=true]").val();
            var params = {
                id: self.id,
                formTitle: formTitle,
                customFormId: self.customFormId,
                formMobileValueJson: JSON.stringify(mobileValueJson),
                formFlowVariableValue:JSON.stringify(formFlowVariableValue)
            };
            window.sessionStorage.setItem('remark',remark);
            window.sessionStorage.setItem('titleName',self.get_title);
            params = _.assign(params,operatorData);
            params.formValueJson = JSON.stringify(filledData);
            params.formValueJson = params.formValueJson.replace(/\n/g, '<br/>');
            // 过滤特殊字符
            params.formMobileValueJson = this.filterSpecStr(params.formMobileValueJson);
            params.formFlowVariableValue = this.filterSpecStr(params.formFlowVariableValue);
            //附件上传

            if(self.isHasAttach){
                var def = new $.Deferred();
                $('#attachmentUpload').xljAttachmentSubmit(function (isSuccess, obj) {
                    if (isSuccess) {
                        if (obj.success === true) {
                            //alert('提交成功');
                        }
                        def.resolve(true);
                    } else {
                        //alert(obj);
                        def.resolve(false);
                    }
                });
            }
            this.upgradeFormValueJson(params,option,this.type);
        },
        //修改 新增
        upgradeFormValueJson:function(params,option,type){
            var that = this;
            var url = type=="edit" ? that.hostUrl+'sys/base/customFormInstance/update/'+params.id : that.hostUrl+'sys/base/customFormInstance/save/';
            var method = type=="edit" ? "PUT" :"POST";
            var strData = JSON.stringify(params);

            strData = strData.replace(/\t/g, '    ');
            $.ajax({
                url: url,
                type: method,
                dataType: 'json',
                timeout: 60000,
                contentType: 'application/json',
                data: strData,
                success: function(data) {
                    if (data.success) {
                        var newJsonData = JSON.parse(strData);
                        //
                        if(option.mode=='TEMP_SAVE'){
                            that.loadingHideStyle();
                            that.dialog_tip("保存成功",function(){
                                if(window.close) window.close();
                                //window.history.back();
                                window.location.href = "start_home.html";
                            });
                        }
                        that.handleOprate({"filledData":newJsonData.filledData,"param":option});
                    } else {
                        that.loadingHideStyle();
                        that.dialog_tip("保存失败");
                    }

                },
                error:function(XMLHttpRequest, textStatus, errorThrown){
                    alert(textStatus+"   "+errorThrown);
                }
            });

        },
        //处理操作
        handleOprate:function(action){
            if (action.param && action.param.mode == 'START_FLOW') {
                var url = action.param.url;
                url=encodeURI(url);
                $("#start_iframe").attr("src",url);
               // window.location.href=url;
            }
        },
        getflowVarFromStageData:function(stageData){
            var flowVar = [];
            _.cloneDeepWith(stageData, (obj) => {
                if ( typeof(obj) == 'object' &&
                    _.has(obj, 'cmpId') &&
                    _.has(obj, 'registerToFlowVar') &&
                    obj['registerToFlowVar'] == 'TRUE'
                ) {
                    flowVar.push(obj.cmpId);
                }
            });
            return flowVar;
        },
        //添加时间事件
        addDateEvent:function(){
            var currYear = (new Date()).getFullYear();
            var opt={};
            opt.date = {preset : 'date'};
            opt.datetime = {preset : 'datetime'};
            opt.time = {preset : 'time'};
            opt.default = {
                theme: 'android-ics light', //皮肤样式
                display: 'modal', //显示方式
                mode: 'clickpick', //日期选择模式
                dateFormat: 'yyyy-mm-dd',
                lang: 'zh',
                showNow: true,
                nowText: "今天",
                startYear: currYear - 50, //开始年份
                endYear: currYear + 10, //结束年份
                onBeforeShow:function(e){
                    $(".inputDate").blur();
                    $('html,body').addClass('ovfHiden');
                    $('html,body').css({"position":"fixed"});
                },
                onClose:function(e){
                    $('html,body').removeClass('ovfHiden');
                    $('html,body').css({"position":"relative"});
                }
            };
            $(".inputDate").each(function(i,n){
                var dateOptionId = $(n).parent().attr("data-dateOptionId");
                opt.datetime = dateOptionId=="DATE_TIME" ? {preset : 'datetime'}:{preset : 'date'};
                $(n).mobiscroll($.extend(opt['datetime'], opt['default']));
            });
        },
        //初始化附件属性
        attachmentInit:function(){
            var that = this;
            $('#attachmentUpload').xljAttachment({
                appId: that.appId,//itemObj.appId
                businessId: that.id,//表单id
                categoryId: $('#categaoryid').text(),//itemObj.categoryId
                mode: customFill.type,
                singleUpload: false,
                customApp:true
            });
        },
        isShowAttach:function(){
            var self = customFill;
            if($("#categaoryid").length>0){
                $("#categaoryid").parents("tr").hide();
                $(".attachmentBoxIsShow").show();
                self.isHasAttach = true;
                self.attachmentInit();
            }else{
                $(".attachmentBoxIsShow").hide();
            }
        },
        bindSel:function(){
            //给处理意见赋值
            var note = $.trim($('#note').val());
            if(this.formValueJson){
                if(this.formValueJson["exp_remark"]){
                    note = this.formValueJson["exp_remark"].cmpValue;
                    note=note.replace(/<br\/>/g, "\n");
                }
            }
            $("#note").attr("data-cmpValue",note);
            $("#note").val(note);
            //组件点击
            $('[data-cmpId]').map(function(i, node){
                var cmpName = node.getAttribute('data-cmpName');
                var mapping = customFill.mapping;
                var sel_mapping = customFill.sel_mapping;
                if(mapping.hasOwnProperty(cmpName) || cmpName=="CustomArchive" || cmpName=="Organization" || cmpName=="LegalPerson" || cmpName=="InnerUsers"){
                    $(node).find(".my-sel").on("click",function(){
                        var that = this;
                        var id = $(node).attr("data-cmpValueIds");
                        var data = {userStatus:true,userDelFlag:false,orgStatus:true,orgDelFlag:false};
                        var url = customFill.hostUrl + 'sys/org/user/getUserTree?random=' + Date.now();
                        if(cmpName=="CustomArchive"){
                            var archiveIdAttr = $(node).attr("archiveIdAttr");
                            data = {mainId:archiveIdAttr,status:"1"};
                            url = customFill.hostUrl +'sys/base/customArchives/getTree?random=' + Date.now();
                        }else if(cmpName=="LegalPerson"){
                            data = {delflag: "0"};
                            url = customFill.hostUrl +'/sys/base/baseCorporation/queryList?random=' + Date.now();
                        }
                        var keyType = sel_mapping.hasOwnProperty(cmpName)?sel_mapping[cmpName]:"";

                        var modal = $.selectModal({
                            $input : $(that),
                            selectionMethod: "single",  //single  multi
                            selectKey :{
                                val:  keyType,
                                key : "type"
                            },
                            selectList : "",//单选不必选中
                            componentType:1,
                            param : {
                                url:url,
                                type: "POST",
                                param:data,
                                contentType:'application/json'
                            },
                            callback : function(data){
                                if(data){
                                    console.log(data);
                                    $(node).find("input").val(data[0].name);
                                    if(data[0].name) $(node).find("input").removeClass("error-border");
                                    $(node).attr("data-cmpValueIds",data[0].id);
                                    $(node).attr("data-cmpValue",data[0].name);

                                }
                            }, close : function(modal){
                                modal.remove();
                            }
                        }).open();
                    });

                }

            });
        },
        /*
        * 渲染表单
        * */
        renderForm:function(resultData){
            //var stageData = {};
            var that = this;
            {_.map(resultData, function(rowData, key){
                var isRowVisible = true;
                if (_.has(rowData, 'display')) {
                    isRowVisible = rowData['display'] == 'VISIBLE'? true : false;
                }
                var isShow = isRowVisible?"table-row":'none';
                that.renderEachBox(rowData,isShow);
            })}
        },
        /*
        * 渲染box
        * */
        renderEachBox:function(rowData,isShow){
            var that = this;
            var newRow = [];
            var layout = [];
            var titleArray = ["标题","主题","发文标题"];
            {rowData['row'].map(function(colData, key){
                if(colData.length>0){

                    if(colData[0].cmpName=="TextInput" &&  titleArray.indexOf(colData[0].showColName)>-1 && !that.findTitleFlag){
                        that.formTitleCmpId = colData[0].cmpId;
                        that.findTitleFlag = true;
                        colData[0].isTitleInput = true;
                    }

                    for(var v_key in that.formValueJson){
                        if(v_key==colData[0].cmpId){

                            colData[0].cmpValueIds = that.formValueJson[v_key].cmpValueIds ? that.formValueJson[v_key].cmpValueIds:"";
                            colData[0].cmpValue = that.formValueJson[v_key].cmpValue;
                            colData[0].cmpValueShowName = that.formValueJson[v_key].cmpValueShowName;
                            //if(that.formTitleCmpId==v_key){
                            //    that.formTitle = that.formValueJson[v_key].cmpValue;
                            //
                            //}
                        }
                    }
                    newRow.push(colData[0]);
                    layout.push(rowData['layout'][key]);
                }
            })}
            {newRow.map(function(r, key){
                var cols= "";
                var colwidth= "";
                if(newRow.length==1){
                    cols = 'colspan="2"';
                }
                if(key%2==0){//为偶数位的时候  建立tr
                    trEle = $("<tr style='display: "+isShow+"'>",{});
                    colwidth =  'width="30%"';
                    if(r.cmpName=="DateInput"){
                        colwidth =  'width="50%"';
                    }
                }
                var tdEle = $("<td "+cols+" "+colwidth+">", {});
                var boxEle = $("<div>", {
                    "data-cmpId": layout[0].cmpId,
                    "data-cmpName": "Box.layout",
                    "data-stage-ele-type": "box"
                });
                var ele = that.renderEachComp(r);
                $(boxEle).append(ele);
                $(tdEle).append(boxEle);

                $(trEle).append(tdEle);
                if($(ele).attr("readonly")){
                    $(trEle).addClass("tr-read-bg");
                }
                if(key%2==0){
                    $("#fill-tbody").append(trEle);
                }
            })}
        },
        //渲染各个组件
        renderEachComp: function (eachCom) {
            var cmpName = eachCom.cmpName;
            switch (cmpName) {
                case'Attach':
                    var cid = eachCom.attachCategoryId ? eachCom.attachCategoryId : "";
                    return "<span id='categaoryid' style='display: none'>"+cid+"</span>";
                case 'TitleForm':
                    return "<div data-cmpId='"+ eachCom.cmpId+"' data-cmpName='TitleForm' data-cmpShowName='"+eachCom.cmpShowName+"'>" +
                    "<span>"+eachCom.staticText+"</span>" +
                    "</div>";

                case 'TextInput':
                    //<i class="fa fa-times input-close" aria-hidden="true"></i>
                    var v = eachCom.cmpValue ? eachCom.cmpValue : "";
                    var isTitle = eachCom.isTitleInput ? eachCom.isTitleInput : "";
                    var that = customFill;
                    var inputDataType = eachCom.inputDataType ? eachCom.inputDataType : "";
                    var rewriteFlag = eachCom.rewriteFlag ? eachCom.rewriteFlag : "";
                    if(rewriteFlag=="vbusinesstype"){
                        v =that.dataValueForSave.vbusinesstype;
                    }
                    var isShow = "";
                    if(v) isShow = "isShow";
                    return "<div class='pr' data-cmpId='"+ eachCom.cmpId+"' isRequired='"+ eachCom.isRequired+"' inputDataType='"+ inputDataType+"' data-cmpName='TextInput' data-cmpShowName='"+eachCom.cmpShowName+"' " +
                        "data-cmpValue='"+v+"' data-showColName='"+eachCom.showColName+"' data-expressStatement='"+eachCom.inputExpressStatement+"'>" +
                        "<input type='text' value='"+v+"' isTitle='"+isTitle+"' />" +
                        '<span class="titleSpan '+isShow+'" style=" margin-top: 14px;  right: 5px; "><img src="image/applist_cancel@2x.png"/></span>'+
                        ''+
                        "</div>";

                case 'DateInput':
                    var v = eachCom.cmpValue ? eachCom.cmpValue : "";
                    var isShow = "";
                    if(v) isShow = "isShow";
                    return '<div class="pr" data-cmpValue="'+v+'" data-dateOptionId="'+ eachCom.dateOptionId+'" isRequired="'+ eachCom.isRequired+'" data-cmpName="DateInput"'+
                    'data-cmpId="'+ eachCom.cmpId+'">'+
                   '<input type="text" readonly name="appDateTime" style="width: 97%;" class="inputDate"  value="'+v+'"/>'+
                   '<span class="dateSpan '+isShow+'" style="margin-top: -8px; right: 28px;"><img src="image/applist_cancel@2x.png"/></span>'+
                        '<i class="fa fa-calendar my-date-icon" aria-hidden="true" style="top: -0.5rem;z-index: -1;"></i>'+
                    '</div>';

                case 'LabelForm':
                    var text = eachCom.staticText;
                    if(text.substring(0,1)=="*"){
                        text = "<label style='color: red'>*</label>"+text.substring(1);
                    }
                    return "<div data-cmpId='"+ eachCom.cmpId+"' data-cmpName='LabelForm' data-cmpShowName='"+eachCom.cmpShowName+"'>" +
                        "<span>"+text+"</span>" +
                        "</div>";

                case 'SingleSelect':
                    var optionSetting = eachCom.optionSetting? eachCom.optionSetting : [];
                    var slectHtml = "";
                    var selVal = "";
                    var selcted = "";
                    var v,showname= "";
                    eachCom.cmpValue = $.trim(eachCom.cmpValue);
                    optionSetting.map(function(optionItem){
                        if (eachCom.cmpValue != '') {
                            if(optionItem.value==eachCom.cmpValue){
                                selcted = "selected";
                            }else{
                                selcted = "";
                            }
                          }else{
                            if(optionItem.default==true){
                                selcted = "selected";
                            }else{
                                selcted = "";
                            }
                        }
                        if(selcted){
                            v = optionItem.value;
                            showname = optionItem.text;
                        }
                        slectHtml+= '<option value="'+optionItem.value+'"  '+selcted+'> '+optionItem.text+' </option>';

                    });
                    return ' <div data-cmpValue="'+v+'" data-cmpValueIds="'+v+'" data-cmpValueShowName="'+showname+'" data-cmpId="'+ eachCom.cmpId+'" data-cmpName="SingleSelect">'+
                     ' <select class="sel-style" value="'+selVal+'"> <option value="">-请选择-</option> '+slectHtml+'</select> </div>';

                case 'MultiSelect':
                    return ' <div data-cmpId="'+ eachCom.cmpId+'" data-cmpName="MultiSelect">'+
                        ' <select multiple> <option value="">-请选择-</option> </select> </div>';

                case 'TextAreaForm':
                    var v = eachCom.cmpValue ? eachCom.cmpValue : "";
                    v=v.replace(/<br\/>/g, "\n");
                    var isShow = "";
                    if(v) isShow = "isShow";
                    return "<div class='pr' data-cmpId='"+ eachCom.cmpId+"' data-cmpName='TextAreaForm' isRequired='"+ eachCom.isRequired+"' data-cmpShowName='"+eachCom.cmpShowName+"' " +
                        "data-cmpValue='"+v+"' data-showColName='"+eachCom.showColName+"' data-expressStatement='"+eachCom.inputExpressStatement+"'>" +
                        "<textarea>"+v+"</textarea>" +
                        '<span class="deleteSpan '+isShow+'" style=" margin-top: 20px;right: 5px;margin-bottom: 0px;"><img src="image/applist_cancel@2x.png"/></span>'+
                        '<span class="glyphicon-remove-sign"></span>'+
                        "</div>";

                case 'RadioForm':
                case 'CheckboxForm':
                    var optionSetting = eachCom.optionSetting? eachCom.optionSetting : [];
                    var radioHtml = "";
                    var checked = "";
                    var v,showname= "";
                    var type = cmpName=="RadioForm"?"radio":"checkbox";
                    optionSetting.map(function(optionItem){
                        console.log(optionItem);
                        if(optionItem.text==""){
                            return false;
                        };
                        if (eachCom.cmpValue) {
                            var v_arr = eachCom.cmpValue.split(",");
                            if(_.indexOf(v_arr,optionItem.value)>-1){
                                checked = "checked";
                            }else{
                                checked = "";
                            }

                        }else{
                            if(optionItem.default){
                                checked = "checked";
                            }else{
                                checked = "";
                            }
                        }
                        if(checked){
                            v = optionItem.value;
                            showname = optionItem.text;
                        }
                        radioHtml+= ' <label class="radio-inline" style="margin-right:5px;margin-left: 5px;"> <input type="'+type+'" '+
                            'name="'+eachCom.cmpId+'_'+type+'" '+checked+' value="'+optionItem.value+'"/><span style="margin-left:0px">'+optionItem.text+'</span></label>';

                    });
                    return ' <div data-cmpValue="'+v+'" data-cmpValueIds="'+v+'" data-cmpValueShowName="'+showname+'" data-cmpId="'+ eachCom.cmpId+'" ' +
                        'data-cmpName="'+cmpName+'">'+
                        ' '+radioHtml+'</div>';

            case 'FormNumber':
            case 'Status':
            case 'CreatePerson':
                case 'CreateOrg':
            case 'UpdatePerson':
            case 'CreateDate':
            case 'UpdateDate':
                var v = "";
                if(cmpName=="CreateDate") v = customFill.dataValueForSave.createDate;
                if(cmpName=="CreatePerson") v = customFill.dataValueForSave.createPersonName;
                if(cmpName=="CreateOrg") v = customFill.dataValueForSave.operateDepartmentName;
                if(cmpName=="Status"){
                    var s = customFill.dataValueForSave.status;
                    if(s=="1") v = "审批中";
                    else if(s=="2") v = "审批完成";
                    else v = "草稿";
                }
                if(cmpName=="UpdatePerson") v = customFill.dataValueForSave.updatePersonName;
                if(cmpName=="UpdateDate") v = customFill.dataValueForSave.updateDate;
                if(cmpName=="FormNumber") v = customFill.dataValueForSave.formNumber;
                return "<div class='noEdit' data-cmpId='"+ eachCom.cmpId+"' data-cmpValue='"+v+"' data-cmpName='"+cmpName+"' data-cmpShowName='"+v+"'>" +
                "<span>"+v+"</span>" +
                "</div>";
            case 'Operator':
            case 'OperateCompany':
            case 'OperateDepartment':
            case 'OperateProject':
            case 'OperateQi':
                case 'LegalPerson':
            case 'InnerUsers':
            case 'InnerCompany':
            case 'Departments':
            case 'Organization':
            case 'CustomArchive':
                var v = "";
                if(cmpName=="Operator") v = customFill.dataValueForSave.operatorName;
                else if(cmpName=="OperateCompany") v = customFill.dataValueForSave.operateCompanyName;
                else if(cmpName=="OperateDepartment") v = customFill.dataValueForSave.operateDepartmentName;
                else if(cmpName=="OperateProject") v = customFill.dataValueForSave.operateProjectName;
                else if(cmpName=="OperateQi") v = customFill.dataValueForSave.operateQiName;
                else v =  eachCom.cmpValueShowName?eachCom.cmpValueShowName:"";
               //var
                var cmpValueIds = "";
                var mapping = customFill.mapping;
                if(mapping.hasOwnProperty(cmpName)){
                    cmpValueIds =  customFill["dataValueForSave"][mapping[cmpName]];
                }else{
                    cmpValueIds = eachCom.cmpValueIds?eachCom.cmpValueIds:"";
                }
                var archiveIdAttr = "";
                if(cmpName=="CustomArchive") {archiveIdAttr = 'archiveIdAttr='+eachCom.archiveCategoryId;}
                return '<div class="pr" data-cmpValue="'+v+'" '+archiveIdAttr+' isRequired="'+ eachCom.isRequired+'" data-cmpId="'+ eachCom.cmpId+'" readonly="true" data-cmpValueIds="'+cmpValueIds+'" data-cmpName="'+cmpName+'" >'+
            '<input type="text" readOnly disabled value="'+v+'"/>' +
                '<i class="fa fa-ellipsis-h my-sel" aria-hidden="true"></i></div>';
            default:
            break;
        }
        },
        // 确定注册流程变量
        getRegisteredFlowVar:function(filledData) {
            if (_.isEmpty(this.registeredFlowVar)) {
                return {};
            }
            var formFlowVariableValue = {};
            var registeredFlowVar = this.registeredFlowVar;
            if (typeof(registeredFlowVar) == 'string') {
                registeredFlowVar = JSON.parse(registeredFlowVar);
            }

            if (_.isArray(registeredFlowVar)) {
                registeredFlowVar.map((cmp) => {
                    var cmpId = cmp['cmpId'];
                    if (filledData && filledData[cmpId]) {
                        formFlowVariableValue[cmpId] = filledData[cmpId].cmpValue;
                    }
                });
            }
            return formFlowVariableValue;
        },
        hasFormNumberCmp:function(formatStr) {
            var formatJson = JSON.parse(formatStr);
            var rtn = false;
            _.cloneDeepWith(formatJson, function(obj){
                if (obj && _.has(obj, 'cmpId') && _.has(obj, 'cmpName') && obj.cmpName == 'FormNumber') {
                rtn = true;
            }
            });
        return rtn;
    },
        /**
         * 获取数据
         */
        fetchFilledData:function (){
            var that = this;
            var ids=this.id;
            var type =this.type;
            var customFormId=this.customFormId;
            var resultData = "";
            $.ajax({
                url:that.hostUrl+"sys/base/customFormInstance/getInstance/"+ids+"/"+customFormId,
                type:'get',
                dataType:'JSON',
                contentType:"application/json",
                success:function (resultData) {
                    if (resultData.success) {
                        if (that.hasFormNumberCmp(resultData.result.formFormatJson)) {
                            var url2 = that.hostUrl + "sys/base/customForm/getFormNumber/" + customFormId + "?time=" + _.random(0, 99999);
                            $.ajax({
                                url: url2,
                                type: 'get',
                                dataType: 'JSON',
                                contentType: "application/json",
                                success: function (dataNumber) {
                                    if (dataNumber.success) {
                                        if (!_.isEmpty(resultData.result)) {
                                            resultData.result['formNumber'] = dataNumber.result;
                                            that.dataValueForSave['formNumber'] = dataNumber.result;
                                            //单据编号赋值
                                            $("div[data-cmpname='FormNumber']").find("span").text(that.dataValueForSave['formNumber']);
                                            $("div[data-cmpname='FormNumber']").attr("data-cmpShowName","单据编号");
                                            $("div[data-cmpname='FormNumber']").attr("data-cmpValue",that.dataValueForSave['formNumber']);
                                        }
                                    }
                                }
                            });
                        }
                        var removedSpecStrValueJson = _.isEmpty(resultData.result.formValueJson)? "{}" : resultData.result.formValueJson;
                        removedSpecStrValueJson = removedSpecStrValueJson.replace(/\r|\n/g, '');
                        removedSpecStrValueJson = removedSpecStrValueJson.replace(/\\\'/g, '\'');
                        removedSpecStrValueJson = removedSpecStrValueJson.replace(/\\\"/g, '\'');
                        removedSpecStrValueJson = removedSpecStrValueJson.replace(/\t/g, '    ');

                        that.code = resultData.result.code;
                        that.appId = resultData.result.appId || '99';
                        //存值
                        for(var key in that.dataValueForSave){
                            that.dataValueForSave[key] = resultData.result[key]?resultData.result[key]:"";
                        }
                        //if (typeof(removedSpecStrValueJson) == 'object') {
                        //    this.stageData = JSON.stringify(removedSpecStrValueJson);
                        //}
                        that.stageData = eval('(' +resultData.result.formFormatJson+ ')');
                        that.registeredFlowVar = resultData.result.flowVariable;
                        that.formValueJson = JSON.parse(removedSpecStrValueJson);
                        that.renderForm(that.stageData);
                        that.isShowAttach();
                        that.bindSel();
                        that.addDateEvent();

                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    that.dialog_tip("服务异常,请联系管理员！");
                }
            });
        },
        /**
         * 页面初始化
         */
        pageInit:function(){
            //绑定按钮事件

            this.id = this.getUrlParam("id");
            this.type = this.getUrlParam("type");
            this.get_title = this.getUrlParam("name");
            //this.get_title = "";
            $('#note').val("请领导审批");
            $("#get_title").html(this.get_title);
            $("title").html(this.get_title);
            this.customFormId = this.getUrlParam("customFormId");
            this.fetchFilledData();
            this.bind_event();

        }
    };
    $(customFill.pageInit());
    window["customFill"] = customFill;

})(window,document);