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
</head>
<body>
<div class="main condition-row-2" ng-app="house.room.all" ng-controller="Ctrl">
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
                    <div class="row" style="margin-top:0;">
                        <div class="row float">
                            <div class="item w240">
                                <div class="form-label w80">
                                    <label>楼盘:</label>
                                </div>
                                <input type="text" class="w150" ng-model="condition.buildingName"/>
                            </div>
                            <div class="item w240">
                                <div class="form-label w80">
                                    <label>楼栋:</label>
                                </div>
                                <input type="text" class="w150" ng-model="condition.blockCode"/>
                            </div>
                            <div class="item w240">
                                <div class="form-label w80">
                                    <label>单元:</label>
                                </div>
                                <input type="text" class="w150" ng-model="condition.unitCode"/>
                            </div>
                            <div class="item w240">
                                <div class="form-label w80">
                                    <label>房屋现状:</label>
                                </div>
                                <select ng-model="condition.houseUseType" class="w150"
                                        ng-options="foo.value as foo.name for foo in useType"></select>
                            </div>
                            <div class="item w240">
                                <div class="form-label w80">
                                    <label>房屋状态:</label>
                                </div>
                                <select ng-model="condition.status" class="w150"
                                        ng-options="foo.value as foo.name for foo in status"></select>
                            </div>
                            <div class="item w240">
                                <div class="form-label w80">
                                    <label>房屋朝向:</label>
                                </div>
                                <select ng-model="condition.orient" class="w150"
                                        ng-options="foo.value as foo.name for foo in orient"></select>
                            </div>
                            <div class="item w240">
                                <div class="form-label w80">
                                    <label>户型:</label>
                                </div>
                                <div class="w150">
                                    <input type="number" ng-model="condition.type1" style="width: 60px;float: left;"
                                           placeholder="室"/>
                                    <input type="number" ng-model="condition.type2"
                                           style="width: 60px;margin-left: 10px;float: left;" placeholder="厅"/>
                                </div>
                            </div>
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
                        <table class="table table-striped table-hover">
                            <thead class="table-header">
                            <tr>
                                <td>房屋编号</td>
                                <td>楼盘</td>
                                <td>栋座</td>
                                <td>单元</td>
                                <td>楼层</td>
                                <td>门牌号</td>
                                <td>户型</td>
                                <td>面积</td>
                                <td>房屋现状</td>
                                <td>业主姓名</td>
                                <td>手机</td>
                                <td>微信</td>
                                <td>状态</td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <tr ng-show="!beans.total">
                                <td colspan="13" class="text-center">无房屋信息!</td>
                            </tr>
                            <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                <td bo-text="foo.roomKey"></td>
                                <td bo-text="foo.buildingName"></td>
                                <td bo-text="foo.blockCode"></td>
                                <td bo-text="foo.unitCode"></td>
                                <td bo-text="foo.floor"></td>
                                <td bo-text="foo.code"></td>
                                <td bo-text="foo.type1+'-'+foo.type2+'-'+foo.type3+'-'+foo.type4"></td>
                                <td bo-text="foo.square"></td>
                                <td bo-text="foo.houseUseTypeName"></td>
                                <td bo-text="foo.cusName"></td>
                                <td bo-text="foo.cusPhone"></td>
                                <td bo-text="foo.cusWechat"></td>
                                <td bo-text="foo.statusName"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div eccrm-page="pager" class="list-pagination"></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/room.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/list/room_all.js"></script>
</html>