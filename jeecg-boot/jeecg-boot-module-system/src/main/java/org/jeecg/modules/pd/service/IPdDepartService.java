package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.model.SysDepartTreeModel;

import java.util.List;
import java.util.Map;

public interface IPdDepartService extends IService<SysDepart> {

    /**
     * 查询所有部门信息,并分节点进行显示
     * @return
     */
    List<SysDepartTreeModel> queryTreeList(SysDepart sysDepart);

    List<String> findSysDepart(SysDepart sysDepart);

    IPage<SysUser> departUserList(Page<SysUser> page, Map<String, Object> parMap);

    void saveDeptRolePermission(String roleId, String permissionIds, String lastPermissionIds, String departId);

    IPage<SysUser> findUserList(Page<SysUser> page, Map<String, Object> parMap);

    List<SysDepart> selectList(SysDepart sysDepart);

    List<SysUserDepart> queryUserDepart(LambdaQueryWrapper<SysUserDepart> eq);
}
