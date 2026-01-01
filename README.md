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
- ✅ 历史数据追踪(目前历史数据存储在本地磁盘里，永久保留所有历史数据，采用的parquet文件格式存储，读取用duckdb，目前每天的数据大小在60M左右，数据条数在1100万条左右，如果自行部署，请留意好自己的磁盘空间)
- ✅ AI总结(按照每个单独分类已经所有分类进行AI总结、分析、预测)
- ✅ GitHub Action 自动部署
  - ✅ 提交代码即自动部署项目到指定服务器
  - ✅ 提交新代码即可自动发布到Docker镜像到DockerHub
  - ✅ 提交新代码即可自动发布到Docker镜像到GHCR(github container registry)

---

## 🔧 配置

### 境变量/参数配置，务必看一下

> docker -e 的时候注意配置自己需要的，里面的`基础配置(敏感)`这块，强烈建议配置

<details>
<summary>点击展开参数详情</summary>

```
# 基础配置(敏感)
MY_CONFIG_ADMIN_PASSWORD=123321                            # 管理员密码，用于调用一些内部接口，比如生成key、查询数据、执行sql、合并历史文件等等（一定要配置一下，因为项目开源，管理员接口其他人是知道的）
MY_CONFIG_GOOGLE_ADS_CLOSE_PASSWORD=123321                 # 如果站内用到谷歌广，然后用户输入这个密码就可以关闭谷歌广告（可忽略）   
MY_CONFIG_UMAMI_WEBSITE_TGMENG_COM=123321                  # 站内用的umami统计的websiteID（可忽略）  
      
# AI配置      
MY_CONFIG_AI_MAX_RETRY_TIMES=3                             # AI模型的最大重试次数，指单个模型的重试次数，达到次数仍失败就找下一个模型去了（默认值3）
MY_CONFIG_AI_RETRY_DELAY_MS=1000                           # AI模型的重试时间间隔(毫秒)，每次调用会循环下面所有平台所有的模型，一旦成功就返回，不成功就重试本平台，达到上面的重试次数后，就换下一个模型、下一个平台轮询请求（默认值1000毫秒）
MY_CONFIG_AI_MAX_TOKENS=40000                              # AI输出的最大token限制，因为部分模型会一直尝试使用最大token数去生成结果，不配置就有可能出现明明很短的请求依旧失败，因为虽然消耗100，但他尝试使用他的最大限制去生成，加上你的输入的，就超出了，然后就失败（默认值40000）
MY_CONFIG_AI_AI_PLATFORM_CONFIG=[{}]                       # AI平台的配置，是一个json数组，格式看下面（不用站里ai功能的可忽略，目前ai在站里是用在切换到ai模式后，总结分析预测各个分类下的数据）
      
# 代理配置      
MY_CONFIG_PROXY_TOP_SEARCH_ENABLED=false                   # 是否开启HTTP代理，因为老哥的服务器访问不到目标网站，或者由于目标网站的qps限制等等（默认值false）
MY_CONFIG_PROXY_CONFIG                                     # 代理配置，如果开启代理，系统会从所有已开启的的代理中随机取一个，这里配置也是json数组，格式看下面，支持HTTP和SOCKS

# 定时任务配置
MY_CONFIG_SCHEDULE_CONTROLLER_API_TOP_SEARCH_ENABLED=true  # 是否开启定时任务，不开启的话，所有数据都是从接口获取，下面的缓存配置就没用了，建议开启。系统里有关定时器的执行周期都是在ScheduleRequestConfigManager.java中，数值是站长测下来比较合理的风控值（默认值true）

# 数据缓存配置
MY_CONFIG_DATA_CACHE_TOP_SEARCH_ENABLED=true               # 是否开启缓存，也就是爬虫回来的数据，是否缓存到内存里，下一次直接走缓存数据（默认值true）
MY_CONFIG_DATA_CACHE_TOP_SEARCH_MAX_SIZE=5000              # 最大缓存条数，一个接口就是一条，我们站内目前200来个接口（默认值5000）
MY_CONFIG_DATA_CACHE_TOP_SEARCH_EXPIRE_TIME=172800         # 通用缓存过期时间 单位是秒,因为定时器会定时刷新刷存利的数据，所以这里可以设置大一点，保证用户都是从缓存拿数据，防止风控（默认值48小时）

# 订阅配置（订阅key相关逻辑目前是把key存在本地文件中，没有用数据库）
MY_CONFIG_SUBSCRIPTION_DIR=./data/subscriptions/           # 订阅去重的key所在的文件夹（默认值./data/subscriptions/）
MY_CONFIG_SUBSCRIPTION_MAX_HOT_NUMBER=10000                # 订阅排除重复推送也是用的本地文件，里面存的给用户推送过的热点的hash值，这样保证不重复推送，这个是设置最多存多少条历史，也就是超过这个阈值后，如果一些热点仍然在榜单，可能出现重复推送（默认值10000）

# 历史数据配置
MY_CONFIG_HISTORY_DIR=./data/history/                      # 热点历史数据文件所在的文件夹，历史数据目前用来查询热点轨迹、搜索历史热点、模糊匹配历史热点、指纹匹配历史热点等等（默认值./data/history/）
MY_CONFIG_HISTORY_KEEP_DAY=36500                           # 热点历史数据保留天数，所有历史数据都是保存成parquet文件存在磁盘，每分钟存一个parquet文件，每天1440个文件，总共1G左右，第二天凌晨两点，会把前一天1440个文件合并成一个，压缩到总共60M左右，也就是每天1000多万条热点，占用60M左右的存储空间（由于非常省空间，一年才1-2G，所以默认保存100年）
MY_CONFIG_HISTORY_SIM_HASH_DISTANCE=15                     # 历史热点相似度哈希汉明距离，就是汉明距离在多少以内就判断为相似热点，这个是用在突发热点里判断热点相似性。这个值越小，说明热点越相似，如果是0，则基本判断为完完全全一致，如果是100，则基本不搭边。15是我自己测下来相对合理的一个值，比较宽松，可能存在误判，但是我不想放过有可能相似的热点，宁可放过也不错杀。你如果要精度高一点，可以把这个值调小（默认值15）      
MY_CONFIG_HISTORY_SUDDEN_HEAT_POINT_TIME_WINDOW=60         # 默认突发热点查询时间窗口，单位是分钟（这个配置没有用，不用看，完全用不到）
MY_CONFIG_HISTORY_SUDDEN_HEAT_POINT_PLATFORM_NUM_LEAST=2   # 突发热点查询最少平台数，就是在时间窗口内，至少有多少个平台搜索到了这个热点，才认为是突发热点（默认值2）
MY_CONFIG_HISTORY_SUDDEN_HEAT_POINT_RESULT_LIMIT=100       # 突发热点查询结果最多返回多少条，就比如数据有十年，你查询有的相似热点他可能非常多，这个是限制取前多少条，系统里是按照热点时间排过序的（默认100）

# 密钥配置
MY_CONFIG_LICENSE_ENABLED=true                             # 是否开启密钥限制，站里接口带@LicenseRequired注解的接口都是需要密钥的功能，如果你要放开所有功能，不限制访问，那么这里设置false即可（默认值true）
MY_CONFIG_LICENSE_DIR=./data/license/                      # 密钥是存在本地文件中，这里是密钥文件的存储位置（默认值./data/license/）

# 日志配置
MY_CONFIG_LOG_LICENSE_DIR=./data/log/license/              # 密钥使用的记录日志文件存储位置，方便后续排除哪些密钥恶意搞事情（默认值./data/log/license/）
MY_CONFIG_LOG_LICENSE_MAX_LOG_SIZE=10485760                # 单个密钥使用记录日志文件的最大文件限制，也就是保证单个密钥的日志文件不超过这个大小（默认值10M）
MY_CONFIG_LOG_LICENSE_KEEP_LOG_SIZE=9437184                # 单个密钥使用记录日志文件滚动大小，比如用户到了10M，那么保留后面的部分，然后继续写，我们这里是保留最后的9M，然后继续写（默认值9M）
```

