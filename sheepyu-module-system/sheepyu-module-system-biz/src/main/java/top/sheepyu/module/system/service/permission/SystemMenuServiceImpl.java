package top.sheepyu.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.exception.CommonException;
import top.sheepyu.module.common.util.MyStrUtil;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuCreateVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuQueryVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuUpdateVo;
import top.sheepyu.module.system.dao.permission.menu.SystemMenu;
import top.sheepyu.module.system.dao.permission.menu.SystemMenuMapper;
import top.sheepyu.module.system.dao.permission.role.SystemRoleMenuMapper;
import top.sheepyu.module.system.enums.menu.MenuTypeEnum;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static top.sheepyu.module.common.enums.CommonStatusEnum.DISABLE;
import static top.sheepyu.module.common.enums.CommonStatusEnum.ENABLE;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.common.util.CollectionUtil.*;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.*;
import static top.sheepyu.module.system.convert.permission.SystemMenuConvert.CONVERT;
import static top.sheepyu.module.system.enums.menu.MenuTypeEnum.*;

/**
 * @author ygq
 * @date 2023-01-29 17:56
 **/
@Service
@Slf4j
@Validated
public class SystemMenuServiceImpl extends ServiceImplX<SystemMenuMapper, SystemMenu> implements SystemMenuService {
    @Resource
    private SystemRoleMenuMapper systemRoleMenuMapper;
    private static final ConcurrentHashMap<Long, SystemMenu> MENUS = new ConcurrentHashMap<>();
    private static final List<Long> forbidIdList = Arrays.asList(1L, 2L, 12L);
    private static final ReentrantLock menuLock = new ReentrantLock();


    @Override
    public void createMenu(SystemMenuCreateVo createVo) {
        menuLock.lock();
        try {
            SystemMenu menu = CONVERT.convert(createVo);
            //如果不是顶级目录需要进行安全校验
            if (!Objects.equals(createVo.getParentId(), 0L)) {
                SystemMenu parentMenu = findByIdThrowIfNotExists(menu.getParentId());
                //如果是目录或者菜单, 那么只能在目录下
                Integer type = menu.getType();
                if ((Objects.equals(type, CATALOG.getCode()) || Objects.equals(type, MENU.getCode())) &&
                        !Objects.equals(parentMenu.getType(), CATALOG.getCode())) {
                    throw exception(LEVEL_RELATION_ERROR);
                }
                //如果是按钮, 那么只能在菜单下
                if (Objects.equals(type, BUTTON.getCode()) && !Objects.equals(parentMenu.getType(), MENU.getCode())) {
                    throw exception(LEVEL_RELATION_ERROR);
                }
            }
            save(menu);
            MENUS.put(menu.getId(), menu);
        } catch (CommonException e) {
            throw exception(MENU_OPERATE_FAILED);
        } finally {
            menuLock.unlock();
        }
    }

    @Override
    public void updateMenu(SystemMenuUpdateVo updateVo) {
        menuLock.lock();
        try {
            //不能操作系统重要的菜单
            checkForbidList(Collections.singletonList(updateVo.getId()));
            SystemMenu menu = CONVERT.convert(updateVo);
            boolean result = updateById(menu);
            if (result) {
                menu = findByIdThrowIfNotExists(menu.getId());
                MENUS.put(menu.getId(), menu);
            }
        } catch (CommonException e) {
            throw exception(MENU_OPERATE_FAILED);
        } finally {
            menuLock.unlock();
        }
    }

    @Transactional
    @Override
    public void changeStatus(Long id) {
        menuLock.lock();
        try {
            //不能操作系统重要的菜单
            checkForbidList(Collections.singletonList(id));
            SystemMenu menu = findByIdThrowIfNotExists(id);
            Set<Long> childrenIds = null;
            //如果不是按钮,说明可能有下级数据,将同步更新状态
            if (!Objects.equals(menu.getType(), BUTTON.getCode())) {
                //深度查询指定菜单的所有子集id
                List<SystemMenu> list = lambdaQuery().select(SystemMenu::getId, SystemMenu::getParentId).list();
                childrenIds = findTreeIds(id, list);
                childrenIds.add(id);
            }
            int status = Objects.equals(menu.getStatus(), ENABLE.getCode()) ? DISABLE.getCode() : ENABLE.getCode();
            boolean result = lambdaUpdate()
                    .in(SystemMenu::getId, childrenIds)
                    .set(SystemMenu::getStatus, status)
                    .update();
            if (!result) return;
            for (SystemMenu value : MENUS.values()) {
                if (childrenIds.contains(value.getId())) {
                    value.setStatus(status);
                }
            }
        } catch (CommonException e) {
            throw exception(MENU_OPERATE_FAILED);
        } finally {
            menuLock.unlock();
        }
    }

