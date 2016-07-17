<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html lang="en">
<head>
    <title>楼栋管理</title>
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
<div class="main" ng-app="house.block.list" ng-controller="Ctrl">
    <div class="dn">
        <input type="hidden" id="buildingId" value="${param.id}"/>
    </div>
    <div class="block">
        <div class="block-header">
            <div class="header-button">
                <a type="button" class="btn btn-green btn-min" ng-click="query();">
                    <span class="glyphicons message_forward"></span> 刷新
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
                                <td style="width: 40px;">序号</td>
                                <td>楼栋号</td>
                                <td>单元数</td>
                                <td>已创建单元数</td>
                                <td>物理层高</td>
                                <td>标号层高</td>
                                <td>操作</td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <c:if test="${param.pageType ne 'detail'}">
                                <tr ng-show="!beans.length">
                                    <td colspan="7" class="text-center"><a class="btn-op blue" ng-click="add();">新建</a>
                                    </td>
                                </tr>
                                <tr ng-repeat="foo in beans" ng-cloak>
                                    <td>{{$index+1}}</td>
                                    <td title="点击查询明细！" style="cursor: pointer;">
                                        <input type="text" ng-model="foo.code" validate validate-required
                                               ng-change="check(foo)"/>
                                    </td>
                                    <td>
                                        <input type="text" ng-model="foo.unitCounts" validate validate-required
                                               validate-int ng-change="check(foo)"/>
                                    </td>
                                    <td>{{foo.realCounts||0}}</td>
                                    <td>
                                        <input type="text" ng-model="foo.physicalLevels" validate validate-required
                                               validate-int ng-change="check(foo)"/>
                                    </td>
                                    <td>
                                        <input type="text" ng-model="foo.levels" validate validate-required validate-int
                                               ng-change="check(foo)"/>
                                    </td>
                                    <td class="text-left">
                                        <a class="btn-op green" ng-click="add(foo);">复制</a>
                                        <a class="btn-op blue" ng-disabled="!foo.$valid" ng-click="save(foo);"
                                           ng-if="!foo.id">保存</a>
                                        <a class="btn-op blue" ng-disabled="!foo.$valid" ng-click="update(foo);"
                                           ng-if="foo.id && !foo.realCounts">更新</a>
                                        <a class="btn-op yellow" ng-click="createUnit(foo.id);" ng-if="!foo.realCounts">自动创建单元</a>
                                        <a class="btn-op red" ng-click="clearUnit(foo.id);" ng-if="foo.realCounts>0">清除所有单元</a>
                                        <a class="btn-op red" ng-click="remove(foo.id,$index);" ng-if="!foo.realCounts">删除</a>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${param.pageType eq 'detail'}">
                                <tr ng-show="!beans.length">
                                    <td colspan="7" class="text-center">无楼栋信息!</td>
                                </tr>
                                <tr bindonce ng-repeat="foo in beans" ng-cloak>
                                    <td bo-text="$index+1"></td>
                                    <td bo-text="foo.code"></td>
                                    <td bo-text="foo.unitCounts"></td>
                                    <td bo-text="foo.realCounts"></td>
                                    <td bo-text="foo.physicalLevels"></td>
                                    <td bo-text="foo.levelsLevels"></td>
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
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/block/block.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/block/list/block_list.js"></script>
</html>