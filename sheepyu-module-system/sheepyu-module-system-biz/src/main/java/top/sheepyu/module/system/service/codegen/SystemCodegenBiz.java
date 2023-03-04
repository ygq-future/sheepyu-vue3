package top.sheepyu.module.system.service.codegen;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenRespVo;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenUpdateVo;
import top.sheepyu.module.system.controller.admin.codegen.vo.TableInfoRespVo;
import top.sheepyu.module.system.dao.codegen.SystemCodegenColumn;
import top.sheepyu.module.system.dao.codegen.SystemCodegenTable;
import top.sheepyu.module.system.enums.codegen.CodegenSceneEnum;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static top.sheepyu.module.system.convert.codegen.SystemCodegenConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-03-03 19:16
 **/
@Service
@Slf4j
@Validated
@AllArgsConstructor
public class SystemCodegenBiz {
    private final DataSource dataSource;
    private final SystemCodegenTableService systemCodegenTableService;
    private final SystemCodegenColumnService systemCodegenColumnService;

    public List<SystemCodegenRespVo> listCodegen(String keyword) {
        List<SystemCodegenTable> tableList = systemCodegenTableService.lambdaQuery()
                .like(SystemCodegenTable::getTableName, keyword).or()
                .orderByAsc(SystemCodegenTable::getCreateTime)
                .list();
        List<SystemCodegenRespVo> result = new ArrayList<>(tableList.size());

        for (SystemCodegenTable table : tableList) {
            SystemCodegenRespVo respVo = CONVERT.convert(table);
            respVo.setColumns(systemCodegenColumnService.listByTableId(table.getId()));
            result.add(respVo);
        }

        return result;
    }

    @Transactional
    public void createCodegen(List<String> tableNames) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(dataSource).build();
        StrategyConfig.Builder strategyConfig = new StrategyConfig.Builder();
        if (CollUtil.isNotEmpty(tableNames)) {
            strategyConfig.addInclude(tableNames);
        }
        ConfigBuilder builder = new ConfigBuilder(null, dataSourceConfig, strategyConfig.build(), null, null, null);

        builder.getTableInfoList().forEach(tableInfo -> {
            String tableName = tableInfo.getName();
            SystemCodegenTable table = new SystemCodegenTable();
            int underline = tableName.indexOf('_');
            String moduleName = tableName.substring(0, underline);
            String businessName = StrUtil.toCamelCase(tableName.substring(underline + 1));
            table.setTableName(tableName).setTableComment(tableInfo.getComment())
                    .setModuleName(moduleName).setBusinessName(businessName)
                    .setClassName(StrUtil.upperFirst(businessName)).setClassComment(tableInfo.getComment())
                    .setAuthor("sheepyu").setScene(CodegenSceneEnum.ADMIN.getCode());
            systemCodegenTableService.save(table);

            List<SystemCodegenColumn> columns = new ArrayList<>();
            for (TableField field : tableInfo.getFields()) {
                SystemCodegenColumn column = new SystemCodegenColumn();
                column.setTableId(table.getId()).setName(field.getName()).setType(field.getType())
                        .setComment(field.getComment()).setPrimaryKey(field.isKeyFlag())
                        .setNullable(field.getMetaInfo().isNullable())
                        .setAutoIncrement(field.isKeyIdentityFlag())
                        .setJavaType(field.getPropertyType()).setJavaField(field.getPropertyName())
                        .setCreateOperation(filterCreateOperation(field.getName()))
                        .setUpdateOperation(filterUpdateOperation(field.getName()))
                        .setQueryOperation(filterQueryOperation(field.getName()))
                        .setQueryCondition(getDefaultQueryCondition(field.getName()))
                        .setListOperationResult(filterListOperation(field.getName()))
                        .setQuickSearch(filterQuickSearch(field.getName()));
                columns.add(column);
            }
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

    public List<TableInfoRespVo> tableList() {
        DataSourceConfig config = new DataSourceConfig.Builder(dataSource).build();
        ConfigBuilder builder = new ConfigBuilder(null, config, null, null, null, null);
        List<TableInfoRespVo> result = new ArrayList<>();
        for (TableInfo tableInfo : builder.getTableInfoList()) {
            if (tableInfo.getName().toLowerCase().startsWith("qrtz")) {
                continue;
            }
            result.add(new TableInfoRespVo(tableInfo.getName(), tableInfo.getComment() == null ? "" : tableInfo.getComment()));
        }
        return result;
    }

    private Boolean filterCreateOperation(String fieldName) {
        return !Arrays.asList("id", "creator", "create_time", "updater", "update_time", "deleted").contains(fieldName);
    }

    private Boolean filterUpdateOperation(String fieldName) {
        return !Arrays.asList("creator", "create_time", "updater", "update_time", "deleted").contains(fieldName);
    }

    private Boolean filterListOperation(String fieldName) {
        return !Arrays.asList("creator", "updater", "update_time", "deleted").contains(fieldName);
    }

    private Boolean filterQueryOperation(String fieldName) {
        return !Arrays.asList("id", "creator", "updater", "update_time", "deleted").contains(fieldName);
    }

    private Boolean filterQuickSearch(String fieldName) {
        return Arrays.asList("id", "name", "title").contains(fieldName);
    }

    private String getDefaultQueryCondition(String fieldName) {
        if (Arrays.asList("name", "title").contains(fieldName)) {
            return "like";
        }
        if (fieldName.contains("time")) {
            return "between";
        }
        return "=";
    }
}
