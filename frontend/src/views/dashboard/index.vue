<template>
  <div class="dashboard">
    <div class="welcome-bar">
      <div class="welcome-info">
        <div class="welcome-avatar">
          <el-avatar :size="48" :icon="UserFilled" />
        </div>
        <div class="welcome-text">
          <h2>早安, {{ userStore.nickname || userStore.username }}</h2>
          <p>{{ currentDate }}，祝您工作愉快</p>
        </div>
      </div>
      <div class="welcome-actions">
        <div class="action-item">
          <div class="action-label">系统版本</div>
          <div class="action-value">v1.0.0</div>
        </div>
        <div class="action-divider"></div>
        <div class="action-item">
          <div class="action-label">运行环境</div>
          <div class="action-value">H2 / Java 17</div>
        </div>
      </div>
    </div>

    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <div class="stat-card card-blue" @click="$router.push('/system/user')">
          <div class="stat-top">
            <div class="stat-icon">
              <el-icon :size="28"><User /></el-icon>
            </div>
            <div class="stat-trend up">
              <el-icon><Top /></el-icon>
            </div>
          </div>
          <div class="stat-number">{{ userCount }}</div>
          <div class="stat-label">用户总数</div>
          <div class="stat-bottom">
            <span>用户管理</span>
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card card-green" @click="$router.push('/system/role')">
          <div class="stat-top">
            <div class="stat-icon">
              <el-icon :size="28"><Avatar /></el-icon>
            </div>
            <div class="stat-trend up">
              <el-icon><Top /></el-icon>
            </div>
          </div>
          <div class="stat-number">{{ roleCount }}</div>
          <div class="stat-label">角色总数</div>
          <div class="stat-bottom">
            <span>角色管理</span>
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card card-orange" @click="$router.push('/system/menu')">
          <div class="stat-top">
            <div class="stat-icon">
              <el-icon :size="28"><Menu /></el-icon>
            </div>
            <div class="stat-trend up">
              <el-icon><Top /></el-icon>
            </div>
          </div>
          <div class="stat-number">{{ menuCount }}</div>
          <div class="stat-label">菜单总数</div>
          <div class="stat-bottom">
            <span>菜单管理</span>
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card card-red" @click="$router.push('/log/operation')">
          <div class="stat-top">
            <div class="stat-icon">
              <el-icon :size="28"><Notebook /></el-icon>
            </div>
            <div class="stat-trend">
              <el-icon><Clock /></el-icon>
            </div>
          </div>
          <div class="stat-number">{{ logCount }}</div>
          <div class="stat-label">日志总数</div>
          <div class="stat-bottom">
            <span>日志查询</span>
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="content-row">
      <el-col :span="14">
        <el-card class="section-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">快捷入口</span>
            </div>
          </template>
          <el-row :gutter="16">
            <el-col :span="6" v-for="item in quickLinks" :key="item.label">
              <div class="quick-link" @click="$router.push(item.path)">
                <el-icon :size="24" :color="item.color">
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.label }}</span>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card class="section-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">系统信息</span>
            </div>
          </template>
          <div class="system-info">
            <div class="info-item">
              <span class="info-label">前端技术</span>
              <span class="info-value">Vue 3 + Element Plus + Vite</span>
            </div>
            <div class="info-item">
              <span class="info-label">后端技术</span>
              <span class="info-value">Spring Boot 3 + MyBatis Plus</span>
            </div>
            <div class="info-item">
              <span class="info-label">认证方式</span>
              <span class="info-value">JWT + BCrypt</span>
            </div>
            <div class="info-item">
              <span class="info-label">权限模型</span>
              <span class="info-value">RBAC (用户-角色-菜单)</span>
            </div>
            <div class="info-item">
              <span class="info-label">数据库</span>
              <span class="info-value">H2 内存数据库</span>
            </div>
            <div class="info-item">
              <span class="info-label">当前时间</span>
              <span class="info-value">{{ serverTime }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getUsers } from '@/api/user'
import { getRoles } from '@/api/role'
import { getMenuTree } from '@/api/menu'
import { getLogs } from '@/api/log'
import {
  User, Avatar, Menu, Notebook, ArrowRight, Top, Clock, UserFilled,
  Setting, Collection, Document
} from '@element-plus/icons-vue'

const userStore = useUserStore()
const userCount = ref(0)
const roleCount = ref(0)
const menuCount = ref(0)
const logCount = ref(0)
const serverTime = ref('')
let timer = null

