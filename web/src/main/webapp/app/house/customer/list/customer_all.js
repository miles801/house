/**
 * 客户管理列表
 * Created by Michael on 2016-06-19 15:16:56.
 */
(function (window, angular, $) {
    var app = angular.module('house.customer.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'house.customer'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, CustomerService, CustomerParam) {
        $scope.condition = {};

        // 性别
        CustomerParam.sex(function (data) {
            $scope.sex = data;
            $scope.sex.unshift({name: '请选择...'});
        });
        // 资产规模
        CustomerParam.money(function (data) {
            $scope.money = data;
            $scope.money.unshift({name: '请选择...'});
        });
        // 年龄段
        CustomerParam.age(function (data) {
            $scope.age = data;
            $scope.age.unshift({name: '请选择...'});
        });
        // 婚姻
        CustomerParam.marriage(function (data) {
            $scope.marriage = data;
            $scope.marriage.unshift({name: '请选择...'});
        });
        // 学历
        CustomerParam.education(function (data) {
            $scope.education = data;
            $scope.education.unshift({name: '请选择...'});
        });
        // 状态
        CustomerParam.status(function (data) {
            $scope.status = data;
            $scope.status.unshift({name: '请选择...'});
        });

        //查询数据
        $scope.query = function () {
            $scope.pager.query();
        };

        // 查看房屋详情
        $scope.viewRoom = function (key) {
            CommonUtils.addTab({
                title: '房屋明细-' + key,
                url: 'house/room/view?code=' + key
            })
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
            if (!id) {
                var ids = [];
                angular.forEach($scope.items || [], function (o) {
                    ids.push(o.id);
                });
                id = ids.join(',');
            }
            if (!id) {
                AlertFactory.error('没有获取到需要删除的数据!');
                return;
            }
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
                title: '新增客户管理',
                url: '/house/customer/add',
                onUpdate: $scope.query
            });
        };

        // 更新
        $scope.modify = function (id) {
            CommonUtils.addTab({
                title: '更新客户管理',
                url: '/house/customer/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        // 查看明细
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看客户管理',
                url: '/house/customer/detail?id=' + id
            });
        };

        // 导出数据
        $scope.exportData = function () {
            if ($scope.pager.total < 1) {
                AlertFactory.error('未获取到可以导出的数据!请先查询出数据!');
                return;
            }
            var o = angular.extend({}, $scope.condition);
            o.start = null;
            o.limit = null;
            window.location.href = CommonUtils.contextPathURL('/house/customer/export?' + $.param(o));
        };
    });

})(window, angular, jQuery);