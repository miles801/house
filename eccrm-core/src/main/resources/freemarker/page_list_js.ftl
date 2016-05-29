/**
* Created by ${author!'CODE GENERATOR'} <#if current??>on ${current}</#if>.
*/
(function (window, angular, $) {
    var app = angular.module('${name}.${entity}.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        '${name}.${entity}'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, ${className}Service, ${className}Param) {
        $scope.condition = { };

        //查询数据
        $scope.query = function() {
            $scope.pager.query();
        };

    <#if listPage.allowPager==true>
        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, {start: this.start, limit: this.limit}, $scope.condition);
                $scope.beans = [];
                return CommonUtils.promise(function(defer){
                    var promise = ${className}Service.pageQuery(param, function(data){
                        param = null;
                        $scope.beans = data.data || {total: 0};
                        defer.resolve($scope.beans);
                    });
                    CommonUtils.loading(promise, 'Loading...');
                });
            }
        }
    </#if>

        // 删除或批量删除
        $scope.remove = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '数据一旦删除将不可恢复，请确认!',
                callback: function () {
                    var promise = ${className}Service.deleteByIds({ids: id}, function(){
                        AlertFactory.success('删除成功!');
                        $scope.query();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 新增
        $scope.add = function () {
            CommonUtils.addTab({
                title: '新增${cnName}',
                url: '/${name}/${entity}/add',
                onUpdate: $scope.query
            });
        };

        // 更新
        $scope.modify = function (id) {
            CommonUtils.addTab({
                title: '更新${cnName}',
                url: '/${name}/${entity}/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        // 查看明细
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看${cnName}',
                url: '/${name}/${entity}/detail?id=' + id
            });
        }
    });
})(window, angular, jQuery);