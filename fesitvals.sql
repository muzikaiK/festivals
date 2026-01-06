-- 1. 用户信息表
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `openid` varchar(128) NOT NULL COMMENT '微信OpenID',
  `nickname` varchar(64) DEFAULT '拾光者',
  `avatar_url` varchar(255) DEFAULT '',
  `birthday` date DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. 系统预设背景图库
CREATE TABLE `sys_image_library` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `img_url` varchar(255) NOT NULL,
  `category` varchar(32) DEFAULT 'DEFAULT',
  `is_active` tinyint(1) DEFAULT '1',
  `sort` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 节日节气配置表 (修改 text 长度限制适配旧版性能)
CREATE TABLE `sys_calendar_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(32) NOT NULL COMMENT '节气或节日名',
  `type` tinyint(4) NOT NULL COMMENT '1-24节气, 2-传统节日, 3-法定假',

  -- 精确日期字段，用于倒计时计算
  `full_date` date NOT NULL COMMENT '2026年的具体公历日期',

  -- 拆分字段，方便前端分段展示或按月过滤
  `c_year` int(4) NOT NULL COMMENT '年份: 如 2026',
  `c_month` tinyint(4) NOT NULL COMMENT '月份: 1-12',
  `c_day` tinyint(4) NOT NULL COMMENT '日期: 1-31',

  `is_lunar` tinyint(1) DEFAULT '0' COMMENT '是否为农历(0-公历, 1-农历)',
  `intro` varchar(500) DEFAULT NULL COMMENT '简短介绍',
  `story` text COMMENT '详细故事由来',
  `bg_image_url` varchar(255) DEFAULT NULL COMMENT '详情页背景图',

  -- 索引优化
  UNIQUE KEY `uk_name_date` (`name`, `full_date`),
  KEY `idx_full_date` (`full_date`),
  KEY `idx_month` (`c_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='节日节气完整配置表';

-- 4. 用户自定义倒数日表
CREATE TABLE `event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(64) NOT NULL,
  `target_date` date NOT NULL,
  `is_top` tinyint(1) DEFAULT '0',
  `image_id` int(11) DEFAULT NULL,
  `bg_color` varchar(20) DEFAULT '#4E73DF',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 时光胶囊表
CREATE TABLE `time_capsule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_id` bigint(20) NOT NULL,
  `content` text NOT NULL,
  `send_date` date NOT NULL,
  `is_sent` tinyint(1) DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  KEY `idx_send_date` (`send_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 初始化24节气示例 (立春、清明、冬至)
