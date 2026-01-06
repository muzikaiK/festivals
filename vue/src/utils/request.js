const BASE_URL = 'http://localhost:8080'; // 后端服务地址

const request = (options) => {
    return new Promise((resolve, reject) => {
        uni.request({
            url: BASE_URL + options.url,
            method: options.method || 'GET',
            data: options.data || {},
            header: {
                'Content-Type': 'application/json',
                // 如果需要token，可以在这里读取并设置
                // 'X-User-Openid': uni.getStorageSync('token') || ''
            },
            success: (res) => {
                if (res.statusCode === 200) {
                    // 后端自定义的code，0为成功
                    if (res.data.code === 0) {
                        resolve(res.data);
                    } else {
                        uni.showToast({
                            title: res.data.message || '请求失败',
                            icon: 'none'
                        });
                        reject(res.data);
                    }
                } else {
                    uni.showToast({
                        title: `网络错误: ${res.statusCode}`,
                        icon: 'none'
                    });
                    reject(res);
                }
            },
            fail: (err) => {
                uni.showToast({
                    title: '请求异常',
                    icon: 'none'
                });
                reject(err);
            }
        });
    });
};

export default request;
