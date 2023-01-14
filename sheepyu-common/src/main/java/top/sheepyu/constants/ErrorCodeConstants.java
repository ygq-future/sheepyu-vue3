package top.sheepyu.constants;

import top.sheepyu.common.ErrorCode;

/**
 * 系统全局异常码
 * @author ygq
 * @date 2022-12-02 21:41
 **/
public interface ErrorCodeConstants {
    ErrorCode INVALID_PARAMS = new ErrorCode(400, "非法参数");
    ErrorCode NOT_AUTHORIZE = new ErrorCode(401, "未登录");
    ErrorCode NOT_PERMISSION = new ErrorCode(403, "没有权限");
    ErrorCode UNKNOWN_ERROR = new ErrorCode(500, "未知错误, 查看日志");
    ErrorCode USERTYPE_ERROR = new ErrorCode(501, "错误的用户类型");
}
