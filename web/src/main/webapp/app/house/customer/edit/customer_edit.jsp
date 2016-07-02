<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>编辑客户管理</title>
    <meta content="text/html" charset="utf-8">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/vendor/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/vendor/moment/moment.min.js"></script>
    <script type="text/javascript">
        window.angular.contextPathURL = "<%=contextPath%>";
    </script>
</head>
<body>
<div class="main" ng-app="house.customer.edit" ng-controller="Ctrl">
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
                        <input type="hidden" id="roomId" value="${roomId}"/>
                        <input type="hidden" id="id" value="${id}"/>
                    </div>
                    <%-- 变更房主 --%>
                    <c:if test="${pageType eq 'add' && roomId ne null && id ne null}">
                        <div class="row">
                            <div class="form-label col-1-half">
                                <label>交易时间:</label>
                            </div>
                            <div class="col-2-half">
                                <input type="text" class="col-12" eccrm-my97="{}" ng-model="rb.occurDate" readonly
                                       validate validate-required placeholder="点击选择时间..."/>
                                <span class="add-on"><i class="icons icon clock"
                                                        ng-click="rb.occurDate=null;"></i></span>
                            </div>
                            <div class="form-label col-1-half">
                                <label validate-error="form.businessType">交易类型:</label>
                            </div>
                            <select ng-model="rb.type" class="col-2-half" name="businessType"
                                    ng-options="foo.value as foo.name for foo in businessType"
                                    validate validate-required>
                            </select>
                            <div class="form-label col-1-half">
                                <label>成交价格:</label>
                            </div>
                            <input ng-model="rb.price" class="col-2-half" type="text" validate validate-required
                                   validate-float
                                   placeholder="单位是元"/>
                        </div>
                        <div class="ycrl split" style="margin-top:15px;"></div>
                        <h3 class="text-center">新客户信息</h3>
                    </c:if>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>客户编号:</label>
                        </div>
                        <c:if test="${roomId ne null && pageType eq 'add'}">
                            <div class="col-2-half">
                                <input class="col-12" type="text" ng-model="beans.code" readonly
                                       placeholder="点击选择客户或手动录入" ng-click="pickCustomer();"/>
                                <span class="add-on"><i class="icons icon user"></i></span>
                            </div>
                        </c:if>
                        <c:if test="${roomId ne null && pageType ne 'add'}">
                            <div class="col-2-half">
                                <input class="col-12" type="text" ng-model="beans.code" readonly/>
                            </div>
                        </c:if>
                        <c:if test="${roomId eq null}">
                            <input class="col-2-half" type="text" ng-model="beans.code" placeholder="自动生成" readonly/>
                        </c:if>

                        <div class="form-label col-1-half">
                            <label>客户姓名:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.name" maxlength="20"/>
                        <div class="form-label col-1-half">
                            <label>身份证号:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.idCard" maxlength="20"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label validate-error="form.phone1">电话（主）:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.phone1" maxlength="20" name="phone1"
                               validate validate-required validate-int/>
                        <div class="form-label col-1-half">
                            <label>电话2:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.phone2" maxlength="20"
                               validate validate-int/>
                        <div class="form-label col-1-half">
                            <label>电话3:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.phone3" maxlength="20"
                               validate validate-int/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>邮箱:</label>
                        </div>
                        <input class="col-10-half" type="text" ng-model="beans.email" maxlength="100"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>公司名称:</label>
                        </div>
                        <input class="col-10-half" type="text" ng-model="beans.company" maxlength="100"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>微信:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.wechat" maxlength="40"/>
                        <div class="form-label col-1-half">
                            <label>教育程度:</label>
                        </div>
                        <select ng-model="beans.education" class="col-2-half"
                                ng-options="foo.value as foo.name for foo in education">
                        </select>
                        <div class="form-label col-1-half">
                            <label>财产规模:</label>
                        </div>
                        <select ng-model="beans.money" class="col-2-half"
                                ng-options="foo.value as foo.name for foo in money">
                        </select>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>年龄段:</label>
                        </div>
                        <select ng-model="beans.age" class="col-2-half"
                                ng-options="foo.value as foo.name for foo in age">
                        </select>
                        <div class="form-label col-1-half">
                            <label>婚姻状况:</label>
                        </div>
                        <select ng-model="beans.marriage" class="col-2-half"
                                ng-options="foo.value as foo.name for foo in marriage">
                        </select>
                        <div class="form-label col-1-half">
                            <label>家庭人数:</label>
                        </div>
                        <input class="col-2-half" type="number" ng-model="beans.familyCounts" validate validate-int
                               maxlength="1"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>车位1:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.carSite1" maxlength="40"/>
                        <div class="form-label col-1-half">
                            <label>车位2:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.carSite2" maxlength="40"/>
                        <div class="form-label col-1-half">
                            <label>车牌号:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.carNo" maxlength="40"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>备注:</label>
                        </div>
                        <div class="col-10-half">
                            <p ng-repeat="foo in descs" ng-cloak>{{foo}}</p>
                        </div>
                    </div>
                    <c:if test="${pageType eq 'add' || pageType eq 'modify'}">
                        <div class="row" ng-show="!newDesc">
                            <div class="form-label col-1-half">&nbsp;</div>
                            <a class="btn btn-blue" ng-click="newDesc=true;">添加新的备注</a>
                        </div>
                        <div class="row" ng-cloak ng-show="!!newDesc">
                            <div class="form-label col-1-half">&nbsp;</div>
                            <input class="col-9-half" type="text" ng-model="myDesc" maxlength="100"
                                   placeholder="请输入新的备注"/>
                            <a class="btn btn-blue" ng-click="addMyDesc();" style="margin-left:8px;">确定</a>
                            <a class="btn btn-blue" ng-click="newDesc=false;" style="margin-left:8px;">取消</a>
                        </div>
                    </c:if>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>状态:</label>
                        </div>
                        <span class="col-2-half" ng-cloak>{{beans.statusName}}</span>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/customer/customer.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/room.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/customer/edit/customer_edit.js"></script>
</html>