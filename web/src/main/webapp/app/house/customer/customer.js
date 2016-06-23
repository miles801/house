/**
 * 业主管理
 * Created by Michael on 2016-06-19 15:16:56.
 */
(function (angular) {
    var app = angular.module('house.customer', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('CustomerService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/house/customer/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update', roomId: '@roomId'}, isArray: false},

            // 申请无效
            applyInvalid: {method: 'POST', params: {method: 'applyInvalid', ids: "@ids"}, isArray: false},

            // 根据id查询信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            // 批量新增
            batchAdd: {method: 'POST', params: {method: 'batchAdd', ids: '@ids'}, isArray: false},
            // 批量修改
            batchModify: {method: 'POST', params: {method: 'batchModify', ids: '@ids'}, isArray: false},
            // 批量同意
            batchPass: {method: 'POST', params: {method: 'batchPass', ids: '@ids'}, isArray: false},
            // 批量拒绝
            batchDeny: {method: 'POST', params: {method: 'batchDeny', ids: '@ids'}, isArray: false},

            // 分页查询
            pageQuery: {
                method: 'POST',
                params: {method: 'pageQuery', limit: '@limit', start: '@start'},
                isArray: false
            },

            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    app.service('CustomerParam', function (ParameterLoader) {
        return {
            /**
             * 年龄段
             * @param callback
             */
            age: function (callback) {
                ParameterLoader.loadBusinessParam('AGE_STAGE', callback);
            },
            /**
             * 婚姻状况
             * @param callback
             */
            marriage: function (callback) {
                ParameterLoader.loadBusinessParam('BP_MARRIAGE', callback);
            },
            /**
             * 性别
             */
            sex: function (callback) {
                ParameterLoader.loadBusinessParam('BP_SEX', callback);
            },
            /**
             * 教育程度
             */
            education: function (callback) {
                ParameterLoader.loadBusinessParam('BP_EDU', callback);
            },
            /**
             * 财产规模
             */
            money: function (callback) {
                ParameterLoader.loadBusinessParam('MONEY_STAGE', callback);
            },
            /**
             * 交易类型
             */
            businessType: function (callback) {
                ParameterLoader.loadBusinessParam('ROOM_BUSINESS_TYPE', callback);
            }
        };
    });

    app.service('CustomerModal', ['$modal', 'CommonUtils', 'CustomerService', 'AlertFactory',
        function ($modal, CommonUtils, CustomerService, AlertFactory) {
            return {
                /**
                 * 选择一个客户
                 * @param options 配置项
                 * @param callback 回调
                 */
                pick: function (options, callback) {
                    var modal = $modal({
                        template: CommonUtils.contextPathURL('/app/house/customer/template/modal-customer.ftl.html'),
                        backdrop: 'static'
                    });
                    var $scope = modal.$scope;
                    options = options || {};
                    $scope.condition = angular.extend({}, options.condition);
                    callback = callback || options.callback;

                    // 分页对象
                    $scope.pager = {
                        limit: 5,
                        fetch: function () {
                            return CommonUtils.promise(function (defer) {
                                var param = angular.extend({status: 'ACTIVE'}, $scope.condition, options.condition, {
                                    start: $scope.pager.start,
                                    limit: $scope.pager.limit
                                });
                                var promise = CustomerService.pageQuery(param, function (data) {
                                    data = data.data || {};
                                    $scope.beans = data;
                                    defer.resolve(data.total);
                                });
                                CommonUtils.loading(promise, '加载中...');
                            });
                        },
                        finishInit: function () {
                            this.query();
                        }
                    };

                    // 清空查询条件
                    $scope.clear = function () {
                        $scope.condition = {};
                    };

                    // 查询
                    $scope.query = function () {
                        $scope.pager.query();
                    };

                    // 点击确认
                    $scope.confirm = function () {
                        if (angular.isFunction(callback)) {
                            callback.call($scope, $scope.selected);
                            modal.hide();
                        }
                    }
                }
            }
        }]);
})(angular);