INSERT INTO `sys_calendar_config` (`name`, `type`, `full_date`, `c_year`, `c_month`, `c_day`, `is_lunar`, `intro`, `story`, `bg_image_url`) VALUES
('小寒', 1, '2026-01-05', 2026, 1, 5, 0, '冷气积久而为寒。', '小寒标志着开始进入一年中最寒冷的日子。', '/sys/solar/xiaohan.jpg'),
('大寒', 1, '2026-01-20', 2026, 1, 20, 0, '寒气之逆极。', '大寒是二十四节气最后一个，此时开始忙着除旧布新。', '/sys/solar/dahan.jpg'),
('立春', 1, '2026-02-04', 2026, 2, 4, 0, '万物起始，一切更迭。', '立春之日谓之“岁始”。民间有咬春习俗。', '/sys/solar/lichun.jpg'),
('雨水', 1, '2026-02-18', 2026, 2, 18, 0, '降雨开始，雨量渐增。', '此时冰雪融化，春风遍地。', '/sys/solar/yushui.jpg'),
('惊蛰', 1, '2026-03-05', 2026, 3, 5, 0, '春雷惊百虫。', '惊蛰时节天气转暖，春雷初响。', '/sys/solar/jingzhe.jpg'),
('春分', 1, '2026-03-20', 2026, 3, 20, 0, '昼夜平分，春色正中。', '春分这天阳光直射赤道，全球昼夜等长。', '/sys/solar/chunfen.jpg'),
('清明', 1, '2026-04-04', 2026, 4, 4, 0, '万物洁齐而清明。', '既是节气也是祭祖的节日。', '/sys/solar/qingming.jpg'),
('谷雨', 1, '2026-04-20', 2026, 4, 20, 0, '雨生百谷。', '播种移苗的最佳时节。', '/sys/solar/guyu.jpg'),
('立夏', 1, '2026-05-05', 2026, 5, 5, 0, '夏之始，万物生长。', '立夏表示夏季开始。民间有斗蛋习俗。', '/sys/solar/lixia.jpg'),
('小满', 1, '2026-05-21', 2026, 5, 21, 0, '物至于此，小得盈满。', '北方夏熟作物籽粒开始饱满。', '/sys/solar/xiaoman.jpg'),
('芒种', 1, '2026-06-05', 2026, 6, 5, 0, '有芒之谷，始可种也。', '这是一个耕种忙碌的节气。', '/sys/solar/mangzhong.jpg'),
('夏至', 1, '2026-06-21', 2026, 6, 21, 0, '白昼之至，日影短至。', '北半球一年中白昼最长的一天。', '/sys/solar/xiazhi.jpg'),
('小暑', 1, '2026-07-07', 2026, 7, 7, 0, '暑气至此尚未极也。', '入伏的开始。', '/sys/solar/xiaoshu.jpg'),
('大暑', 1, '2026-07-22', 2026, 7, 22, 0, '湿热交蒸，酷暑难耐。', '一年中最热的时候。', '/sys/solar/dashu.jpg'),
('立秋', 1, '2026-08-07', 2026, 8, 7, 0, '秋之始，凉风至。', '虽然仍处酷暑，但秋意渐起。', '/sys/solar/liqiu.jpg'),
('处暑', 1, '2026-08-23', 2026, 8, 23, 0, '暑气至此而止。', '表示酷热即将过去。', '/sys/solar/chushu.jpg'),
('白露', 1, '2026-09-07', 2026, 9, 7, 0, '露凝而白，气始寒也。', '气温下降，水汽凝结。', '/sys/solar/bailu.jpg'),
('秋分', 1, '2026-09-23', 2026, 9, 23, 0, '秋意平分，阴阳平衡。', '昼夜再次等长，秋色最浓。', '/sys/solar/qiufen.jpg'),
('寒露', 1, '2026-10-08', 2026, 10, 8, 0, '露气寒冷，将凝结也。', '由凉转冷的标志。', '/sys/solar/hanlu.jpg'),
('霜降', 1, '2026-10-23', 2026, 10, 23, 0, '露结为霜，气肃而凝。', '秋季的最后一个节气。', '/sys/solar/shuangjiang.jpg'),
('立冬', 1, '2026-11-07', 2026, 11, 7, 0, '冬之始，万物收藏。', '表示冬季正式开始。', '/sys/solar/lidong.jpg'),
('小雪', 1, '2026-11-22', 2026, 11, 22, 0, '地寒未甚，雪未大也。', '西北风开始频繁。', '/sys/solar/xiaoxue.jpg'),
('大雪', 1, '2026-12-07', 2026, 12, 7, 0, '积阴为雪，至此而大。', '寒潮降雪概率增大。', '/sys/solar/daxue.jpg'),
('冬至', 1, '2026-12-21', 2026, 12, 21, 0, '日短之至，阳气始萌。', '北半球白昼最短的一天。', '/sys/solar/dongzhi.jpg');', 2, 1, '月缺有圆，人间团圆。', '中秋节自古代齐国盛行，因其恰好在秋季的正中。人们通过祭月、赏月、吃月饼，寄托对家乡和亲人的思念，祈盼丰收和幸福。', '/sys/festival/zhongqiu.jpg');

-- 确保字符集
SET NAMES utf8mb4;

