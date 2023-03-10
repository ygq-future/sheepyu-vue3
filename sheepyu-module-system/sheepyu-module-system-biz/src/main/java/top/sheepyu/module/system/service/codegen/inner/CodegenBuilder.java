package top.sheepyu.module.system.service.codegen.inner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;
import top.sheepyu.module.system.dao.codegen.SystemCodegenColumn;
import top.sheepyu.module.system.dao.codegen.SystemCodegenTable;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.lowerFirst;
import static cn.hutool.core.text.CharSequenceUtil.upperFirst;
import static top.sheepyu.module.system.enums.codegen.CodegenSceneEnum.ADMIN;

/**
 * @author ygq
 * @date 2023-03-05 22:49
 **/
@Component
public class CodegenBuilder {
    @Resource
    private DataSource dataSource;
    //基本排除的字段
    private static final List<String> BASE_MODEL_FIELD = Lists.newArrayList();
    //创建操作排序的字段
    private static final List<String> CREATE_OPERATION_EXCLUDE_FIELD = Lists.newArrayList("id");
    //修改操作排除的字段
    private static final List<String> UPDATE_OPERATION_EXCLUDE_FIELD = Lists.newArrayList();
    //查询结果排除的字段
    private static final List<String> LIST_OPERATION_EXCLUDE_FIELD = Lists.newArrayList();
    //查询条件排除的字段
    private static final List<String> QUERY_OPERATION_EXCLUDE_FIELD = Lists.newArrayList("id");
    //快速搜索包含的字段
    private static final List<String> QUICK_SEARCH_INCLUDE_FIELD = Lists.newArrayList("id", "title", "name");
    //需要转换的类型
    private static final Map<String, String> CONVERT_PROPERTY_TYPE = MapUtil.<String, String>builder()
            .put("LocalDateTime", "Date")
            .put("LocalDate", "Date")
            .build();
    //条件查询类型判断
    private static final Map<String, String> QUERY_CONDITIONS = MapUtil.<String, String>builder()
            .put("title", "like")
            .put("name", "like")
            .put("time", "between")
            .build();
    //表单显示类型判断
    private static final Map<String, String> FORM_SHOW_TYPES = MapUtil.<String, String>builder()
            .put("Date", "datetime")
            .put("Integer", "number")
            .put("Long", "number")
            .build();
    //代码生成模板路径和输出路径
    private static final Map<String, String> TEMPLATES = MapUtil.<String, String>builder()
            .put("codegen/java/controller/controller.vm", buildControllerPath())
            .put("codegen/java/controller/vo/base.vm", buildControllerVoPath("BaseVo"))
            .put("codegen/java/controller/vo/create.vm", buildControllerVoPath("CreateVo"))
            .put("codegen/java/controller/vo/update.vm", buildControllerVoPath("UpdateVo"))
            .put("codegen/java/controller/vo/query.vm", buildControllerVoPath("QueryVo"))
            .put("codegen/java/controller/vo/resp.vm", buildControllerVoPath("RespVo"))
            .put("codegen/java/controller/vo/excel.vm", buildControllerVoPath("ExcelVo"))
            .put("codegen/java/convert/convert.vm", buildConvertPath())
            .put("codegen/java/dao/entity.vm", buildDaoPath(".java"))
            .put("codegen/java/dao/mapper.vm", buildDaoPath("Mapper.java"))
            .put("codegen/java/dao/xml.vm", buildDaoPath("Mapper.xml"))
            .put("codegen/java/service/service.vm", buildServicePath(""))
            .put("codegen/java/service/impl.vm", buildServicePath("Impl"))
            .put("codegen/vue3/api/api.vm", buildVue3ApiPath())
            .put("codegen/vue3/views/view.vm", buildVue3ViewPath())
            .build();

    public CodegenBuilder() {
        for (Field field : ReflectUtil.getFields(BaseModel.class)) {
            BASE_MODEL_FIELD.add(field.getName());
        }
        CREATE_OPERATION_EXCLUDE_FIELD.addAll(BASE_MODEL_FIELD);
        UPDATE_OPERATION_EXCLUDE_FIELD.addAll(BASE_MODEL_FIELD);
        LIST_OPERATION_EXCLUDE_FIELD.addAll(BASE_MODEL_FIELD);
        LIST_OPERATION_EXCLUDE_FIELD.remove("createTime");
        QUERY_OPERATION_EXCLUDE_FIELD.addAll(BASE_MODEL_FIELD);
        QUERY_OPERATION_EXCLUDE_FIELD.remove("createTime");
    }

