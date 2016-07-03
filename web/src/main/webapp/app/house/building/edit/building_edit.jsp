<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>编辑楼盘管理</title>
    <meta content="text/html" charset="utf-8">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/app/base/region/region.js"></script>
    <script type="text/javascript">
        window.angular.contextPathURL = "<%=contextPath%>";
    </script>
</head>
<body>
<div class="main" ng-app="house.building.edit" ng-controller="Ctrl">
    <div class="block">
        <div class="block-header">
                <span class="header-text">
                    <span class="glyphicons info-sign"></span>
                </span>
                <span class="header-button">
                    <c:if test="${pageType eq 'add'}">
                    <button type="button" class="btn btn-green btn-min" ng-click="save()" ng-disabled="form.$invalid">
                        <span class="glyphicons disk_save"></span> 保存
                    </button>
                    </c:if>
                    <c:if test="${pageType eq 'modify'}">
                    <button type="button" class="btn btn-green btn-min" ng-click="update()" ng-disabled="form.$invalid">
                        <span class="glyphicons claw_hammer"></span> 更新
                    </button>
                    </c:if>
                    <a type="button" class="btn btn-green btn-min" ng-click="back();">
                        <span class="glyphicons message_forward"></span> 返回
                    </a>
                </span>
        </div>
        <div class="block-content">
            <div class="content-wrap">
                <form name="form" class="form-horizontal" role="form">
                    <div style="display: none;">
                        <input type="hidden" id="pageType" value="${pageType}"/>
                        <input type="hidden" id="id" value="${id}"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label validate-error="form.name">楼盘名称:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.name" validate validate-required
                               name="name"/>
                        <div class="form-label col-1-half">
                            <label validate-error="form.cityName">城市、区域:</label>
                        </div>
                        <div class="col-2-half">
                            <input class="col-12" type="text" ng-model="cityAndArea" validate validate-required
                                   name="cityName"
                                   readonly ztree-single="regionTree" placeholder="点击选择，定位到区"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label validate-error="form.address">楼盘地址:</label>
                        </div>
                        <input class="col-6-half" type="text" ng-model="beans.address" validate validate-required
                               name="address"/>
                        <div class="form-label col-1-half">
                            <label>均价:</label>
                        </div>
                        <input class="col-2-half" type="number" ng-model="beans.avgPrice" validate validate-float
                               placeholder="元/m2"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label validate-error="form.year">建筑年份:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.year" validate validate-required
                               validate-int name="year"/>
                        <div class="form-label col-1-half">
                            <label validate-error="form.type">建筑类型:</label>
                        </div>
                        <select class="col-2-half" ng-model="beans.type" name="type" validate validate-required
                                ng-options="foo.value as foo.name for foo in type"></select>
                        <div class="form-label col-1-half">
                            <label validate-error="form.houseType">房屋权属:</label>
                        </div>
                        <select class="col-2-half" ng-model="beans.houseType" name="houseType" validate
                                validate-required
                                ng-options="foo.value as foo.name for foo in houseType"></select>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>用途:</label>
                        </div>
                        <select class="col-2-half" ng-model="beans.usages"
                                ng-options="foo.value as foo.name for foo in usages"></select>
                        <div class="form-label col-1-half">
                            <label>供暖类型:</label>
                        </div>
                        <select class="col-2-half" ng-model="beans.warmType"
                                ng-options="foo.value as foo.name for foo in warmType"></select>
                        <div class="form-label col-1-half">
                            <label>物业费:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.propertyPrice" placeholder="元/平方米"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>物业公司:</label>
                        </div>
                        <input class="col-6-half" type="text" ng-model="beans.propertyName"/>
                        <div class="form-label col-1-half">
                            <label>物业电话:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.propertyPhone"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>开发商:</label>
                        </div>
                        <input class="col-6-half" type="text" ng-model="beans.developerName"/>
                        <div class="form-label col-1-half">
                            <label>开发商电话:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.developerPhone"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>楼栋总数:</label>
                        </div>
                        <input class="col-2-half" type="number" ng-model="beans.buildingCounts" validate validate-int/>
                        <div class="form-label col-1-half">
                            <label>总户数:</label>
                        </div>
                        <input class="col-2-half" type="number" ng-model="beans.houseCounts" validate validate-int/>
                        <div class="form-label col-1-half">
                            <label>车位数:</label>
                        </div>
                        <input class="col-2-half" type="number" ng-model="beans.carCounts" validate validate-int/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>地铁距离:</label>
                        </div>
                        <select class="col-2-half" ng-model="beans.subway"
                                ng-options="foo.value as foo.name for foo in subway"></select>
                        <div class="form-label col-1-half">
                            <label>学区:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.school"/>
                        <div class="form-label col-1-half">
                            <label>过户指导价格:</label>
                        </div>
                        <input class="col-2-half" type="number" ng-model="beans.price" validate validate-float
                               placeholder="元/m2"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>容积率:</label>
                        </div>
                        <input class="col-2-half" type="number" ng-model="beans.containPercent" validate
                               validate-float/>
                        <div class="form-label col-1-half">
                            <label>绿化率(%):</label>
                        </div>
                        <input class="col-2-half" type="number" ng-model="beans.greenPercent" validate validate-float/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>备注:</label>
                        </div>
                        <textarea class="col-10-half" rows="6" ng-model="beans.description"></textarea>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/building/building.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/building/edit/building_edit.js"></script>
</html>