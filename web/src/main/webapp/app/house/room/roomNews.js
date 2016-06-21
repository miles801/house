/**
 * 房间管理
 * Created by Michael on 2016-06-16 06:23:23.
 */
(function (angular) {
    var app = angular.module('house.roomNews', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('RoomNewsService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/house/roomNews/:method'), {}, {
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

    app.service('RoomNewsModal', function ($modal, CommonUtils, ModalFactory, AlertFactory, RoomNewsService, ParameterLoader) {
        return {
            /**
             * options必须参数：roomId
             * @param options
             * @param callback
             */
            add: function (options, callback) {
                if (!options || !options.roomId) {
                    AlertFactory.error('操作失败!未获取到房屋ID!');
                    return;
                }
                var modal = $modal({
                    template: CommonUtils.contextPathURL('/app/house/room/template/modal-roomNews.ftl.html'),
                    backdrop: 'static'
                });
                var $scope = modal.$scope;
                $scope.beans = {
                    roomId: options.roomId
                };

                // 跟进方式
                $scope.types = [];
                ParameterLoader.loadBusinessParam('ROOM_NEWS_TYPE', function (data) {
                    $scope.types = data;
                    $scope.types.unshift({name: '请选择..'});
                });


                $scope.save = function () {
                    var o = {roomId: $scope.beans.roomId};
                    var name = '';
                    var type = $scope.beans.type;
                    for (var i = 0; i < $scope.types.length; i++) {
                        if ($scope.types[i].value == type) {
                            name = $scope.types[i].name;
                            break;
                        }
                    }

                    o.content = '跟进方式:' + name;
                    o.content += ('<br/>跟进内容：' + $scope.beans.content);
                    RoomNewsService.save(o, function () {
                        AlertFactory.success('保存成功!');
                        callback && callback();
                        modal.hide();
                    });
                };
            }
        };
    });

})(angular);
