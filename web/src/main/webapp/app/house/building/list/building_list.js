/**
 * Created by Michael on 2016-06-11 21:45:57.
 */
(function (window, angular, $) {
    var app = angular.module('house.building.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'house.building'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, BuildingService, BuildingParam) {
        $scope.condition = {};

        // 学区
        $scope.types = [{name: '全部'}];
        BuildingParam.type(function (data) {
            $scope.types.unshift.apply($scope.types, data);
        });
        //查询数据
        $scope.query = function () {
            $scope.pager.query();
        };

        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, {start: this.start, limit: this.limit}, $scope.condition);
                $scope.beans = [];
                return CommonUtils.promise(function (defer) {
                    var promise = BuildingService.pageQuery(param, function (data) {
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
            ModalFactory.confirm({
                scope: $scope,
                content: '数据一旦删除将不可恢复，请确认!',
                callback: function () {
                    var promise = BuildingService.deleteByIds({ids: id}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.query();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 新增
        $scope.add = function () {
            $scope.closeTab();
            CommonUtils.addTab({
                title: '新增楼盘管理',
                url: '/house/building/add',
                onUpdate: $scope.query
            });
        };

        // 更新
        $scope.modify = function (id) {
            $scope.closeTab();
            CommonUtils.addTab({
                title: '更新楼盘管理',
                url: '/house/building/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        // 查看明细
        $scope.view = function (id) {
            $scope.closeTab();
            CommonUtils.addTab({
                title: '查看楼盘管理',
                url: '/house/building/detail?id=' + id
            });
        };

        // 关闭其他页签
        $scope.closeTab = function () {
            $(window.parent.document.body).find('ul.nav-tabs>li>span.icons.fork').trigger('click');
        };


        $scope.closeTab();
    });
})(window, angular, jQuery);