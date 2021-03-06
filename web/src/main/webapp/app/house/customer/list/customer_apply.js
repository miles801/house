/**
 * 客户管理列表
 * Created by Michael on 2016-06-19 15:16:56.
 */
(function (window, angular, $) {
    var app = angular.module('house.customer.apply', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'house.customer'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, CustomerService, CustomerParam) {

        var defaults = {
            manager: $('#isManager').val(),
            master: !$('#isManager').val() || true,
            statusInclude: ['APPLY_ADD', 'APPLY_INVALID']
        };

        $scope.condition = angular.extend({}, defaults);

        $scope.reset = function () {
            $scope.condition = angular.extend({}, defaults);
            $scope.query();
        };
        // 性别
        CustomerParam.sex(function (data) {
            $scope.sex = data;
            $scope.sex.unshift({name: '请选择...'});
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
        // 类型
        CustomerParam.type(function (data) {
            $scope.type = data;
            $scope.type.unshift({name: '全部'});
        });

        $scope.status = [
            {name: '全部...'},
            {name: '新增申请', value: 'APPLY_ADD'},
            {name: '无效申请', value: 'APPLY_INVALID'}
        ];

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

        // 查看房屋详情
        $scope.viewRoom = function (key) {
            CommonUtils.addTab({
                title: '房屋明细-' + key,
                url: 'house/room/view?code=' + key
            })
        };

        // 查看明细
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看客户管理',
                url: '/house/customer/detail?id=' + id
            });
        };


        // 同意
        $scope.pass = function (id) {
            if (!id) {
                id = $scope.items.map(function (o) {
                    return o.id;
                }).join(',');
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '是否确认同意?',
                callback: function () {
                    var promise = CustomerService.batchPass({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        // 拒绝
        $scope.deny = function (id) {
            if (!id) {
                id = $scope.items.map(function (o) {
                    return o.id;
                }).join(',');
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '是否确认拒绝?',
                callback: function () {
                    var promise = CustomerService.batchDeny({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };
    });

})(window, angular, jQuery);