/**
 * 区域负责人
 */
(function (angular, $) {
    var app = angular.module('spec.building.region', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.angular.ztree',
        'eccrm.base.region',
        'eccrm.base.employee.modal'    // 员工
    ]);
    app.controller('Ctrl', function ($scope, RegionService, ModalFactory, AlertFactory, CommonUtils, EmployeeModal) {

        //查询数据
        $scope.query = function () {
            var promise = RegionService.mine(function (data) {
                $scope.beans = data.data || [];
            });
            CommonUtils.loading(promise, '加载中...');
        };

        // 设置负责人
        $scope.setMaster = function (id, name) {
            EmployeeModal.pickEmployee({
                condition: {
                    manager: $('#isManager').val(),
                    creatorId: $('#isManager').val() ? null : $('#empId').val()
                }
            }, function (emp) {
                ModalFactory.confirm({
                    scope: $scope,
                    content: '是否要将区域[' + name + ']的负责人设置为:' + emp.name + ',请确认?',
                    callback: function () {
                        var promise = RegionService.setMaster({id: id, masterId: emp.id}, function () {
                            AlertFactory.success('操作成功!');
                            $scope.query();
                        });
                        CommonUtils.loading(promise);
                    }
                });
            });
        };

        // 清空负责人
        $scope.clearMaster = function (id, name) {
            ModalFactory.confirm({
                scope: $scope,
                content: '是否要将区域[' + name + ']的负责人,请确认?',
                callback: function () {
                    var promise = RegionService.clearMaster({id: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        $scope.query();
    });
})(angular, jQuery);