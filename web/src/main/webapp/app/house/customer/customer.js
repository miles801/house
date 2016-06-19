/**
 * 业主管理
 * Created by Michael on 2016-06-19 15:16:56.
 */
(function (angular) {
    var app = angular.module('house.customer', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('CustomerService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/house/customer/:method'), {}, {
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

    app.service('CustomerParam', function (ParameterLoader) {
        return {
            /**
             * 年龄段
             * @param callback
             */
            age: function (callback) {
                ParameterLoader.loadBusinessParam('AGE_STAGE', callback);
            },
            /**
             * 婚姻状况
             * @param callback
             */
            marriage: function (callback) {
                ParameterLoader.loadBusinessParam('BP_MARRIAGE', callback);
            },
            /**
             * 性别
             */
            sex: function (callback) {
                ParameterLoader.loadBusinessParam('BP_SEX', callback);
            },
            /**
             * 教育程度
             */
            education: function (callback) {
                ParameterLoader.loadBusinessParam('BP_EDU', callback);
            },
            /**
             * 财产规模
             */
            money: function (callback) {
                ParameterLoader.loadBusinessParam('MONEY_STAGE', callback);
            }
        };
    });

})(angular);
