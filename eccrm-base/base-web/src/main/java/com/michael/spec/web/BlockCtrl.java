package com.michael.spec.web;

import com.michael.spec.bo.BlockBo;
import com.michael.spec.domain.Block;
import com.michael.spec.service.BlockService;
import com.michael.spec.vo.BlockVo;
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
@RequestMapping(value = {"/house/block"})
public class BlockCtrl extends BaseController {
    @Resource
    private BlockService blockService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/block/list/block_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "base/block/edit/block_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Block block = GsonUtils.wrapDataToEntity(request, Block.class);
        String id = blockService.save(block);
        GsonUtils.printData(response, id);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "base/block/edit/block_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Block block = GsonUtils.wrapDataToEntity(request, Block.class);
        blockService.update(block);
        GsonUtils.printSuccess(response);
    }

    // 创建单元
    @RequestMapping(value = "/createUnit", params = "id", method = RequestMethod.POST)
    @ResponseBody
    public void createUnit(String id, HttpServletResponse response) {
        blockService.createUnit(id);
        GsonUtils.printSuccess(response);
    }

    // 清除单元
    @RequestMapping(value = "/clearUnit", params = "id", method = RequestMethod.POST)
    @ResponseBody
    public void clearUnit(String id, HttpServletResponse response) {
        blockService.clearUnit(id);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "base/block/edit/block_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        BlockVo vo = blockService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        BlockBo bo = GsonUtils.wrapDataToEntity(request, BlockBo.class);
        PageVo pageVo = blockService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/query", params = "buildingId", method = RequestMethod.POST)
    public void query(String buildingId, HttpServletRequest request, HttpServletResponse response) {
        BlockBo bo = GsonUtils.wrapDataToEntity(request, BlockBo.class);
        if (bo == null) {
            bo = new BlockBo();
        }
        bo.setBuildingId(buildingId);
        List<Block> data = blockService.query(bo);
        GsonUtils.printData(response, data);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        blockService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

}
