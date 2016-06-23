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

            // 添加客户
            addCustomer: {method: 'POST', params: {method: 'customer', roomId: '@roomId'}, isArray: false},

            // 查询房屋的所属客户
            customer: {method: 'GET', params: {method: 'customer', roomId: '@roomId'}, isArray: false},

            // 查询房屋的所属小区
            building: {method: 'GET', params: {method: 'building', roomId: '@roomId'}, isArray: false},

            // 根据id查询信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},
            // 批量同意
            batchPass: {method: 'POST', params: {method: 'batchPass', ids: '@ids'}, isArray: false},
            // 批量拒绝
            batchDeny: {method: 'POST', params: {method: 'batchDeny', ids: '@ids'}, isArray: false},

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
        return {
            orient: function (callback) {
                ParameterLoader.loadBusinessParam('ORIENT', callback);
            },
            /**
             * 产权性质
             */
            property: function (callback) {
                ParameterLoader.loadBusinessParam('HOUSE_PROPERTY', callback);
            },
            /**
             * 现状
             */
            useType: function (callback) {
                ParameterLoader.loadBusinessParam('HOUSE_USE_TYPE', callback);

            },
            /**
             * 状态
             */
            status: function (callback) {
                ParameterLoader.loadSysParam('HOUSE_STATUS', callback);
            }
        };
    });

})(angular);
