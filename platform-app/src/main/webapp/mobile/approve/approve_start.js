/**
 * Created by Administrator on 2018/1/9.
 */

(function(){
	var startPage = {
		baseUrl :'/',
		hostUrl : '/platform-app/',
		businessId : "",
		appId:"",
		customFormId: "",
		businessObjectCode:"",
		resultData:[],
		settitle:"",
		disabledFlag:false,
		needisStart:false,
		postIsEmpty:false,
		approverIsEmpty:false,
		lastNodeIsEmpty:false,
		_ajax : function(cb,dataP,url){
			var self = startPage;
			if(!dataP){
				dataP = '{}';
			}
			if(!url){
				url = self.hostUrl+"flow/start";
			}
			$.ajax({
				url: url,
				type: 'POST',
				dataType: 'json',
				contentType: 'application/json',
				data: JSON.stringify(dataP),
				success: function(resultData) {
					if (resultData && resultData.success) {
						console.log(resultData)
						cb && cb (resultData)
					}else {
						alert("接口异常");
					}
					self.loadingHideStyle();
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alert(textStatus+"==="+errorThrown);
				}
			});
		},
		bind_event : function(){
			//刷新
			$("#refresh_btn").on("click",function(e){
				location.reload();
				e.stopPropagation();
			});
			//提示框样例
			var dialog_tip = this.dialog_tip = function (text){
				$(document).dialog({
					type : 'notice',
					infoText: text,
					autoClose: 2500,
					position: 'top'  // center: 居中; bottom: 底部
				});
			}
			$("#submit").on("click",function(){
				startPage.doSubmit();
			});
			//折叠展开
			$("body").delegate(".mui-navigate-right", "click", function(e){
				$(this).next().toggle();
				var classflag = $(this).attr("classflag");
				var css=function(t,s){
					s=document.createElement('style');
					s.innerText=t;
					document.body.appendChild(s);
				};
				var cont;
				if($(this).next().is(":hidden")){
					cont = '\\e581';
				} else{
					cont = '\\e580';
				}
				css(".mui-table-view-cell.mui-collapse>.mui-navigate-right."+classflag+":after{content:'"+cont+"'}");
			});

			//选择责任人
			$("body").delegate(".selectPerson", "click", function(e){
				var that = this;
				var acId = $(this).attr("dataId");
				var data = {userStatus: true, userPostSelector: "defultUserPostSelector"};
				var table = $(this).parents("table");
				var ids = [];

				var $bd = $(this);
				// $(table).find(".p_box").each(function(){
				// 	ids.push(this.attr("dataId"));
				// });
				var searchDataRes = $bd.data("selectObj");

				var id = "";
				var url = startPage.hostUrl + 'sys/org/roleUser/selectUserPostTree?random=' + Date.now();
				var modal = $.selectModal({
					$input : $(that),
					selectionMethod: "multi",  //single  multi
					selectKey :{
						val:  "user",
						key : "type"
					},
					// selectList : id,
					searchSelectedList : searchDataRes,
					// param : {
					// 	url:url,
					// 	type: "POST",
					// 	param:data,
					// 	contentType:'application/json'
					// },
					searchParam : {
						url : startPage.hostUrl + 'sys/org/user/queryUserAndPostsByUname',
						type : 'POST',
						param: {"uType":"belongOrg"},
						contentType: 'application/json',
						dataType: 'JSON',
					},
					searchHtml: function(data,bd,menu,scope){

						var my = this;
						var fn = function(list,box){
							var t = list;
							if(t && t.length){
								var boxData = box.data("select-node");
								if(!boxData){
									boxData = [];
								}
								// var _select = id;
								for(var i = 0; i< t.length; i++){
									var data = t[i];

									data.mySelected = false;
									data.open = true;
									if(searchDataRes && searchDataRes[data.uId]){
										data.mySelected = true;
									}
									data.id = data.uId;
									data.name = data.realName;
									data.type = "user";
									data.prefixName = data.pName;
									var select_bg = "";
									if(data["type"] == "user"){
										select_bg  = "select_bg";
									}

									var itemType ;
									var imgUrl = "";
									switch (data.type){
										case "company":
											itemType = 'cor1';
											break;
										case "dept":
											itemType = 'cor2';
											break;
										case "user":
											itemType = 'cor3';
											imgUrl = scope._getGender(data.id);
											if(!imgUrl){
												imgUrl = "../../common/utils/selectMobile/person@2x.png?v=1"
											}
											break;
										default:
											itemType = 'cor1';
											break;
									}
									var $li = $(`
											<li data-listid="${data.id || data.uId}" data-index="${data.id || data.uId}" >
												<div class="select_list ${data.titleNode ? 's-title-node':''}">
													<span class="s_statu" data-isselect="${data.mySelected ? '1': '0'}">
													<input type="checkbox" id="${data.id || data.uId}" class="selectTypeInput">
													<label for="${data.id || data.uId}" class="selectType ${select_bg}"></label>
												</span>
												<div class="s_ct">
														<span class="s_img">
															<span class="${itemType}">
															<img src="${imgUrl}&imgType=0" style="display:${imgUrl ? 'block': 'none' }">
															</span>
														</span>
													<div class="s_txt">
														<h2>${data.realName}</h2>
														<p style="display:${data.pName ? 'block':'none'}">${data.pName}</p>
													</div>
												</div>
												</div>
											</li>
											`);
									$li.data(data);
									box.append($li);
									if(data.open){
										// box.parents("li").show();
										boxData.push(data);
										box.data("select-node",boxData);
									}
								}
							}
						}
						fn(data,bd);
					},
					//showTypeName:'user',
					callback : function(data){
						if(data && data.length>0){
							var searchSelectedList = [],searchObj = {};
							$.extend(searchSelectedList,data);
							if(searchSelectedList && searchSelectedList.length){
								for(var i = 0; i< searchSelectedList.length; i++){
									var temp = searchSelectedList[i];
									searchObj[temp.uId] = temp;
								}
							}
							$bd.data("selectObj",searchObj);
							startPage.renderSelectTd(data,that,acId,searchObj);
							startPage.selectPerson(data,that,acId);

						}
					}, close : function(modal){
						modal.remove();
					}
				}).open();
			});
			//选择人员 确定
			$("body").delegate(".result-get", "click", function(e){

			});
		},
		 selectPerson:function(backData,ele,acId){
			$.each(startPage.resultData.acDtoList, function(i, ac) {
				if(acId == ac.id) {
					var posts = [];
					$.each(backData, function(index, item){
						var prefixName = item.prefixName;
						var postNameold = '';
							if(prefixName.lastIndexOf('/')>-1){
								prefixName = prefixName.substring(0,prefixName.lastIndexOf('/'));
							}
							if(prefixName.lastIndexOf('/')>-1){
								postNameold = prefixName.substring(prefixName.lastIndexOf('/')+1);
								prefixName = prefixName.substring(0,prefixName.lastIndexOf('/'));
							}
						var postId = item.postId;
						var postName = prefixName + '/' + postNameold;
                        if( !postId ){
                            postId = item.pId;
                            postName = item.pName +"/"
                        }
						var person = [{id: item.id, name: item.name}];
						var post = {id: postId, name: postName};
						post.users = person;
						posts.push(post);
					});
					ac.posts = JSON.stringify(posts);
				}
			});
	},
		checkIsNeedSelect:function(flowList){
			//审批列表
			var self = this;
			if (flowList) {
				for (var i = 0; i<flowList.length-1; i++) {
					var itemObj = flowList[i];
					var setApproverWhenStart = itemObj.setApproverWhenStart;
					if(setApproverWhenStart){
						var isStart = itemObj.isStart;
						if(isStart && !itemObj.approverId){
							self.needisStart = true;
						}
					}else{
						//岗位为空检查:postNull=1表示不能发起postNull=''表示未配置
						if(!itemObj.postId) {
							if(itemObj.postNull == '1' || !itemObj.postNull) {
								self.postIsEmpty = true;
							}
						} else {
							//审批人为空检查
							if(!itemObj.approverId) {
								if(itemObj.approverNull == '1' || !itemObj.approverNull) {
									self.approverIsEmpty = true;
								}
							}
						}
					}
				}
				//流程完整性校验
				//校验没有结束环节的情况
				var lastNode = flowList[flowList.length-1];
				var acType = lastNode.acType;
				if(acType != '3') {
					self.lastNodeIsEmpty = true;
				}
				if(self.needisStart || self.postIsEmpty || self.approverIsEmpty || self.lastNodeIsEmpty){
					return true;
				}
			}
			return false;
		},
		downLoad:function(obj,type,name,path,url) {
			var officeType = '*.pdf;*.doc;*.ppt;*.xls;*.docx;*.pptx;*.xlsx';
			var pngType = "image/jpg,image/jpeg,image/png,image/gif";
			if("url"==type){
				window.location.href=url;
			}else {
				var obj = {};
				obj.FILENAME = url.substring(url.lastIndexOf('/') + 1);
				obj.GROUP = path;
				obj.NAME = name;
				var extensionName = obj.FILENAME.substring(obj.FILENAME.lastIndexOf(".") + 1);
				if (officeType.indexOf(extensionName.toLowerCase()) > -1) {
					$.ajax({
						url: "/platform-app/univ/attachment/attachment/docConverter" + "?time=" + Math.random(),
						data: JSON.stringify(obj),
						type: "POST",
						contentType: 'application/json',
						dataType: 'JSON',
						async: false,
						success: function (resultData) {
							if (resultData) {
								var successFlag = resultData.success;
								if (successFlag) {
									var exName = resultData.msg.substring(resultData.msg.lastIndexOf(".") + 1);
									if ("html" == exName) {
										//window.location.href=$.xljUtils.serverAddr+resultData.msg;
										window.location.href = $.xljUtils.serverAddr + "/mobile/approve/approve_view.html?path=" + resultData.msg.replace(/\\/g, "/") + "&fileName=" + encodeURIComponent(name);
									} else {
										// window.location.href=$.xljUtils.serverAddr+"/pdf.html?path="+resultData.msg.replace(/\\/g,"/");
										window.location.href = $.xljUtils.serverAddr + "/pdf/viewer.html?path=" + resultData.msg.replace(/\\/g, "/") + "&fileName=" + encodeURIComponent(name);
									}
								} else {
									$.xljUtils.tip("red", '文件中存在特殊格式文本，不支持预览！');
								}
							}
						},
						error: function (XMLHttpRequest, textStatus, errorThrown) {
							alert("服务异常,请联系管理员！");
						}
					});
				} else if (pngType.indexOf(extensionName.toLowerCase()) > -1) {
					$.post($.xljUtils.serverAddr + "univ/attachment/attachment/getStorageIP",
						{filePath: path},
						function (ip) {
							if (ip) {
								// window.location.href=location.protocol + '//' +ip+":"+$.xljUtils.fdfsStoragePort+"/"+file.path;
								window.location.href = $.xljUtils.serverAddr + "/mobile/approve/approve_view.html?path=" + location.protocol + '//' + ip + ":" + $.xljUtils.fdfsStoragePort + "/" + path + "&fileName=" + encodeURIComponent(name);
							}
						}
					);
				} else {
					alert("该文件不支持预览,请在电脑上查看！")
					return;
				}
			}
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
	getCustomData:function(){
		//debugger;
		var self = startPage;
		var dataP = {
			businessId:self.businessId,
			businessObjectCode: self.businessObjectCode,
			flCode: null,
			limit: -1,
			nd: Math.random(),
			page: 1,
			rows: -1,
			sidx: "",
			sord: "asc",
			start: 0,
			_search: false
		};
		self.loadingShowStyle("渲染数据中...");
		self._ajax(function(resultData){
			//debugger;
			self.resultData = resultData.result;
			self.appId = resultData.result.appId;
			self.settitle = resultData.result.flowTitle;
			self.resultData.id = self.initUUID();
			var isHref = self.checkIsNeedSelect(resultData.result.approvalLists);
			if(!isHref) self.doSubmit({"isHref":true});
				var dataList = resultData.result.mobileFormDtoList;
				if (dataList) {
					$("#ywxx").empty();
					var bd = $("#ywxx").append("<table class='table_ywxx'></table>");
					var $table_ywxx = $(".table_ywxx");
					for (var i = 0; i < dataList.length; i++) {
						var nameText = dataList[i].name;
						var valueText = dataList[i].value;

						if (valueText && valueText.length > 1) {
							try{
								valueText = decodeURIComponent(valueText.replace(/\+/g, " "));
							}catch(e){
							}
						}
						if(!valueText){
							valueText = "";
						}
						valueText =  valueText.replace(/\n/g, '<br/>');
//                	str = "<p><span>"+dataList[i].name+":&nbsp;</span>  <span>"+valueText+"</span></p>";
						str = "<tr><td class='txt-center'><span>" + dataList[i].name + "</span></td><td class='txt-r'><span>" + valueText + "</span></td></tr>";
						$table_ywxx.append(str);
					}
					var note = window.sessionStorage.getItem('remark') ? window.sessionStorage.getItem('remark'):"";
					note =  note.replace(/\n/g, '<br/>');
					var remark = "<tr><td class='txt-center'><span>处理意见</span></td><td class='txt-r'><span>" + note + "</span></td></tr>";
					$table_ywxx.append(remark);
				}
			//附件
			var fileList = resultData.result.uploadAttachmentDtoList;// 附件列表
			if (fileList) {
				$("#attachmentSum").empty().append(fileList.length);
				$("#attachmentList").empty();
				for (var i = 0; i < fileList.length; i++) {
					var fullName = fileList[i].filename;
					var smallIcon = "default"
					if (fullName.indexOf("pdf") >= 0) {
						smallIcon = "pdf";
					}
					if (fullName.indexOf("doc") >= 0) {
						smallIcon = "word";
					}
					if (fullName.indexOf("xls") >= 0) {
						smallIcon = "excel";
					}
					if (fullName.indexOf("rar") >= 0 || fullName.indexOf("zip") >= 0) {
						smallIcon = "rarzip";
					}
					if (fullName.indexOf("ppt") >= 0) {
						smallIcon = "ppt";
					}
					if (fullName.indexOf("txt") >= 0) {
						smallIcon = "txt";
					}

					if (fullName.indexOf("bmp") >= 0 || fullName.indexOf("tif") >= 0
						|| fullName.indexOf("jpg") >= 0 || fullName.indexOf("png") >= 0) {
						smallIcon = "picture";
					}
					var liObj = $(`<li>
					<img src="../myimg/attach_suffix/${smallIcon}_s.png">
					<span class="">${fullName}</span></li>`);
					var path = fileList[i].url.split("//");
					var group = path[1].substring(path[1].indexOf("/")+1);
					var url = fileList[i].url;
					liObj.on("click",function(){
						startPage.downLoad(this,"",fullName,group,url);
					});
					$("#attachmentList").append(liObj);
				}
			} else {
				$("#attachmentSum").empty().append("0");
			}
			var flowList = resultData.result.approvalLists;
			console.log(flowList);
			//审批列表
			if (flowList) {
				//审批流程
				$("#splc").empty();
				var flag = "";
				var instanceStatus;
				for (var i = 0; i<flowList.length; i++) {
					var $btn = "";
					var itemObj = flowList[i];
					var taskStatus = itemObj.taskStatus;
					var isStart = itemObj.isStart;
					instanceStatus = itemObj.instanceStatus;
					var postName = itemObj.postName;
					var setApproverWhenStart = itemObj.setApproverWhenStart;
					var approverName = itemObj.approverName;
					if(!approverName || approverName=="null" ){
						approverName = "";
					}
					if (setApproverWhenStart && !postName) {
						postName = '';
						approverName = '手选责任人';
						$btn = $("<button>",{dataId:itemObj.acId,dataStart:isStart,class:"selectPerson",style:"float:right;margin-right:20px;",html:"选择责任人"});
					} else if (!setApproverWhenStart && !postName) {
						postName = '无岗位';
						approverName = '无';
					}else if(postName){
						var arr=postName.split("/");
						postName=arr[arr.length-1];
						postName1=arr[arr.length-2];
					}
					if (itemObj.acType == '3') {
						postName = '';
						approverName = '';
					}
					if(!postName){
						if(approverName=="手选责任人"){
							postName="";
						}else{
							postName=postName1;
						}
					}
					if (taskStatus == '1' || taskStatus==null || taskStatus=="null" ) {// 未办
						flag = "waiting";
					}

					var acName = itemObj.acName;
					if(!acName || acName=="null" ){
						acName = "";
					}
					if(acName=="end" || "结束"==acName){
						postName="";
					}
					//结束节点不显示部门
					var taskComments = itemObj.taskComments;
					taskComments = taskComments ? taskComments : '';

					var taskEndTime = itemObj.taskEndTime;
					if(!taskEndTime || taskEndTime=="null" ){
						taskEndTime = "";
					}
					if("开始"==acName || "start"==acName){
						flag = "current";
					}
					var flowLi = $("<li class='dian_"+flag+"' a=''>");
					var flowApproveDiv = $("<div class='list_detail_approve jiao_"+flag+" mui-clearfix'>");
					var flowAttImg = $("<span id='flAtt"+itemObj.groupKey+"' class='fujian_icon_common' style='display:"+ (flag !='pass'||acName=="end"||acName=="结束"? 'none': 'block') +"'></span>");
					var flowDiv = $("<div class='mui-clearfix top_list_content'>").append(flowAttImg)
						.append('<table class="table_detail_approve">'+
							'<tr>'+
							'<td class="td_w1 pass_name">'+ approverName +'</td>'+
							'<td class="td_w2 info_s selectPersonTd">'+ postName +'</td>'+
							'</tr>'+
							'<tr>'+
							'<td class="td_w1 pass_acname"></td>'+
							'<td class="td_w2 info_s">'+ taskEndTime +'</td>'+
							'</tr>'+
							'<tr>'+
							'<td class="td_w1 pass_acname">'+acName +'</td>'+
							'<td class="td_w2 info_text">'+ taskComments +'</td>'+
							'</tr>'+
							'</table>');

					$("#splc").append(flowLi.append(flowApproveDiv.append(flowDiv)));
					if($btn){
						$(flowDiv).find("table .info_text").append($btn);
						//责任人列表
						var acList = resultData.result.acDtoList;
						if( acList && acList.length>0){
							$(acList).each(function(i,n){
								if(n.id == itemObj.acId && !n.post){
									var data =  eval('(' +n.post+ ')');
									if(data)
									startPage.renderSelectTd(data,$btn,itemObj.acId);
								}
							})
						}
					}
					//flowAttImg.click();
				}
				self.fujianEvent();

			}
			self.loadingHideStyle();
			if($(window.parent.document).find("#start_iframe").length>0 && isHref){
				var url=  "approve_start.html"
					+ "?businessObjectCode=" + self.businessObjectCode
					+ "&businessId=" + self.businessId
					+ "&customFormId=" + self.customFormId
					+ "&time=" + new Date().getTime();
				window.parent.location.href = url;
			}
			},dataP);
		},
		renderSelectTd:function(data,btn,acId,searchObj){
			var $table = $(btn).parents("table");
			$table.find(".selectPersonTd").html("");
			for(var i = 0;i<data.length;i++){
				var postnamearr = data[i].prefixName.split("/");
				var postname = postnamearr[postnamearr.length-2];
				var $p_box = $("<p>",{class:"p_box",dataId:data[i].id,style:"width:auto;background:none;"});
				var $p_name = $("<span>",{class:"p_name",style:"margin-right:20px",html:data[i].name});
                if(!data[i].postId){
                    postname = data[i].pName;
                }
				var $p_mainPostName = $("<span>",{class:"p_mainPostName",html:postname});
				$p_box.append($p_name,$p_mainPostName);
				var $delBtn = $("<span class='del_p_box'></span>");
				$p_box.append($delBtn);
				$delBtn.data("delselectperson",data[i]);
				$table.find(".selectPersonTd").append($p_box);
			}
			if(searchObj){
				startPage.delPersonEvent(data,searchObj,acId);
			}
		},
		delPersonEvent : function(data,searchObj,acId){
			var $del = $(".del_p_box"),
				my = this;
				$del.off().on("click",function(e){
					var that = $(this),
						thatData = that.data("delselectperson"),
						$parent = that.parent(".p_box");
						$parent.remove();
						if(data && data.length){
							for(var i = 0; i< data.length; i++){
								var temp = data[i];
								if(temp["uId"] = thatData.uId){
									data.splice(i,1);
									break;
								}
							}
						}
						delete searchObj[thatData.uId];
						var $bd = $("button[dataid=" + acId + "]");
						$bd.data("selectObj",searchObj);
						startPage.selectPerson(data,"",acId);
				})
		},
		fujianEvent:function(){
			var $splc = $("#splc li");
			$splc.on("click",".fujian_icon_common",function(e){
				var target = $(e.delegateTarget),
					currentTargeet = $(e.currentTarget);
				currentTargeet.toggleClass("fujian_icon_sel");
				target.find(".attachment-box").toggle();
			});
		},
		 doSubmit:function(flag) {
			 var self = startPage;
			 resultData = self.resultData;
			 //var resultData = {};
			 //附件上传
			 if(!flag){

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
				 var submitBtn = $("#submit");
				 submitBtn.attr('disabled', true);
				 submitBtn.attr("disabled", "disabled");
				 var sleFlag = false;
				 $(".selectPerson").each(function(i,n){
					 if($(n).attr("dataStart")=="true" && $(n).parents("table").find(".selectPersonTd").html()==""){
						 sleFlag = true;
					 }
				 });
				 if(sleFlag){
					 submitBtn.attr('disabled',false);
					 submitBtn.removeAttr("disabled");
					 self.dialog_tip("请选择责任人！");
					 return;
				 }
				 if(self.postIsEmpty){
					 submitBtn.attr('disabled',false);
					 submitBtn.removeAttr("disabled");
					 self.dialog_tip("岗位为空！");
					 return;
				 }
				 if(self.approverIsEmpty){
					 submitBtn.attr('disabled',false);
					 submitBtn.removeAttr("disabled");
					 self.dialog_tip("审批人为空！");
					 return;
				 }
				 if(self.lastNodeIsEmpty){
					 submitBtn.attr('disabled',false);
					 submitBtn.removeAttr("disabled");
					 self.dialog_tip("流程没有结束节点，请检查！");
					 return;
				 }
			 }
			 self.loadingShowStyle("数据提交中...");
			 resultData.name = self.settitle;
			 var userNote = window.sessionStorage.getItem('remark') ? window.sessionStorage.getItem('remark'):"";
			 resultData.userNote = userNote;
			 resultData.customFormId = self.customFormId;
			 self.transferData();
			 $.ajax({
				 type: "post",//提交数据的类型 POST GET
				 url: self.hostUrl + "flow/instance/saveAllInstanceData",
				 timeout: 60000,
				 //async:false,
				 data : JSON.stringify(resultData),
				 contentType: 'application/json',
				 dataType: "json",//返回数据的格式

				 complete: function () {//请求完成后回调函数 (请求成功或失败之后均调用)。
					 self.loadingHideStyle();
				 },
				 //成功返回之后调用的函数
				 success: function (data) {
					//
					 if (data) {
						 console.log(data);
						 var successFlag = data.success;
						 if (successFlag) {
							 var instanceId = data.result.instanceId;
							 //startPage.dialog_tip("提交成功");

							 var oUrl= "approve_detail.html"
								 + "?instanceId=" + instanceId
								 + "&businessId=" + startPage.businessId
								 + "&appId=" + startPage.appId
								 + "&appCode=''&time=" + new Date().getTime();
							 oUrl=encodeURI(oUrl);
							 if(flag){
								 window.parent.location.href=oUrl;
							 }else{
								 window.location.href=oUrl;
							 }

						 } else {
							 alert("提交接口异常");
						 }
					 }
				 },error:function(XMLHttpRequest, textStatus, errorThrown){
					 alert(textStatus+" 提交发生错误  "+errorThrown);
				 }
			 });
		 },
		/**
		 * 转换数据格式
		 */
		 transferData:function(){
			//转换业务变量数据格式
			var data = resultData.variableDtoList;
			var paramArray = new Array();
			for (x in data){
				var item = {};
				item.name = x;
				item.val = data[x]+"";
				paramArray.push(item);
			};
			resultData.variableDtoList = paramArray;
			delete resultData.approvalLists;
			delete resultData.flowTitle;

			window.pcUrl = resultData.pcUrl;
			delete resultData.pcUrl;
			delete resultData.returnSessionId;
			delete resultData.returnUserId;
	},
		/**
		 * 获取uuid
		 * @returns {*}
		 */
		initUUID:function () {
			var guuid;
			var url = this.hostUrl+'generator/getGuuid?time='+Math.random();
			$.ajax({
				type : 'get',
				async:false,
				url : url,
				success : function(data) {
					guuid = data.result;
					try {
						$('#attachmentUpload').xljAttachment({
							appId: startPage.appId,//itemObj.appId
							businessId: guuid,//itemObj.businessId
							categoryId: guuid,//itemObj.categoryId
							mode: 'add',
							singleUpload: false,
							customApp:true
						});
					} catch (e) {
						console.warn('附件组件初始化失败');
					}
				}
			});
			return guuid;
		},
		getUrlParam:function(name){
			var reg = new RegExp("(^|&)"+ name + "=([^&]*)(&|$)");
			var r = decodeURI(window.location.search).substr(1).match(reg);
			if (r!=null ){
				return unescape(r[2]);
			}
			return null;
		},
		/**
		 * 页面初始化
		 */
		pageInit:function(){
			this.businessId = this.getUrlParam("businessId");
			this.customFormId = this.getUrlParam("customFormId");
			var titleName = window.sessionStorage.getItem('titleName') ? window.sessionStorage.getItem('titleName'):"表单发起";
			$("title").html(titleName);
			this.businessObjectCode = this.getUrlParam("businessObjectCode");
			this.getCustomData();
			this.bind_event();
		}
	};
	startPage.pageInit();
	window["startPage"]= startPage;
})()