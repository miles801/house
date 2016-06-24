(function (window, angular, $) {
    //获取模块
    var app = angular.module("eccrm.panel.base.list", [
        'eccrm.angular',
        'eccrm.angularstrap',
        'base.emp'
    ]);


    //
    app.controller('Ctrl', function (EmpService, $scope, CommonUtils, AlertFactory) {
        var empId = $('#empId').val();
        var promise = EmpService.get({id: empId}, function (data) {
            $scope.beans = data.data || {};
        });
        CommonUtils.loading(promise);
    });


})(window, angular, jQuery);
