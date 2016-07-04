/**
 * <p>
 *     依赖：eccrm-ztree-all.js
 * </p>
 * Created by Michael on 2014/10/26.
 */
(function () {
    var app = angular.module('eccrm.base.employee.modal', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.angular.ztree'
    ]);

    // 访问员工的后台服务
    app.service('EmpService', ['$resource', 'CommonUtils', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL('/base/emp/:method:_tmp'), {}, {

            pageQuery: {method: 'POST', params: {method: 'pageQuery', start: '@start', limit: '@limit'}, isArray: false}

        });
    }]);

    // 提供员工选择相关的弹出层
    app.service('EmployeeModal', ['$modal', 'CommonUtils', 'EmpService', 'AlertFactory', 'ModalFactory',
        function ($modal, CommonUtils, EmpService, AlertFactory, ModalFactory) {
            return {
                /**
                 * 选择一个员工
                 * @param options 配置项
                 * @param callback 回调
                 */
                pickEmployee: function (options, callback) {
                    var modal = $modal({
                        template: CommonUtils.contextPathURL('/app/employee/template/modal-employee.ftl.html'),
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
                                var obj = angular.extend({locked: 0}, $scope.condition, options.condition, {
                                    start: $scope.pager.start,
                                    limit: $scope.pager.limit
                                });
                                var promise = EmpService.pageQuery(obj);
                                CommonUtils.loading(promise, '加载中...', function (data) {
                                    data = data.data || {};
                                    $scope.emps = data;
                                    defer.resolve(data.total);
                                }, $scope);
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
                },

                /**
                 * 多选员工
                 * @param options 配置项
                 * @param callback 回调
                 */
                pickMultiEmployee: function (options, callback) {
                    var modal = $modal({
                        template: CommonUtils.contextPathURL('/app/employee/template/modal-employee-multi.ftl.html'),
                        backdrop: 'static'
                    });
                    var $scope = modal.$scope;
                    options = options || {};
                    $scope.condition = angular.extend({}, options.condition);
                    callback = callback || options.callback;

                    // 分页对象
                    $scope.pager = {
                        limit: 10,
                        fetch: function () {
                            return CommonUtils.promise(function (defer) {
                                var obj = angular.extend({locked: 0}, $scope.condition, options.condition, {
                                    start: $scope.pager.start,
                                    limit: $scope.pager.limit
                                });
                                var promise = EmpService.pageQuery(obj);
                                CommonUtils.loading(promise, '加载中...', function (data) {
                                    data = data.data || {};
                                    $scope.emps = data;
                                    defer.resolve(data.total);
                                }, $scope);
                            });
                        }, finishInit: function () {
                            this.query();
                        }
                    };

                    // 清空查询条件
                    $scope.clear = function () {
                        $scope.condition = {};
                        $scope.orgName = null;
                    };

                    $scope.remove = function (index) {
                        $scope.items.splice(index, 1);
                    };

                    // 查询
                    $scope.query = function () {
                        $scope.pager.query();
                    };

                    // 点击确认
                    $scope.confirm = function () {
                        if (angular.isFunction(callback)) {
                            callback.call($scope, $scope.items);
                            modal.hide();
                        }
                    };

                    ModalFactory.afterShown(modal, function () {
                        $('input').bind('keydown', function (e) {
                            var code = e.which || e.keyCode;
                            if (code == 13) {
                                $scope.query();
                                e.preventDefault();
                                return;
                            }
                        });

                        $scope.$on('destroy', function () {
                            alert('移除');
                            $('input').unbind('keydown');
                        });
                    });

                }
            }
        }]);
})();