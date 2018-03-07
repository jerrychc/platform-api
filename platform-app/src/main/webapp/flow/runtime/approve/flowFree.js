/**
 * Created by Administrator on 2018/2/11.
 */


var resultData,xName;
var postData = {};
var num = 3;

$.getUrlParam = function(name){
    var reg = new RegExp("(^|&)"+ name + "=([^&]*)(&|$)");
    var r = decodeURI(window.location.search).substr(1).match(reg);
    if (r!=null ){
        return unescape(r[2]);
    }
    return null;
};
postData.businessObjectCode = $.getUrlParam("businessObjectCode");
postData.businessId = $.getUrlParam("businessId");
postData.title = $("#flowNameTitle").text();
var acListArr = [];
postData.acList = "";
/**
 * 添加已选的人addMultipleGird
 */
function addMultipleGird(ele) {
    $(ele).xljMultipleSelector({
        selectorType: 'onlyPerson',//选择器类型
        selectNodeType:{type:'user',msg:'只能选择人员！'},
        immediatelyShow: true,//是否立即显示，默认是false，已click事件触发
        title: '选择',//选择器标题
        targetId: $(ele).siblings('input:hidden').attr('id'),
        treeParam:{userStatus:true},
        //选择器保存按钮回调函数
        saveCallback: function (data, e){
            console.log(data);
            console.log(e);
            selectPerson(data,e);
        }
    });
}
//计算iframe高度自适应:供子页面调用
window.resizeIframe = function () {
    //高度计算
    var b_height;
    if (document.bizForm) {
        b_height = $("#form-composer", document.bizForm.document.body).height();
    } else {
        // ff
        var iframeBody = document.getElementById('bizForm').contentDocument.body;
        b_height = Math.max(iframeBody.scrollHeight, iframeBody.clientHeight);

    }

    //自适应设置
    if (document.bizForm) {
        // ie, chrome
        var b_iframe = document.getElementById("bizForm");
        if (!$.isEmptyObject(document.bizForm) && $(document.bizForm.document).find('#form-composer')) {
            //自定义表单自适应设置
            $(b_iframe).height(b_height);
            $(document.bizForm.document).find('#form-composer').width("100%")
        } else {
            $(b_iframe).height(b_height + 20);
        }
    } else {
        //ff
        var iframeBody = document.getElementById('bizForm').contentDocument.body;
        var $iframeBody = $(iframeBody).find('#form-composer');

        var b_iframe = document.getElementById("bizForm");
        $iframeBody.width("100%");
        if ($iframeBody) {
            //自定义表单自适应设置
            $(b_iframe).height(b_height);
        } else {
            $(b_iframe).height(b_height + 20);
        }
    }
};
function selectPerson(backData,ele){
    var personIdsEle = $(ele).parents(".item").find(".personIds");
    var personNamesEle = $(ele).parents(".item").find("input[name='PersonName']");
    var names = "";
    var ids = "";
    $.each(backData, function(index, item){
        names+=item.name+",";
        ids+=item.id+",";
    });
    if(ids) ids = ids.substring(0,ids.length-1);
    if(names) names = names.substring(0,names.length-1);
    personIdsEle.val(ids);
    personNamesEle.val(names);
}
function _ajax(cb,dataP,url){
    if(!dataP){
        dataP = '';
    }
    if(!url){
        // url ="http://10.17.4.254:8080/platform-app/flow/instance/saveAll"+'?time='+Math.random();
       url = hostUrl+"flow/instance/saveAll"+'?time='+Math.random();
    }
    $.ajax({
        url: url,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(dataP),
        success: function(resultData) {
            if (resultData && resultData.success) {
                console.log(resultData);
                cb && cb (resultData);
            }else {
            }

        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}
$(function () {
    $("body").delegate(".item .delete_h", "click", function(event){
        if($(".item").length>1){
            $(this).parents(".item").remove();
            event.stopPropagation();
        }

    });
    //添加环节
    $(".add_h").on("click",function(){
        var child_item = $(`
                        <div class="item clearfix col-xs-12 pd0">
                            <div class="col-xs-2 tlcenter"><span class="huanjie_Name">环节${num}</span></div>
                            <div class="item-con clearfix col-xs-9">
                                <div class="type">
                                    <input type="radio" name="h_radio${num}" checked value="2"><span style="margin-right: 20px;">审批</span>
                                    <input type="radio" name="h_radio${num}" value="1"><span>抄送</span>

                                </div>
                                <div class="person-box">
                                    <div class="input-group" style="width: 100%">
                                        <input type="hidden" class="personIds" name="personIds" readonly="">
                                        <input type="text" class="form-control" style="min-width: 200px;" readonly="" name="PersonName" placeholder="选择人员" data-placeholder="选择人员">
                                        <div class="input-group-addon" onclick="empty(this)"> <a class="glyphicon glyphicon-remove"></a></div>
                                        <span class="input-group-addon w28" onclick="addMultipleGird(this)">
                                        <a class="fa fa-ellipsis-h fatherNodeSelector">
                                        </a>
                                        </span>
                                    </div>
                                </div>

                            </div>
                            <div class="del-box col-xs-1">
                                <a href="#" class="delete_h"></a>
                            </div>
                        </div>
                    `);
        $(".add-new-item").before(child_item);
        num++;
    });


    //提交
    $("#submit_h").on("click",function(){
        var isFaverCheckbox = $("input[type='checkbox']").is(":checked");
        if(isFaverCheckbox){
            //_ajax();
        }
        $(".item-box .item").each(function(i,n){
            var userids = $(n).find("input[name='personIds']").val();
            if(userids){
                //有值
                var userNames = $(n).find("input[name='PersonName']").val();
                var idsArr = userids.split(",");
                var namesArr = userNames.split(",");
                var approveObj = {};
                approveObj.name = $(n).find(".huanjie_Name").text();//环节名称
                approveObj.approvaltype = $(n).find("input[type='radio']").val();//环节类型
                var approversArr = [];
                for(var i = 0;i<idsArr.length;i++){
                    var user = {};
                    user.participantName = namesArr[i];
                    user.participantId = idsArr[i];
                    approversArr.push(user);
                }
                approveObj.approvers = JSON.stringify(approversArr);
                acListArr.push(approveObj);
            }
        });
        //ajax请求
        if(acListArr.length>0){
            postData.acList = JSON.stringify(acListArr);

            _ajax(function(data){
                console.log(data);
                //alert();
            },postData);
        }else{
            $.xljUtils.tip('blue', '请选择审批人！',"3000");
        }
    });

});
    //编辑
    function ShowElement(element) {
        var oldhtml = element.innerHTML;
        //创建新的input元素
        var newobj = document.createElement('input');
        //为新增元素添加类型
        newobj.type = 'text';
        //设置宽度
        newobj.style.width=250+"px";
        //设置高度
        newobj.style.height=30+"px";
        //为新增元素添加value值
        newobj.value = oldhtml;
        //为新增元素添加光标离开事件
        newobj.onblur = function() {
            //当触发时判断新增元素值是否为空，为空则不修改，并返回原有值
            this.value = !$.trim(this.value) ? oldhtml : $.trim(this.value);
            element.innerHTML = this.value == oldhtml ? oldhtml : this.value;
            //当触发时设置父节点的双击事件为ShowElement
            element.setAttribute("ondblclick", "ShowElement(this);");
        }
        //设置该标签的子节点为空
        element.innerHTML = '';
        //添加该标签的子节点，input对象
        element.appendChild(newobj);
        //设置选择文本的内容或设置光标位置（两个参数：start,end；start为开始位置，end为结束位置；如果开始位置和结束位置相同则就是光标位置）
        newobj.setSelectionRange(0, oldhtml.length);
        //设置获得光标
        newobj.focus();

        //设置父节点的双击事件为空
        newobj.parentNode.setAttribute("ondblclick", "");
    }

    //点击删除
    $(document).on("click",".tmp-del",function(e){
        $(this).parent().remove();
    });

    //点击上移
    $(document).on("click",".up",function(e){
        if($(this).parents('li').prevAll().length > 0){
            $(this).parents('li').prev().before($(this).parents('li').prop('outerHTML'));  
            $(this).parents('li').remove();         
            }  
    });

    //点击下移
    $(document).on("click",".down",function(e){
        if($(this).parents('li').nextAll().length > 0){  
            $(this).parents('li').next().after($(this).parents('li').prop('outerHTML'));  
            $(this).parents('li').remove();  
            }  
    });
