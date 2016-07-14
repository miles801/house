<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html lang="en">
<head>
    <title>客户审核</title>
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
<div class="main condition-row-2" ng-app="house.customer.apply" ng-controller="Ctrl">
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
                    <div class="row float">
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>编号:</label>
                            </div>
                            <input class="w150" type="text" ng-model="condition.code"/>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>姓名:</label>
                            </div>
                            <input class="w150" type="text" ng-model="condition.name"/>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>手机:</label>
                            </div>
                            <input class="w150" type="text" ng-model="condition.phone"/>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>微信:</label>
                            </div>
                            <input class="w150" type="text" ng-model="condition.wechat"/>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>职业:</label>
                            </div>
                            <input class="w150" type="text" ng-model="condition.duty"/>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>性别:</label>
                            </div>
                            <select class="w150" ng-model="condition.sex"
                                    ng-options="foo.value as foo.name for foo in sex" ng-change="query();"></select>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>婚姻状况:</label>
                            </div>
                            <select class="w150" ng-model="condition.marriage"
                                    ng-options="foo.value as foo.name for foo in marriage"
                                    ng-change="query();"></select>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>资产规模:</label>
                            </div>
                            <select class="w150" ng-model="condition.money"
                                    ng-options="foo.value as foo.name for foo in money" ng-change="query();"></select>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>年龄段:</label>
                            </div>
                            <select class="w150" ng-model="condition.age"
                                    ng-options="foo.value as foo.name for foo in age" ng-change="query();"></select>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>学历:</label>
                            </div>
                            <select class="w150" ng-model="condition.education"
                                    ng-options="foo.value as foo.name for foo in education"
                                    ng-change="query();"></select>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>状态:</label>
                            </div>
                            <select class="w150" ng-model="condition.status"
                                    ng-options="foo.value as foo.name for foo in status" ng-change="query();"></select>
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
                    <span>待审核客户列表</span>
                </div>
                <span class="header-button">
                    <a type="button" class="btn btn-green btn-min" ng-click="pass();" ng-cloak
                       ng-disabled="!anyone">
                            <span class="glyphicons plus"></span> 批量通过
                        </a>
                    <a type="button" class="btn btn-green btn-min" ng-click="deny();" ng-cloak
                       ng-disabled="!anyone">
                            <span class="glyphicons plus"></span> 批量不通过
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
                                <td>客户编号</td>
                                <td>姓名</td>
                                <td>性别</td>
                                <td>年龄段</td>
                                <td>婚姻状况</td>
                                <td>学历</td>
                                <td>名下房产</td>
                                <td>录入人</td>
                                <td>录入时间</td>
                                <td>状态</td>
                                <td>操作</td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <tr ng-show="!beans || !beans.total">
                                <td colspan="12" class="text-center">没有查询到数据！</td>
                            </tr>
                            <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                <td><input type="checkbox" ng-model="foo.isSelected"/></td>
                                <td title="点击查询明细！" style="cursor: pointer;">
                                    <a ng-click="view(foo.id)" bo-text="foo.code"></a>
                                </td>
                                <td bo-text="foo.name"></td>
                                <td bo-text="foo.sexName"></td>
                                <td bo-text="foo.ageName"></td>
                                <td bo-text="foo.marriageName"></td>
                                <td bo-text="foo.educationName"></td>
                                <td style="white-space: normal">
                                    <a ng-click="viewRoom(key)" ng-repeat="key in foo.roomKeys.split(',')"
                                       style="margin-left:8px;cursor: pointer;">{{key}}</a>
                                </td>
                                <td bo-text="foo.modifierName||foo.creatorName"></td>
                                <td bo-text="(foo.modifiedDatetime||foo.createdDatetime)|eccrmDatetime"></td>
                                <td bo-text="foo.statusName"></td>
                                <td>
                                    <a class="btn-op blue" ng-click="pass(foo.id);">通过</a>
                                    <a class="btn-op red" ng-click="deny(foo.id);">不通过</a>
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
<script type="text/javascript" src="<%=contextPath%>/app/house/customer/customer.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/customer/list/customer_apply.js"></script>
</html>