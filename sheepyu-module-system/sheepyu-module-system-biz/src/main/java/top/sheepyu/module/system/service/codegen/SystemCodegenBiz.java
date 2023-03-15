package top.sheepyu.module.system.service.codegen;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenQueryVo;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenUpdateVo;
import top.sheepyu.module.system.controller.admin.codegen.vo.TableInfoRespVo;
import top.sheepyu.module.system.dao.codegen.SystemCodegenColumn;
import top.sheepyu.module.system.dao.codegen.SystemCodegenTable;
import top.sheepyu.module.system.service.codegen.inner.CodegenBuilder;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static top.sheepyu.module.system.convert.codegen.SystemCodegenConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-03-03 19:16
 **/
@Service
@Slf4j
@Validated
public class SystemCodegenBiz {
    @Resource
    private SystemCodegenTableService systemCodegenTableService;
    @Resource
    private SystemCodegenColumnService systemCodegenColumnService;
    @Resource
    private CodegenBuilder codegenBuilder;

    public PageResult<SystemCodegenTable> pageCodegen(SystemCodegenQueryVo queryVo) {
        return systemCodegenTableService.page(queryVo);
    }

    public SystemCodegenTable findCodegen(Long id) {
        SystemCodegenTable table = systemCodegenTableService.findById(id);
        table.setColumns(systemCodegenColumnService.listByTableId(id));
        return table;
    }

    @Transactional
    public void createCodegen(List<String> tableNames) {
        codegenBuilder.convertTable(tableNames).forEach(table -> {
            systemCodegenTableService.save(table);

            List<SystemCodegenColumn> columns = table.getColumns();
            columns.forEach(e -> e.setTableId(table.getId()));
            systemCodegenColumnService.saveBatch(columns);
        });
    }

    @Transactional
    public void updateCodegen(@Valid SystemCodegenUpdateVo updateVo) {
        SystemCodegenTable table = CONVERT.convert(updateVo);
        systemCodegenTableService.updateById(table);
        systemCodegenColumnService.updateBatchById(updateVo.getColumns());
    }

    @Transactional
    public void deleteCodegen(String ids) {
        systemCodegenColumnService.batchDelete(ids, SystemCodegenColumn::getTableId);
        systemCodegenTableService.batchDelete(ids, SystemCodegenTable::getId);
    }

    /**
     * 只会同步新增的或者删除的或者字段类型改变了的
     * 其他都不同步, 为了避免影响到之前的配置
     *
     * @param id tableId
     */
    @Transactional
    public void syncCodegen(Long id) {
        SystemCodegenTable table = systemCodegenTableService.findById(id);
        table = codegenBuilder.convertTable(table.getTableName());
        table.setId(id);
        //更新table基本信息
        systemCodegenTableService.updateById(table);

        //最新的columns信息
        List<SystemCodegenColumn> newColumns = table.getColumns();
        newColumns.forEach(co -> co.setTableId(id));
        //之前旧的column信息
        List<SystemCodegenColumn> oldColumns = systemCodegenColumnService.listByTableId(id);
        //需要删除的column
        List<Long> deleteColumnIds = new ArrayList<>();
        //需要添加的column
        List<SystemCodegenColumn> addColumns = new ArrayList<>();
        //需要更新的column
        List<SystemCodegenColumn> updateColumns = new ArrayList<>();

        //先找到需要删除的id和需要修改的column
        for (SystemCodegenColumn oldColumn : oldColumns) {
            boolean deleted = true;
            for (SystemCodegenColumn newColumn : newColumns) {
                String newColumnType = newColumn.getType().replaceAll("\\(.*\\)", "");
                String oldColumnType = oldColumn.getType().replaceAll("\\(.*\\)", "");
                if (Objects.equals(newColumn.getName(), oldColumn.getName())) {
                    //字段名相同但是类型不同, 需要修改
                    if (!Objects.equals(newColumnType, oldColumnType)) {
                        updateColumns.add(newColumn.setId(oldColumn.getId()));
                    }
                    deleted = false;
                    break;
                }
            }
            if (deleted) {
                deleteColumnIds.add(oldColumn.getId());
            }
        }
        //找到需要添加的
        for (SystemCodegenColumn newColumn : newColumns) {
            boolean add = true;
            for (SystemCodegenColumn oldColumn : oldColumns) {
                if (Objects.equals(newColumn.getName(), oldColumn.getName())) {
                    add = false;
                    break;
                }
            }
            if (add) {
                addColumns.add(newColumn);
            }
        }

        //执行操作
        if (CollUtil.isNotEmpty(deleteColumnIds)) {
            systemCodegenColumnService.removeBatchByIds(deleteColumnIds);
        }
        if (CollUtil.isNotEmpty(addColumns)) {
            systemCodegenColumnService.saveBatch(addColumns);
        }
        if (CollUtil.isNotEmpty(updateColumns)) {
            systemCodegenColumnService.updateBatchById(updateColumns);
        }
    }

    public List<TableInfoRespVo> tableList() {
        List<TableInfoRespVo> result = new ArrayList<>();
        for (TableInfo tableInfo : codegenBuilder.tableInfoList()) {
            result.add(new TableInfoRespVo(tableInfo.getName(), tableInfo.getComment()));
        }
        return result;
    }

    public Map<String, String> generateCode(Long id) {
        SystemCodegenTable table = systemCodegenTableService.findById(id);
        List<SystemCodegenColumn> columns = systemCodegenColumnService.listByTableId(id);
        return codegenBuilder.buildCode(table, columns);
    }
}
