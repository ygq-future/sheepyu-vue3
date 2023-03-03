package top.sheepyu.module.system.service.codegen;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.module.system.controller.admin.codegen.vo.TableInfoRespVo;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

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

    public List<TableInfoRespVo> tableList() {
        DataSourceConfig config = new DataSourceConfig.Builder(dataSource).build();
        ConfigBuilder builder = new ConfigBuilder(null, config, null, null, null, null);
        List<TableInfoRespVo> result = new ArrayList<>();
        for (TableInfo tableInfo : builder.getTableInfoList()) {
            result.add(new TableInfoRespVo(tableInfo.getName(), tableInfo.getComment()));
        }
        return result;
    }
}
