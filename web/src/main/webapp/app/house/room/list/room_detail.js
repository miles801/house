/**
 * 业主管理编辑
 */
(function (window, angular, $) {
    var app = angular.module('house.room.detail', [
        'house.room',
        'house.customer',
        'house.roomStar',
        'house.roomNews',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, RoomParam, RoomService,
                                     RoomStarService, RoomNewsService, RoomNewsModal, CustomerService) {

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
                    $scope.loadNews();
                }
            });
        };
        // 录入跟进
        $scope.addInfo = function (id) {
            RoomNewsModal.add({roomId: id}, $scope.loadNews);
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

        // 查看小区信息
        $scope.loadBuilding = function () {
            var promise = RoomService.building({roomId: id}, function (data) {
                $scope.building = data.data || {};
            });
            CommonUtils.loading(promise);

        };
        // 查看业主信息
        $scope.loadCustomer = function () {
            var promise = RoomService.customer({roomId: id}, function (data) {
                $scope.customer = data.data || {};
            });
            CommonUtils.loading(promise);
        };
        $scope.pager = {
            fetch: function () {
                var start = this.start;
                var limit = this.limit;
                return CommonUtils.promise(function (defer) {
                    var promise = RoomNewsService.pageQuery({
                        roomId: id,
                        start: start,
                        limit: limit,
                        orderBy: 'createdDatetime',
                        reverse: true
                    }, function (data) {
                        $scope.news = data.data || {};
                        defer.resolve($scope.news);
                    });
                    CommonUtils.loading(promise);
                });
            },
            finishInit: function () {
                this.query();
            }
        };
        // 查看最新动态
        $scope.loadNews = function () {
            $scope.pager.query();
        };

        // 变更业主信息
        $scope.changeCustomer = function (customerId) {
            CommonUtils.addTab({
                title: '业主信息',
                url: 'house/customer/add?id=' + customerId + '&roomId=' + id,
                onUpdate: function () {
                    window.location.reload();
                }
            })
        };

        // 申请无效
        $scope.applyInvalid = function (customerId) {
            ModalFactory.confirm({
                scope: $scope,
                content: '确定要将该客户申请为无效客户吗?',
                callback: function () {
                    var promise = CustomerService.applyInvalid({id: customerId}, function () {
                        AlertFactory.success('操作成功!页面即将返回!');
                        CommonUtils.delay($scope.back, 2000);
                    });
                    CommonUtils.loading(promise);
                }
            });
        };
        //
        $scope.load(id);
        $scope.loadBuilding();
        $scope.loadCustomer();
        // $scope.loadNews();
    });
})(window, angular, jQuery);