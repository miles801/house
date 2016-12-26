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
                        <a type="button" class="btn btn-green btn-min"
                           ng-disabled="!(condition.blockId)"
                           ng-click="query();" ng-cloak>
                                查询
                        </a>
                        <a type="button" class="btn btn-green btn-min" ng-click="reload();">
                                刷新本页面
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
                                    ng-options="o.id as o.code for o in units"
                                    ng-change="unitChange();"></select>
                            <div class="form-label col-1-half">
                                <label>楼层:</label>
                            </div>
                            <input type="text" ng-model="condition.floor" class="col-2-half" placeholder="请输入数字"
                                   maxlength="3" validate validate-int/>
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
                <div class="header-button">
                    <c:if test="${sessionScope.get('OP_ROOM_IMPORT') eq true}">
                        <a type="button" class="btn btn-green btn-min" ng-click="importData();">
                            批量导入
                        </a>
                    </c:if>
                    <c:if test="${sessionScope.get('OP_ROOM_BATCH_DELETE') eq true}">
                        <a type="button" class="btn btn-green btn-min" ng-click="remove();" ng-cloak
                           ng-disabled="!anyone">
                            删除
                        </a>
                    </c:if>
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
                                        <div select-all-checkbox checkboxes="beans.data" selected-items="items"
                                             anyone-selected="anyone"></div>
                                    </td>
                                    <td style="width: 40px;">序号</td>
                                    <td style="width: 40px;">楼栋</td>
                                    <td style="width: 40px;">单元</td>
                                    <td>楼层</td>
                                    <td style="width: 120px;">房屋编号</td>
                                    <td style="width: 80px;">门牌号</td>
                                    <td>面积</td>
                                    <td>户型(室-厅-厨-卫)</td>
                                    <td>朝向</td>
                                    <td>操作</td>
                                </tr>
                                </thead>
                                <tbody class="table-body">
                                <c:if test="${param.pageType ne 'detail' || sessionScope.get('EDIT_ROOM') ne null}">
                                    <tr ng-show="!beans.total">
                                        <td colspan="11" class="text-center">
                                            <a class="btn-op blue"
                                               ng-click="!(condition.blockId && condition.unitCode) || add();"
                                               ng-disabled="!(condition.blockId && condition.unitCode)" ng-cloak>新建</a>
                                        </td>
                                    </tr>
                                    <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                        <td><input type="checkbox" ng-model="foo.isSelected"/></td>
                                        <td bo-text="pager.start+$index+1"></td>
                                        <td bo-text="foo.blockCode"></td>
                                        <td bo-text="foo.unitCode"></td>
                                        <td>
                                            <input type="text" ng-model="foo.floor" validate validate-required
                                                   validate-int ng-change="check(foo);" style="width: 50px;"/>
                                        </td>
                                        <td bo-text="foo.roomKey"></td>
                                        <td>
                                            <input type="text" name="code" ng-model="foo.code" validate
                                                   validate-required ng-change="check(foo);" style="width: 60px;"/>
                                        </td>
                                        <td>
                                            <input type="text" ng-model="foo.square" validate validate-required
                                                   validate-float ng-change="check(foo);" style="width: 60px;"/>
                                        </td>
                                        <td>
                                            <input type="text" ng-model="foo.type1" style="width: 40px;" validate
                                                   validate-required validate-int
                                                   placeholder="室" ng-change="check(foo);"/>
                                            <input type="text" ng-model="foo.type2" style="width: 40px;" validate
                                                   validate-required validate-int ng-change="check(foo);"
                                                   placeholder="厅"/>
                                            <input type="text" ng-model="foo.type3" style="width: 40px;" validate
                                                   validate-required validate-int ng-change="check(foo);"
                                                   placeholder="厨"/>
                                            <input type="text" ng-model="foo.type4" style="width: 40px;" validate
                                                   validate-required validate-int ng-change="check(foo);"
                                                   placeholder="卫"/>
                                        </td>
                                        <td>
                                            <select ng-model="foo.orient"
                                                    ng-options="o.value as o.name for o in orients"
                                                    ng-change="check(foo);" style="min-width: 60px;"></select>
                                        </td>
                                        <td>
                                            <a class="btn-op blue" ng-disabled="foo.$invalid" ng-click="save(foo);"
                                               ng-cloak
                                               ng-if="!foo.id">保存</a>
                                            <a class="btn-op blue" ng-disabled="!foo.$valid" ng-click="update(foo);"
                                               ng-cloak
                                               ng-if="foo.id">更新</a>
                                            <a class="btn-op red" ng-click="remove(foo.id,$index);">删除</a>
                                            <a class="btn-op green" ng-click="add(foo);">新建</a>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${param.pageType eq 'detail' && sessionScope.get('EDIT_ROOM') eq null}">
                                    <tr ng-show="!beans.total">
                                        <td colspan="11" class="text-center">无房屋信息</td>
                                    </tr>
                                    <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                        <td><input type="checkbox" ng-model="foo.isSelected"/></td>
                                        <td bo-text="$index+1"></td>
                                        <td bo-text="foo.blockCode"></td>
                                        <td bo-text="foo.unitCode"></td>
                                        <td bo-text="foo.floor"></td>
                                        <td bo-text="foo.roomKey"></td>
                                        <td bo-text="foo.code"></td>
                                        <td bo-text="foo.square"></td>
                                        <td bo-text="foo.type1+'-'+foo.type2+'-'+foo.type3+'-'+foo.type4"></td>
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
    <div class="list-pagination" eccrm-page="pager"></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/room.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/block/block.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/unit/unit.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/list/room_list.js"></script>
</html>