import { defineStore } from 'pinia'
import { login, getUserInfo, logout } from '@/api/auth'
import { setToken, removeToken, getToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    username: '',
    nickname: '',
    avatar: '',
    permissions: [],
    menus: []
  }),
  getters: {
    isLoggedIn: (state) => !!state.token
  },
  actions: {
    async loginAction(loginForm) {
      const data = await login(loginForm)
      this.token = data.token
      setToken(data.token)
      await this.fetchUserInfo()
    },
    async fetchUserInfo() {
      const data = await getUserInfo()
      this.username = data.username
      this.nickname = data.nickname
      this.avatar = data.avatar
      this.permissions = data.permissions || []
      this.menus = data.menus || []
    },
    async logoutAction() {
      try { await logout() } catch (e) { /* ignore */ }
      this.resetState()
    },
    resetState() {
      this.token = ''
      this.username = ''
      this.nickname = ''
      this.avatar = ''
      this.permissions = []
      this.menus = []
      removeToken()
    },
    hasPermission(perm) {
      return this.permissions.includes(perm)
    }
  }
})