    public Map<String, String> buildCode(SystemCodegenTable table, List<SystemCodegenColumn> columns) {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setResourceMode(TemplateConfig.ResourceMode.CLASSPATH);
        TemplateEngine templateEngine = TemplateUtil.createEngine(templateConfig);

        String sceneName = Objects.equals(table.getScene(), ADMIN.getCode()) ? "admin" : "app";
        String fullClassName = upperFirst(table.getModuleName() + table.getClassName());
        List<SystemCodegenColumn> quickSearchColumns = columns.stream().filter(SystemCodegenColumn::getQuickSearch).collect(Collectors.toList());
        boolean hasQuickSearch = quickSearchColumns.size() > 0;
        Map<Object, Object> params = MapUtil.builder()
                .put("table", table)
                .put("columns", columns)
                .put("hasQuickSearch", hasQuickSearch)
                .put("quickSearchColumns", quickSearchColumns)
                .put("fullClassName", fullClassName)
                .put("classVarName", lowerFirst(fullClassName))
                .put("baseModelFields", BASE_MODEL_FIELD)
                .put("sceneName", sceneName)
                .put("date", DateUtil.now())
                .put("notExistsName", table.getClassName().toUpperCase() + "_NOT_EXISTS")
                .build();

        MapBuilder<String, String> builder = MapUtil.builder();
        TEMPLATES.forEach((templatePath, outputPath) -> {
            String path = formatPath(outputPath, table, fullClassName);
            String render = templateEngine.getTemplate(templatePath).render(params);
            builder.put(path, render);
        });
        return builder.build();
    }

    private static String buildVue3ViewPath() {
        return "vue3/src/views/{moduleName}/{businessName}/index.vue";
    }

    private static String buildVue3ApiPath() {
        return "vue3/src/api/{moduleName}/{businessName}.ts";
    }

    private static String buildServicePath(String suffixName) {
        return buildJavaPath("service/{businessName}/{fullClassName}Service" + suffixName + ".java");
    }

    private static String buildDaoPath(String suffixName) {
        return buildJavaPath("dao/{businessName}/{fullClassName}" + suffixName);
    }

    private static String buildConvertPath() {
        return buildJavaPath("convert/{businessName}/{fullClassName}Convert.java");
    }

    private static String buildControllerVoPath(String suffixName) {
        return buildJavaPath("controller/{sceneName}/{businessName}/vo/{fullClassName}" + suffixName + ".java");
    }

    private static String buildControllerPath() {
        return buildJavaPath("controller/{sceneName}/{businessName}/{fullClassName}Controller.java");
    }

    private static String buildJavaPath(String path) {
        return "src/main/java/top/sheepyu/module/{moduleName}/" + path;
    }

    private static String formatPath(String path, SystemCodegenTable table, String fullClassName) {
        String sceneName = Objects.equals(table.getScene(), ADMIN.getCode()) ? "admin" : "app";
        return path.replace("{sceneName}", sceneName)
                .replace("{moduleName}", table.getModuleName())
                .replace("{businessName}", table.getBusinessName())
                .replace("{fullClassName}", fullClassName);
    }

    public List<TableInfo> tableInfoList() {
        DataSourceConfig config = new DataSourceConfig.Builder(dataSource).build();
        ConfigBuilder builder = new ConfigBuilder(null, config, null, null, null, null);
        return builder.getTableInfoList().stream().filter(e -> {
            if (e.getComment() == null) {
                e.setComment("");
            }
            String tableName = e.getName().toLowerCase();
            return !tableName.startsWith("qrtz");
        }).collect(Collectors.toList());
    }

