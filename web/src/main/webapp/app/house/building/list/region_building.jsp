<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<title>区域负责人</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css">
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js"></script>
<script>
    window.angular.contextPathURL = '<%=contextPath%>';
</script>
</head >
<body>
<div class="main" ng-app="spec.building.region" ng-controller="Ctrl">
    <div class="block">
        <div class="row block table-list">
            <div class="block-header">
                <div class="header-text">
                    <span class="glyphicons list"></span>
                    <span>区域列表</span>
                </div>
                <div class="header-button">
                    <a type="button" class="btn btn-green btn-min" ng-click="query();">
                        <span class="glyphicons search"></span>
                        查询
                    </a>
                </div>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table first-min">
                        <table class="table table-striped table-hover">
                            <thead class="table-header">
                            <tr>
                                <td>序号</td>
                                <td>城市名称</td>
                                <td>区域名称</td>
                                <td style="width: 120px;">负责人</td>
                                <td class="length-min">操作</td>
                            </tr>
                            </thead>
                            <tbody class="table-body" ng-cloak>
                            <tr ng-show="!beans.length">
                                <td colspan="5" class="text-center">没有符合条件的记录！</td>
                            </tr>
                            <tr bindonce ng-repeat="foo in beans">
                                <td>{{$index+1}}</td>
                                <td bo-text="foo.parentName"></td>
                                <td bo-text="foo.name"></td>
                                <td bo-text="foo.masterName"></td>
                                <td>
                                    <a ng-click="setMaster(foo.id,foo.name);" class="btn-op blue" ng-if="!foo.masterId">设置负责人 </a>
                                    <a ng-click="clearMaster(foo.id,foo.name);" class="btn-op red" ng-if="foo.masterId">清空负责人 </a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

</div >

</body>
<script type="text/javascript" src="<%=contextPath%>/app/base/region/region.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/employee/employee-modal.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/building/list/region_building.js"></script>
</html>