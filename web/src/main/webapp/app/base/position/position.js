/**
 * 岗位
 * Created by Michael on 2016-05-03 11:09:53.
 */
(function (angular) {
    var app = angular.module('base.position', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('PositionService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/base/position/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 根据id查询信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            // 加载整颗树
            tree: {method: 'GET', params: {method: 'tree'}, isArray: false},

            // 加载有效的树
            validTree: {method: 'GET', params: {method: 'tree-valid'}, isArray: false},

            // 分页查询
            pageQuery: {
                method: 'POST',
                params: {method: 'pageQuery', limit: '@limit', start: '@start'},
                isArray: false
            },

            // 批量禁用
            disable: {method: 'POST', params: {method: 'disable', ids: '@ids'}, isArray: false},

            // 批量启用
            enable: {method: 'POST', params: {method: 'enable', ids: '@ids'}, isArray: false},

            // 添加员工
            addEmp: {
                method: 'POST',
                params: {method: 'emp-add', positionId: '@positionId', empIds: '@empIds'},
                isArray: false
            },

            // 移除员工
            removeEmp: {
                method: 'POST',
                params: {method: 'emp-remove', positionId: '@positionId', empIds: '@empIds'},
                isArray: false
            },

            // 查询员工
            queryEmp: {
                method: 'POST', params: {
                    method: 'emp-query', positionId: '@positionId', limit: '@limit', start: '@start'
                },
                isArray: false
            }
        })
    });

    app.service('PositionParam', function (ParameterLoader) {
        return {};
    });

    app.service('PositionTree', function (CommonUtils, PositionService) {
        return {
            /**
             * 选择有效的树形
             * @param onClick
             * @returns {{data: PositionTree.data, click: *}}
             */
            pick: function (onClick) {
                return {
                    data: function () {
                        return CommonUtils.promise(function (defer) {
                            var promise = PositionService.validTree(function (data) {
                                defer.resolve(data.data || []);
                            });
                            CommonUtils.loading(promise);
                        });
                    },
                    click: onClick
                }
            },
            /**
             * 岗位树（显示所有的）
             * @param scope
             * @param id
             * @param onClick
             */
            tree: function (scope, id, onClick) {
                var setting = {
                    view: {
                        fontCss: function (treeId, node) {
                            // 如果已经被删除，则用灰色显示
                            if (node.deleted) {
                                return {'background-color': '#ddd', color: 'white'}
                            }
                            return {};
                        },
                        showIcon: false
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "parentId"
                        }
                    },
                    callback: {
                        onClick: function (event, treeId, treeNode) {
                            var obj = this.getZTreeObj(treeId);
                            if (angular.isFunction(onClick)) {
                                scope.$apply(function () {
                                    onClick.call(obj, treeNode);
                                });
                            }
                        }
                    }
                };

                var promise = PositionService.tree(function (data) {
                    data = data.data || [];
                    var treeObj = $.fn.zTree.init($("#" + id), setting, data);
                    defer.resolve(treeObj);
                });
                CommonUtils.loading(promise);
                var defer = CommonUtils.defer();
                return defer.promise;
            },

            /**
             * 岗位树（只查询有效的岗位）
             * 配置项：
             * scope：
             * id：树要展示的区域的dom ID
             * onClick：点击树形节点后的回调事件
             * showEmpCounts:false,是否显示员工数量
             * @param options
             */
            validTree: function (options) {
                if (!options) {
                    alert('请提供配置对象!');
                    return;
                }
                if (!options.scope) {
                    alert('配置对象中缺少scope对象!');
                    return;
                }
                if (!options.id) {
                    alert('配置对象中缺少ID，用于指定树形要显示的地方!');
                    return;
                }
                var setting = {
                    view: {
                        showIcon: false
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "parentId"
                        }
                    },
                    callback: {
                        onClick: function (event, treeId, treeNode) {
                            var obj = this.getZTreeObj(treeId);
                            if (angular.isFunction(options.onClick)) {
                                options.scope.$apply(function () {
                                    options.onClick.call(obj, treeNode);
                                });
                            }
                        }
                    }
                };

                var promise = PositionService.validTree(function (data) {
                    data = data.data || [];
                    var treeObj = $.fn.zTree.init($("#" + options.id), setting, data);
                    defer.resolve(treeObj);
                });
                CommonUtils.loading(promise);
                var defer = CommonUtils.defer();
                return defer.promise;
            }
        }
    });

    app.service('PositionModal', function ($modal, ModalFactory, AlertFactory, CommonUtils, PositionService) {
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
                var promise = PositionService.save($scope.beans, function (data) {
                    AlertFactory.success('保存成功!');
                    angular.isFunction(callback) && callback();
                    $scope.$hide();
                });
                CommonUtils.loading(promise, '保存中...');
            };

            $scope.update = function () {
                var promise = PositionService.update($scope.beans, function (data) {
                    AlertFactory.success('更新成功!');
                    angular.isFunction(callback) && callback();
                    $scope.$hide();
                });
                CommonUtils.loading(promise, '更新中...');
            };

            var load = function (id, callback) {
                var promise = PositionService.get({id: id}, function (data) {
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
            }
        }
    });


})(angular);
