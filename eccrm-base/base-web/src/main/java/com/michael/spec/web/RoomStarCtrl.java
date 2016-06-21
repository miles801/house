package com.michael.spec.web;

import com.michael.spec.bo.RoomStarBo;
import com.michael.spec.service.RoomStarService;
import com.ycrl.core.context.SecurityContext;
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
@RequestMapping(value = {"/house/roomStar"})
public class RoomStarCtrl extends BaseController {
    @Resource
    private RoomStarService roomStarService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/roomStar/list/roomStar_list";
    }

    /**
     * 添加关注
     *
     * @param roomId 房屋ID
     */
    @RequestMapping(value = "/star", params = "roomId", method = RequestMethod.POST)
    @ResponseBody
    public void save(String roomId, HttpServletRequest request, HttpServletResponse response) {
        roomStarService.star(roomId, SecurityContext.getEmpId());
        GsonUtils.printSuccess(response);
    }

    /**
     * 取消关注
     *
     * @param roomId 房屋ID
     */
    @RequestMapping(value = "/cancel", params = "roomId", method = RequestMethod.POST)
    @ResponseBody
    public void cancel(String roomId, HttpServletRequest request, HttpServletResponse response) {
        roomStarService.cancel(roomId, SecurityContext.getEmpId());
        GsonUtils.printSuccess(response);
    }


    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        RoomStarBo bo = GsonUtils.wrapDataToEntity(request, RoomStarBo.class);
        PageVo pageVo = roomStarService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        roomStarService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

}
