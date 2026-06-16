import request from './index'

export function getDicts(params) {
  return request.get('/dicts', { params })
}

export function getDict(id) {
  return request.get(`/dicts/${id}`)
}

export function createDict(data) {
  return request.post('/dicts', data)
}

export function updateDict(id, data) {
  return request.put(`/dicts/${id}`, data)
}

export function deleteDict(id) {
  return request.delete(`/dicts/${id}`)
}

export function getDictItems(dictCode) {
  return request.get(`/dicts/code/${dictCode}`)
}

export function createDictItem(dictId, data) {
  return request.post(`/dicts/${dictId}/items`, data)
}

export function updateDictItem(dictId, itemId, data) {
  return request.put(`/dicts/${dictId}/items/${itemId}`, data)
}

export function deleteDictItem(dictId, itemId) {
  return request.delete(`/dicts/${dictId}/items/${itemId}`)
}
