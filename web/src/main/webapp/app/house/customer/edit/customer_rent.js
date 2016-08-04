/**
 * 客户管理编辑
 */
(function (window, angular, $) {
    var app = angular.module('house.customer.rent', [
        'house.customer',
        'house.customerNews',
        'house.room',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, CustomerService, CustomerParam, RoomService,
                                     CustomerModal, CustomerNewsModal, CustomerNewsService) {

        var pageType = $scope.pageType = $('#rent').val();
        var id = $('#id').val();
        var roomId = $('#roomId').val();

        // 房屋用途
        $scope.rentUsage = [
            {name: '请选择', value: ''},
            {name: '居家', value: '1'},
            {name: '办公', value: '2'}
        ];

        // 是否为变更客户
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

        // 录入跟进
        $scope.addCusInfo = function (id) {
            CustomerNewsModal.add({customerId: id}, $scope.loadNews);
        };

        // 选择客户
        $scope.pickCustomer = function () {
            CustomerModal.pick({}, function (o) {
                $scope.beans = o;
            });
        };

        // 新增租户
        $scope.save = function () {
            var o = {
                roomId: roomId,
                customer: $scope.beans,
                roomRent: $scope.rr
            };
            var promise = RoomService.addRent(o, function () {
                AlertFactory.success('保存成功!');
                CommonUtils.addTab('update');
                $scope.form.$setValidity('committed', false);
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise);
        };

        $scope.change = function () {
            $scope.beans.rent = true;
            var o = {
                roomId: roomId,
                customer: $scope.beans,
                roomRent: $scope.rr
            };
            var promise = RoomService.changeRent(o, function () {
                AlertFactory.success('保存成功!');
                CommonUtils.addTab('update');
                $scope.form.$setValidity('committed', false);
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise);
        };

        // 更新
        $scope.update = function () {
            var o = {
                roomId: roomId,
                customer: $scope.beans,
                roomRent: $scope.rr
            };
            var promise = RoomService.updateRent(o, function () {
                AlertFactory.success('保存成功!');
                CommonUtils.addTab('update');
                $scope.form.$setValidity('committed', false);
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise);
        };


        $scope.loadRent = function () {
            var promise = RoomService.rent({id: roomId}, function (data) {
                $scope.rr = data.data || {};
            });
            CommonUtils.loading(promise);
        };

        // 加载数据
        $scope.load = function (id) {
            var promise = CustomerService.get({id: id}, function (data) {
                $scope.beans = data.data || {};
                if (pageType !== 'add') {
                    $scope.loadNews();
                    $scope.loadRent();
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

        if (pageType == 'add' || pageType == 'change') {
            $scope.rr = {
                rentUsage: ''
            };
            $scope.beans = {
                rent: true
            };
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