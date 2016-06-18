/**
 * 房间管理
 * Created by Michael on 2016-06-16 06:23:23.
 */
(function (angular) {
    var app = angular.module('house.room', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('RoomService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/house/room/:method'), {}, {
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

            // 查询指定单元下的所有房间
            query: {method: 'POST', params: {method: 'query', unitId: '@unitId'}, isArray: false},

            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    app.service('RoomParam', function (ParameterLoader) {
        return {};
    });

})(angular);