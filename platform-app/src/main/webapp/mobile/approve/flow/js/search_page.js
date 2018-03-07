/**
 * Created by Administrator on 2017/12/12.
 */

(function(){

    var searchPage = {
        baseUrl : '/',
        hostUrl : '/platform-app/',
        search:window.location.search,
        isadd:"0",
        /**
         * 绑定事件
         */
        bind_event:function() {//事件绑定
            //$(".search-box input").on("change", function (e) {
            //
            //});
            //确定
            $(".search-head .cancel").on("click", function (e) {
                //$(".search-head input").val("");//设置位空
                ////跳转---todo
                //window.location.href="start_home.html";
                var val = $(this).prev().find("input").val();
                searchPage.changeValFn(val);
                e.stopPropagation();
            });
            //清空搜索记录
            $(".searchRecentBox .close-span").on("click", function (e) {
                //请求后台
                var self = searchPage;
                menuPage._ajax(function(data){
                    $("#recentBoxUl").empty();
                    $(".searchRecentBox").hide();
                    $(".empty-box").show();
                },"",self.hostUrl+"/sys/quick/entry/delRecentSearchContent");
                e.stopPropagation();
            });

             //删除搜索
             $(document).on("click",".searchSpan",function(e){
                console.log($(this).prev().val());
                $(this).prev().val("");
            });

            //按菜单查询
            $("body").delegate(".menu-wrap .menu li", "click", function(event){
                if($(this).hasClass("all")){
                    $("#search_list").show();
                    $("#search_menu_list").hide();
                    $("#search_menu_list").children().hide();
                }else{
                    $("#search_list").hide();
                    $("#search_menu_list").show();
                    $("#search_menu_list").children().hide();
                    $("#search_menu_list").children().eq($(this).index()-1).show();
                }
                event.stopPropagation();
            });
            //最近搜索点击某一项
            $("body").delegate("#recentBoxUl li", "click", function(){
                var val = $(this).find(".title").text();
                $(".search-box input").val(val);
                searchPage.changeValFn(val);
            });
            //折叠展开
            $("body").delegate(".icon-my", "click", function(event){
                $(this).find(".arrow-box").toggleClass("down");
                $(this).next().toggle();
            });

        },
        changeValFn:function(val){
            var self = searchPage;
            //ajax请求
            if($.trim(val)){
                //clearTimeout($(this).data('timer'));
                //$(this).data('timer', setTimeout(function() {
                    var dataP = '{"name":"'+val+'","isMobileAllowed":"1"}';
                    var content = '{"content":"'+val+'"}';
                    menuPage._ajax(function(data){
                        console.log(data);
                        $(".searchRecentBox,.empty-box").hide();
                        $("#search_list").show();
                        self.renderSearchList(data);
                        //存放搜索记录
                        menuPage._ajax(function(data){
                        },content,self.hostUrl+"/sys/quick/entry/setRecentSearchContent");
                    },dataP);
               // }, 1000));
            }
        },
        //获取搜索到的数据列表
        renderSearchList:function(data){
            var res = data.result;
            $("#searchListData").empty();
            if(res.length<1) {
                $(".menu-wrap").hide();
                $(".empty-box").show();
                $("#search_list").hide();
                return;
            }
            $(".menu-wrap").show();
            var $ul = $(".menu");
            var $search_menu_list = $("#search_menu_list");
            $search_menu_list.empty();
            $ul.empty();
            var all = $(`<li class="current all">全部</li>`);
            $ul.append(all);
            res.map(function(r) {
                var li = $(`<li>${r.name}</li>`);
                li.data(li);
                $ul.append(li);
                var $html = $(`<div class="list-sort-show" style="display: none">
                    <ul class="list-sort-ul list-pad-left" id="menu_con">

                    </ul>
                    </div>`);

                r['children'].map(function(child) {
                    var item = $(`<li>
                        <h2 class="icon-my icon-my-none">
                            <span class="title">${r.name}/<span id="childName"></span></span>
                        </h2>
                        <ul id="level2Ul">
                        </ul>
                        </li>`);
                    item.data(item);
                    $(item).find("#childName").text(child.name);
                    var $htmlLi = $(`<li>
                        <h2 class="icon-my icon-my-none">
                            <span class="title">${child.name}</span>
                        </h2>
                        <ul id="htmlLevel2Ul">
                        </ul>
                        </li>`);
                    $htmlLi.data($htmlLi);
                    child['children'].map(function(l) {
                        var li = $(`
                                <li>
                                <h2 class="icon-my icon-my-s2" data-mobileUrl=${l.mobileUrl} data-isInner=${l.isInner} data-id=${l.id} data-url = ${l.url}>
                                    <span class="selected-border" style="display: none"></span>
                                    <span class="title">${l.name}</span>
                                </h2>
                            </li>
                            `);
                        $(item).find("#level2Ul").append($(li));
                        $($htmlLi).find("#htmlLevel2Ul").html($(item).find("#level2Ul").html());
                    });
                    $("#searchListData").append(item);
                    $html.find("#menu_con").append($htmlLi);
                });

                $search_menu_list.append($html);

            });

            menuPage.MenuEvent();
            menuPage.myIconEvent();
            menuPage._addStyle($ul);
        },
        getUrlParam:function(name){
            var reg = new RegExp("(^|&)"+ name + "=([^&]*)(&|$)");
            var r = decodeURI(this.search).substr(1).match(reg);
            if (r!=null ){
                return unescape(r[2]);
            }
            return null;
        },
        
        //获取最近搜索的记录
        getRecentData:function(){
            var url = this.hostUrl+"sys/quick/entry/getRecentSearchContent";
            menuPage._ajax(function(data){
                //处理函数
                var result = data.result;
                if(result.length<1){//搜索记录为空
                    $(".empty-box").show();
                    $(".searchRecentBox").hide();
                    return;
                }
                $("#recentBoxUl").empty();
                for(var i in result){
                    var item = $(`<li>
                        <h2 class="icon-my icon-my-s2">
                            <span class="title">${JSON.parse(result[i]).content}</span>
                        </h2>
                    </li>`);
                    $("#recentBoxUl").append(item);
                }
            },"",url);
        },
        /**
         * 页面初始化
         */
        pageInit:function(){
            this.isadd = this.getUrlParam("isadd");
            this.getRecentData();
            this.bind_event();
        }
    };
    searchPage.pageInit();
    window["searchPage"] = searchPage;

})();