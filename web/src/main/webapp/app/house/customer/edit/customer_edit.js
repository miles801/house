/**
 * 客户管理编辑
 */
(function (window, angular, $) {
    var app = angular.module('house.customer.edit', [
        'house.customer',
        'house.customerNews',
        'house.room',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, CustomerService, CustomerParam, RoomService,
                                     CustomerModal, CustomerNewsModal, CustomerNewsService) {

        var pageType = $scope.pageType = $('#pageType').val();
        var id = $('#id').val();
        var roomId = $('#roomId').val();

        // 是否为变更客户
        var isChange = !!(pageType == 'add' && id && roomId);
        var customerId = id;
        $scope.back = CommonUtils.back;

        // 年龄段
        CustomerParam.age(function (data) {
            $scope.age = data;
            $scope.age.unshift({name: '请选择...'});
        });
        // 客户类型
        CustomerParam.type(function (data) {
            $scope.type = data;
            $scope.type.unshift({name: '请选择...'});
        });
        // 性别
        CustomerParam.sex(function (data) {
            $scope.sex = data;
            $scope.sex.unshift({name: '请选择...'});
        });
        if (isChange) {
            id = null;  // 将ID置为空
            // 交易类型
            CustomerParam.businessType(function (data) {
                $scope.businessType = data;
                $scope.businessType.unshift({name: '请选择...'});
            });
        }
        // 教育程度
        CustomerParam.education(function (data) {
            $scope.education = data;
            $scope.education.unshift({name: '请选择...'});
        });
        // 婚姻状况
        CustomerParam.marriage(function (data) {
            $scope.marriage = data;
            $scope.marriage.unshift({name: '请选择...'});
        });
        // 财产规模
        CustomerParam.money(function (data) {
            $scope.money = data;
            $scope.money.unshift({name: '请选择...'});
        });
        // 保存
        $scope.save = function () {
            var promise;
            if (isChange) {
                var o = {
                    roomId: roomId,
                    customer: $scope.beans,
                    roomBusiness: $scope.rb
                };
                o.roomBusiness.originCustomerId = id;
                promise = RoomService.addCustomer(o, function () {
                    AlertFactory.success('保存成功!');
                    CommonUtils.addTab('update');
                    $scope.form.$setValidity('committed', false);
                    CommonUtils.delay($scope.back, 2000);
                });
            } else if (roomId) {
                $scope.beans.roomId = roomId;
                var o = {
                    roomId: roomId,
                    customer: $scope.beans
                };
                promise = RoomService.addCustomer(o, function () {
                    AlertFactory.success('保存成功!');
                    CommonUtils.addTab('update');
                    $scope.form.$setValidity('committed', false);
                    CommonUtils.delay($scope.back, 2000);
                });
            } else {
                promise = CustomerService.save($scope.beans, function (data) {
                    AlertFactory.success('保存成功!');
                    CommonUtils.addTab('update');
                    if (createNew === true) {
                        $scope.beans = {};
                    } else {
                        $scope.form.$setValidity('committed', false);
                        CommonUtils.delay($scope.back, 2000);
                    }
                });
            }
            CommonUtils.loading(promise, '保存中...');
        };


        // 申请无效
        $scope.applyInvalid = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '是否确定将选中的客户申请为“无效电话”客户?',
                callback: function () {
                    var promise = CustomerService.applyInvalid({ids: id}, function () {
                        AlertFactory.success('操作成功!页面即将返回!');
                        CommonUtils.addTab('update');
                        CommonUtils.delay($scope.back, 2000);
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        // 变为有效
        $scope.applyValid = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '是否确定将选中的客户申请为“正常”客户?',
                callback: function () {
                    var promise = CustomerService.applyValid({ids: id}, function () {
                        AlertFactory.success('操作成功!页面即将返回!');
                        CommonUtils.addTab('update');
                        CommonUtils.delay($scope.back, 2000);
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        // 更新
        $scope.update = function () {
            $scope.beans.roomId = roomId || '';
            var promise = CustomerService.update($scope.beans, function (data) {
                AlertFactory.success('更新成功!');
                $scope.form.$setValidity('committed', false);
                CommonUtils.addTab('update');
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise, '更新中...');
        };

        // 选择客户
        $scope.pickCustomer = function () {
            CustomerModal.pick({}, function (o) {
                $scope.beans = o;
            });
        };

        // 录入跟进
        $scope.addCusInfo = function (id) {
            CustomerNewsModal.add({customerId: id}, $scope.loadNews);
        };

        // 加载数据
        $scope.load = function (id) {
            var promise = CustomerService.get({id: id}, function (data) {
                $scope.beans = data.data || {};
                if (pageType == 'detail' || pageType == 'modify') {
                    $scope.loadNews();
                }
            });
            CommonUtils.loading(promise, 'Loading...');
        };

        $scope.pager = {
            fetch: function () {
                var start = this.start;
                var limit = this.limit;
                return CommonUtils.promise(function (defer) {
                    var promise = CustomerNewsService.pageQuery({
                        customerId: id,
                        start: start,
                        limit: limit
                    }, function (data) {
                        $scope.news = data.data || {};
                        defer.resolve($scope.news);
                    });
                    CommonUtils.loading(promise);
                });
            }
        };
        // 查看最新动态
        $scope.loadNews = function () {
            $scope.pager.query();
        };

        if (pageType == 'add') {
            $scope.beans = {};
            if (roomId && id) {
                $scope.load(id);
            }
        } else if (pageType == 'modify') {
            $scope.load(id);
        } else if (pageType == 'detail') {
            $scope.load(id);
            $('input,textarea,select').attr('disabled', 'disabled');
        } else {
            AlertFactory.error($scope, '错误的页面类型');
        }

    });
})(window, angular, jQuery);