<template>
  <view class="calendar-container">
    <!-- 选项卡 -->
    <view class="tab-container">
      <view 
        class="tab-item" 
        :class="{ 'tab-item-active': activeTab === 'holidays' }"
        @click="activeTab = 'holidays'"
      >
        <text>节假日列表</text>
      </view>
      <view 
        class="tab-item" 
        :class="{ 'tab-item-active': activeTab === 'solarTerms' }"
        @click="activeTab = 'solarTerms'"
      >
        <text>24节气</text>
      </view>
    </view>

    <!-- 节假日列表 -->
    <view v-if="activeTab === 'holidays'" class="tab-content">
      <view class="holiday-list">
        <view 
          class="holiday-card" 
          v-for="(item, index) in holidays"
          :key="index"
        >
          <view class="holiday-left">
            <image 
              :src="'http://127.0.0.1:9000'+item.bgImageUrl"
              class="holiday-image"
              mode="aspectFill"
            ></image>
            <view class="holiday-info">
              <text class="holiday-name">{{ item.name }}</text>
              <text class="holiday-date">{{ item.fullDate }}</text>
            </view>
          </view>
          <view class="holiday-right">
            <text 
              class="days-difference" 
              :class="{ 'days-past': item.daysDifference < 0, 'days-future': item.daysDifference >= 0 }"
            >
              {{ item.daysDifference >= 0 ? `还有${item.daysDifference}天` : `已过去${Math.abs(item.daysDifference)}天` }}
            </text>
            <u-button size="mini" type="primary">查看详情</u-button>
          </view>
        </view>
      </view>
    </view>

    <!-- 24节气表格 -->
    <view v-if="activeTab === 'solarTerms'" class="tab-content">
      <u-table style="margin: 20rpx;">
        <u-tr v-for="(row, index) in solarTermsRows" :key="index">
          <u-td v-for="term in row" :key="term.id" class="term-cell">
            <view class="term-item">
              <text class="term-name">{{ term.name }}</text>
              <text class="term-date">{{ term.startDate }}-{{ term.endDate }}</text>
            </view>
          </u-td>
          <!-- 如果当前行不足4个，用空单元格补齐 -->
          <u-td v-for="i in 4 - row.length" :key="'empty' + i" class="empty-cell"></u-td>
        </u-tr>
      </u-table>
    </view>
  </view>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import {getAllSolarTerms, getHolidays} from '@/service/customApi.js';

const loading = ref(true);
const solarTerms = ref([]);
const holidays = ref([]);
const activeTab = ref('holidays'); // 默认显示节假日列表

// 将节气数据按每行4个分组
const solarTermsRows = computed(() => {
  const rows = [];
  for (let i = 0; i < solarTerms.value.length; i += 4) {
    rows.push(solarTerms.value.slice(i, i + 4));
  }
  return rows;
});

onMounted(async () => {
  try {
    const resHolidays = await getHolidays();
    const res = await getAllSolarTerms();
    if (resHolidays.code === 0) {
      holidays.value = resHolidays.data.list || [];
      console.log('获取节假日数据成功:', holidays.value);
    } else {
      console.error('获取节假日数据失败:', resHolidays.message);
    }
    if (res.code === 0) {
      solarTerms.value = res.data || [];
    } else {
      console.error('获取节气数据失败:', res.message);
    }
  } catch (error) {
    console.error('节气或节假日数据异常:', error);
  } finally {
    loading.value = false;
  }
});
</script>

<style>
.calendar-container {
  padding: 20rpx;
}

.tab-container {
  display: flex;
  background-color: #f5f5f5;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 20rpx 0;
  font-size: 30rpx;
  color: #666;
}

.tab-item-active {
  background-color: #fff;
  color: #007aff;
  border-radius: 10rpx;
  font-weight: bold;
}

.tab-content {
  min-height: 200rpx;
}

.holiday-list {
  padding: 20rpx 0;
}

.holiday-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx;
  margin-bottom: 20rpx;
  background-color: #ffffff;
  border-radius: 10rpx;
  box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.1);
}

.holiday-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.holiday-image {
  width: 80rpx;
  height: 80rpx;
  border-radius: 10rpx;
  margin-right: 20rpx;
}

.holiday-info {
  display: flex;
  flex-direction: column;
}

.holiday-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 5rpx;
}

.holiday-date {
  font-size: 24rpx;
  color: #888;
}

.holiday-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.days-difference {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.days-future {
  color: #ff6b6b; /* 未来节日显示为红色 */
}

.days-past {
  color: #888; /* 过去节日显示为灰色 */
}

.term-cell {
  text-align: center;
  padding: 10rpx;
}

.term-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 10rpx;
  min-height: 80rpx;
}

.term-name {
  font-weight: bold;
  font-size: 28rpx;
  margin-bottom: 5rpx;
}

.term-date {
  font-size: 24rpx;
  color: #888;
}

.empty-cell {
  visibility: hidden;
}

.title {
  font-size: 36rpx;
  color: #8f8f94;
}

.time {
  @include flex;
  align-items: center;

  &__item {
    color: #fff;
    font-size: 12px;
    text-align: center;
  }
}
</style>