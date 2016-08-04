/**
 * 我关注的房间管理列表
 * Created by Michael on 2016-06-16 06:23:23.
 */
(function (window, angular, $) {
    var app = angular.module('house.room.star', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'house.room',
        'house.roomStar'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, RoomParam, RoomStarService) {

        var defaults = {
            orderBy: 'roomKey',
            manager: true
        };
        $scope.condition = angular.extend({}, defaults);

        // 重置查询条件
        $scope.reset = function () {
            $scope.condition = angular.extend({}, defaults);
            $scope.query();
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
                $scope.condition.start = this.start;
                $scope.condition.limit = this.limit;
                return CommonUtils.promise(function (defer) {
                    var promise = RoomStarService.pageQuery($scope.condition, function (data) {
                        $scope.beans = data.data || {};
                        defer.resolve(data.data);
                    });
                    CommonUtils.loading(promise);
                });
            }
            ,
            finishInit: function () {
                this.query();
            }
        }
        ;

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

        $scope.cancel = function (roomId) {
            ModalFactory.confirm({
                scope: $scope,
                content: '是否确定取消关注该房屋信息?',
                callback: function () {
                    var promise = RoomStarService.cancel({roomId: roomId}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };
    });
})(window, angular, jQuery);