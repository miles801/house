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
        $scope.condition = {
            orderBy: 'roomKey',
            statusInclude: ['INACTIVE', 'ACTIVE']
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


        $scope.update = function (id) {
            CommonUtils.addTab({
                title: '录入房屋',
                url: 'house/room/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        $scope.detail = function (id) {
            CommonUtils.addTab({
                title: '房屋明细',
                url: '/app/house/room/list/room_detail.jsp?id=' + id
            });

        };
        $scope.addCustomer = function (id, customerId) {
            CommonUtils.addTab({
                title: '业主录入',
                url: 'house/customer/add?id=' + customerId + '&roomId=' + id,
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
                content: '是否确定将选中的房屋申请为“正常”客户?',
                callback: function () {
                    var promise = RoomService.batchAdd({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };
    });
})(window, angular, jQuery);