-- 2. 传统节日 (基于农历换算为 2026 年公历)
-- 包含：腊八、小年、除夕、春节、元宵、龙抬头、端午、七夕、中元、中秋、重阳、下元
INSERT INTO `sys_calendar_config` (`name`, `type`, `full_date`, `c_year`, `c_month`, `c_day`, `is_lunar`, `intro`, `story`, `bg_image_url`) VALUES
('腊八节', 2, '2026-01-26', 2026, 1, 26, 1, '腊七腊八，冻死旱鸭。', '农历腊月初八。这天有吃腊八粥的习俗，寓意来年五谷丰登，也标志着“年”序幕的开启。', '/sys/festival/laba.jpg'),
('小年', 2, '2026-02-10', 2026, 2, 10, 1, '祭灶扫尘，辞旧迎新。', '农历腊月二十三（北方）或二十四（南方）。民间有祭灶神、吃灶糖、大扫除的习俗。', '/sys/festival/xiaonian.jpg'),
('除夕', 2, '2026-02-16', 2026, 2, 16, 1, '岁末最后一天，阖家团圆。', '农历腊月三十。全家围坐吃年夜饭、守岁，是辞旧迎新最重要的时刻。', '/sys/festival/chuxi.jpg'),
('春节', 2, '2026-02-17', 2026, 2, 17, 1, '万象更新，岁之元辰。', '农历正月初一。中国最隆重的传统节日。2026年是丙午马年，习俗包括拜年、贴春联。', '/sys/festival/chunjie.jpg'),
('元宵节', 2, '2026-03-03', 2026, 3, 3, 1, '正月十五，大地回春。', '农历正月十五。第一个月圆之夜，习俗有赏花灯、猜灯谜、吃元宵。', '/sys/festival/元宵.jpg'),
('端午节', 2, '2026-06-19', 2026, 6, 19, 1, '仲夏登高，顺阳在上。', '农历五月初五。纪念爱国诗人屈原。习俗有赛龙舟、吃粽子、挂艾草。', '/sys/festival/duanwu.jpg'),
('七夕节', 2, '2026-08-19', 2026, 8, 19, 1, '柔情似水，佳期如梦。', '农历七月初七。源于牛郎织女的传说，是中国最具浪漫色彩的传统节日。', '/sys/festival/qixi.jpg'),
('中秋节', 2, '2026-09-25', 2026, 9, 25, 1, '月缺有圆，人间团圆。', '农历八月十五。以月之圆兆人之团圆。习俗有赏月、吃月饼、寄托思念。', '/sys/festival/zhongqiu.jpg'),
('重阳节', 2, '2026-10-19', 2026, 10, 19, 1, '登高远眺，敬老怀远。', '农历九月初九。双九重叠，寓意长久。习俗有登高、插茱萸、饮菊花酒。', '/sys/festival/chongyang.jpg');

-- 3. 法定/公历假日 (类型 3)
-- 包含：元旦、劳动节、国庆节等
INSERT INTO `sys_calendar_config` (`name`, `type`, `full_date`, `c_year`, `c_month`, `c_day`, `is_lunar`, `intro`, `story`, `bg_image_url`) VALUES
('元旦', 3, '2026-01-01', 2026, 1, 1, 0, '新一年的开始。', '公历1月1日，全球通用的新年。', '/sys/festival/yuandan.jpg'),
('劳动节', 3, '2026-05-01', 2026, 5, 1, 0, '致敬每一位平凡的建设者。', '5月1日。这是赞扬劳动者权益与奉献的节日。', '/sys/festival/laodong.jpg'),
('青年节', 3, '2026-05-04', 2026, 5, 4, 0, '不负韶华，致敬青春。', '纪念五四运动。', '/sys/festival/qingnian.jpg'),
('儿童节', 3, '2026-06-01', 2026, 6, 1, 0, '守护每一颗纯真的心。', '属于全世界少年儿童的节日。', '/sys/festival/ertong.jpg'),
('国庆节', 3, '2026-10-01', 2026, 10, 1, 0, '盛世华诞，家国同梦。', '10月1日。庆祝中华人民共和国成立。', '/sys/festival/guoqing.jpg');