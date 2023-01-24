package top.sheepyu.module.common.constants;

import top.sheepyu.module.common.common.ErrorCode;

/**
 * 系统全局异常码
 *
 * @author ygq
 * @date 2022-12-02 21:41
 **/
public interface ErrorCodeConstants {
    ErrorCode INVALID_PARAMS = new ErrorCode(400, "非法参数");
    ErrorCode NOT_AUTHORIZE = new ErrorCode(401, "未登录");
    ErrorCode NOT_PERMISSION = new ErrorCode(403, "没有权限");
    ErrorCode NOT_FOUND = new ErrorCode(404, "请求资源未找到");
    ErrorCode UNKNOWN_ERROR = new ErrorCode(500, "未知错误, 查看日志");
    ErrorCode USERTYPE_ERROR = new ErrorCode(501, "错误的用户类型");
    ErrorCode DEMO_DENY = new ErrorCode(502, "演示模式, 不能操作!");
    ErrorCode EXISTS = new ErrorCode(503, "资源已存在!");
    ErrorCode OPERATION_FAILED = new ErrorCode(504, "操作失败!");
    ErrorCode CHECK_FIELD_NOT_NULL = new ErrorCode(505, "检查的字段不能为空!");
    ErrorCode CAPTCHA_TYPE_ERROR = new ErrorCode(506, "验证码类型错误!");
    ErrorCode FLOW_LIMIT_ON_METHOD = new ErrorCode(507, "限流标在错误的方法上!");
    ErrorCode ACCESS_FREQUENTLY = new ErrorCode(508, "访问过于频繁, 请稍后重试!");
    ErrorCode REPEATEDLY_REQUEST = new ErrorCode(508, "重复请求, 请稍后重试!");
    ErrorCode SMS_SENDER_DONT_MATCH = new ErrorCode(509, "sms没有开启或者没有匹配的smsSender!");
    ErrorCode USER_DISABLE = new ErrorCode(510, "用户已被禁用!");
}
