<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%
        String contextPath = request.getContextPath();
    %>
    <title>仪表盘</title>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>

    <script type="text/javascript">
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
    <style type="text/css">
        body {
            margin: 0;
            padding: 0;
            font-size: 14px;
        }

        .mybtn .btn-blue {
            height: 35px;
            width: 100px;
            line-height: 32px;
            font-size: 14px;
            font-weight: 500;
            font-family: "微软雅黑", "宋体"
        }

        .http_method a {
            text-transform: uppercase;
            text-decoration: none;
            color: white;
            display: inline-block;
            width: 70px;
            font-size: 0.7em;
            text-align: center;
            padding: 7px 0 4px;
        }

        ul, li {
            margin: 0;
            padding: 0;
            list-style: none;
        }

        span.path {
            padding-left: 10px
        }

        span.path a {
            color: black;
            text-decoration: none;
        }

        h2 a {
            color: black;
        }

        .get {
            background-color: #0f6ab4
        }

        .post {
            background-color: #10a54a;
        }

        .delete {
            background-color: #a50d0e;
        }
    </style>
</head>

<body id="ng-app">
<div class="main" ng-app="eccrm.panel.base.list" ng-controller="Ctrl" style="overflow: auto;">
    <div class="row" style="height: 200px;padding: 5px 20px;">
        <table style="height: 100%;width: 100%;">
            <tbody>
            <tr>
                <td style="width: 180px;" id="imageId">
                </td>
                <td style="width: 200px;">
                    <div ng-cloak>
                        <p>姓名：{{beans.employeeName}}</p>

                        <p>性别：{{beans.genderName}}</p>

                        <p>岗位：{{beans.positionName}}</p>

                        <p>民族：{{beans.nationName}}</p>

                        <p>组织机构：{{beans.orgName}}</p>

                        <p>手机号码：{{beans.mobile}}</p>

                    </div>

                </td>
                <td style="text-align: left">
                    <div class="row mybtn">
                        <a type="button" class="btn btn-blue"
                           href="<%=contextPath%>/base/employee/modify/"
                           style="width: 110px;">
                            <span class="glyphicons plus"></span> 完善个人信息
                        </a>
                        <a type="button" class="btn btn-blue" ng-click="api();" style="width: 110px;"> <span
                                class="glyphicons plus"></span> API
                        </a>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="row" id="report" style="width: 960px;margin: 0 auto;" ng-cloak>
        <ul bindonce ng-repeat="api in apis" style="list-style: none;">
            <li style="border-bottom: 1px solid #dddddd;">
                <div class="heading"
                     style="border: 1px solid transparent; float: none; clear: both; overflow: hidden; display: block;">
                    <h2 style="color: #000; font-size:24px;line-height:24px;padding-left: 0; display: block; clear: none; font-weight: bold;">
                        <a bo-text="api.url" class="cp" ng-click="api.detail=!api.detail"></a>
                        <a bo-text="api.name" style="float: right;"></a>
                    </h2>
                </div>
            </li>
            <ul style="display: block;list-style: none">
                <li bindonce ng-repeat="op in api.operates">
                    <ul style="list-style: none;font-size: 0.9em;">
                        <li style="float: none; clear: both; overflow: hidden; display: block; margin: 0 0 10px; padding: 0;line-height: 30px;">
                            <div style="background-color: #e7f0f7; border: 1px solid #c3d9ec;border-top:0;">
                                <h3 style="display: block; clear: none; width: auto; margin: 0; padding: 0; color: black;">
                                    <span class="http_method">
                                        <a ng-class="{get:op.method=='GET',post:op.method=='POST',delete:op.method=='DELETE'}"
                                           bo-text="op.method"></a>
                                    </span>
                                    <span class="path">
                                        <a bo-text="api.url+op.url" class="cp" ng-click="op.show=!op.show"></a>
                                        <span ng-repeat="p in op.params">
                                            <span ng-if="$index==0" style="margin-left:-8px;">?</span><span
                                                ng-if="$index>0">&</span><span>{{p.name}}=</span>
                                        </span>
                                    </span>
                                    <span style="float: right;">
                                        <a bo-text="op.name"
                                           style="font-size: 16px; line-height: 16px; margin-right: 8px;"></a>
                                    </span>
                                </h3>
                                <div class="content" ng-show="op.show">
                                    <div style="padding: 5px 20px;color: #B40808;" ng-if="op.desc"> -*- {{op.desc}}
                                    </div>
                                    <div ng-if="op.params.length">
                                        <h4 style="font-size: 16px; font-weight: 700; margin-left: 21px;">
                                            地址栏参数（?xx=11&yy=22）</h4>
                                        <div class="table-responsive panel panel-table">
                                            <table class="table table-striped table-hover">
                                                <thead>
                                                <tr>
                                                    <td>名称</td>
                                                    <td>参数</td>
                                                    <td>是否必填</td>
                                                    <td>描述</td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr bindonce ng-repeat="model in op.params">
                                                    <td bo-text="model.name"></td>
                                                    <td bo-text="model.value"></td>
                                                    <td bo-text="model.required"></td>
                                                    <td bo-text="model.desc"></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div ng-if="op.requestModels.length">
                                        <h4 style="font-size: 16px; font-weight: 700; margin-left: 21px;">
                                            请求参数(application/json)</h4>
                                        <div class="table-responsive panel panel-table">
                                            <table class="table table-striped table-hover">
                                                <thead>
                                                <tr>
                                                    <td>名称</td>
                                                    <td>参数</td>
                                                    <td>是否必填</td>
                                                    <td>描述</td>
                                                    <td>类型</td>
                                                    <td>关联</td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr bindonce ng-repeat="model in op.requestModels">
                                                    <td bo-text="model.name"></td>
                                                    <td bo-text="model.field"></td>
                                                    <td bo-text="model.required"></td>
                                                    <td bo-text="model.desc"></td>
                                                    <td bo-text="model.type"></td>
                                                    <td bo-text="model.models.length"></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div ng-if="op.responseModels.length">
                                        <h4 style="font-size: 16px; font-weight: 700; margin-left: 21px;">响应结果</h4>
                                        <div class="table-responsive panel panel-table">
                                            <table class="table table-striped table-hover">
                                                <thead>
                                                <tr>
                                                    <td>名称</td>
                                                    <td>参数</td>
                                                    <td>描述</td>
                                                    <td>类型</td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr bindonce ng-repeat="model in op.responseModels">
                                                    <td bo-text="model.name"></td>
                                                    <td bo-text="model.field"></td>
                                                    <td bo-text="model.desc"></td>
                                                    <td bo-text="model.type"></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                </li>
            </ul>
        </ul>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/home/panel/panel.js"></script>
</html>



