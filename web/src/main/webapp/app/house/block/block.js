/**
 * 楼栋管理
 * Created by Michael on 2016-06-12 01:19:26.
 */
(function (angular) {
    var app = angular.module('house.block', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('BlockService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/house/block/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 创建单元
            createUnit: {method: 'POST', params: {method: 'createUnit', id: '@id'}, isArray: false},

            // 清除单元
            clearUnit: {method: 'POST', params: {method: 'clearUnit', id: '@id'}, isArray: false},

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
            // 查询指定楼盘下所有的
            query: {method: 'POST', params: {method: 'query', buildingId: '@buildingId'}, isArray: false},

            // 强制删除
            forceDelete: {method: 'DELETE', params: {method: 'delete-force', ids: '@ids'}, isArray: false},
            
            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    app.service('BlockParam', function (ParameterLoader) {
        return {};
    });

})(angular);
