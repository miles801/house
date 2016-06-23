<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html lang="en">
<head>
    <title>房间管理</title>
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
<div class="main condition-row-1" ng-app="house.room.list" ng-controller="Ctrl">
    <div class="dn">
        <input type="hidden" id="buildingId" value="${param.id}"/>
    </div>
    <div class="list-condition">
        <div class="block">
            <div class="block-header">
                <span class="header-text">
                    <span class="glyphicons search"></span>
                </span>
                <span class="header-button">
                        <a type="button" class="btn btn-green btn-min"
                           ng-disabled="!(condition.blockId && condition.unitId)" ng-click="query();"
                           ng-cloak>
                                <span class="glyphicons search"></span>
                                查询
                        </a>
                </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="row" style="margin-top:0;">
                        <div class="row">
                            <div class="form-label col-1-half">
                                <label>楼栋:</label>
                            </div>
                            <select ng-model="condition.blockId" class="col-2-half"
                                    ng-options="o.id as o.code for o in blocks" ng-change="blockChange();"></select>
                            <div class="form-label col-1-half">
                                <label>单元:</label>
                            </div>
                            <select ng-model="condition.unitId" class="col-2-half"
                                    ng-options="o.id as o.code+' -- '+o.doorCode for o in units"
                                    ng-change="unitChange();"></select>
                        </div>
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
                    <span>房间管理</span>
                </div>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table">
                        <form name="form" role="form" style="margin: 0;padding: 0;">
                            <table class="table table-striped table-hover">
                                <thead class="table-header">
                                <tr>
                                    <td>楼栋</td>
                                    <td>单元</td>
                                    <td>楼层</td>
                                    <td>门牌号</td>
                                    <td>面积</td>
                                    <td>户型(室-厅-厨-卫)</td>
                                    <td>朝向</td>
                                    <td>操作</td>
                                </tr>
                                </thead>
                                <tbody class="table-body">
                                <tr ng-show="!beans.length">
                                    <td colspan="8" class="text-center">
                                        <a class="btn-op blue"
                                           ng-click="!(condition.blockId && condition.unitId) || add();"
                                           ng-disabled="!(condition.blockId && condition.unitId)" ng-cloak>新建</a>
                                    </td>
                                </tr>
                                <tr bindonce ng-repeat="foo in beans" ng-cloak>
                                    <td bo-text="foo.blockCode"></td>
                                    <td bo-text="foo.unitCode"></td>
                                    <td>
                                        <input type="text" ng-model="foo.floor" validate validate-required
                                               validate-int/>
                                    </td>
                                    <td>
                                        <input type="text" name="code" ng-model="foo.code" validate validate-required/>
                                    </td>
                                    <td>
                                        <input type="text" ng-model="foo.square" validate validate-required
                                               validate-float/>
                                    </td>
                                    <td>
                                        <input type="text" ng-model="foo.type1" style="width: 50px;" validate
                                               validate-required validate-int
                                               placeholder="室"/>
                                        <input type="text" ng-model="foo.type2" style="width: 50px;" validate
                                               validate-required validate-int
                                               placeholder="厅"/>
                                        <input type="text" ng-model="foo.type3" style="width: 50px;" validate
                                               validate-required validate-int
                                               placeholder="厨"/>
                                        <input type="text" ng-model="foo.type4" style="width: 50px;" validate
                                               validate-required validate-int
                                               placeholder="卫"/>
                                    </td>
                                    <td>
                                        <select ng-model="foo.orient"
                                                ng-options="o.value as o.name for o in orients"></select>
                                    </td>
                                    <td>
                                        <a class="btn-op blue" ng-disabled="!(form.$valid)"
                                           ng-click="save(foo,form);">保存</a>
                                        <a class="btn-op red" ng-click="remove(foo.id,$index);">删除</a>
                                        <a class="btn-op green" ng-click="add(foo);">新建</a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/room.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/block/block.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/unit/unit.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/list/room_list.js"></script>
</html>