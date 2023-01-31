package top.sheepyu.module.system.constants;

import top.sheepyu.module.common.common.ErrorCode;

/**
 * @author ygq
 * @date 2023-01-16 18:17
 **/
public interface ErrorCodeConstants {
    /**
     * 整个系统的业务模块的ErrorCodeConstants的code命名方式为三段组成
     * 第一段代表业务或者模块, 例如system
     * 第二段代表业务中的某一块功能或者表, 例如job功能
     * 第三段就是错误编码依次递增
     */

    //job相关 100_001_001
    ErrorCode CRON_ERROR = new ErrorCode(100_001_001, "cron表达式错误");
    ErrorCode JOB_HANDLER_EXISTS = new ErrorCode(100_001_002, "任务处理器已存在");
    ErrorCode JOB_HANDLER_NOT_EXISTS = new ErrorCode(100_001_003, "任务处理器不存在");
    ErrorCode JOB_UPDATE_ONLY_NORMAL_STATUS = new ErrorCode(100_001_004, "只能在开启状态下修改任务");
    ErrorCode STATUS_ERROR = new ErrorCode(100_001_005, "错误的状态!");
    ErrorCode LOG_NOT_EXISTS = new ErrorCode(100_002_001, "任务日志不存在!");

    //dict相关 100_002_001
    ErrorCode DICT_TYPE_NOT_EXISTS = new ErrorCode(100_002_001, "字典类型不存在!");
    ErrorCode DICT_DATA_NOT_EXISTS = new ErrorCode(100_002_002, "字典数据不存在!");
    ErrorCode DICT_DATA_EXISTS = new ErrorCode(100_002_002, "字典数据已存在!");
    ErrorCode DICT_TYPE_HAS_DATA = new ErrorCode(100_002_003, "字典类型下还有数据!");

    //systemUser相关 100_003_001
    ErrorCode USER_NOT_EXISTS = new ErrorCode(100_003_001, "用户不存在!");
    ErrorCode LOGIN_FAILED = new ErrorCode(100_003_002, "登录失败, 用户名或密码错误!");
    ErrorCode USER_EXISTS = new ErrorCode(100_003_003, "用户已存在!");
    ErrorCode CODE_ERROR = new ErrorCode(100_003_004, "验证码错误!");

    //systemConfig相关 100_004_001
    ErrorCode CONFIG_NOT_EXISTS = new ErrorCode(100_004_001, "配置不存在!");
    ErrorCode NOT_SUPPORT_TYPE = new ErrorCode(100_004_002, "不支持的类型!");
    ErrorCode CONFIG_KEY_EXISTS = new ErrorCode(100_004_003, "配置已存在!");

    //log相关 100_005_001
    ErrorCode API_LOG_NOT_EXISTS = new ErrorCode(100_005_001, "日志不存在!");
    ErrorCode ALREADY_HANDLE = new ErrorCode(100_005_002, "错误已处理!");
    ErrorCode SUCCESS_API_DONT_HANDLE = new ErrorCode(100_005_003, "访问成功的api不用处理!");

    //file相关 100_006_001
    ErrorCode FILE_NOT_EXISTS = new ErrorCode(100_006_001, "文件不存在!");

    //dept相关 100_007_001
    ErrorCode DEPT_NOT_EXISTS = new ErrorCode(100_007_001, "部门不存在!");
    ErrorCode DEPT_HAS_CHILDREN = new ErrorCode(100_007_002, "部门还有子部门!");

    //post相关 100_008_001
    ErrorCode POST_NOT_EXISTS = new ErrorCode(100_008_001, "职位不存在!");
    ErrorCode POST_EXISTS = new ErrorCode(100_008_001, "职位已存在!");

    //menu相关 100_009_001
    ErrorCode MENU_NOT_EXISTS = new ErrorCode(100_009_001, "菜单不存在!");
    ErrorCode MENU_HAS_CHILDREN = new ErrorCode(100_009_002, "菜单还有子菜单!");

    //role相关 100_010_001
    ErrorCode ROLE_NOT_EXISTS = new ErrorCode(100_010_001, "角色不存在!");
}
