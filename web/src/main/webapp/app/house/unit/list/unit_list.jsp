<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html lang="en">
<head>
    <title>单元管理</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
    <style>
        td input {
            text-align: center;
            padding: 0 8px;
        }
    </style>
</head>
<body>
<div class="main condition-row-1" ng-app="house.unit.list" ng-controller="Ctrl">
    <div class="dn">
        <input type="hidden" id="buildingId" value="${param.id}">
    </div>
    <div class="list-condition">
        <div class="block">
            <div class="block-header">
                <span class="header-text">
                    <span class="glyphicons search"></span>
                </span>
                <span class="header-button">
                        <a type="button" class="btn btn-green btn-min" ng-click="query();"
                           ng-disabled="!condition.blockId" ng-cloak>
                                <span class="glyphicons search"></span>
                                查询
                        </a>
                </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>楼栋:</label>
                        </div>
                        <select ng-model="condition.blockId" class="col-2-half"
                                ng-options="o.id as o.code for o in blocks" ng-change="blockChange();"></select>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="list-result ">
        <div class="block">
            <div class="block-header">
                <div class="header-text">
                    <span class="glyphicons list"></span>
                    <span ng-cloak>单元管理 -- {{blockCode}}</span>
                </div>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table">
                        <table class="table table-striped table-hover">
                            <thead class="table-header">
                            <tr>
                                <td>单元号</td>
                                <td>门牌号</td>
                                <td>面积(m2)</td>
                                <td>户型</td>
                                <td>朝向</td>
                                <td style="width: 180px;">操作</td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <tr ng-show="!beans.length">
                                <td colspan="6" class="text-center">
                                    <a class="btn-op blue" ng-click="!condition.blockId || add();"
                                       ng-disabled="!condition.blockId" ng-cloak>新建</a>
                                </td>
                            </tr>
                            <tr ng-repeat="foo in beans" ng-cloak>
                                <td title="点击查询明细！" style="cursor: pointer;">
                                    <form name="form" role="form" style="margin: 0;padding: 0;">
                                        <input type="text" name="code" ng-model="foo.code" validate validate-required/>
                                    </form>
                                </td>
                                <td>
                                    <input type="text" ng-model="foo.doorCode" validate validate-required/>
                                </td>
                                <td>
                                    <input type="text" ng-model="foo.square" validate validate-required
                                           validate-float/>
                                </td>
                                <td>
                                    <input type="text" ng-model="foo.type" validate validate-required/>
                                </td>
                                <td>
                                    <select ng-model="foo.orient"
                                            ng-options="o.value as o.name for o in orients"></select>
                                </td>
                                <td>
                                    <a class="btn-op blue" ng-disabled="!(form.$valid && form.$dirty)"
                                       ng-click="save(foo,form);">保存</a>
                                    <a class="btn-op red" ng-click="remove(foo.id,$index);">删除</a>
                                    <a class="btn-op green" ng-click="add(foo);">新建</a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/unit/unit.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/block/block.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/unit/list/unit_list.js"></script>
</html>