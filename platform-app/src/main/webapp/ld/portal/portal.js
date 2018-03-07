$(function () {
    function init(){
        //新闻加载
        initjtxwNews();//集团新闻
        initjtggNews();//集团公告
        initfzgsggNews();//分子公司公告
        initlc();//流程
        inithyjy();//会议纪要
    }
    //初始化页面
    init();
    //加载集团新闻
    function initjtxwNews () {
        var dataP = {newsType:"0",start:0,limit:10};//集团动态
        //获取新闻的访问路径
        var url = $('#jtxwdiv').attr("data-contenturl");
        $.ajax({
            url: url+'?time='+Math.random(),
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(dataP),
            success: function(resultData) {
                if (resultData && resultData.success) {
                    $('#jtxwul').find('li').remove();
                    $.each(resultData.result, function (m, item) {
                        /*<li>
                         <a target="_blank" id="1" data-href="content/contentRowType/contentRowType_staticPage.html?from=portal&amp;id=6f1b533b38e8482bbbfbba88e9e59668" title="鑫苑集团信字〔2018〕2号 关于深入应用巨洲云CC的通知" href="/platform-app/content/contentRowType/contentRowType_staticPage.html?from=portal&amp;id=6f1b533b38e8482bbbfbba88e9e59668" style="display: inline-block;"><i></i>鑫苑集团信字〔2018〕2号 关于深入应用巨洲云CC的通知</a>
                         <span class="date">2018-01-25</span>
                         </li>*/
                        var li = $("<li>");
                        var a = $('<a target="_blank" title="' + item.name + '" href="' + item.url + '" style="display: inline-block;"><i></i>' + item.name + '</a>');
                        var span = $('<span class="date">' + item.startDate + '</span>');
                        li.append(a);
                        li.append(span);
                        $('#jtxwul').append(li);
                    });
                }else{
                    $.xljUtils.tip("red",resultData.msg);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                $.xljUtils.tip("red","服务异常,请联系管理员！");
            }
        });
    }
    //加载集团公告
    function initjtggNews () {
        var dataP = {newsType:"1",start:0,limit:10};//集团公告
        //获取新闻的访问路径
        var url = $('#jtggdiv').attr("data-contenturl");
        $.ajax({
            url: url+'?time='+Math.random(),
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(dataP),
            success: function(resultData) {
                if (resultData && resultData.success) {
                    $('#jtggul').find('li').remove();
                    $.each(resultData.result, function (m, item) {
                        /*<li>
                         <a target="_blank" id="1" data-href="content/contentRowType/contentRowType_staticPage.html?from=portal&amp;id=6f1b533b38e8482bbbfbba88e9e59668" title="鑫苑集团信字〔2018〕2号 关于深入应用巨洲云CC的通知" href="/platform-app/content/contentRowType/contentRowType_staticPage.html?from=portal&amp;id=6f1b533b38e8482bbbfbba88e9e59668" style="display: inline-block;"><i></i>鑫苑集团信字〔2018〕2号 关于深入应用巨洲云CC的通知</a>
                         <span class="date">2018-01-25</span>
                         </li>*/
                        var li = $("<li>");
                        var a = $('<a target="_blank" title="' + item.name + '" href="' + item.url + '" style="display: inline-block;"><i></i>' + item.name + '</a>');
                        var span = $('<span class="date">' + item.startDate + '</span>');
                        li.append(a);
                        li.append(span);
                        $('#jtggul').append(li);
                    });
                }else{
                    $.xljUtils.tip("red",resultData.msg);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                $.xljUtils.tip("red","服务异常,请联系管理员！");
            }
        });
    }
    //加载分子公司公告
    function initfzgsggNews () {
        var dataP = {newsType:"2",start:0,limit:10};//分子公司公告
        //获取新闻的访问路径
        var url = $('#fzgsggdiv').attr("data-contenturl");
        $.ajax({
            url: url+'?time='+Math.random(),
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(dataP),
            success: function(resultData) {
                if (resultData && resultData.success) {
                    $('#fzgsggul').find('li').remove();
                    $.each(resultData.result, function (m, item) {
                        /*<li>
                         <a target="_blank" id="1" data-href="content/contentRowType/contentRowType_staticPage.html?from=portal&amp;id=6f1b533b38e8482bbbfbba88e9e59668" title="鑫苑集团信字〔2018〕2号 关于深入应用巨洲云CC的通知" href="/platform-app/content/contentRowType/contentRowType_staticPage.html?from=portal&amp;id=6f1b533b38e8482bbbfbba88e9e59668" style="display: inline-block;"><i></i>鑫苑集团信字〔2018〕2号 关于深入应用巨洲云CC的通知</a>
                         <span class="date">2018-01-25</span>
                         </li>*/
                        var li = $("<li>");
                        var a = $('<a target="_blank" title="' + item.name + '" href="' + item.url + '" style="display: inline-block;"><i></i>' + item.name + '</a>');
                        var span = $('<span class="date">' + item.startDate + '</span>');
                        li.append(a);
                        li.append(span);
                        $('#fzgsggul').append(li);
                    });
                }else{
                    $.xljUtils.tip("red",resultData.msg);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                $.xljUtils.tip("red","服务异常,请联系管理员！");
            }
        });
    }
    //加载会议纪要
    function inithyjy () {
        var dataP = {start:0,limit:10};//会议纪要
        //获取新闻的访问路径
        var url = $('#hyjydiv').attr("data-contenturl");
        $.ajax({
            url: url+'?time='+Math.random(),
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(dataP),
            success: function(resultData) {
                if (resultData && resultData.success) {
                    $('#hyjyul').find('li').remove();
                    $.each(resultData.result, function (m, item) {
                        var li = $("<li>");
                        var a = $('<a target="_blank" title="' + item.docSubject + '" href="' + item.url + '" style="display: inline-block;"><i></i>' + item.docSubject + '</a>');
                        /*var span = $('<span class="date">' + item.fdPlanHoldTime + '</span>');*/
                        li.append(a);
                       /* li.append(span);*/
                        $('#hyjyul').append(li);
                    });
                }else{
                    $.xljUtils.tip("red",resultData.msg);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                $.xljUtils.tip("red","服务异常,请联系管理员！");
            }
        });
    }
    //加载流程
    function initlc () {
        var dataP = {userId:"e8aa6c00ee1a42358fd2de9fc91ed2ac",start:0,limit:10};
        //获取新闻的访问路径
        var url = $('#lcdiv').attr("data-contenturl");
        $.ajax({
            url: url+'?time='+Math.random(),
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(dataP),
            success: function(resultData) {
                if (resultData && resultData.success) {
                    $('#lcul').find('li').remove();
                    $.each(resultData.result, function (m, item) {
                        /*<li>
                         <a class="t_content" href="http://oa.xyre.com/platform-app/content/contentRowType/contentRowType_list.html?contentTypeId=be39075aed7f4d3bbd86dfad5f6edcd0&amp;contentType=NEWS&amp;newOpenWin=true&amp;btnMenuCode=XYJGG" title="请审批:【部门计划汇报】平台部（平台、OA、IM）/OA/平台问题处理-张方志-移动端校验发起：根据加锁状态-部门计划汇报" style="display: inline; word-wrap: break-word;">请审批:【部门计划汇报】平台部（平台、OA、IM）/OA/平台问题处理-张方志-移动端校验发起：根据加锁状态-部门计划汇报</a>
                         </li>*/
                        var li = $("<li>");
                        var a = $('<a target="t_content" title="' + item.name + '" href="' + item.url + '" style="display: inline; word-wrap: break-word;"><i></i>' + item.name + '</a>');
                        li.append(a);
                        $('#lcul').append(li);
                    });
                } else {
                    $.xljUtils.tip("red",resultData.msg);
                }
                $('.content-list').niceScroll().resize();
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                pop_tip_open("red","服务异常,请联系管理员！");
            }
        });
    }
});