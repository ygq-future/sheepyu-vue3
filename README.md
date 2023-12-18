# sheepyu-vue3项目介绍

一个简洁的管理后台, 后端架构思路来源于ruo-yi-pro管理系统
前端架构思路来源于ElementPlus的BuildAdmin

- 前端采用vue3 + vite + ts + pinia流行技术栈, ui使用ElementPlus
- 后端是maven聚合项目, 使用springboot+mybatisplus+redis主流技术栈
- 权限认证使用Spring Security, 实现接口细粒度权限控制
- 代码生成器可一键生成 前端+后端+knife4j文档+validator参数校验
- 文件上传服务集成腾讯云, 阿里云, 本地上传. 支持大文件分片上传, 支持后台动态切换上传方式
- 短信服务目前只集成了Java Mail邮箱服务
- 适配移动端
- 权限控制是基于Level RBAC的权限控制

# 项目运行

1. 使用git克隆之后, 新建数据库sheepyu, 导入项目根目录下的sheepyu.sql
2. 修改sheepyu-server下的application-dev.yml配置文件中的MySQL和redis配置
3. 运行项目访问localhost:18080/doc.html, 找到 测试 随意访问一个接口, 返回ok即后端项目没有问题
4. 前端项目进入sheepyu_admin_vue3, npm i安装依赖, 安装完成之后npm start运行项目即可
5. 登录用户名: admin, 密码: 2003

> 注意: jdk使用1.8, node使用18


# 运行截图
![](https://s3.bmp.ovh/imgs/2023/12/18/1ec5170ad89c18a4.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/136d367438279a77.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/63c802ccb305b23e.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/1f3e50be0b4f0381.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/3c111821f770ba59.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/7a0a16f0de55cd96.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/cf32a95b346f9115.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/85c79b5d3b11d9ce.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/ff9ba8603f660ff6.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/0d15cede2129044f.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/978da92519c3bde6.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/3161e037e1b066c2.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/0a92da942b2e76b4.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/61815da6e9600a86.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/294a9f164425438e.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/5218627778d397de.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/e98124cefd4ef44d.jpeg)
![](https://s3.bmp.ovh/imgs/2023/12/18/24c8d22ab7645168.jpeg)
![](https://3o.hk/images/2023/12/17/Screenshot_18-12-2023_112512_demo.sheepyu.top.jpg)
