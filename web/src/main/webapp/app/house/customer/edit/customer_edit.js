/**
 * 业主管理编辑
 */
(function (window, angular, $) {
    var app = angular.module('house.customer.edit', [
        'house.customer',
        'house.room',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, CustomerService, CustomerParam, RoomService, CustomerModal) {

        var pageType = $('#pageType').val();
        var id = $('#id').val();
        var roomId = $('#roomId').val();

        $scope.back = CommonUtils.back;

        // 年龄段
        CustomerParam.age(function (data) {
            $scope.age = data;
            $scope.age.unshift({name: '请选择...'});
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
        // 保存
        $scope.save = function () {
            var promise;
            if (roomId) {
                $scope.beans.roomId = roomId;
                promise = RoomService.addCustomer($scope.beans, function () {
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

        $scope.addMyDesc = function () {
            $scope.beans.description = ($scope.beans.description || '') + moment().format('YYYY-MM-DD HH:mm:ss') + ' \t' + CommonUtils.loginContext().employeeName + ' \t ' + $scope.myDesc;
            $scope.newDesc = false;
        };

        // 更新
        $scope.update = function () {
            var promise = CustomerService.update($scope.beans, function (data) {
                AlertFactory.success('更新成功!');
                $scope.form.$setValidity('committed', false);
                CommonUtils.addTab('update');
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise, '更新中...');
        };

        $scope.pickCustomer = function () {
            CustomerModal.pick({}, function (o) {
                $scope.beans = o;
            });
        };

        // 加载数据
        $scope.load = function (id) {
            var promise = CustomerService.get({id: id}, function (data) {
                $scope.beans = data.data || {};
            });
            CommonUtils.loading(promise, 'Loading...');
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