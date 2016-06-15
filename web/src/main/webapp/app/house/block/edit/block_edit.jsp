<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>编辑楼栋管理</title>
    <meta content="text/html" charset="utf-8">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/vendor/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        window.angular.contextPathURL = "<%=contextPath%>";
    </script>
</head>
<body>
<div class="main" ng-app="house.block.edit" ng-controller="Ctrl">
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
                        <button type="button" class="btn btn-green btn-min" ng-click="save(true)"
                                ng-disabled="form.$invalid">
                            <span class="glyphicons disk_open"></span> 保存并新建
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
                            <label>标题:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.title"/>
                        <div class="form-label col-1-half">
                            <label>类型:</label>
                        </div>
                        <select ng-model="beans.type" class="col-2-half"
                                ng-options="foo.value as foo.name for foo in x">
                        </select>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>内容:</label>
                        </div>
                        <textarea class="col-6-half" rows="3" ng-model="beans.content"></textarea>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>关键字:</label>
                        </div>
                        <input class="col-6-half" type="text" ng-model="beans.keywords"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>外部链接:</label>
                        </div>
                        <input class="col-6-half" type="text" ng-model="beans.url"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>状态:</label>
                        </div>
                        <select ng-model="beans.status" class="col-6-half"
                                ng-options="foo.value as foo.name for foo in x">
                        </select>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/block/block.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/block/edit/block_edit.js"></script>
</html>