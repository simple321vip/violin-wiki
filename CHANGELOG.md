# 1.2
    
    1. 删除Jenkinfile
    2. 数据库由原有的mongodb+mysql转为postgres
    3. JDK由11　→　17
    4. 项目重构

## 1.1.1

  1. 修改 Jenkinsfile，增加获取github release tag 处理，来实现 自动化打包，并且更新 tag 到 cicd 工程。


## 1.0.0
---
### 新增

1. blog分类，blog的排序功能，
2. 增加了 @CurrentUser 注解，TenantHandlerMethodArgumentResolver注解解释器，实现业务逻辑中用户信息的注入。
3. 增加 auth_master master表来实现对授权用户的判断。 
4. 增加了登录认证api，通过第三方认证后返给前端的信息，需要再次通过该api进行验证，来保证安全性。

### 调整
1. 将blog分类删除策略调整为，只剩下一个blog分类时，无法删除。
2. 将blog删除策略调整为，该分类只剩下一个blog时，无法删除，只能通过该删除分类来进行删除。
3. 非master数据的table 完全转为mongodb的collection来管理。



