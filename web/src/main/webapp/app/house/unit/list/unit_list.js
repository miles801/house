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
                content: '警告!该功能是高风险操作!一旦确认，将会删除单元和单元下的所有房屋信息，请仔细确认后再行删除,确认删除?',
                callback: function () {
                    var promise = UnitService.forceDelete({ids: ids.join(',')}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.query();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 新增
        $scope.add = function (bean) {
            if (!bean) {
                $scope.beans.push({blockId: $scope.condition.blockId});
            } else {
                var o = angular.extend({}, bean);
                o.doorCode = null;
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
                CommonUtils.addTab('update');
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

        $scope.generateRoom = function () {
            ModalFactory.confirm({
                scope: $scope,
                content: '<span class="error">该操作将会根据该楼栋下的所有单元信息创建出所有的房屋列表，请确认已经完善了单元信息，并且之前并没有执行该操作!</span>',
                callback: function () {
                    var promise = UnitService.generate({blockId: $scope.condition.blockId}, function (data) {
                        if (data.data > 0) {
                            AlertFactory.success('创建成功!共创建了' + data.data + '个房屋!');
                        } else {
                            AlertFactory.success('创建失败!');
                        }
                    });
                    CommonUtils.loading(promise, "创建中...")
                }

            });
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