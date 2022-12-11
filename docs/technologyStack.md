## 架构 ##

本商城采用前后端分离架构，由前后端人员商议约定好数据格式后分别自行开发，前端可以造假数据进行测试，完全不需要依赖于后端，最后完成前后端集成即可，实现了前后端应用的解耦。
两端项目代码各自管理，避免了代码冲突；项目分别部署，避免了非前后端分离时前端或后端单独更新功能却需要一起重新部署项目的时间浪费。

### 后端 ###

    后端使用JAVA语言开发，使用MVC开发模式，采用SpringBoot快速开发框架、MybatisPlus ORM框架进行开发。

### 前端 ###

* #### 商城端 #### 
        使用静态HTML页面+Javascript语言开发，使用MVVM模式，采用VUE2框架、webpack工具、view-design ui框架以及axios请求库进行开发

* #### 后台端 #### 
        使用静态HTML页面+Javascript语言开发，使用MVVM模式，采用VUE3框架、vite 工具、Element-Plus ui框架以及axios请求库进行开发

### 数据库 ###

    采用Mysql8数据库，微小体量下，mysql8足以满足项目所需

### 项目部署 ###

    xxshop部署在两台腾讯云轻量级服务器组成的迷你K3S集群中，由docker构建相应镜像并上传到私服，编写文件后通过k3s进行部署