</details>

<br/>

<details>
<summary>查看参数中 AI_PLATFORM_CONFIG 的格式</summary>

```json
[
  {
    "platform": "无敌人工AI",
    "api":"https://api.*****.com/v1/chat/completions",
    "key": "sk-tg8Bvn601KF*******************",
    "from": "老逼登给的",
    "models": 
        [
            "deepseek-ai/deepseek-v3.1",
            "openai/gpt-oss-120b"
        ]
  },
  {
    "platform": "傻叼AI",
    "api":"https://ai.***.com/v1/chat/completions",
    "key": "sk-q0YWZPM0dnP5aBnU*******************",
    "from": "大鸟先生",
    "models": 
        [
            "claude-sonnet-4.5",
            "gemini-2.5-pro",
            "deepseek-ai/DeepSeek-R1",
            "deepseek-ai/DeepSeek-V3.1",
            "moonshotai/Kimi-K2-Instruct",
            "grok-4.1-thinking",
            "qwen/qwen3-next-80b-a3b-thinking",
            "zai-org/GLM-4.6",
            "claude-sonnet-4"
        ]
  }
  ....
]
```

</details>

<br/>

<details>
<summary>查看参数中 MY_CONFIG_PROXY_CONFIG 的格式</summary>

```json
[
  {
    "user": "1234",
    "password":"1234",
    "host": "123.123.123.123",
    "port": 1234,
    "type": "HTTP",
    "enabled": true
  },
  {
    "user": "1234",
    "password": "1234",
    "host": "123.123.123.123",
    "port": 1234,
    "type": "SOCKS",
    "enabled": true
  }
  ......
]
```


