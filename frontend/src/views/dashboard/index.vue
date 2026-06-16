<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF"><User /></div>
            <div class="stat-info">
              <div class="stat-number">{{ userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A"><Avatar /></div>
            <div class="stat-info">
              <div class="stat-number">{{ roleCount }}</div>
              <div class="stat-label">角色总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C"><Menu /></div>
            <div class="stat-info">
              <div class="stat-number">{{ menuCount }}</div>
              <div class="stat-label">菜单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C"><Notebook /></div>
            <div class="stat-info">
              <div class="stat-number">{{ logCount }}</div>
              <div class="stat-label">日志总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px">
      <template #header>
        <span>欢迎使用后台管理系统</span>
      </template>
      <p>当前用户：{{ userStore.nickname || userStore.username }}</p>
      <p>登录时间：{{ new Date().toLocaleString() }}</p>
      <p>这是一个基于 Vue3 + Element Plus + Spring Boot + MyBatis Plus 构建的后台管理系统脚手架。</p>
      <p>功能包括：用户管理、角色管理、菜单管理、字典管理、操作日志查询等。</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getUsers } from '@/api/user'
import { getRoles } from '@/api/role'
import { getMenuTree } from '@/api/menu'
import { getLogs } from '@/api/log'

const userStore = useUserStore()
const userCount = ref(0)
const roleCount = ref(0)
const menuCount = ref(0)
const logCount = ref(0)

onMounted(async () => {
  try {
    const users = await getUsers({ page: 1, pageSize: 1 })
    userCount.value = users.total || 0
  } catch (e) { /* ignore */ }
  try {
    const roles = await getRoles({ page: 1, pageSize: 1 })
    roleCount.value = roles.total || 0
  } catch (e) { /* ignore */ }
  try {
    const menus = await getMenuTree()
    menuCount.value = countMenus(menus)
  } catch (e) { /* ignore */ }
  try {
    const logs = await getLogs({ page: 1, pageSize: 1 })
    logCount.value = logs.total || 0
  } catch (e) { /* ignore */ }
})

function countMenus(menus) {
  let count = menus.length
  for (const menu of menus) {
    if (menu.children && menu.children.length) {
      count += countMenus(menu.children)
    }
  }
  return count
}
</script>

<style scoped>
.dashboard { padding: 0; }
.stat-card { cursor: pointer; }
.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}
.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #fff;
}
.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
</style>
