/**
 * 楼盘管理
 * Created by Michael on 2016-06-11 21:45:57.
 */
(function (angular) {
    var app = angular.module('house.building', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('BuildingService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/house/building/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 根据id查询信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            // 分页查询
            pageQuery: {
                method: 'POST',
                params: {method: 'pageQuery', limit: '@limit', start: '@start'},
                isArray: false
            },

            // 更新楼盘负责人
            updateMaster: {method: 'POST', params: {method: 'master', id: '@id', empId: '@empId'}, isArray: false},
            // 添加楼盘维护人员
            addMaintain: {
                method: 'POST',
                params: {method: 'maintian-add', id: '@id', empIds: '@empIds'},
                isArray: false
            },

            // 更新楼盘负责人
            removeMaintain: {
                method: 'POST',
                params: {method: 'maintian-delete', id: '@id', empId: '@empId'},
                isArray: false
            },

            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    app.service('BuildingParam', function (ParameterLoader) {
        return {
            /**
             * 建筑类型
             */
            type: function (callback) {
                ParameterLoader.loadBusinessParam('BUILDING_TYPE', callback);
            },

            /**
             * 房屋权属
             */
            houseType: function (callback) {
                ParameterLoader.loadBusinessParam('HOUSE_TYPE', callback);
            },

            /**
             * 房屋用途
             */
            usage: function (callback) {
                ParameterLoader.loadBusinessParam('USAGE', callback);
            },

            /**
             * 供暖方式
             */
            warmType: function (callback) {
                ParameterLoader.loadBusinessParam('WARM_TYPE', callback);
            },

            /**
             * 距离地铁
             */
            subway: function (callback) {
                ParameterLoader.loadBusinessParam('SUBWAY', callback);
            }
        };
    });

    app.service('BuildingModal', function ($modal, CommonUtils, ModalFactory, AlertFactory, BuildingService, EmployeeModal) {
        return {
            /**
             * {
             *  id:楼盘ID
             * }
             * @param options
             * @param callback 回调函数
             */
            maintain: function (options, callback) {
                var modal = $modal({
                    template: CommonUtils.contextPathURL('/app/house/building/list/template/building-maintain.ftl.html'),
                    backdrop: 'static'
                });
                var id = options.id;
                if (!id) {
                    AlertFactory.error('初始化失败!未获取到楼盘ID！');
                    return;
                }
                var $scope = modal.$scope;
                var load = function () {
                    BuildingService.get({id: id}, function (data) {
                        data = data.data;
                        if (!data) {
                            AlertFactory.error('楼盘不存在!请关闭该页面后刷新页面重试!');
                            return;
                        }
                        $scope.name = data.name;
                        $scope.beans = [];
                        var ids = (data.maintainId || '').split(';')
                        var names = (data.maintainName || '').split(';');
                        angular.forEach(ids, function (id, index) {
                            if (id) {
                                $scope.beans.push({id: id, name: names[index]});
                            }
                        });
                    });
                };
                load();

                // 移除维护人员
                $scope.remove = function (index, empId, empName) {
                    ModalFactory.confirm({
                        scope: $scope,
                        content: '是否要将[' + empName + ']从维护人中移除，请确认!',
                        callback: function () {
                            var promise = BuildingService.removeMaintain({id: id, empId: empId}, function () {
                                AlertFactory.success('移除成功!');
                                $scope.beans.splice(index, 1);

                                // 回调
                                if (angular.isFunction(callback)) {
                                    callback();
                                }
                            });
                            CommonUtils.loading(promise);
                        }
                    });
                };

                // 添加维护人员
                $scope.add = function () {
                    EmployeeModal.pickMultiEmployee({}, function (emps) {
                        ModalFactory.confirm({
                            scope: $scope,
                            content: '将选中的员工加入到维护人员列表中，请确认!',
                            callback: function () {
                                var ids = [];
                                angular.forEach(emps, function (e) {
                                    ids.push(e.id);
                                });
                                var promise = BuildingService.addMaintain({id: id, empIds: ids.join(',')}, function () {
                                    AlertFactory.success('操作成功!');
                                    load();

                                    // 回调
                                    if (angular.isFunction(callback)) {
                                        callback();
                                    }
                                });
                                CommonUtils.loading(promise);
                            }
                        });
                    });
                };

                $scope.close = function () {
                    if (angular.isFunction(options.callback)) {
                        options.callback();
                    }
                    $scope.$hide();
                }
            }
        }
    });

})(angular);
