/**
 * 房间管理
 * Created by Michael on 2016-06-16 06:23:23.
 */
(function (angular) {
    var app = angular.module('house.roomStar', [
        'ngResource',
        'eccrm.angular',
        'eccrm.base.param',
        'eccrm.angularstrap'
    ]);

    app.service('RoomStarService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/house/roomStar/:method'), {}, {
            // 关注
            star: {method: 'POST', params: {method: 'star', roomId: '@roomId'}, isArray: false},

            // 取消关注
            cancel: {method: 'POST', params: {method: 'cancel', roomId: '@roomId'}, isArray: false},

            // 判断指定的房屋是否已经关注
            isStar: {method: 'GET', params: {method: 'isStar', roomId: '@roomId'}, isArray: false},

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
})(angular);
