import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'
import path from 'path'

export default defineConfig({
    plugins: [uni()],
    css: {
        preprocessorOptions: {
            scss: {
                // 👇 关键：静默两个弃用警告
                silenceDeprecations: ['legacy-js-api', 'import'],
                additionalData: `@import "uview-plus/index.scss";`,
            },
        },
    },

    resolve: {
        alias: {
            // 可选：如果你在代码中用 @ 引用 src 下的文件
            '@': path.resolve(__dirname, 'src'),
        },
    },
})