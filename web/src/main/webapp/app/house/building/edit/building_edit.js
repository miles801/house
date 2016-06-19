/**
 *
 */
(function (window, angular, $) {
    var app = angular.module('house.building.edit', [
        'house.building',
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.angular.ztree',
        'eccrm.base.region'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, BuildingService, BuildingParam,
                                     RegionPicker) {

        var pageType = $('#pageType').val();
        var id = $('#id').val();

        // 建筑类型
        BuildingParam.type(function (data) {
            $scope.type = data;
            $scope.type.unshift({name: '请选择...'});
        });
        // 房屋权属
        BuildingParam.houseType(function (data) {
            $scope.houseType = data;
            $scope.houseType.unshift({name: '请选择...'});
        });
        // 房屋用途
        BuildingParam.usage(function (data) {
            $scope.usages = data;
            $scope.usages.unshift({name: '请选择...'});
        });
        // 供暖方式
        BuildingParam.warmType(function (data) {
            $scope.warmType = data;
            $scope.warmType.unshift({name: '请选择...'});
        });
        // 距离地铁
        BuildingParam.subway(function (data) {
            $scope.subway = data;
            $scope.subway.unshift({name: '请选择...'});
        });
        $scope.regionTree = RegionPicker.getRegionTree(function (o) {
            if (o.type !== 3) {
                AlertFactory.error('请选择区县!');
                return;
            }
            $scope.beans.city = o.getParentNode().id;
            $scope.beans.cityName = o.getParentNode().name;
            $scope.beans.area = o.id;
            $scope.beans.areaName = o.name;
            $scope.cityAndArea = $scope.beans.cityName + ' - ' + $scope.beans.areaName;
        });

        $scope.back = CommonUtils.back;

        // 保存
        $scope.save = function () {
            var promise = BuildingService.save($scope.beans, function (data) {
                AlertFactory.success('保存成功!');
                CommonUtils.addTab('update');
                window.location.href = "/house/building/modify?id=" + data.data;
            });
            CommonUtils.loading(promise, '保存中...');
        };


        // 更新
        $scope.update = function () {
            var promise = BuildingService.update($scope.beans, function (data) {
                AlertFactory.success('更新成功!');
                $scope.form.$setValidity('committed', false);
                CommonUtils.addTab('update');
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise, '更新中...');
        };

        // 加载数据
        $scope.load = function (id) {
            var promise = BuildingService.get({id: id}, function (data) {
                $scope.beans = data.data || {};
                $scope.cityAndArea = $scope.beans.cityName + ' - ' + $scope.beans.areaName;

                $scope.initTab(id);
            });
            CommonUtils.loading(promise, 'Loading...');
        };

        /**
         * 初始化页签，当新增页面保存成功后打开
         * 更新、明细页面自动打开
         * @param id 楼盘ID
         */
        $scope.initTab = function (id) {
            // 楼栋信息
            CommonUtils.addTab({
                title: '楼栋信息',
                url: 'app/house/block/list/block_list.jsp?id=' + id,
                active: false
            });
            CommonUtils.addTab({
                title: '单元信息',
                url: 'app/house/unit/list/unit_list.jsp?id=' + id,
                active: false
            });
            // 楼栋信息
            CommonUtils.addTab({
                title: '房间信息',
                url: 'app/house/room/list/room_list.jsp?id=' + id,
                active: false
            });
        };

        if (pageType == 'add') {
            $scope.beans = {};
        } else if (pageType == 'modify') {
            $scope.load(id);
        } else if (pageType == 'detail') {
            $scope.load(id);
            $('input,textarea,select').attr('disabled', 'disabled');
        } else {
            AlertFactory.error($scope, '错误的页面类型');
        }

    });
})(window, angular, jQuery);