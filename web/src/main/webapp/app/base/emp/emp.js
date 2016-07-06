/**
 * 员工
 * Created by Michael on 2016-04-25 13:24:54.
 */
(function (angular) {
    var app = angular.module('base.emp', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('EmpService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/base/emp/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save', attachmentIds: '@icon'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update', attachmentIds: '@icon'}, isArray: false},

            // 根据id查询信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            // 分页查询
            pageQuery: {
                method: 'POST',
                params: {method: 'pageQuery', limit: '@limit', start: '@start'},
                isArray: false
            },

            // 更新密码
            updatePwd: {method: 'POST', params: {method: 'update-pwd'}, isArray: false},

            // 启用员工
            start: {method: 'POST', params: {method: 'start', ids: '@ids'}, isArray: false},


            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    app.service('EmpParam', function (ParameterLoader) {
        return {};
    });

    app.service('EmpModal', function ($modal, ModalFactory, AlertFactory, CommonUtils, EmpService) {
        var common = function (options, callback) {
            var defaults = {
                id: null,//id
                pageType: null,     // 必填项,页面类型add/modify/view
                callback: null,     // 点击确定后要执行的函数
                afterShown: null    // 模态对话框显示完成后要执行的函数
            };
            options = angular.extend({}, defaults, options);
            callback = callback || options.callback;
            var modal = $modal({
                template: CommonUtils.contextPathURL('app/base/parameter/template/param-type-modal.ftl.html'),
                backdrop: 'static'
            });
            var $scope = modal.$scope;
            var pageTypes = ['add', 'modify', 'view'];
            var pageType = $scope.pageType = options.pageType;
            if ($.inArray(options.pageType, pageTypes) == -1) {
                CommonUtils.errorDialog('不合法的页面类型!');
                throw '不合法的页面类型，仅支持[' + pageTypes.join(',') + ']类型!';
            }
            var id = options.id;
            $scope.save = function () {
                var promise = EmpService.save($scope.beans, function (data) {
                    AlertFactory.success('保存成功!');
                    angular.isFunction(callback) && callback();
                    $scope.$hide();
                });
                CommonUtils.loading(promise, '保存中...');
            };

            $scope.update = function () {
                var promise = EmpService.update($scope.beans, function (data) {
                    AlertFactory.success('更新成功!');
                    angular.isFunction(callback) && callback();
                    $scope.$hide();
                });
                CommonUtils.loading(promise, '更新中...');
            };

            var load = function (id, callback) {
                var promise = EmpService.get({id: id}, function (data) {
                    $scope.beans = data.data || {};
                    if (angular.isFunction(callback)) {
                        callback($scope.beans);
                    }
                });
                CommonUtils.loading(promise, 'Loading...');
            };

            if (pageType == 'add') {
                $scope.beans = {};
            } else if (pageType == 'modify') {
                load(id);
            } else {
                load(id, function () {
                    $('.modal-body').find('input,select,textarea,.icon').attr('disabled', 'disabled');
                });
            }
        };
        return {
            add: function (options, callback) {
                var o = angular.extend({}, options, {pageType: 'add'});
                common(o, callback);
            },
            modify: function (options, callback) {
                if (!options.id) {
                    CommonUtils.errorDialog('没有获得ID');
                    throw '没有获得ID!';
                }
                var o = angular.extend({}, options, {pageType: 'modify'});
                common(o, callback);
            },
            view: function (options, callback) {
                if (!options.id) {
                    CommonUtils.errorDialog('没有获得ID');
                    throw '没有获得ID!';
                }
                var o = angular.extend({}, options, {pageType: 'view'});
                common(o, callback);
            },
            /**
             * @param callback 成功后的回调
             */
            updatePwd: function (callback) {
                var modal = $modal({
                    template: CommonUtils.contextPathURL('/app/base/emp/tempalte/pwd-modify.html'),
                    backdrop: 'static'
                });
                var $scope = modal.$scope;
                $scope.beans = {
                    newPwd: null,    // 新密码
                    oldPwd: null     // 原始密码
                };

                // 执行密码更新
                $scope.update = function () {
                    var promise = EmpService.updatePwd($scope.beans, function () {
                        if (angular.isFunction(callback)) {
                            callback();
                        }
                        $scope.$hide();
                    });
                    CommonUtils.loading(promise);
                };
            }
        };
    });
})(angular);
