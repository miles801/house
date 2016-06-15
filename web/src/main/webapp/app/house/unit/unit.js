/**
 * 单元管理
 * Created by Michael on 2016-06-16 04:36:39.
 */
(function (angular) {
    var app = angular.module('house.unit', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('UnitService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/house/unit/:method'), {}, {
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

            // 查询指定楼栋下的所有单元
            query: {method: 'POST', params: {method: 'query', blockId: '@blockId'}, isArray: false},

            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    app.service('UnitParam', function (ParameterLoader) {
        return {
            /**
             * 朝向
             */
            orient: function (callback) {
                ParameterLoader.loadBusinessParam('ORIENT', callback);
            }
        };
    });

})(angular);
