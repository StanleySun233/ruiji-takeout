# ruiji-takeout

瑞吉外卖，涉及java, spring boot, redis, mysql.

# 一键部署
```shell
git clone https://github.com/StanleySun233/ruiji-takeout.git
cd ruiji-takeout
```
修改`application.yaml`文件的参数
```shell
sh docker.sh
```

# 问题整理

1. 登录拦截，但是直接诶访问可以看到前端界面，有一定风险。
2. BigInt(20)无法存储；
   目前解决方法，全改为`String`存储BigInt。

```java
private String id;
```