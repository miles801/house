/**
 * 单元管理列表
 * Created by Michael on 2016-06-16 04:36:39.
 */
(function (window, angular, $) {
    var app = angular.module('house.unit.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'house.block',
        'house.unit'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, BlockService, UnitService, UnitParam) {
        $scope.beans = [];

        var buildingId = $('#buildingId').val();
        if (!buildingId) {
            AlertFactory.error('错误的访问方式!没有获得楼盘ID!');
            return false;
        }

        // 朝向
        UnitParam.orient(function (data) {
            $scope.orients = data || [];
            $scope.orients.unshift({name: '请选择...'});
        });


        $scope.blocks = [];
        // 获取楼栋列表
        $scope.loadBlock = function () {
            var promise = BlockService.query({buildingId: buildingId}, function (data) {
                $scope.blocks = data.data || [];
                if ($scope.blocks.length < 1) {
                    AlertFactory.error('未获取到楼栋信息!请先维护楼栋信息，然后重新加载本页面!');
                }
                $scope.blocks.unshift({code: '请选择..'});
            });
            CommonUtils.loading(promise);
        };

        $scope.blockChange = function () {
            $scope.query();
            // 获取当前的楼栋编号
            var blockId = $scope.condition.blockId;
            for (var i = 0; i < $scope.blocks.length; i++) {
                if ($scope.blocks[i].id == blockId) {
                    $scope.blockCode = $scope.blocks[i].code;
                    return;
                }
            }
        };

        // 重新加载该页面
        $scope.reload = function () {
            window.location.reload();
        };


        $scope.condition = {};

        //查询数据
        $scope.query = function () {
            var promise = UnitService.query($scope.condition, function (data) {
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
                    var promise = UnitService.deleteByIds({ids: id}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.beans.splice(index, 1);
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        // 新增
        $scope.add = function (bean) {
            if (!bean) {
                $scope.beans.push({blockId: $scope.condition.blockId});
            } else {
                var o = angular.extend({}, bean);
                o.code = null;
                o.id = null;
                $scope.beans.push(o);
            }
        };

        $scope.typeValid = {
            validateMsg: '格式必须是:x室x厅x厨x卫这样的数据，例如：3-2-1-2',
            validateFn: function (value) {
                return /\d-\d-\d-\d/g.test(value);
            }
        };

        // 更新
        $scope.save = function (bean) {
            var promise = UnitService.save(bean, function (data) {
                bean.id = data.data;
                bean.$valid = false;
                AlertFactory.success('保存成功!');
            });
            CommonUtils.loading(promise);
        };

        $scope.update = function (bean) {
            var promise = UnitService.update(bean, function () {
                AlertFactory.success('更新成功!');
                bean.$valid = false;
            });
            CommonUtils.loading(promise);
        };

        $scope.check = function (bean) {
            bean.$valid = false;
            var flag = true;
            if (!bean.code || !bean.doorCode || !bean.square || !bean.orient || !bean.type) {
                flag = false;
            }
            bean.$valid = flag;
        };

        $scope.loadBlock();
    });
})(window, angular, jQuery);