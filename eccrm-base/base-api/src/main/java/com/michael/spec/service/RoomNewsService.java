package com.michael.spec.service;

import com.michael.spec.bo.RoomNewsBo;
import com.michael.spec.domain.RoomNews;
import com.michael.spec.vo.RoomNewsVo;
import com.ycrl.core.pager.PageVo;

/**
 * @author Michael
 */
public interface RoomNewsService {

    /**
     * 保存
     */
    String save(RoomNews roomNews);

    /**
     * 更新
     */
    void update(RoomNews roomNews);

    /**
     * 分页查询
     */
    PageVo pageQuery(RoomNewsBo bo);

    /**
     * 根据ID查询对象的信息
     */
    RoomNewsVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

}
