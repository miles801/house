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
        var buildingId = $('#buildingId').val();
        $scope.condition = {buildingId: buildingId, orderBy: 'floor', manager: $('#isManager').val()};
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
        $scope.units = [{id: null, code: '', name: '请选择'}];
        // 获取单元列表
        $scope.loadUnit = function () {
            var promise = UnitService.query({blockId: $scope.condition.blockId}, function (data) {
                data = data.data || [];
                // 查询出来的是单元的ID等信息，实际需要的是单元的编号
                // 所以需要将单元编号进行去重处理
                var code = {};
                angular.forEach(data, function (o) {
                    if (!code[o.id]) {
                        code[o.id] = true;
                        o.name = o.code;
                        $scope.units.push(o);
                    }
                });
                code = null;
            });
            CommonUtils.loading(promise);
        };


        // 批量导入
        $scope.importData = function () {
            CommonUtils.addTab({
                title: '批量导入房屋',
                url: 'app/house/room/list/room_import.jsp'
            })
        };

        $scope.blockChange = function () {
            $scope.units.length = 1;
            $scope.condition.unitId = null;
            $scope.unit = null;
            $scope.query();
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
            // 获取当前的单元ID
            for (var i = 0; i < $scope.units.length; i++) {
                if ($scope.units[i].id == $scope.condition.unitId) {
                    $scope.unit = $scope.units[i];
                    return;
                }
            }
        };

        $scope.pager = {
            limit: 10,
            fetch: function () {
                var param = angular.extend({}, {start: this.start, limit: this.limit}, $scope.condition);
                if (param.floor && !(/^\d+$/g.test(param.floor))) {
                    AlertFactory.error('楼层只能是正整数!');
                    return;
                }
                $scope.beans = [];
                return CommonUtils.promise(function (defer) {
                    var promise = RoomService.pageQuery(param, function (data) {
                        param = null;
                        $scope.beans = data.data || {total: 0};
                        defer.resolve($scope.beans);
                    });
                    CommonUtils.loading(promise, 'Loading...');
                });
            },
            finishInit: function () {
                this.query();
            }
        };

        //查询数据
        $scope.query = function () {
            $scope.pager.query();
        };


        // 删除或批量删除
        $scope.remove = function (id, index) {
            // 临时数据
            if (!id && index !== undefined) {
                $scope.beans.data.splice(index, 1);
                $scope.beans.total--;
                return;
            }
            // 批量删除
            if (!id && index == undefined) {
                var ids = [];
                angular.forEach($scope.items, function (o) {
                    ids.push(o.id);
                });
                id = ids.join(',');
            }
            if (!id) {
                AlertFactory.error('请先选择需要删除的数据!');
                return;
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '<span class="text-danger">数据一旦删除将不可恢复，并且本页面未做保存的数据也将会被退回到原始状态，请确认!</span>',
                callback: function () {
                    var promise = RoomService.deleteByIds({ids: id}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.pager.query();
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
                $scope.beans = $scope.beans || {};
                $scope.beans.data = $scope.beans.data || [];
                $scope.beans.data.push(newItem);
                $scope.beans.total = ($scope.beans.total || 0) + 1;
            } else {
                var o = angular.extend({}, bean);
                o.code = null;
                o.id = null;
                $scope.beans = $scope.beans || {};
                $scope.beans.data = $scope.beans.data || [];
                $scope.beans.data.push(o);
                $scope.beans.total = ($scope.beans.total || 0) + 1;
            }
        };

        // 保存
        $scope.save = function (bean) {
            var promise = RoomService.save(bean, function (data) {
                bean.id = data.data;
                AlertFactory.success('保存成功!');
                bean.$valid = false;
            });
            CommonUtils.loading(promise);
        };

        // 更新
        $scope.update = function (bean) {
            var promise = RoomService.update(bean, function () {
                AlertFactory.success('更新成功!');
                bean.$valid = false;
            });
            CommonUtils.loading(promise);
        };

        $scope.check = function (bean) {
            bean.$valid = false;
            var flag = true;
            if (!bean.floor || !bean.code || !bean.square || !bean.orient || !bean.type1 || !bean.type2 || !bean.type3 || !bean.type4) {
                flag = false;
            }
            bean.$valid = flag;
        };
        // 重新加载该页面
        $scope.reload = function () {
            window.location.reload();
        };


        $scope.loadBlock();
    });
})(window, angular, jQuery);