/**
 * 业主管理列表
 * Created by Michael on 2016-06-19 15:16:56.
 */
(function (window, angular, $) {
    var app = angular.module('house.customer.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'house.customer'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, CustomerService, CustomerParam) {
        $scope.condition = {
            statusInclude: ['INACTIVE', 'ACTIVE']
        };

        //查询数据
        $scope.query = function () {
            $scope.pager.query();
        };

        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, {start: this.start, limit: this.limit}, $scope.condition);
                $scope.beans = [];
                return CommonUtils.promise(function (defer) {
                    var promise = CustomerService.pageQuery(param, function (data) {
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
                content: '<span class="text-danger">数据一旦删除将不可恢复，请确认!</span>',
                callback: function () {
                    var promise = CustomerService.deleteByIds({ids: id}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.query();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 新增
        $scope.add = function () {
            CommonUtils.addTab({
                title: '新增业主管理',
                url: '/house/customer/add',
                onUpdate: $scope.query
            });
        };

        $scope.applyAdd = function (id) {
            if (!id) {
                id = $scope.items.map(function (o) {
                    return o.id;
                }).join(',');
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '是否确定将选中的客户申请为“正常”客户?',
                callback: function () {
                    var promise = CustomerService.batchAdd({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };
        $scope.applyInvalid = function (id) {
            if (!id) {
                id = $scope.items.map(function (o) {
                    return o.id;
                }).join(',');
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '是否确定将选中的客户申请为“无效”客户?',
                callback: function () {
                    var promise = CustomerService.applyInvalid({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        // 更新
        $scope.modify = function (id) {
            CommonUtils.addTab({
                title: '更新业主管理',
                url: '/house/customer/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        // 查看明细
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看业主管理',
                url: '/house/customer/detail?id=' + id
            });
        }
    });

    app.filter('contact', function () {
        return function () {
            var v = '';
            angular.forEach(arguments, function (o) {
                v += o ? o : '';
            });
            return v;
        }
    });
})(window, angular, jQuery);