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
</head>

<body id="ng-app">
<div class="main" ng-app="eccrm.panel.base.list" ng-controller="Ctrl" style="overflow: auto;">
    <input type="hidden" id="empId" value="${sessionScope.get("employeeId")}"/>
    <div class="row" style="height: 200px;padding: 5px 20px;">
        <table style="height: 100%;width: 100%;">
            <tbody>
            <tr>
                <td style="width: 180px;" id="imageId">
                </td>
                <td style="width: 200px;">
                    <div ng-cloak>
                        <p>姓名：{{beans.name}}</p>

                        <p>性别：{{beans.sexName}}</p>

                        <p>手机号码：{{beans.phone}}</p>

                    </div>

                </td>
                <td style="text-align: left">
                    <div class="row mybtn">
                        <a type="button" class="btn btn-blue"
                           href="<%=contextPath%>/base/emp/modify?id=${sessionScope.get("employeeId")}"
                           style="width: 110px;">
                            <span class="glyphicons plus"></span> 完善个人信息
                        </a>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/home/panel/panel.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/emp/emp.js"></script>
</html>



