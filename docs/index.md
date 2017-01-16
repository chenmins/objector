---
layout: default
---

# [](#objector-1)Objector

Objector 是一个简单的对象创建器。
主要支持讲普通的Pojo对象通过\\@annotation方式进行字节码增强，并将对象直接持久化的微型框架。
目前采用阿里云的**表格存储**实现，详情见
[https://www.aliyun.com/product/ots](https://www.aliyun.com/product/ots)
待此版本文档后，讲提供其他实现方式如**MongoDB**

## [](#maven-install)安装方式Install for Maven

来自我的私有maven库，加入以下依赖。

```xml
<dependency>
		<groupId>org.chenmin.open</groupId>
		<artifactId>objector-ots</artifactId>
		<version>0.0.3</version>
</dependency>
<repositories>
	<repository>
		<id>dpm</id>
		<name>Team dpm Repository</name>
		<url>http://vpn.dpm.im:8081/nexus/content/repositories/releases/</url>
	</repository>
</repositories>
```
