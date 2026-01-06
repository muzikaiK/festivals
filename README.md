想要做一个小程序,前端使用的uniapp和 Vue3,js,vite,Pinia,uView-plus,后端java的springboot-web,mybatisplus，连接池druid，数据库mysql5.7和redis,定时任务@Scheduled，文件minio
整体风格是唯美类型的(重点)
菜单	包含功能	对应数据库表 / 接口数据源
1. 时光 (首页)	实时时钟、人生进度条、倒计时卡片、随机一言	user, event, 外部一言API
2. 岁月 (历法)	节假日倒计时、24节气、节日故事、月相	sys_calendar_info, 算法计算(月相)
3. 见微 (百宝箱)	历史上的今天、时光胶囊、60s新闻、决策转盘、星座	time_capsule, 多个外部API缓存
4. 余生 (我的)	个人设置、数据同步、关于user

1.当前实时的年月日，时分秒可以用卡片风格
2.人生进度条 (有第三方接口数据)https://free.xwteam.cn/api/life/progress?birthdate=19950112
3.自定义倒计时，展示事件名称和时间，支持正序倒叙，正方形卡片风格
4.24节气展示及其介绍,数据初始化存在数据库中
5.全年的节假日(传统节假日+法定节假日)倒计时，节日介绍，节日故事或由来，数据初始化在数据库中
6.时光胶囊信件，设定发送日期，写一段话。到了那天，小程序通过微信服务通知
7.月相与宇宙 (美学组件)展示今天的月相（新月、满月），以及当天的日出日落时间。提升App的“高级感”。
8.决策转盘 (趣味)解决“今天吃什么”、“周末去哪里”的小工具。
9.历史上的今天（有第三方接口） http://www.wudada.online/Api/ScLsDay?month=01&&day=04  https://v2.api-m.com/api/history https://api.52vmy.cn/api/wl/today?type=text
10.今日星座运势（有第三方接口） http://api.suxun.site/api/constellation?type=scorpio&time=today
11.星座列表及介绍 
12。随机一言，名人名言，随机一言（有多个第三方接口）https://v.api.aa1.cn/api/yiyan/index.php  https://api.kuleu.com/api/yiyan  https://v.api.aa1.cn/api/pyq/index.php?aa1=json
13。60s看世界新闻(第三方接口返回图片)  https://60s.lylme.com/