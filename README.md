# 域名Replace=> ipv4

通过查询myssl网站，动态修改hosts文件实现翻墙域名提速
需要通过管理员权限运行（这暂时没完成)

## 项目介绍

- [X] 根据`Config.java`配置文件配置所需域名
- [X] 请直接运行`StartMain.java`文件获取最新ip域名映射
- [ ] 根据系统不同，动态修改host文件，并实现刷新（各个系统间的权限问题不好处理，暂时搁置)

## 各系统Host文件位置

- Linux/Mac
    - 请使用`sudo vim /etc/hosts`命令修改文件
    - 刷新DNS解析`sudo killall -HUP mDNSResponder`

- Windows
    - 请直接修改`C:\Windows\System32\drivers\etc\hosts`文件，管理员运行
    - 刷新DNS解析`ipconfig/flushdns`