    @Transactional
    @Override
    public void deleteMenu(String ids) {
        menuLock.lock();
        try {
            //所有数据
            List<SystemMenu> list = list();
            List<Long> idList = MyStrUtil.splitToLong(ids, ',');
            //不能删除系统重要的菜单
            checkForbidList(idList);
            //要删除的数据
            List<SystemMenu> removeMenus = list.stream().filter(e -> idList.contains(e.getId())).collect(Collectors.toList());
            if (CollUtil.isEmpty(removeMenus)) {
                return;
            }
            //删除的数据中, 类型为目录的数据
            Set<Long> removeCatalogIds = convertSetFilter(removeMenus, SystemMenu::getId,
                    e -> Objects.equals(e.getType(), CATALOG.getCode()));

            //所有最终能删除的id
            Set<Long> removeIds = new HashSet<>();
            //没有目录类型的菜单, 可以直接删除
            if (CollUtil.isEmpty(removeCatalogIds)) {
                for (SystemMenu menu : removeMenus) {
                    removeIds.add(menu.getId());
                    //如果是菜单, 直接删除菜单下所有的按钮
                    if (Objects.equals(menu.getType(), MenuTypeEnum.MENU.getCode())) {
                        //拿到这个菜单下所有的按钮的id
                        List<Long> childIds = convertListFilter(list, SystemMenu::getId,
                                e -> Objects.equals(e.getParentId(), menu.getId())
                        );
                        removeIds.addAll(childIds);
                    }
                }
            } else {
                Set<Long> menuIds = convertSet(removeMenus, SystemMenu::getId);
                for (Long catalogId : removeCatalogIds) {
                    Set<Long> childIds = findTreeIds(catalogId, list);
                    Collection<Long> intersection = CollUtil.intersection(menuIds, childIds);
                    /*
                     * 说明要删除的数据中并没有包含此目录下所有的数据
                     * 所以不能删除
                     */
                    if (intersection.size() != childIds.size()) {
                        throw exception(MENU_HAS_CHILDREN);
                    }
                    //说明删除的数据中包含了这个目录下所有的数据, 所以可以删除
                    removeIds.add(catalogId);
                    removeIds.addAll(childIds);
                }
            }
            removeBatchByIds(removeIds);
            removeIds.forEach(MENUS::remove);
            //同时删除关联数据
            systemRoleMenuMapper.deleteByMenuIds(removeIds);
        } catch (CommonException e) {
            throw exception(MENU_OPERATE_FAILED);
        } finally {
            menuLock.unlock();
        }
    }

    private static void checkForbidList(List<Long> idList) {
        if (!CollUtil.intersection(idList, forbidIdList).isEmpty()) {
            throw exception(MENU_FORBID_OPERATE);
        }
    }

    @Override
    public List<SystemMenu> listMenu(SystemMenuQueryVo queryVo) {
        List<SystemMenu> list = list(buildQuery()
                .likeIfPresent(SystemMenu::getName, queryVo.getKeyword())
                .eqIfPresent(SystemMenu::getStatus, queryVo.getStatus())
                .orderByAsc(SystemMenu::getSort));
        return convertToTree(list);
    }

    @Override
    public List<SystemMenu> convertToTree(List<SystemMenu> list) {
        List<SystemMenu> result = new ArrayList<>();
        Set<Long> menuIds = convertSet(list, SystemMenu::getId);

        for (SystemMenu menu : list) {
            //筛选没有上级的目录
            if (!menuIds.contains(menu.getParentId())) {
                //递归封装数据
                menu.setChildren(fillTreeData(list, menu.getId()));
                result.add(menu);
            }
        }

        return result;
    }

    @Override
    public SystemMenu findById(Long id) {
        return findByIdThrowIfNotExists(id);
    }

    @Override
    public Set<Long> listMenuIdFromCache() {
        return MENUS.keySet();
    }

    @Override
    public Set<String> findPermissionByMenuIdsFromCache(Set<Long> menuIds, boolean enable) {
        return convertSetFilter(
                findMenuByIdsFromCache(menuIds, enable),
                SystemMenu::getPermission,
                e -> StrUtil.isNotBlank(e.getPermission())
        );
    }

    @Override
    public List<SystemMenu> findMenuByIdsFromCache(Set<Long> menuIds, boolean enable) {
        return menuIds.stream().map(MENUS::get)
                .filter(e -> !enable || Objects.equals(e.getStatus(), ENABLE.getCode()))
                .sorted(Comparator.comparingInt(SystemMenu::getSort))
                .collect(Collectors.toList());
    }

    private Set<Long> findTreeIds(Long id, List<SystemMenu> list) {
        Set<Long> result = new HashSet<>();

        for (SystemMenu menu : list) {
            if (Objects.equals(menu.getParentId(), id)) {
                result.add(menu.getId());
                result.addAll(findTreeIds(menu.getId(), list));
            }
        }

        return result;
    }

    private List<SystemMenu> fillTreeData(List<SystemMenu> list, Long id) {
        List<SystemMenu> result = new ArrayList<>();

        for (SystemMenu menu : list) {
            if (Objects.equals(menu.getParentId(), id)) {
                menu.setChildren(fillTreeData(list, menu.getId()));
                result.add(menu);
            }
        }

        return result.isEmpty() ? null : result;
    }

    private SystemMenu findByIdThrowIfNotExists(Long id) {
        return findByIdThrowIfNotExists(id, MENU_NOT_EXISTS);
    }

    @PostConstruct
    public void initMenus() {
        log.info("初始化加载菜单缓存");
        List<SystemMenu> list = list();
        for (SystemMenu menu : list) {
            MENUS.put(menu.getId(), menu);
        }
    }
}
