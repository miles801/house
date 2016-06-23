/**
 * 房屋交易管理
 * Created by Michael on 2016-06-16 06:23:23.
 */
(function (angular) {
    var app = angular.module('house.roomBusiness', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('RoomBusinessService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/house/roomBusiness/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 根据id查询信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            // 分页查询
            pageQuery: {
                method: 'POST',
                params: {
                    method: 'pageQuery',
                    limit: '@limit',
                    start: '@start',
                    orderBy: '@orderBy',
                    reverse: '@reverse'
                },
                isArray: false
            },

            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    app.service('RoomBusinessModal', function ($modal, CommonUtils, ModalFactory, AlertFactory, RoomBusinessService) {
        return {
            /**
             * @param roomId 房屋ID
             */
            query: function (roomId) {
                if (!roomId) {
                    AlertFactory.error('操作失败!未获取到房屋ID!');
                    return;
                }
                var modal = $modal({
                    template: CommonUtils.contextPathURL('/app/house/room/template/modal-roomBusiness.ftl.html'),
                    backdrop: 'static'
                });
                var $scope = modal.$scope;
                $scope.condition = {
                    roomId: roomId,
                    orderBy: 'occurDate',
                    reverse: true
                };

                $scope.pager = {
                    limit: 5,
                    fetch: function () {
                        var o = {start: this.start, limit: this.limit};
                        return CommonUtils.promise(function (defer) {
                            var promise = RoomBusinessService.pageQuery(angular.extend(o, $scope.condition), function (data) {
                                data = data.data || {total: 0};
                                $scope.beans = data;
                                defer.resolve(data);
                            });
                            CommonUtils.loading(promise);

                        });
                    },
                    finishInit: function () {
                        this.query();
                    }
                };

                $scope.query = function () {
                    $scope.pager.query();
                };

            }
        };
    });

})(angular);
