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

        // 强制删除!
        $scope.forceDelete = function () {
            var ids = [];
            angular.forEach($scope.items, function (o) {
                if (o.id) {
                    ids.push(o.id);
                }
            });
            if (ids.length == 0) {
                AlertFactory.error('无可以删除的数据!请选择后重试!');
                return;
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '警告!该功能是高风险操作!一旦确认，将会删除楼栋及楼栋下的所有单元和房屋信息，请仔细确认后再行删除,确认删除?',
                callback: function () {
                    var promise = BlockService.forceDelete({ids: ids.join(',')}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.query();
                    });
                    CommonUtils.loading((promise));
                }
            });
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
                var o = {
                    buildingId: buildingId,
                    unitCounts: bean.unitCounts,
                    physicalLevels: bean.physicalLevels,
                    levels: bean.levels
                };
                $scope.beans.push(o);
            }
        };

        // 保存
        $scope.save = function (bean) {
            var promise = BlockService.save(bean, function (data) {
                bean.id = data.data;
                bean.$valid = false;
                AlertFactory.success('保存成功!');
            });
            CommonUtils.loading(promise);
        };

        // 更新
        $scope.update = function (bean) {
            var promise = BlockService.update(bean, function () {
                AlertFactory.success('更新成功!');
                bean.$valid = false;
            });
            CommonUtils.loading(promise);

        };

        // 清除单元
        $scope.clearUnit = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '是否要清除该楼栋下已经创建的单元信息，这个一个敏感操作，请仔细确认!',
                callback: function () {
                    var promise = BlockService.clearUnit({id: id}, function () {
                        AlertFactory.success('清除成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        // 自动创建单元
        $scope.createUnit = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '是否要自动创建该楼栋下的所有预设的单元，请仔细确认!',
                callback: function () {
                    var promise = BlockService.createUnit({id: id}, function () {
                        AlertFactory.success('创建成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        $scope.check = function (bean) {
            bean.$valid = false;
            var flag = true;
            if (!bean.code || !bean.physicalLevels || !bean.levels) {
                flag = false;
            }
            bean.$valid = flag;
        };
        $scope.query();
    });
})(window, angular, jQuery);