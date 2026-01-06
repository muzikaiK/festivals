<template>
  <view class="container-fluid">
    <!-- 实时时钟 -->
    <u-card :title="null" :body-style="{padding: '30rpx'}" margin="20rpx">
      <template #head>
        <view class="card-head">
          <u-icon name="clock" size="22"></u-icon>
          <text class="card-title">实时时钟</text>
        </view>
      </template>
      <template #body>
        <view class="time-display">
          {{ currentTime }}
        </view>
      </template>
    </u-card>

    <!-- 倒计时卡片 -->
    <u-row gutter="16">
      <u-col span="6">
        <u-card margin="20rpx 0 20rpx 20rpx">
          <template #head>
            <view class="card-head">
              <u-icon name="calendar" size="22"></u-icon>
              <text class="card-title">下一个节气</text>
            </view>
          </template>
          <template #body>
            <view v-if="upcoming.solarTerm" class="countdown-card">
              <text class="event-name">{{ upcoming.solarTerm.name }}</text>
              <text class="days-left">{{ upcoming.solarTerm.daysDifference }}</text>
              <text class="days-label">天</text>
            </view>
            <u-empty v-else mode="data" text="暂无"></u-empty>
          </template>
        </u-card>
      </u-col>
      <u-col span="6">
        <u-card margin="20rpx 20rpx 20rpx 0">
          <template #head>
            <view class="card-head">
              <u-icon name="gift" size="22"></u-icon>
              <text class="card-title">下一个假日</text>
            </view>
          </template>
          <template #body>
            <view v-if="upcoming.holiday" class="countdown-card">
              <text class="event-name">{{ upcoming.holiday.name }}</text>
              <text class="days-left">{{ upcoming.holiday.daysDifference }}</text>
              <text class="days-label">天</text>
            </view>
            <u-empty v-else mode="data" text="暂无"></u-empty>
          </template>
        </u-card>
      </u-col>
    </u-row>

    <!-- 人生进度条 -->
    <u-card margin="20rpx">
      <template #head>
        <view class="card-head">
          <u-icon name="account" size="22"></u-icon>
          <text class="card-title">人生进度</text>
        </view>
      </template>
      <template #body>
        <view class="life-progress">
          <div class="section-title">假设你我都能活到80岁, 要开心呀！</div>
          <text class="section-title">{{ lifeProgressData.years }}年，是你与世界共度的时光。</text>
          <u-line-progress :percentage="lifePercentage" :active-color="getProgressColor(lifePercentage)" height="18">
            <text class="progress-text">{{ lifePercentage.toFixed(2) }}%</text>
          </u-line-progress>
          <text class="progress-label"></text>
          <u-divider></u-divider>

          <text class="section-title">今日进度已过去{{ lifeProgressData.hoursPassedToday }}小时</text>
          <u-line-progress :percentage="parseFloat(lifeProgressData.todayProgress)"
                           :active-color="getProgressColor(parseFloat(lifeProgressData.todayProgress))"
                           striped-active="true"
                           height="16"></u-line-progress>
          <text class="section-title">今年进度已过去{{ lifeProgressData.daysPassedThisYear }}天</text>
          <u-line-progress :percentage="parseFloat(lifeProgressData.yearProgress)"
                           :active-color="getProgressColor(parseFloat(lifeProgressData.yearProgress))"
                           inactiveColor=""
                           height="16"></u-line-progress>
        </view>
      </template>
    </u-card>

    <!-- 随机一言 -->
    <u-card margin="20rpx">
      <template #head>
        <view class="card-head">
          <u-icon name="chat" size="22"></u-icon>
          <text class="card-title">随机一言</text>
        </view>
      </template>
      <template #body>
        <view class="quote-card" @click="fetchRandomQuote">
          <text class="quote-content">{{ randomQuote || '...' }}</text>
        </view>
      </template>
    </u-card>

  </view>
</template>

