/**
 * 房间管理列表
 * Created by Michael on 2016-06-16 06:23:23.
 */
(function (window, angular, $) {
    var app = angular.module('house.room.view', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'house.room'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, RoomParam, RoomService) {
        var defaults = {
            orderBy: 'roomKey',
            buildingId: $('#buildingId').val() || null,
            manager: $('#isManager').val()
        };
        $scope.condition = angular.extend({}, defaults);

        // 重置查询条件
        $scope.reset = function () {
            $scope.condition = angular.extend({}, defaults);
            $scope.query();
        };
        var condition = $scope.condition;

        // 房屋现状
        RoomParam.useType(function (data) {
            $scope.useType = data || [];
            $scope.useType.unshift({name: '全部...'});
        });
        // 状态
        RoomParam.status(function (data) {
            $scope.status = data || [];
            $scope.status.unshift({name: '全部...'});
        });
        // 朝向
        RoomParam.orient(function (data) {
            $scope.orient = data || [];
            $scope.orient.unshift({name: '全部...'});
        });

        $scope.pager = {
            fetch: function () {
                $scope.condition.start = this.start;
                $scope.condition.limit = this.limit;
                return CommonUtils.promise(function (defer) {
                    var promise = RoomService.pageQuery($scope.condition, function (data) {
                        $scope.beans = data.data || {};
                        defer.resolve(data.data);
                    });
                    CommonUtils.loading(promise);
                });
            },
            finishInit: function () {
                this.query();
            }
        };

        //查询数据
        $scope.query = function () {
            $scope.pager.query();
        };


        $scope.update = function (id) {
            CommonUtils.addTab({
                title: '录入房屋',
                url: 'house/room/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        $scope.detail = function (code) {
            CommonUtils.addTab({
                title: '房屋明细',
                url: '/house/room/view?code=' + code,
                onUpdate: $scope.query
            });

        };
        $scope.addCustomer = function (id) {
            CommonUtils.addTab({
                title: '业主录入',
                url: 'house/customer/add?roomId=' + id,
                onUpdate: $scope.query
            });
        };

        $scope.applyAdd = function (id) {
            if (!id) {
                id = $scope.items.map(function (o) {
                    return o.id;
                }).join(',');
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '是否对选中的房屋进行“新增申请”操作?',
                callback: function () {
                    var promise = RoomService.batchAdd({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };
        $scope.applyInvalid = function (id) {
            if (!id) {
                id = $scope.items.map(function (o) {
                    return o.id;
                }).join(',');
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '是否对选中的房屋进行“无效电话”操作?',
                callback: function () {
                    var promise = RoomService.applyInvalid({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        $scope.remove = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '确定要删除该数据?',
                callback: function () {
                    var promise = RoomService.deleteByIds({ids: id}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };
        // 导出数据
        $scope.exportData = function () {
            if ($scope.pager.total < 1) {
                AlertFactory.error('未获取到可以导出的数据!请先查询出数据!');
                return;
            }
            var o = angular.extend({}, $scope.condition);
            o.start = null;
            o.limit = null;
            window.open(CommonUtils.contextPathURL('/house/room/export?' + encodeURI(encodeURI($.param(o)))));
        };
    });
})(window, angular, jQuery);