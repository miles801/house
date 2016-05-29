(function (window, angular, $) {
    //获取模块
    var app = angular.module("eccrm.panel.base.list", [
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);


    //
    app.controller('Ctrl', function ($http, $scope, CommonUtils, AlertFactory) {

        $scope.api = function () {
            $http.get(CommonUtils.contextPathURL('/api'))
                .success(function (data) {
                    $scope.apis = data.data || {};
                });
        };
    });


})(window, angular, jQuery);
