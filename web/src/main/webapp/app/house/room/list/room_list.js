/**
 * 房间管理列表
 * Created by Michael on 2016-06-16 06:23:23.
 */
(function (window, angular, $) {
    var app = angular.module('house.room.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'house.block',
        'house.unit',
        'house.room'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, UnitParam, UnitService, BlockService, RoomService) {
        $scope.beans = [];
        $scope.condition = {};
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
        $scope.units = [{code: '请选择楼栋'}];
        // 获取单元列表
        $scope.loadUnit = function () {
            var promise = UnitService.query({blockId: $scope.condition.blockId}, function (data) {
                data = data.data || [];
                if (data < 1) {
                    AlertFactory.error('未获取到单元信息!请先维护单元信息，然后重新加载本页面!');
                }
                data.unshift($scope.units[0]);
                $scope.units = data;
            });
            CommonUtils.loading(promise);
        };


        $scope.blockChange = function () {
            $scope.loadUnit();
            // 获取当前的楼栋编号
            var blockId = $scope.condition.blockId;
            for (var i = 0; i < $scope.blocks.length; i++) {
                if ($scope.blocks[i].id == blockId) {
                    $scope.blockCode = $scope.blocks[i].code;
                    return;
                }
            }
        };

        $scope.unit = {};
        $scope.unitChange = function () {
            $scope.query();
            var promise = UnitService.get({id: $scope.condition.unitId}, function (data) {
                $scope.unit = data.data || {};
            });
            CommonUtils.loading(promise);
        };


        //查询数据
        $scope.query = function () {
            var promise = RoomService.query($scope.condition, function (data) {
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
                    var promise = RoomService.deleteByIds({ids: id}, function () {
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
                var unit = $scope.unit;
                var newItem = {
                    buildingId: buildingId,
                    blockId: $scope.condition.blockId, blockCode: $scope.blockCode,
                    unitId: unit.id, unitCode: unit.code, code: unit.doorCode,
                    square: unit.square, orient: unit.orient
                };
                if (unit.type && /\d-\d-\d-\d/.test(unit.type)) {
                    var types = unit.type.split('-');
                    newItem.type1 = types[0] || 0;
                    newItem.type2 = types[1] || 0;
                    newItem.type3 = types[2] || 0;
                    newItem.type4 = types[3] || 0;
                }
                $scope.beans.push(newItem);
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
                promise = RoomService.save(bean, function (data) {
                    bean.id = data.data;
                    form.$setDirty(false);
                    AlertFactory.success('保存成功!');
                });
            } else {
                promise = RoomService.update(bean, function () {
                    AlertFactory.success('更新成功!');
                    form.$setDirty(false);
                });
            }
            CommonUtils.loading(promise);
        };


        // 重新加载该页面
        $scope.reload = function () {
            window.location.reload();
        };
        
        
        $scope.loadBlock();
    });
})(window, angular, jQuery);