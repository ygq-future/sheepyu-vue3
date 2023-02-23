# sheepyu-vue3项目介绍

一个简洁的管理后台, 后端架构思路来源于ruo-yi-pro管理系统
前端架构思路来源于ElementPlus的BuildAdmin

- 前端采用vue3 + vite + ts + pinia流行技术栈, ui使用ElementPlus
- 后端是maven聚合项目, 使用springboot+mybatisplus+redis主流技术栈
- 权限认证使用Spring Security, 实现接口细粒度权限控制
- 代码生成器可一键生成 前端+后端+knife4j文档+validator参数校验
- 文件上传服务集成腾讯云, 阿里云, 本地上传. 支持大文件分片上传, 支持后台动态切换上传方式
- 短信服务目前只集成了Java Mail邮箱服务, 后面会集成腾讯云短信服务
- 适配移动端

# 项目运行

1. 使用git克隆之后, 新建数据库sheepyu, 导入项目根目录下的sheepyu.sql
2. 修改sheepyu-server下的application-dev.yml配置文件中的MySQL和redis配置
3. 运行项目访问localhost:18080/doc.html, 找到 测试 随意访问一个接口, 返回ok即后端项目没有问题
4. 前端项目进入sheepyu_admin_vue3, npm i安装依赖, 安装完成之后npm start运行项目即可
5. 登录用户名: admin, 密码: 2003

> 注意: jdk使用1.8, node使用18


# 运行截图
![](https://s3.bmp.ovh/imgs/2023/02/23/8ec772dc333ee187.png)
![](https://s3.bmp.ovh/imgs/2023/02/23/304ea8512c2e53ca.png)
