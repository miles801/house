package com.michael.spec.web;

import com.michael.spec.bo.BuildingBo;
import com.michael.spec.domain.Building;
import com.michael.spec.service.BuildingService;
import com.michael.spec.vo.BuildingVo;
import com.ycrl.base.common.JspAccessType;
import com.ycrl.core.pager.PageVo;
import com.ycrl.core.web.BaseController;
import com.ycrl.utils.gson.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/house/building"})
public class BuildingCtrl extends BaseController {
    @Resource
    private BuildingService buildingService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/building/list/building_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "house/building/edit/building_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Building building = GsonUtils.wrapDataToEntity(request, Building.class);
        String id = buildingService.save(building);
        GsonUtils.printData(response, id);
    }

    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @ResponseBody
    public void commit(HttpServletRequest request, HttpServletResponse response) {
        Building building = GsonUtils.wrapDataToEntity(request, Building.class);
        String id = buildingService.commit(building);
        GsonUtils.printData(response, id);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "house/building/edit/building_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Building building = GsonUtils.wrapDataToEntity(request, Building.class);
        buildingService.update(building);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "house/building/edit/building_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        BuildingVo vo = buildingService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        BuildingBo bo = GsonUtils.wrapDataToEntity(request, BuildingBo.class);
        PageVo pageVo = buildingService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        buildingService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

    // -----------------------  以下是楼盘维护人相关接口 ---------------------------------------

    // 设置楼盘负责人
    @ResponseBody
    @RequestMapping(value = "/master", params = {"id", "empId"}, method = RequestMethod.POST)
    public void updateMaster(@RequestParam String id, @RequestParam String empId, HttpServletResponse response) {
        buildingService.updateMaster(id, empId);
        GsonUtils.printSuccess(response);
    }

    // 添加楼盘维护人员
    @ResponseBody
    @RequestMapping(value = "/maintian-add", params = {"id", "empIds"}, method = RequestMethod.POST)
    public void addMaintain(@RequestParam String id, @RequestParam String empIds, HttpServletResponse response) {
        buildingService.addMaintain(id, empIds.split(","));
        GsonUtils.printSuccess(response);
    }

    // 移除楼盘维护人员
    @ResponseBody
    @RequestMapping(value = "/maintian-delete", params = {"id", "empId"}, method = RequestMethod.POST)
    public void removeMaintain(@RequestParam String id, @RequestParam String empId, HttpServletResponse response) {
        buildingService.removeMaintain(id, empId);
        GsonUtils.printSuccess(response);
    }
}
