<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html lang="en">
<head>
    <title>楼盘管理</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
</head>
<body>
<div class="main condition-row-1" ng-app="house.building.list" ng-controller="Ctrl">
    <div class="list-condition">
        <div class="block">
            <div class="block-header">
                <span class="header-text">
                    <span class="glyphicons search"></span>
                </span>
                <span class="header-button">
                        <a type="button" class="btn btn-green btn-min" ng-click="query();">
                                <span class="glyphicons search"></span>
                                查询
                        </a>
                </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>楼盘名称:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="condition.name"/>
                        <div class="form-label col-1-half">
                            <label>开发商:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="condition.developerName"/>
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
                    <span>楼盘管理</span>
                </div>
                <span class="header-button">
                        <a type="button" class="btn btn-green btn-min" ng-click="add();">
                            <span class="glyphicons plus"></span> 新建
                        </a>
                        <a type="button" class="btn btn-green btn-min" ng-click="remove();">
                            <span class="glyphicons plus"></span> 删除
                        </a>
                </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table">
                        <table class="table table-striped table-hover">
                            <thead class="table-header">
                            <tr>
                                <td class="width-min">
                                    <div select-all-checkbox checkboxes="beans.data" selected-items="items"
                                         anyone-selected="anyone"></div>
                                </td>
                                <td>楼盘名称</td>
                                <td>城市</td>
                                <td>行政区域</td>
                                <td>楼盘地址</td>
                                <td>建筑年代</td>
                                <td>建筑类型</td>
                                <td>房屋权属</td>
                                <td>房屋用途</td>
                                <td>供暖方式</td>
                                <td>开发商</td>
                                <td>楼栋总数</td>
                                <td>总户数</td>
                                <td>车位总数</td>
                                <td>绿化率</td>
                                <td>操作</td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <tr ng-show="!beans || !beans.total">
                                <td colspan="16" class="text-center">没有查询到数据！</td>
                            </tr>
                            <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                <td><input type="checkbox" ng-model="foo.isSelected"/></td>
                                <td title="点击查询明细！" style="cursor: pointer;">
                                    <a ng-click="view(foo.id)" bo-text="foo.name"></a>
                                </td>
                                <td bo-text="foo.cityName"></td>
                                <td bo-text="foo.areaName"></td>
                                <td bo-text="foo.address"></td>
                                <td bo-text="foo.year"></td>
                                <td bo-text="foo.typeName"></td>
                                <td bo-text="foo.houseTypeName"></td>
                                <td bo-text="foo.usagesName"></td>
                                <td bo-text="foo.warmTypeName"></td>
                                <td bo-text="foo.developerName"></td>
                                <td bo-text="foo.buildingCounts"></td>
                                <td bo-text="foo.houseCounts"></td>
                                <td bo-text="foo.carCounts"></td>
                                <td bo-text="foo.greenPercent"></td>
                                <td>
                                    <a class="btn-op blue" ng-click="modify(foo.id);">编辑</a>
                                    <a class="btn-op red" ng-click="remove(foo.id);">删除</a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="list-pagination" eccrm-page="pager"></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/building/building.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/building/list/building_list.js"></script>
</html>