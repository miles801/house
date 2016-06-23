/**
 * 房间管理列表
 * Created by Michael on 2016-06-16 06:23:23.
 */
(function (window, angular, $) {
    var app = angular.module('house.room.apply', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'house.room'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, RoomParam, RoomService) {
        $scope.condition = {
            orderBy: 'roomKey',
            statusInclude: ['APPLY_ADD', 'APPLY_MODIFY', 'APPLY_INVALID']
        };

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


        $scope.detail = function (id) {
            CommonUtils.addTab({
                title: '房屋明细',
                url: '/app/house/room/list/room_detail.jsp?id=' + id
            });
        };

        // 同意
        $scope.pass = function (id) {
            if (!id) {
                id = $scope.items.map(function (o) {
                    return o.id;
                }).join(',');
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '是否确认同意?',
                callback: function () {
                    var promise = RoomService.batchPass({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        // 拒绝
        $scope.deny = function (id) {
            if (!id) {
                id = $scope.items.map(function (o) {
                    return o.id;
                }).join(',');
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '是否确认拒绝?',
                callback: function () {
                    var promise = RoomService.batchDeny({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };
    });
})(window, angular, jQuery);