<script setup>
import {onMounted, onUnmounted, ref} from 'vue';
import {getLifeProgress, getRandomQuote, getUpcomingEvents} from '@/service/customApi.js';
import {timeFormat as formatDate} from 'uview-plus/libs/function/index.js';

// 实时时钟
const currentTime = ref(formatDate(new Date(), 'hh:MM:ss'));
let clockInterval = null;

// 人生进度
const lifePercentage = ref(0);
const lifeProgressData = ref(null);

const fetchLifeProgress = async () => {
  try {
    const res = await getLifeProgress({
      birthdate: '1995-01-12' // 暂时写死，后期可从微信获取
    });
    if (res.code === 0) {
      console.log("人生进度数据:", res.data)
      lifeProgressData.value = res.data;
      lifePercentage.value = parseFloat(res.data.progress);  // 修正字段名，后端返回的是"progress"而不是"Progress.progress"
    }
  } catch (error) {
    console.error("获取人生进度失败:", error);
  }
};

// 根据进度值返回不同颜色
const getProgressColor = (percentage) => {
  if (percentage < 25) {
    return '#19be6b'; // 绿色
  } else if (percentage < 50) {
    return '#409eff'; // 蓝色
  } else if (percentage < 75) {
    return '#e6a23c'; // 橙色
  } else {
    return '#f56c6c'; // 红色
  }
};

// 倒计时
const upcoming = ref({
  solarTerm: null,
  holiday: null
});

// 随机一言
const randomQuote = ref('');

const fetchUpcomingEvents = async () => {
  try {
    const res = await getUpcomingEvents();
    if (res.code === 0) {
      upcoming.value.solarTerm = res.data.nearestSolarTerm;
      upcoming.value.holiday = res.data.nearestHoliday;
    }
  } catch (error) {
    console.error("获取倒计时事件失败:", error);
  }
};

const fetchRandomQuote = async () => {
  try {
    const res = await getRandomQuote();
    if (res.code === 0) {
      randomQuote.value = res.data;
    }
  } catch (error) {
    console.error("获取名言失败:", error);
  }
};

onMounted(() => {
  // 启动时钟
  clockInterval = setInterval(() => {
    currentTime.value = formatDate(new Date(), 'hh:MM:ss');
  }, 1000);

  // 获取数据
  fetchUpcomingEvents();
  fetchRandomQuote();
  fetchLifeProgress();
});

onUnmounted(() => {
  // 清除定时器
  if (clockInterval) {
    clearInterval(clockInterval);
  }
});
</script>

<style lang="scss">
.container-fluid {
  padding: 10rpx 0;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.card-head {
  display: flex;
  align-items: center;
}

.card-title {
  margin-left: 10rpx;
  font-size: 30rpx;
  font-weight: bold;
  color: $u-main-color;
}

.time-display {
  font-size: 70rpx;
  font-weight: bold;
  text-align: center;
  letter-spacing: 4rpx;
  font-family: 'Helvetica Neue', Arial, sans-serif;
}

.life-progress {
  .progress-text {
    color: #ffffff;
    font-size: 24rpx;
  }
  .progress-label {
    font-size: 24rpx;
    color: $u-tips-color;
    text-align: center;
    margin-top: 10rpx;
    display: block;
    margin-left: auto;
  }
}

.countdown-card {
  text-align: center;

  .event-name {
    font-size: 32rpx;
    font-weight: bold;
    display: block;
    color: $u-main-color;
  }

  .days-left {
    font-size: 60rpx;
    font-weight: bold;
    color: $u-primary;
    line-height: 1.2;
  }

  .days-label {
    font-size: 28rpx;
    color: $u-tips-color;
  }
}

.quote-card {
  .quote-content {
    font-size: 30rpx;
    display: block;
    margin-bottom: 20rpx;
    color: $u-main-color;
  }

  .quote-author {
    font-size: 26rpx;
    display: block;
    text-align: right;
    color: $u-tips-color;
  }
}
</style>
