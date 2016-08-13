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
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/app/base/region/region.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
</head>
<body>
<div class="main condition-row-2" ng-app="house.building.list" ng-controller="Ctrl">
    <div class="dn">
        <c:if test='${sessionScope.get("POSTION_MANAGER") eq true}'>
            <input type="hidden" id="isManager" value="true"/>
        </c:if>
        <input type="hidden" id="empId" value="${sessionScope.get('employeeId')}"/>
    </div>
    <div class="list-condition">
        <div class="block">
            <div class="block-header">
                <span class="header-text">
                    <span class="glyphicons search"></span>
                </span>
                <span class="header-button">
                        <a type="button" class="btn btn-green btn-min" ng-click="query();"> 查询 </a>
                </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="float">
                        <div class="item w200">
                            <div class="form-label w80">
                                <label>楼盘名称:</label>
                            </div>
                            <input class="w120" type="text" ng-model="condition.name"/>
                        </div>
                        <div class="item w200">
                            <div class="form-label w80">
                                <label>楼盘编号:</label>
                            </div>
                            <input class="w120" type="text" ng-model="condition.code"/>
                        </div>
                        <div class="item w200">
                            <div class="form-label w80">
                                <label>负责人电话:</label>
                            </div>
                            <input class="w120" type="text" ng-model="condition.phone" placeholder="只针对负责人"/>
                        </div>
                        <div class="item w300">
                            <div class="form-label w80">
                                <label>城市/区域:</label>
                            </div>
                            <div class="w200" style="position: relative;">
                                <input class="col-12" type="text" ng-model="city" readonly ztree-single="regionTree"
                                       placeholder="点击选择城市/区县"/>
                                <span class="add-on" style="top:3px;right: 22px;cursor:pointer;" title="清除"><i
                                        class="icons fork" ng-click="clearRegion();"></i></span>
                            </div>
                        </div>
                        <div class="item w200">
                            <div class="form-label w80">
                                <label>建筑类型:</label>
                            </div>
                            <select class="w120" ng-model="condition.type" ng-change="query();"
                                    ng-options="foo.value as foo.name for foo in types"></select>
                        </div>
                        <div class="item w200">
                            <div class="form-label w80">
                                <label>学区:</label>
                            </div>
                            <input class="w120" type="text" ng-model="condition.school"/>
                        </div>
                        <div class="item w300">
                            <div class="form-label w80">
                                <label>均价:</label>
                            </div>
                            <input class="w100" type="text" ng-model="condition.price1"/>
                            <input class="w100" type="text" ng-model="condition.price2" style="margin-left:10px;"/>
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
                    <span>楼盘管理</span>
                </div>
                <span class="header-button">
                    <c:if test="${sessionScope.get('OP_BUILDING_ADD') eq true}">
                        <a type="button" class="btn btn-green btn-min" ng-click="add();">
                            <span class="glyphicons plus"></span> 新建
                        </a>
                    </c:if>
                    <c:if test="${sessionScope.get('OP_BUILDING_DELETE') eq true}">
                            <a type="button" class="btn btn-green btn-min" ng-click="remove();" ng-disabled="!anyone"
                               ng-cloak>
                                <span class="glyphicons plus"></span> 删除
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
                                <td>楼盘名称</td>
                                <td>编号</td>
                                <td>城市</td>
                                <td>均价(元/m2)</td>
                                <td>建筑类型</td>
                                <td>学区</td>
                                <td>总户数</td>
                                <td>录入户数</td>
                                <td>有效户数</td>
                                <td>负责人</td>
                                <td>维护人</td>
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
                                    <a ng-click="view(foo.id)" bo-text="foo.name"></a>
                                </td>
                                <td bo-text="foo.code"></td>
                                <td bo-text="foo.cityName+' - '+foo.areaName"></td>
                                <td bo-text="foo.avgPrice"></td>
                                <td bo-text="foo.typeName"></td>
                                <td bo-text="foo.school"></td>
                                <td bo-text="foo.houseCounts"></td>
                                <td>
                                    <a class="cp" ng-click="viewRoom(foo.id,foo.name)" bo-text="foo.allRooms"></a>
                                </td>
                                <td>
                                    <a class="cp" ng-click="viewValidRoom(foo.id,foo.name)"
                                       bo-text="foo.validRooms"></a>
                                </td>
                                <td bo-text="foo.masterName"></td>
                                <td bo-text="foo.maintainName"></td>
                                <td style="text-align: left">
                                    <c:if test='${sessionScope.get("OP_BUILDING_MODIFY") eq true}'>
                                        <a class="btn-op blue" ng-click="modify(foo.id);">编辑</a>
                                    </c:if>
                                    <c:if test='${sessionScope.get("OP_BUILDING_MASTER") eq true}'>
                                        <a class="btn-op yellow" ng-click="modifyMaster(foo.id);" title="变更负责人">负责人</a>
                                    </c:if>
                                    <c:if test='${sessionScope.get("OP_BUILDING_MAINTAIN") eq true}'>
                                        <a class="btn-op yellow" ng-click="modifyMaintain(foo.id);"
                                           title="更新楼盘的维护人列表">维护人</a>
                                    </c:if>
                                    <c:if test='${sessionScope.get("OP_BUILDING_DELETE") eq true}'>
                                        <a class="btn-op red" ng-click="remove(foo.id);" ng-if="foo.status=='INACTIVE'">删除</a>
                                    </c:if>
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
<script type="text/javascript" src="<%=contextPath%>/app/employee/employee-modal.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/building/building.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/building/list/building_list.js"></script>
</html>