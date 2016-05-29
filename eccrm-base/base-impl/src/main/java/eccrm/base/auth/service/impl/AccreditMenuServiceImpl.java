package eccrm.base.auth.service.impl;

import com.michael.base.resource.domain.Resource;
import com.michael.base.resource.vo.ResourceVo;
import com.ycrl.core.beans.BeanWrapCallback;
import eccrm.base.auth.dao.AccreditMenuDao;
import eccrm.base.auth.domain.AccreditMenu;
import eccrm.base.auth.service.AccreditMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael
 */
@Service("accreditMenuService")
public class AccreditMenuServiceImpl implements AccreditMenuService, BeanWrapCallback<Resource, ResourceVo> {
    @javax.annotation.Resource
    private AccreditMenuDao accreditMenuDao;


    @Override
    public List<String> queryAccreditMenuIds(String[] deptIds) {
        if (deptIds == null || deptIds.length < 1) {
            return new ArrayList<String>();
        }

        return accreditMenuDao.queryMenuIds(deptIds);
    }

    @Override
    public void saveByDept(String deptId, String[] menuIds) {
        if (StringUtils.isEmpty(deptId)) {
            throw new RuntimeException("给岗位授权时，机构ID不能为空!");
        }
        // 删掉历史岗位的授权信息
        accreditMenuDao.deleteByDeptId(deptId);

        // 保存新的授权关系
        if (menuIds != null && menuIds.length > 0) {
            for (String menuId : menuIds) {
                AccreditMenu accredit = new AccreditMenu();
                accredit.setResourceId(menuId);
                accredit.setDeptId(deptId);
                accreditMenuDao.save(accredit);
            }
        }
    }


    @Override
    public void doCallback(Resource menu, ResourceVo vo) {
    }

    @Override
    public List<ResourceVo> queryAccreditMenus(String[] deptIds) {
        return null;
    }

    @Override
    public List<ResourceVo> queryPersonalAccreditMenus(String empId) {
        return null;
    }


    @Override
    public List<ResourceVo> queryPersonalAccreditMenus(String userId, String parentId) {
        return null;
    }

    @Override
    public List<ResourceVo> queryPersonalAccreditMenusRoot(String userId) {
        return null;
    }
}