</details>


## 🗼 部署

### 1 GitHub Action 一键部署

- 修改 <span style="color:yellow">.github/workflows/deploy.yml</span> 中的下列值，

- 你的代码推送到github仓库后，就会自动触发action，自动部署本项目

```shell
SERVER_HOST      # 你的服务器IP
SERVER_USER      # 你的服务器用户名
SERVER_PASSWORD  # 你的服务器密码
SERVER_PORT      # 你的服务器端口
REMOTE_APP_DIR   # 你的要部署的目录，这个有默认值:/home/用户名/tgmeng-api，可以这里配置覆盖
# 流程是先打包镜像到dockerHub，然后从dockerHub里拉取镜像来启动，所以需要配置dockerHub的信息
DOCKERHUB_USERNAME # DockerHub 用户名
DOCKERHUB_TOKEN    # DockerHub 访问令牌
DOCKER_IMAGE_NAME  # DockerHub 镜像名称（也就是dockerHub里面的仓库的名字）
CONTAINER_NAME     # 你要启动的容器名称(看你自己随便写，比如tgmeng-api)
```

### 2 Docker镜像一键部署

- -e 里面把你自己需要的变量加进去

```shell
docker pull tgmeng/tgmeng-api:latest                     # 这是dockerhub里的镜像
# docker pull ghcr.io/tgmeng-com/tgmeng-api:latest       # 这是ghcr里的镜像，和上面是一样的，拉哪个都行
docker run -d \
      --name tgmeng-api \
      --restart unless-stopped \
      -p 8080:4399 \
      -v /home/root/tgmeng-api/logs:/app/logs \
      -v /home/root/tgmeng-api/data:/app/data \
      -v /home/root/tgmeng-api/heapdumps:/app/heapdumps \
      -e JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/heapdumps -XX:ErrorFile=/app/logs/hs_err_%p.log" \
      -e TZ=Asia/Shanghai \
      tgmeng/tgmeng-api:latest

docker ps
docker logs -f --tail=50 tgmeng-api
```

### 3 DockerCompose一键部署

- 下载本项目根目录下的docker-compose.yml，然后environment处加上你自己需要的变量

- 然后在他的同级目录执行下面命令

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