    /**
     * 将tableNames对应的表转成CodegenTable
     * 注意, table中的columns没有tableId
     *
     * @param tableNames 表名集合
     * @return 返回CodegenTable集合
     */
    public List<SystemCodegenTable> convertTable(List<String> tableNames) {
        if (CollUtil.isEmpty(tableNames)) {
            return Collections.emptyList();
        }

        List<SystemCodegenTable> result = new ArrayList<>();
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(dataSource).build();
        StrategyConfig.Builder strategyConfig = new StrategyConfig.Builder().addInclude(tableNames);
        ConfigBuilder builder = new ConfigBuilder(null, dataSourceConfig, strategyConfig.build(), null, null, null);
        builder.getTableInfoList().forEach(tableInfo -> {
            String tableName = tableInfo.getName();
            SystemCodegenTable table = new SystemCodegenTable();
            int underline = tableName.indexOf('_');
            String moduleName = tableName.substring(0, underline);
            String businessName = StrUtil.toCamelCase(tableName.substring(underline + 1));
            table.setTableName(tableName).setTableComment(tableInfo.getComment())
                    .setModuleName(moduleName).setBusinessName(businessName.toLowerCase())
                    .setClassName(upperFirst(businessName)).setClassComment(tableInfo.getComment())
                    .setAuthor("sheepyu").setScene(ADMIN.getCode())
                    .setColumns(convertColumns(tableInfo.getFields()));
            result.add(table);
        });

        return result;
    }

    /**
     * 单表转换
     *
     * @param tableName 表名
     * @return 返回 table对象
     */
    public SystemCodegenTable convertTable(String tableName) {
        List<SystemCodegenTable> tableList = convertTable(Collections.singletonList(tableName));
        return CollUtil.getFirst(tableList);
    }

    /**
     * 将TableField转成CodegenColumn, 注意column没有tableId
     *
     * @param fields fields
     * @return 返回column列表
     */
    public List<SystemCodegenColumn> convertColumns(List<TableField> fields) {
        List<SystemCodegenColumn> columns = new ArrayList<>();
        for (TableField field : fields) {
            SystemCodegenColumn column = new SystemCodegenColumn();
            column.setName(field.getName()).setType(field.getType())
                    .setComment(field.getComment()).setPrimaryKey(field.isKeyFlag())
                    .setNullable(field.getMetaInfo().isNullable())
                    .setAutoIncrement(field.isKeyIdentityFlag())
                    .setJavaField(field.getPropertyName())
                    .setJavaType(filterPropertyType(field.getPropertyType()))
                    .setCreateOperation(filterCreateOperation(field.getPropertyName()))
                    .setUpdateOperation(filterUpdateOperation(field.getPropertyName()))
                    .setQueryOperation(filterQueryOperation(field.getPropertyName()))
                    .setQueryCondition(getDefaultQueryCondition(field.getPropertyName()))
                    .setListOperationResult(filterListOperation(field.getPropertyName()))
                    .setQuickSearch(filterQuickSearch(field.getPropertyName()))
                    .setFormShowType(filterFormShowType(column.getJavaType()));
            filterDictType(column);
            columns.add(column);
        }
        return columns;
    }

    private String filterPropertyType(String propertyType) {
        return CONVERT_PROPERTY_TYPE.getOrDefault(propertyType, propertyType);
    }

    private Boolean filterCreateOperation(String fieldName) {
        return !CREATE_OPERATION_EXCLUDE_FIELD.contains(fieldName);
    }

    private Boolean filterUpdateOperation(String fieldName) {
        return !UPDATE_OPERATION_EXCLUDE_FIELD.contains(fieldName);
    }

    private Boolean filterListOperation(String fieldName) {
        return !LIST_OPERATION_EXCLUDE_FIELD.contains(fieldName);
    }

    private Boolean filterQueryOperation(String fieldName) {
        return !QUERY_OPERATION_EXCLUDE_FIELD.contains(fieldName);
    }

    private Boolean filterQuickSearch(String fieldName) {
        return QUICK_SEARCH_INCLUDE_FIELD.contains(fieldName);
    }

    private String getDefaultQueryCondition(String fieldName) {
        for (Map.Entry<String, String> entry : QUERY_CONDITIONS.entrySet()) {
            if (fieldName.toLowerCase().contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "=";
    }

    private String filterFormShowType(String javaType) {
        return FORM_SHOW_TYPES.getOrDefault(javaType, "text");
    }

    private void filterDictType(SystemCodegenColumn column) {
        if (column.getJavaField().contains("status")) {
            column.setDictType("common_status");
        } else if (column.getJavaType().equalsIgnoreCase("Boolean")) {
            column.setDictType("common_boolean_status");
        }
    }
}
