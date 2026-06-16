<template>
  <div class="login-container">
    <div class="login-bg-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
    </div>

    <div class="login-wrapper">
      <div class="login-banner">
        <div class="banner-content">
          <div class="banner-icon">
            <svg viewBox="0 0 80 80" width="80" height="80">
              <rect width="80" height="80" rx="18" fill="rgba(255,255,255,0.2)"/>
              <path d="M25 50 L40 30 L55 50 L48 50 L48 58 L32 58 L32 50Z" fill="#fff"/>
              <circle cx="40" cy="26" r="6" fill="#fff"/>
            </svg>
          </div>
          <h1 class="banner-title">Admin System</h1>
          <p class="banner-desc">企业级后台管理系统脚手架</p>
          <div class="banner-features">
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>RBAC 权限管理</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>JWT 安全认证</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>动态路由菜单</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>Vue3 + Spring Boot</span>
            </div>
          </div>
        </div>
      </div>

      <div class="login-form-wrapper">
        <div class="login-form-card">
          <h2 class="form-title">欢迎登录</h2>
          <p class="form-subtitle">请输入您的账号信息</p>

          <el-form ref="formRef" :model="form" :rules="rules" size="large" class="login-form">
            <el-form-item prop="username">
              <el-input
                v-model="form.username"
                placeholder="用户名"
                :prefix-icon="User"
                class="custom-input"
              />
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="密码"
                :prefix-icon="Lock"
                show-password
                class="custom-input"
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="loading"
                class="login-btn"
                @click="handleLogin"
              >
                {{ loading ? '登录中...' : '登 录' }}
              </el-button>
            </el-form-item>
          </el-form>

          <div class="form-tip">
            <span>默认账号: admin / admin123</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { User, Lock, Check } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await userStore.loginAction(form)
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #0f172a 100%);
  overflow: hidden;
  position: relative;
}

.login-bg-shapes {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.03;
}

.shape-1 {
  width: 600px;
  height: 600px;
  background: #667eea;
  top: -200px;
  right: -100px;
  animation: float 8s ease-in-out infinite;
}

.shape-2 {
  width: 400px;
  height: 400px;
  background: #f093fb;
  bottom: -100px;
  left: -50px;
  animation: float 10s ease-in-out infinite reverse;
}

.shape-3 {
  width: 300px;
  height: 300px;
  background: #4facfe;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: float 12s ease-in-out infinite 2s;
}

@keyframes float {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-30px) scale(1.05); }
}

.login-wrapper {
  display: flex;
  width: 1000px;
  min-height: 560px;
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(24px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.4);
  position: relative;
  z-index: 1;
}

.login-banner {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.3) 0%, rgba(118, 75, 162, 0.3) 100%);
  padding: 60px;
}

.banner-content {
  text-align: center;
  color: #fff;
}

.banner-icon {
  margin-bottom: 24px;
  display: inline-block;
}

.banner-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 8px 0;
  letter-spacing: 2px;
}

.banner-desc {
  font-size: 15px;
  opacity: 0.8;
  margin: 0 0 40px 0;
}

.banner-features {
  text-align: left;
  display: inline-flex;
  flex-direction: column;
  gap: 16px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  opacity: 0.9;
}

.login-form-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
}

.login-form-card {
  width: 100%;
  max-width: 360px;
}

.form-title {
  color: #fff;
  font-size: 26px;
  font-weight: 600;
  margin: 0 0 6px 0;
  text-align: center;
}

.form-subtitle {
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
  margin: 0 0 32px 0;
  text-align: center;
}

.login-form {
  margin-top: 8px;
}

.login-form :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 10px;
  box-shadow: none;
  transition: all 0.3s;
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: rgba(255, 255, 255, 0.25);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #409eff;
}

.login-form :deep(.el-input__inner) {
  color: #fff;
}

.login-form :deep(.el-input__inner::placeholder) {
  color: rgba(255, 255, 255, 0.35);
}

.login-form :deep(.el-input__prefix) {
  color: rgba(255, 255, 255, 0.4);
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 4px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  margin-top: 8px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

.login-btn:active {
  transform: translateY(0);
}

.form-tip {
  text-align: center;
  margin-top: 20px;
  color: rgba(255, 255, 255, 0.3);
  font-size: 13px;
}
</style>
