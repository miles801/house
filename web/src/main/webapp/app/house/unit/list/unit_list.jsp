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
                        <a type="button" class="btn btn-green btn-min" ng-click="reload();">
                                刷新本页面
                        </a>
                    <c:if test="${sessionScope.get('OP_UNIT_DELETE_FORCE') eq true}">
                        <a type="button" class="btn btn-green btn-min" ng-click="forceDelete();" ng-cloak
                           ng-disabled="!anyone">
                            <span class="glyphicons message_forward"></span> 强制删除
                        </a>
                    </c:if>
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
                    <span ng-cloak>单元管理</span>
                </div>
                <div class="header-button">
                    <a type="button" class="btn btn-green btn-min" ng-click="generateRoom();"
                       ng-disabled="!condition.blockId">
                        一键生成房屋信息
                    </a>
                </div>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table">
                        <form name="form" role="form" style="margin: 0;padding: 0;">
                            <table class="table table-striped table-hover">
                                <thead class="table-header">
                                <tr>
                                    <td class="width-min">
                                        <div select-all-checkbox checkboxes="beans" selected-items="items"
                                             anyone-selected="anyone"></div>
                                    </td>
                                    <td>序号</td>
                                    <td>单元号</td>
                                    <td>门牌号</td>
                                    <td>面积(m2)</td>
                                    <td>户型</td>
                                    <td>朝向</td>
                                    <td style="width: 180px;">操作</td>
                                </tr>
                                </thead>
                                <tbody class="table-body">
                                <c:if test="${param.pageType ne 'detail'}">
                                    <tr ng-show="!beans.length">
                                        <td colspan="8" class="text-center">
                                            <a class="btn-op blue" ng-click="!condition.blockId || add();"
                                               ng-disabled="!condition.blockId" ng-cloak>新建</a>
                                        </td>
                                    </tr>
                                    <tr ng-repeat="foo in beans" ng-cloak>
                                        <td><input type="checkbox" ng-model="foo.isSelected"/></td>
                                        <td>{{$index+1}}</td>
                                        <td title="点击查询明细！" style="cursor: pointer;">
                                            <input type="text" name="code" ng-model="foo.code" validate
                                                   validate-required ng-change="check(foo);"/>
                                        </td>
                                        <td>
                                            <input type="text" ng-model="foo.doorCode" validate validate-required
                                                   ng-change="check(foo);"/>
                                        </td>
                                        <td>
                                            <input type="text" ng-model="foo.square" validate validate-required
                                                   validate-float ng-change="check(foo);"/>
                                        </td>
                                        <td>
                                            <input type="text" ng-model="foo.type" validate validate-required
                                                   validate-options="typeValid" ng-change="check(foo);"/>
                                        </td>
                                        <td>
                                            <select ng-model="foo.orient"
                                                    ng-options="o.value as o.name for o in orients"
                                                    ng-change="check(foo);"></select>
                                        </td>
                                        <td class="text-left">
                                            <a class="btn-op blue" ng-disabled="!foo.$valid" ng-click="save(foo);"
                                               ng-if="!foo.id">保存</a>
                                            <a class="btn-op blue" ng-disabled="!foo.$valid" ng-click="update(foo);"
                                               ng-if="foo.id">更新</a>
                                            <a class="btn-op red" ng-click="remove(foo.id,$index);">删除</a>
                                            <a class="btn-op green" ng-click="add(foo);">复制</a>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${param.pageType eq 'detail'}">
                                    <tr ng-show="!beans.length">
                                        <td colspan="8" class="text-center">无单元数据!</td>
                                    </tr>
                                    <tr bindonce ng-repeat="foo in beans" ng-cloak>
                                        <td><input type="checkbox" ng-model="foo.isSelected"/></td>
                                        <td bo-text="$index+1"></td>
                                        <td bo-text="foo.code"></td>
                                        <td bo-text="foo.doorCode"></td>
                                        <td bo-text="foo.square"></td>
                                        <td bo-text="foo.type"></td>
                                        <td bo-text="foo.orientName"></td>
                                        <td></td>
                                    </tr>
                                </c:if>
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
<script type="text/javascript" src="<%=contextPath%>/app/house/unit/unit.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/block/block.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/unit/list/unit_list.js"></script>
</html>