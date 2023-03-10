package top.sheepyu.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.util.MyStrUtil;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuCreateVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuQueryVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuUpdateVo;
import top.sheepyu.module.system.dao.permission.menu.SystemMenu;
import top.sheepyu.module.system.dao.permission.menu.SystemMenuMapper;
import top.sheepyu.module.system.dao.permission.menu.SystemRoleMenuMapper;
import top.sheepyu.module.system.enums.menu.MenuTypeEnum;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    private static final Map<Long, SystemMenu> MENUS = new ConcurrentHashMap<>();
    private static final List<Long> forbidIdList = Arrays.asList(1L, 2L, 12L);

    @Override
    public void createMenu(SystemMenuCreateVo createVo) {
        SystemMenu menu = CONVERT.convert(createVo);
        //????????????????????????????????????????????????
        if (!Objects.equals(createVo.getParentId(), 0L)) {
            SystemMenu parentMenu = findByIdValidateExists(menu.getParentId());

            //???????????????????????????, ????????????????????????
            Integer type = menu.getType();
            if ((Objects.equals(type, CATALOG.getCode()) || Objects.equals(type, MENU.getCode())) &&
                    !Objects.equals(parentMenu.getType(), CATALOG.getCode())) {
                throw exception(LEVEL_RELATION_ERROR);
            }
            //???????????????, ????????????????????????
            if (Objects.equals(type, BUTTON.getCode()) && !Objects.equals(parentMenu.getType(), MENU.getCode())) {
                throw exception(LEVEL_RELATION_ERROR);
            }
        }

        save(menu);
        MENUS.put(menu.getId(), menu);
    }

    @Override
    public void updateMenu(SystemMenuUpdateVo updateVo) {
        SystemMenu menu = CONVERT.convert(updateVo);
        boolean result = updateById(menu);
        if (result) {
            menu = findByIdValidateExists(menu.getId());
            MENUS.put(menu.getId(), menu);
        }
    }

    @Transactional
    @Override
    public void deleteMenu(String ids) {
        //????????????
        List<SystemMenu> list = list();
        List<Long> idList = MyStrUtil.splitToLong(ids, ',');
        //????????????
        if (CollUtil.isEmpty(idList)) {
            return;
        }
        //?????????????????????????????????
        if (CollUtil.intersection(idList, forbidIdList).size() > 0) {
            throw exception(FORBID_REMOVE);
        }
        //??????????????????
        List<SystemMenu> removeMenus = list.stream().filter(e -> idList.contains(e.getId())).collect(Collectors.toList());
        if (CollUtil.isEmpty(removeMenus)) {
            return;
        }
        //??????????????????, ????????????????????????
        Set<Long> removeCatalogIds = convertSetFilter(removeMenus, SystemMenu::getId,
                e -> Objects.equals(e.getType(), CATALOG.getCode()));

        //????????????????????????id
        Set<Long> removeIds = new HashSet<>();
        //???????????????????????????, ??????????????????
        if (CollUtil.isEmpty(removeCatalogIds)) {
            for (SystemMenu menu : removeMenus) {
                removeIds.add(menu.getId());
                //???????????????, ????????????????????????????????????
                if (Objects.equals(menu.getType(), MenuTypeEnum.MENU.getCode())) {
                    //???????????????????????????????????????id
                    List<Long> childIds = convertListFilter(list, SystemMenu::getId,
                            e -> Objects.equals(e.getParentId(), menu.getId())
                    );
                    removeIds.addAll(childIds);
                }
            }
        } else {
            Set<Long> menuIds = convertSet(removeMenus, SystemMenu::getId);
            for (Long catalogId : removeCatalogIds) {
                Set<Long> childIds = findTreeIds(list, catalogId);
                Collection<Long> intersection = CollUtil.intersection(menuIds, childIds);
                /*
                 * ?????????????????????????????????????????????????????????????????????
                 * ??????????????????
                 */
                if (intersection.size() != childIds.size()) {
                    throw exception(MENU_HAS_CHILDREN);
                }
                //???????????????????????????????????????????????????????????????, ??????????????????
                removeIds.add(catalogId);
                removeIds.addAll(childIds);
            }
        }

        removeBatchByIds(removeIds);
        removeIds.forEach(MENUS::remove);
        //????????????????????????
        systemRoleMenuMapper.deleteByMenuIds(removeIds);
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
            //???????????????????????????
            if (!menuIds.contains(menu.getParentId())) {
                //??????????????????
                menu.setChildren(fillTreeData(list, menu.getId()));
                result.add(menu);
            }
        }

        return result;
    }

    @Override
    public SystemMenu findById(Long id) {
        return findByIdValidateExists(id);
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

    private Set<Long> findTreeIds(List<SystemMenu> list, Long id) {
        Set<Long> result = new HashSet<>();

        for (SystemMenu menu : list) {
            if (Objects.equals(menu.getParentId(), id)) {
                result.add(menu.getId());
                result.addAll(findTreeIds(list, menu.getId()));
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

    private SystemMenu findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, MENU_NOT_EXISTS);
    }

    @PostConstruct
    public void initMenus() {
        log.info("???????????????????????????");
        List<SystemMenu> list = list();
        for (SystemMenu menu : list) {
            MENUS.put(menu.getId(), menu);
        }
    }
}