const quickLinks = [
  { label: '用户管理', path: '/system/user', icon: 'User', color: '#409EFF' },
  { label: '角色管理', path: '/system/role', icon: 'Avatar', color: '#67C23A' },
  { label: '菜单管理', path: '/system/menu', icon: 'Menu', color: '#E6A23C' },
  { label: '字典管理', path: '/tool/dict', icon: 'Collection', color: '#909399' },
  { label: '操作日志', path: '/log/operation', icon: 'Document', color: '#F56C6C' },
  { label: '系统设置', path: '/system/user', icon: 'Setting', color: '#722ED1' },
  { label: '首页', path: '/dashboard', icon: 'HomeFilled', color: '#13C2C2' },
  { label: '帮助文档', path: '/dashboard', icon: 'Reading', color: '#FA8C16' }
]

const currentDate = (() => {
  const now = new Date()
  const h = now.getHours()
  const greet = h < 6 ? '凌晨好' : h < 9 ? '早上好' : h < 12 ? '上午好' : h < 14 ? '中午好' : h < 18 ? '下午好' : '晚上好'
  const week = ['日', '一', '二', '三', '四', '五', '六']
  return `${greet}，今天是${now.getFullYear()}年${now.getMonth() + 1}月${now.getDate()}日 星期${week[now.getDay()]}`
})()

const updateTime = () => {
  serverTime.value = new Date().toLocaleString('zh-CN', { hour12: false })
}

onMounted(async () => {
  updateTime()
  timer = setInterval(updateTime, 1000)

  try { const r = await getUsers({ page: 1, pageSize: 1 }); userCount.value = r.total || 0 } catch (e) {}
  try { const r = await getRoles({ page: 1, pageSize: 1 }); roleCount.value = r.total || 0 } catch (e) {}
  try { const menus = await getMenuTree(); menuCount.value = countMenus(menus) } catch (e) {}
  try { const r = await getLogs({ page: 1, pageSize: 1 }); logCount.value = r.total || 0 } catch (e) {}
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})

function countMenus(menus) {
  let count = menus.length
  for (const menu of menus) {
    if (menu.children && menu.children.length) count += countMenus(menu.children)
  }
  return count
}
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.welcome-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 24px 32px;
  margin-bottom: 20px;
  color: #fff;
}

.welcome-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.welcome-avatar :deep(.el-avatar) {
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid rgba(255, 255, 255, 0.4);
}

.welcome-text h2 {
  margin: 0 0 4px 0;
  font-size: 20px;
  font-weight: 600;
}

.welcome-text p {
  margin: 0;
  font-size: 13px;
  opacity: 0.85;
}

.welcome-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.action-item {
  text-align: center;
}

.action-label {
  font-size: 12px;
  opacity: 0.7;
}

.action-value {
  font-size: 15px;
  font-weight: 500;
  margin-top: 2px;
}

.action-divider {
  width: 1px;
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-card::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
}

.card-blue::after { background: linear-gradient(90deg, #409EFF, #66b1ff); }
.card-green::after { background: linear-gradient(90deg, #67C23A, #85ce61); }
.card-orange::after { background: linear-gradient(90deg, #E6A23C, #ebb563); }
.card-red::after { background: linear-gradient(90deg, #F56C6C, #f78989); }

.stat-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.card-blue .stat-icon { background: #409EFF; }
.card-green .stat-icon { background: #67C23A; }
.card-orange .stat-icon { background: #E6A23C; }
.card-red .stat-icon { background: #F56C6C; }

.stat-trend {
  color: #c0c4cc;
  font-size: 18px;
}
.stat-trend.up { color: #67C23A; }

.stat-number {
  font-size: 30px;
  font-weight: 700;
  color: #303133;
  line-height: 1;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 12px;
}

.stat-bottom {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--el-color-primary);
  border-top: 1px solid #f0f0f0;
  padding-top: 12px;
}

.content-row {
  margin-bottom: 0;
}

.section-card {
  border-radius: 12px;
  border: none;
}
.section-card :deep(.el-card__header) {
  border-bottom: 1px solid #f0f0f0;
  padding: 16px 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.quick-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 8px;
  border-radius: 10px;
  background: #fafafa;
  cursor: pointer;
  transition: all 0.25s;
  color: #606266;
  font-size: 13px;
}

.quick-link:hover {
  background: #ecf5ff;
  color: #409eff;
  transform: translateY(-2px);
}

.system-info {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.info-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 14px;
  color: #909399;
}

.info-value {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}
</style>
