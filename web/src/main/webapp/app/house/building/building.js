/**
 * 楼盘管理
 * Created by Michael on 2016-06-11 21:45:57.
 */
(function (angular) {
    var app = angular.module('house.building', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('BuildingService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/house/building/:method'), {}, {
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

            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    app.service('BuildingParam', function (ParameterLoader) {
        return {
            /**
             * 建筑类型
             */
            type: function (callback) {
                ParameterLoader.loadBusinessParam('BUILDING_TYPE', callback);
            },

            /**
             * 房屋权属
             */
            houseType: function (callback) {
                ParameterLoader.loadBusinessParam('HOUSE_TYPE', callback);
            },

            /**
             * 房屋用途
             */
            usage: function (callback) {
                ParameterLoader.loadBusinessParam('USAGE', callback);
            },

            /**
             * 供暖方式
             */
            warmType: function (callback) {
                ParameterLoader.loadBusinessParam('WARM_TYPE', callback);
            },

            /**
             * 距离地铁
             */
            subway: function (callback) {
                ParameterLoader.loadBusinessParam('SUBWAY', callback);
            }
        };
    });

})(angular);
