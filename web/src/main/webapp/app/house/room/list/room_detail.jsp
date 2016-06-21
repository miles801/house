<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>房屋跟进</title>
    <meta content="text/html" charset="utf-8">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript">
        window.angular.contextPathURL = "<%=contextPath%>";
    </script>
    <style>
        .item > span {
            height: 28px;
            line-height: 28px;
        }
    </style>
</head>
<body>
<div class="main" ng-app="house.room.detail" ng-controller="Ctrl">
    <div class="block">
        <div class="block-header">
                <span class="header-text" ng-cloak>
                    {{beans.roomKey}} | {{beans.buildingName}} | {{beans.blockCode}} | {{beans.unitCode}} | {{beans.code}}
                </span>
                <span class="header-button">
                    <a type="button" class="btn btn-green btn-min" ng-click="back();">
                        <span class="glyphicons message_forward"></span> 返回
                    </a>
                </span>
        </div>
        <div class="block-content">
            <div class="content-wrap">
                <form name="form" class="form-horizontal" role="form">
                    <div style="display: none;">
                        <input type="hidden" id="id" value="${param.id}"/>
                    </div>
                    <div class="row" ng-cloak>
                        <div class="form-label col-1-half">
                            <label validate-error="form.square">面积:</label>
                        </div>
                        <span class="col-2-half">{{beans.square}}</span>
                        <div class="form-label col-1-half">
                            <label validate-error="form.square">户型:</label>
                        </div>
                        <span class="col-2-half">{{beans.type1}}-{{beans.type2}}-{{beans.type3}}-{{beans.type4}}</span>
                        <div class="form-label col-1-half">
                            <label>朝向:</label>
                        </div>
                        <span class="col-2-half">{{beans.orientName}}</span>
                    </div>
                    <div class="row" ng-cloak>
                        <div class="form-label col-1-half">
                            <label>面积:</label>
                        </div>
                        <span class="col-2-half">{{beans.square}}</span>
                        <div class="form-label col-1-half">
                            <label>现状:</label>
                        </div>
                        <span class="col-2-half">{{beans.houseUseTypeName}}</span>
                        <div class="form-label col-1-half">
                            <label>录入人:</label>
                        </div>
                        <span class="col-2-half">{{beans.creatorName}}</span>
                    </div>
                    <div class="row text-center" style="margin: 10px 0;">
                        <a type="button" class="btn btn-blue" ng-click="modifyRoom(beans.id);"> 修改房屋信息 </a>
                        <a type="button" class="btn btn-blue" ng-click="addInfo(beans.id);"> 录入跟进 </a>
                        <a type="button" class="btn btn-blue" ng-click="star(beans.id);"> 添加关注 </a>
                        <a type="button" class="btn btn-blue" ng-click="viewBuyLog();"> 查看成交记录 </a>
                    </div>

                    <%-- 小区信息 --%>
                    <div class="panel panel-tab" style="margin: 0;position: relative;">
                        <ul class="nav nav-tabs" style="margin: 5px 0 0 0;width:100%;">
                            <li class="active"><i>小区信息</i></li>
                        </ul>
                        <div class="tab-content" style="height: 100%;width:100%;padding-top: 0!important;">
                            <div class="float items" ng-cloak>
                                <div class="item w300">
                                    <div class="form-label w120"><label>学区:</label></div>
                                    <span class="w180">{{building.school}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>过户指导价:</label></div>
                                    <span class="w180">{{building.price|number:2}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>房屋权属:</label></div>
                                    <span class="w180">{{building.houseTypeName}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>年代:</label></div>
                                    <span class="w180">{{building.year}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>建筑类型:</label></div>
                                    <span class="w180">{{building.typeName}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>房屋用途:</label></div>
                                    <span class="w180">{{building.usagesName}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>物业费:</label></div>
                                    <span class="w180">{{building.propertyPrice|number:2}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>供暖方式:</label></div>
                                    <span class="w180">{{building.warmTypeName}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>距地铁:</label></div>
                                    <span class="w180">{{building.subwayName}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>地址:</label></div>
                                    <span class="w180">{{building.address}}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%-- 业主信息 --%>
                    <div class="panel panel-tab" style="margin: 15px 0;position: relative;">
                        <ul class="nav nav-tabs" style="margin: 5px 0 0 0;width:100%;">
                            <li class="active"><i>业主信息</i></li>
                        </ul>
                        <div class="tab-content" style="height: 100%;width:100%;padding-top: 0!important;">
                            <div class="float items" ng-cloak>
                                <div class="item w300">
                                    <div class="form-label w120"><label>编号:</label></div>
                                    <span class="w180">{{customer.code}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>姓名:</label></div>
                                    <span class="w180">{{customer.name}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>性别:</label></div>
                                    <span class="w180">{{customer.sexName}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>年龄:</label></div>
                                    <span class="w180">{{customer.age}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>婚姻状况:</label></div>
                                    <span class="w180">{{customer.marriageName}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>电话1:</label></div>
                                    <span class="w180">{{customer.phone1}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>电话2:</label></div>
                                    <span class="w180">{{customer.phone2}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>电话3:</label></div>
                                    <span class="w180">{{customer.phone3}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>微信:</label></div>
                                    <span class="w180">{{customer.wechat}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>邮箱:</label></div>
                                    <span class="w180">{{customer.email}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>家庭人口:</label></div>
                                    <span class="w180">{{customer.familyCounts}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>工作单位:</label></div>
                                    <span class="w180">{{customer.company}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>状态:</label></div>
                                    <span class="w180">{{customer.statusName}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>描述1:</label></div>
                                    <span class="w180">{{customer.c1}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>描述2:</label></div>
                                    <span class="w180">{{customer.c2}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>描述3:</label></div>
                                    <span class="w180">{{customer.c3}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>描述4:</label></div>
                                    <span class="w180">{{customer.c4}}</span>
                                </div>
                                <div class="item w300">
                                    <div class="form-label w120"><label>描述5:</label></div>
                                    <span class="w180">{{customer.c5}}</span>
                                </div>
                                <div class="item " style="width: 100%;">
                                    <div class="form-label w120"><label>备注:</label></div>
                                    <span class="w720">{{customer.description}}</span>
                                </div>
                            </div>
                            <div class="row text-center" style="clear:both;margin: 10px 0;">
                                <a type="button" class="btn btn-blue" ng-click="changeCustomer(customer.id);">
                                    业主信息修改 </a>
                                <a type="button" class="btn btn-blue" ng-click="applyInvalid(customer.id);"> 申请无效 </a>
                            </div>
                        </div>
                    </div>
                    <%-- 最新动态 --%>
                    <div class="panel panel-tab" style="margin: 15px 0;position: relative;">
                        <ul class="nav nav-tabs" style="margin: 5px 0 0 0;width:100%;">
                            <li class="active"><i>最新动态</i></li>
                        </ul>
                        <div class="tab-content" style="height: 100%;width:100%;padding-top: 0!important;">
                            <div class="table-responsive panel panel-table">
                                <table class="table table-striped table-hover">
                                    <thead class="table-header">
                                    <tr>
                                        <td style="width: 120px;">操作人</td>
                                        <td style="width: 120px;">操作人电话</td>
                                        <td style="text-align: left;">内容</td>
                                        <td style="width: 120px;">操作时间</td>
                                    </tr>
                                    </thead>
                                    <tbody class="table-body">
                                    <tr ng-show="!news.total">
                                        <td colspan="4" class="text-center">无最新动态!</td>
                                    </tr>
                                    <tr bindonce ng-repeat="foo in news.data" ng-cloak>
                                        <td bo-text="foo.empName"></td>
                                        <td bo-text="foo.phone"></td>
                                        <td style="text-align: left;" bo-html="foo.content"></td>
                                        <td bo-text="foo.createdDatetime|eccrmDatetime"></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div eccrm-page="pager" class="list-pagination"></div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/room.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/roomStar.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/customer/customer.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/roomNews.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/house/room/list/room_detail.js"></script>
</html>