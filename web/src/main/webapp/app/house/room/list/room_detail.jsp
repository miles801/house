<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>房屋跟进</title>
    <meta content="text/html" charset="utf-8">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript">
        window.angular.contextPathURL = "<%=contextPath%>";
    </script>
    <style>
        .item > span.w180 {
            height: 28px;
            line-height: 28px;
        }
    </style>
</head>
<body>
<div class="main" ng-app="house.room.detail" ng-controller="Ctrl">
    <div class="block">
        <div class="block-header">
                <span class="header-text" ng-cloak>
                    {{beans.roomKey}} | {{beans.buildingName}} | {{beans.blockCode}} | {{beans.unitCode}} | {{beans.code}}
                </span>
                <span class="header-button">
                    <a type="button" class="btn btn-green btn-min" ng-click="back();">
                        <span class="glyphicons message_forward"></span> 返回
                    </a>
                </span>
        </div>
        <div class="block-content">
            <div class="content-wrap">
                <form name="form" class="form-horizontal" role="form">
                    <div style="display: none;">
                        <input type="hidden" id="id" value="${param.id}"/>
                    </div>
                    <div class="row" ng-cloak>
                        <div class="form-label col-1-half">
                            <label validate-error="form.square">面积:</label>
                        </div>
                        <span class="col-2-half">{{beans.square}}</span>
                        <div class="form-label col-1-half">
                            <label validate-error="form.square">户型:</label>
                        </div>
                        <span class="col-2-half">{{beans.type1}}-{{beans.type2}}-{{beans.type3}}-{{beans.type4}}</span>
                        <div class="form-label col-1-half">
                            <label>朝向:</label>
                        </div>
                        <span class="col-2-half">{{beans.orientName}}</span>
                    </div>
                    <div class="row" ng-cloak>
                        <div class="form-label col-1-half">
                            <label>面积:</label>
                        </div>
                        <span class="col-2-half">{{beans.square}}</span>
                        <div class="form-label col-1-half">
                            <label>现状:</label>
                        </div>
                        <span class="col-2-half">{{beans.houseUseTypeName}}</span>
                        <div class="form-label col-1-half">
                            <label>录入人:</label>
                        </div>
                        <span class="col-2-half">{{beans.creatorName}}</span>
                    </div>
                    <div class="row text-center" style="margin: 10px 0;">
                        <a type="button" class="btn btn-blue" ng-click="modifyRoom(beans.id);"> 修改房屋信息 </a>
                        <a type="button" class="btn btn-blue" ng-click="addInfo(beans.id);"> 录入跟进 </a>
                        <a type="button" class="btn btn-blue" ng-click="star(beans.id);"> 添加关注 </a>
                        <a type="button" class="btn btn-blue" ng-click="viewBuyLog();"> 查看成交记录 </a>
                    </div>

                    <%-- 小区信息 --%>
                    <div class="panel panel-tab" style="margin: 0;position: relative;">
                        <ul class="nav nav-tabs" style="margin: 5px 0 0 0;width:100%;">
                            <li class="active"><i>小区信息</i></li>
                        </ul>
                        <div class="tab-content" style="height: 100%;width:100%;padding-top: 0!important;">
                            <div class="float items">
                                <div class="item w300">
                                    <div class="form-label w120"><label>学区:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>过户指导价:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>房屋权属:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>年代:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>建筑类型:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>房屋用途:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>物业费:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>供暖方式:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>距地铁:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>地址:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%-- 业主信息 --%>
                    <div class="panel panel-tab" style="margin: 15px 0;position: relative;">
                        <ul class="nav nav-tabs" style="margin: 5px 0 0 0;width:100%;">
                            <li class="active"><i>业主信息</i></li>
                        </ul>
                        <div class="tab-content" style="height: 100%;width:100%;padding-top: 0!important;">
                            <div class="float items">
                                <div class="item w300">
                                    <div class="form-label w120"><label>学区:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>过户指导价:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>房屋权属:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>年代:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>建筑类型:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>房屋用途:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>物业费:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>供暖方式:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>距地铁:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>地址:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%-- 最新动态 --%>
                    <div class="panel panel-tab" style="margin: 15px 0;position: relative;">
                        <ul class="nav nav-tabs" style="margin: 5px 0 0 0;width:100%;">
                            <li class="active"><i>最新动态</i></li>
                        </ul>
                        <div class="tab-content" style="height: 100%;width:100%;padding-top: 0!important;">
                            <div class="float items">
                                <div class="item w300">
                                    <div class="form-label w120"><label>学区:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>过户指导价:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>房屋权属:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>年代:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>建筑类型:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>房屋用途:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>物业费:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>供暖方式:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>距地铁:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>地址:</label></div>
                                    <span class="w180">{{beans.square}}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/room.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/list/room_detail.js"></script>
</html>