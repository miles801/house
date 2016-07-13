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
</head>
<body>
<div class="main" ng-app="house.room.edit" ng-controller="Ctrl">
    <div class="block">
        <div class="block-header">
                <span class="header-text">
                    <span class="glyphicons info-sign"></span>
                </span>
                <span class="header-button">
                    <button type="button" class="btn btn-green btn-min" ng-click="update()"
                            ng-disabled="form.$invalid">
                            <span class="glyphicons claw_hammer"></span> 更新
                        </button>
                    <a type="button" class="btn btn-green btn-min" ng-click="back();">
                        <span class="glyphicons message_forward"></span> 返回
                    </a>
                </span>
        </div>
        <div class="block-content">
            <div class="content-wrap">
                <form name="form" class="form-horizontal" role="form" style="width: 600px;margin: 0 auto;">
                    <div style="display: none;">
                        <input type="hidden" id="pageType" value="${pageType}"/>
                        <input type="hidden" id="id" value="${id}"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-4">
                            <label validate-error="form.square">面积:</label>
                        </div>
                        <input class="col-7" type="text" ng-model="beans.square"
                               validate validate-float name="square" placeholder="m2"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-4">
                            <label validate-error="form.square">户型（室-厅-厨-卫）:</label>
                        </div>
                        <div class="col-7">
                            <div class="col-3">
                                <input class="col-12" type="text" ng-model="beans.type1" validate
                                       validate-int placeholder="室"/>
                            </div>
                            <div class="col-3" style="padding-left: 8px;">
                                <input class="col-12" type="text" ng-model="beans.type2" validate
                                       validate-int placeholder="厅"/>
                            </div>
                            <div class="col-3" style="padding-left: 8px;">
                                <input class="col-12" type="text" ng-model="beans.type3" validate
                                       validate-int placeholder="厨"/>
                            </div>
                            <div class="col-3" style="padding-left: 8px;">
                                <input class="col-12" type="text" ng-model="beans.type4" validate
                                       validate-int placeholder="卫"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-label col-4">
                            <label>朝向:</label>
                        </div>
                        <select ng-model="beans.orient" class="col-7"
                                ng-options="o.value as o.name for o in orients"></select>
                    </div>
                    <div class="row">
                        <div class="form-label col-4">
                            <label>房屋现状:</label>
                        </div>
                        <select ng-model="beans.houseUseType" class="col-7"
                                ng-options="o.value as o.name for o in useType"></select>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/room.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/list/room_edit.js"></script>
</html>