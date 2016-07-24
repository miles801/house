package com.michael.spec.web;

import com.michael.spec.bo.UnitBo;
import com.michael.spec.domain.Unit;
import com.michael.spec.service.UnitService;
import com.michael.spec.vo.UnitVo;
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
import java.util.List;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/house/unit"})
public class UnitCtrl extends BaseController {
    @Resource
    private UnitService unitService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/unit/list/unit_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "base/unit/edit/unit_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Unit unit = GsonUtils.wrapDataToEntity(request, Unit.class);
        String id = unitService.save(unit);
        GsonUtils.printData(response, id);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "base/unit/edit/unit_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Unit unit = GsonUtils.wrapDataToEntity(request, Unit.class);
        unitService.update(unit);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "base/unit/edit/unit_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        UnitVo vo = unitService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        UnitBo bo = GsonUtils.wrapDataToEntity(request, UnitBo.class);
        PageVo pageVo = unitService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/generate", params = "blockId", method = RequestMethod.POST)
    public void generate(String blockId, HttpServletResponse response) {
        Integer total = unitService.generate(blockId);
        GsonUtils.printData(response, total);
    }

    @ResponseBody
    @RequestMapping(value = "/query", params = "blockId", method = RequestMethod.POST)
    public void query(String blockId, HttpServletRequest request, HttpServletResponse response) {
        UnitBo bo = GsonUtils.wrapDataToEntity(request, UnitBo.class);
        if (bo == null) {
            bo = new UnitBo();
        }
        bo.setBlockId(blockId);
        List<Unit> data = unitService.query(bo);
        GsonUtils.printData(response, data);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        unitService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

    /**
     * 强制删除!慎重使用改功能！
     */
    @ResponseBody
    @RequestMapping(value = "/delete-force", params = {"ids"}, method = RequestMethod.DELETE)
    public void forceDelete(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        unitService.forceDelete(idArr);
        GsonUtils.printSuccess(response);
    }

}
