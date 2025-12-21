![img](https://github-r2.tgmeng.com/github/readme/gihub-readme-head.png)

<h2><div align="center">糖果梦 热榜（后端） |  Tgmeng Trend</div>
<div>&nbsp;</div>
<!-- profile logo 个人资料徽标1 -->
  <div align="center">
    <a href="https://tgmeng.com"><img src="https://img.shields.io/badge/Home-主页-blue" /></a>&emsp;
    <a href="https://bbs.tgmeng.com"><img src="https://img.shields.io/badge/BBS-论坛-c32136" /></a>&emsp;
    <a href="https://bilibili.tgmeng.com"><img src="https://img.shields.io/badge/Bilibili-B站-8c36db" /></a>&emsp;
    <a href="https://wechat.tgmeng.com"><img src="https://img.shields.io/badge/WeChat-微信-07c160" /></a>&emsp;
    <a href="https://tg.tgmeng.com"><img src="https://img.shields.io/badge/Bilibili-TG-ff69b4" /></a>&emsp;
    <!-- visitor -->
    <img src="https://komarev.com/ghpvc/?username=CandyDream6&label=Views&color=orange&style=flat" alt="访问量统计" />&emsp;
    <!-- GPL-3.0 License -->
    <a href="https://www.gnu.org/licenses/gpl-3.0"><img src="https://img.shields.io/badge/License-GPL%203.0-blue.svg" alt="GPL-3.0 License" /></a>
  </div>

---
## 🏩 项目主页：https://trend.tgmeng.com

- <h4>本站前后端均已100%全面开源，欢迎大家Star，Fork，PR，Issues。<br/>

- <h4>这里是后端，<a href="https://github.com/CandyDream6/tgmeng-top-search-frontend" target="_blank">👉前端项目源码请点击前往👈</a>

---
## 👁️ 预览

![Screenshot](https://r2-trend.tgmeng.com/tgmeng-trend/tgmeng-trend-yulan.png)


---

## 📖 简介
糖果梦热榜是一个实时更新的热门清单，汇集了最新的资讯，涵盖新闻、社交、媒体、GitHub等多个领域。只需打开页面，即可轻松浏览各大热门内容，时刻跟进行业动态，掌握最热话题。

---
## 🌍 时效性
- 数据每分钟更新一次（GitHub20-40分钟，网易云音乐10-15分钟）
- 所有接口均来自官方，无任何第三方中转，主打无情铁手，AA接W接A接无情铁手接外圈刮接一刀斩

---
## ✨ 已支持功能

- ✅ 支持开启代理访问第三方
  - ✅ 配置代理池
  - ✅ 代理池可选部分开启
  - ✅ 从开启的代理池中随机选择
  - ✅ HTTPS和SOCKS代理都支持
- ✅ 支持内存缓存
  - ✅ 使用Caffeine进行内存缓存
  - ✅ 可选是否开启缓存(不开启就每次都走接口)
  - ✅ 配置最大缓存条数
  - ✅ 配置全局缓存过期时间
  - ✅ 配置单独特殊数据独立缓存过期时间
  - ✅ 配置单独特殊缓存数据随机浮动过期时间范围
- ✅ 定时器自动更新缓存
  - ✅ 可选是否开启自动定时更新缓存
  - ✅ 配置定时自动缓存执行频率
  - ✅ 配置定时自动缓存具体开启接口
- ✅ 异常处理
  - ✅ 全局异常拦截处理 
  - ✅ 自定义异常支持
- ✅ 请求头自动模拟
- ✅ 全局返回结果封装
- ✅ 全局日志处理
- ✅ 跨域配置
- ✅ 热点关键词订阅推送(支持飞书、钉钉、企业微信、Telegram、NTFY、GOTIFY、网易POPO)
- ✅ GitHub Action 自动部署
  - ✅ 提交代码即自动部署项目到指定服务器
  - ✅ 提交新代码即可自动发布到Docker镜像到DockerHub
  - ✅ 提交新代码即可自动发布到Docker镜像到GHCR(github container registry)

---
## 🗼 部署

### 1 GitHub Action 一键部署

- 修改 <span style="color:yellow">.github/workflows/deploy.yml</span> 中的下列值，

- 如果你不需要把自动打镜像到dockerhub，把里面dockerhub相关的代码注释/删掉即可（文件里搜DockerHub就行）。
 
- 你的代码推送到github仓库后，就会自动触发action，自动部署本项目

```shell
SERVER_HOST      # 你的服务器IP
SERVER_USER      # 你的服务器用户名
SERVER_PASSWORD  # 你的服务器密码
SERVER_PORT      # 你的服务器端口
REMOTE_JAR_DIR   # 你的要部署的目录
```

### 2 Docker镜像一键部署

```shell
docker pull tgmeng/tgmeng-api:latest                     # 这是dockerhub里的镜像
# docker pull ghcr.io/tgmeng-com/tgmeng-api:latest       # 这是ghcr里的镜像，和上面是一样的，拉哪个都行
docker run -d -p 8080:4399 --name tgmeng-api tgmeng/tgmeng-api:latest
docker ps
docker logs -f --tail=50 tgmeng-api
```

### 3 DockerCompose一键部署

- 下载本项目根目录下的docker-compose.yml，然后在他的同级目录执行下面命令

```shell
docker-compose up -d
docker-compose ps
docker-compose logs -f --tail=50 tgmeng-api
```

## ⚖️ 免责声明

- 软件性质：本项目为开源软件，按 “现状” 提供，不保证功能、性能或兼容性。
- 风险承担：使用风险由用户自行承担，开发者不提供任何明示或暗示担保（如适销性、特定用途适用性等）。
- 责任限制：因使用本软件导致的直接 / 间接损失（包括数据丢失、业务中断等），开发者不承担责任。
- 合规义务：二次开发或使用需遵守法律法规，相关责任由使用者自行承担。
- 侵权处理：如发现本项目存在侵权内容，请联系开发者，将及时处理。
- 条款变更：开发者有权修改免责声明及软件功能，持续使用即视为接受更新。

---

## ❤️ 联系我

<div align="center">
    <a href="https://tgmeng.com"><img src="https://img.shields.io/badge/Home-主页-blue" /></a>&emsp;
    <a href="https://bbs.tgmeng.com"><img src="https://img.shields.io/badge/BBS-论坛-c32136" /></a>&emsp;
    <a href="https://bilibili.tgmeng.com"><img src="https://img.shields.io/badge/Bilibili-B站-8c36db" /></a>&emsp;
    <a href="https://wechat.tgmeng.com"><img src="https://img.shields.io/badge/WeChat-微信-07c160" /></a>&emsp;
    <a href="https://tg.tgmeng.com"><img src="https://img.shields.io/badge/Bilibili-TG-ff69b4" /></a>&emsp;
    <!-- visitor -->
    <img src="https://komarev.com/ghpvc/?username=CandyDream6&label=Views&color=orange&style=flat" alt="访问量统计" />&emsp;
  </div>

---

## 💐 致谢

- 

---

## 🌎 许可证

- 本项目采用 [GNU GENERAL PUBLIC LICENSE Version 3 许可证](LICENSE) 授权。
- [![License: GPL v3](https://img.shields.io/badge/License-GPL%203.0-red.svg)](https://www.gnu.org/licenses/gpl-3.0)

---

## 🧧 投喂

![Screenshot](https://github-r2.tgmeng.com/github/readme/donate.png)

---

## ⭐ Star History

<a href="https://www.star-history.com/#tgmeng-com/tgmeng-api&Date">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=tgmeng-com/tgmeng-api&type=Date&theme=dark" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=tgmeng-com/tgmeng-api&type=Date" />
   <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=tgmeng-com/tgmeng-api&type=Date" />
 </picture>
</a>
