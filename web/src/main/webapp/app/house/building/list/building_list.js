/**
 * Created by Michael on 2016-06-11 21:45:57.
 */
(function (window, angular, $) {
    var app = angular.module('house.building.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.base.employee.modal',    // 员工
        'house.building'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, BuildingService, BuildingParam,
                                     EmployeeModal, BuildingModal) {
        $scope.condition = {
            manager: $('#isManager').val()
        };

        // 学区
        $scope.types = [{name: '全部'}];
        BuildingParam.type(function (data) {
            $scope.types.unshift.apply($scope.types, data);
        });
        //查询数据
        $scope.query = function () {
            $scope.pager.query();
        };

        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, {start: this.start, limit: this.limit}, $scope.condition);
                $scope.beans = [];
                return CommonUtils.promise(function (defer) {
                    var promise = BuildingService.pageQuery(param, function (data) {
                        param = null;
                        $scope.beans = data.data || {total: 0};
                        defer.resolve($scope.beans);
                    });
                    CommonUtils.loading(promise, 'Loading...');
                });
            },
            finishInit: function () {
                this.query();
            }
        };

        // 删除或批量删除
        $scope.remove = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '数据一旦删除将不可恢复，请确认!',
                callback: function () {
                    var promise = BuildingService.deleteByIds({ids: id}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.query();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 新增
        $scope.add = function () {
            $scope.closeTab();
            CommonUtils.addTab({
                title: '新增楼盘管理',
                url: '/house/building/add',
                onClose: $scope.closeTab,
                onUpdate: $scope.query
            });
        };

        // 更新
        $scope.modify = function (id) {
            $scope.closeTab();
            CommonUtils.addTab({
                title: '更新楼盘管理',
                onClose: $scope.closeTab,
                url: '/house/building/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        // 查看明细
        $scope.view = function (id) {
            $scope.closeTab();
            CommonUtils.addTab({
                title: '楼盘明细',
                onClose: $scope.closeTab,
                url: '/house/building/detail?id=' + id
            });
        };
        // 查看楼盘户数
        $scope.viewRoom = function (id, name) {
            $scope.closeTab();
            CommonUtils.addTab({
                title: '房屋列表--' + name,
                onClose: $scope.closeTab,
                url: 'app/house/room/list/room_all.jsp?buildingId=' + id
            });
        };
        // 查看楼盘有效户数
        $scope.viewValidRoom = function (id, name) {
            $scope.closeTab();
            CommonUtils.addTab({
                title: '房屋列表--' + name,
                onClose: $scope.closeTab,
                url: 'app/house/room/list/room_view.jsp?buildingId=' + id
            });
        };

        // 关闭其他页签
        $scope.closeTab = function () {
            $(window.parent.document.body).find('ul.nav-tabs>li>span.icons.fork').trigger('click');
        };

        // 更新负责人
        $scope.modifyMaster = function (id) {
            EmployeeModal.pickEmployee({}, function (emp) {
                ModalFactory.confirm({
                    scope: $scope,
                    content: '变更楼盘负责人，请确认该操作！',
                    callback: function () {
                        var promise = BuildingService.updateMaster({id: id, empId: emp.id}, function () {
                            AlertFactory.success('楼盘负责人变更成功!');
                            $scope.pager.load();
                        });
                        CommonUtils.loading(promise);
                    }
                });
            });
        };

        // 更新维护人
        $scope.modifyMaintain = function (id) {
            BuildingModal.maintain({id: id}, $scope.pager.load);
        };

        $scope.closeTab();
    });
})(window, angular, jQuery);