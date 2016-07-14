/**
 * 楼栋管理列表
 * Created by Michael on 2016-06-12 01:19:26.
 */
(function (window, angular, $) {
    var app = angular.module('house.block.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'house.block'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, BlockService) {

        $scope.beans = [];

        var buildingId = $('#buildingId').val();
        if (!buildingId) {
            AlertFactory.error('错误的访问方式!没有获得楼盘ID!');
            return false;
        }

        $scope.condition = {
            buildingId: buildingId
        };

        //查询数据
        $scope.query = function () {
            var promise = BlockService.query($scope.condition, function (data) {
                $scope.beans = data.data || [];
            });

            CommonUtils.loading(promise);
        };


        // 删除或批量删除
        $scope.remove = function (id, index) {
            if (!id) {
                $scope.beans.splice(index, 1);
                return;
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '<span class="text-danger">数据一旦删除将不可恢复，请确认!</span>',
                callback: function () {
                    var promise = BlockService.deleteByIds({ids: id}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.beans.splice(index, 1);
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 新增
        $scope.add = function (bean) {
            if (!bean) {
                $scope.beans.push({buildingId: buildingId});
            } else {
                var o = angular.extend({}, bean);
                o.code = null;
                o.id = null;
                $scope.beans.push(o);
            }
        };

        // 更新
        $scope.save = function (bean, form) {
            var promise;
            if (!bean.id) {
                promise = BlockService.save(bean, function (data) {
                    bean.id = data.data;
                    form.$setDirty(false);
                    AlertFactory.success('保存成功!');
                });
            } else {
                promise = BlockService.update(bean, function () {
                    AlertFactory.success('更新成功!');
                    form.$setDirty(false);
                });
            }
            CommonUtils.loading(promise);
        };

        $scope.query();
    });
})(window, angular, jQuery);