import request from './index'

export function getLogs(params) {
  return request.get('/logs', { params })
}

export function getLog(id) {
  return request.get(`/logs/${id}`)
}
