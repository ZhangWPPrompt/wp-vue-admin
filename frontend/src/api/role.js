import request from './index'

export function getRoles(params) {
  return request.get('/roles', { params })
}

export function getAllRoles() {
  return request.get('/roles/all')
}

export function getRole(id) {
  return request.get(`/roles/${id}`)
}

export function createRole(data) {
  return request.post('/roles', data)
}

export function updateRole(id, data) {
  return request.put(`/roles/${id}`, data)
}

export function deleteRole(id) {
  return request.delete(`/roles/${id}`)
}

export function getRoleMenuIds(roleId) {
  return request.get(`/roles/${roleId}/menus`)
}

export function assignRoleMenus(roleId, menuIds) {
  return request.put(`/roles/${roleId}/menus`, menuIds)
}
