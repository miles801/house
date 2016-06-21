/**
 * 业主管理编辑
 */
(function (window, angular, $) {
    var app = angular.module('house.room.detail', [
        'house.room',
        'house.roomStar',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, RoomParam, RoomService, RoomStarService) {

        var id = $('#id').val();

        $scope.back = CommonUtils.back;


        // 朝向
        RoomParam.orient(function (data) {
            $scope.orients = data || [];
            $scope.orients.unshift({name: '请选择...'});
        });
        // 房屋现状
        RoomParam.useType(function (data) {
            $scope.useType = data || [];
            $scope.useType.unshift({name: '请选择...'});
        });

        // 更新
        $scope.update = function () {
            var promise = RoomService.update($scope.beans, function (data) {
                AlertFactory.success('更新成功!');
                $scope.form.$setValidity('committed', false);
                CommonUtils.addTab('update');
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise, '更新中...');
        };

        // 加载数据
        $scope.load = function (id) {
            var promise = RoomService.get({id: id}, function (data) {
                $scope.beans = data.data || {};
            });
            CommonUtils.loading(promise, 'Loading...');
        };

        // 修改房屋信息
        $scope.modifyRoom = function (id) {
            CommonUtils.addTab({
                title: '房屋修改',
                url: 'house/room/modify?id=' + id,
                onUpdate: function () {
                    $scope.load(id);
                }
            });
        };
        // 录入跟进
        $scope.addInfo = function (id) {
            // FIXME 未实现
        };
        // 添加关注
        $scope.star = function (id) {
            var promise = RoomStarService.star({roomId: id}, function () {
                AlertFactory.success('关注成功!');
            });
            CommonUtils.loading(promise);
        };

        // 查看成交记录
        $scope.viewBuyLog = function () {
            // FIXME 未实现
        };

        $scope.load(id);

    });
})(window, angular, jQuery);