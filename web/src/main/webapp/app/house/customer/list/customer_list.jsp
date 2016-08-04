<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html lang="en">
<head>
    <title>客户管理</title>
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
<div class="main condition-row-2" ng-app="house.customer.list" ng-controller="Ctrl">
    <div class="dn">
        <c:if test='${sessionScope.get("POSTION_MANAGER") eq true}'>
            <input type="hidden" id="isManager" value="true"/>
        </c:if>
    </div>
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
                                <label>年龄段:</label>
                            </div>
                            <select class="w150" ng-model="condition.age"
                                    ng-options="foo.value as foo.name for foo in age" ng-change="query();"></select>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>状态:</label>
                            </div>
                            <select class="w150" ng-model="condition.status"
                                    ng-options="foo.value as foo.name for foo in status" ng-change="query();"></select>
                        </div>
                        <div class="item w240">
                            <div class="form-label w80">
                                <label>客户类型:</label>
                            </div>
                            <select class="w150" ng-model="condition.type"
                                    ng-options="foo.value as foo.name for foo in type"
                                    ng-change="query();"></select>
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
                    <span>客户列表</span>
                </div>
                <span class="header-button">
                    <a type="button" class="btn btn-green btn-min" ng-click="add();">
                        <span class="glyphicons plus"></span> 新建
                    </a>
                    <c:if test='${sessionScope.get("CUSTOMER_DATA_EXPORT") eq true}'>
                        <a type="button" class="btn btn-green btn-min" ng-click="exportData();"
                           ng-disabled="!beans.total">
                            <span class="glyphicons plus"></span> 导出数据
                        </a>
                    </c:if>
                    <c:if test='${sessionScope.get("CUSTOMER_DELETE") eq true}'>
                        <a type="button" class="btn btn-green btn-min" ng-click="remove();"
                           ng-disabled="!anyone">
                            <span class="glyphicons plus"></span> 删除客户
                        </a>
                    </c:if>
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
                                <td>类型</td>
                                <td>性别</td>
                                <td>年龄段</td>
                                <td>名下房产</td>
                                <td>在租房屋</td>
                                <td style="width: 120px;">录入时间</td>
                                <td>婚姻状况</td>
                                <td>学历</td>
                                <td style="width: 120px;">状态</td>
                                <td>操作</td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <tr ng-show="!beans || !beans.total">
                                <td colspan="13" class="text-center">没有查询到数据！</td>
                            </tr>
                            <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                <td><input type="checkbox" ng-model="foo.isSelected"/></td>
                                <td title="点击查询明细！" style="cursor: pointer;">
                                    <a ng-click="view(foo.id)" bo-text="foo.code"></a>
                                </td>
                                <td bo-text="foo.name"></td>
                                <td bo-text="foo.typeName"></td>
                                <td bo-text="foo.sexName"></td>
                                <td bo-text="foo.ageName"></td>
                                <td style="white-space: normal">
                                    <a ng-click="viewRoom(key)" ng-repeat="key in foo.roomKeys.split(',')"
                                       style="margin-left:8px;cursor: pointer;">{{key}}</a>
                                </td>
                                <td style="white-space: normal">
                                    <a ng-click="viewRoom(key)" ng-repeat="key in foo.rentKeys.split(',')"
                                       style="margin-left:8px;cursor: pointer;">{{key}}</a>
                                </td>
                                <td bo-text="foo.createdDatetime|eccrmDatetime"></td>
                                <td bo-text="foo.marriageName"></td>
                                <td bo-text="foo.educationName"></td>
                                <td bo-text="foo.statusName"></td>
                                <td class="text-left">
                                    <a class="btn-op blue" ng-click="modify(foo);"
                                       ng-if="foo.status!=='APPLY_ADD'&& foo.status!=='APPLY_INVALID'">跟进</a>
                                    <a class="btn-op yellow" ng-click="applyAdd(foo.id);"
                                       ng-if="foo.status=='INVALID_ADD'||foo.status=='INACTIVE'">新增申请</a>
                                    <a class="btn-op red" ng-click="remove(foo.id);"
                                       ng-if="foo.status=='INVALID_ADD'">删除</a>
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
<script type="text/javascript" src="<%=contextPath%>/app/house/customer/list/customer_list.js"></script>
</html>