<template>
  <div class="page-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>字典列表</span>
              <el-button type="primary" size="small" @click="handleAddDict">新增</el-button>
            </div>
          </template>
          <el-table :data="dictList" v-loading="dictLoading" highlight-current-row
            @row-click="row => handleSelectDict(row)" border stripe>
            <el-table-column prop="dictName" label="字典名称" />
            <el-table-column prop="dictCode" label="字典编码" />
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button type="warning" size="small" link @click.stop="handleEditDict(row)">编辑</el-button>
                <el-button type="danger" size="small" link @click.stop="handleDeleteDict(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 12px">
            <el-pagination
              v-model:current-page="dictQuery.page"
              v-model:page-size="dictQuery.pageSize"
              :total="dictTotal"
              :page-sizes="[5, 10, 20]"
              layout="total, prev, pager, next"
              small
              @size-change="fetchDicts"
              @current-change="fetchDicts"
            />
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>字典项列表 {{ currentDict ? `- ${currentDict.dictName}` : '' }}</span>
              <el-button type="primary" size="small" @click="handleAddItem" :disabled="!currentDict">新增</el-button>
            </div>
          </template>
          <el-table :data="itemList" v-loading="itemLoading" border stripe>
            <el-table-column prop="itemLabel" label="标签" width="150" />
            <el-table-column prop="itemValue" label="值" width="150" />
            <el-table-column prop="sortOrder" label="排序" width="80" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                  {{ row.status === 1 ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="140">
              <template #default="{ row }">
                <el-button type="warning" size="small" link @click="handleEditItem(row)">编辑</el-button>
                <el-button type="danger" size="small" link @click="handleDeleteItem(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="dictDialogVisible" :title="dictDialogTitle" width="500px">
      <el-form ref="dictFormRef" :model="dictForm" label-width="80px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="dictForm.dictName" />
        </el-form-item>
        <el-form-item label="字典编码" prop="dictCode">
          <el-input v-model="dictForm.dictCode" :disabled="isDictEdit" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="dictForm.description" type="textarea" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="dictForm.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dictDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleDictSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="itemDialogVisible" :title="itemDialogTitle" width="400px">
      <el-form ref="itemFormRef" :model="itemForm" label-width="80px">
        <el-form-item label="标签" prop="itemLabel">
          <el-input v-model="itemForm.itemLabel" />
        </el-form-item>
        <el-form-item label="值" prop="itemValue">
          <el-input v-model="itemForm.itemValue" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="itemForm.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="itemForm.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="itemDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleItemSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDicts, getDict, createDict, updateDict, deleteDict, getDictItems, createDictItem, updateDictItem, deleteDictItem } from '@/api/dict'

const dictList = ref([])
const dictTotal = ref(0)
const dictLoading = ref(false)
const dictQuery = reactive({ page: 1, pageSize: 5 })

const currentDict = ref(null)
const itemList = ref([])
const itemLoading = ref(false)

const dictDialogVisible = ref(false)
const dictDialogTitle = ref('')
const isDictEdit = ref(false)
const dictFormRef = ref(null)
const dictForm = reactive({ id: null, dictName: '', dictCode: '', description: '', status: 1 })

const itemDialogVisible = ref(false)
const itemDialogTitle = ref('')
const isItemEdit = ref(false)
const itemFormRef = ref(null)
const itemForm = reactive({ id: null, dictId: null, itemLabel: '', itemValue: '', sortOrder: 0, status: 1 })

const fetchDicts = async () => {
  dictLoading.value = true
  try {
    const res = await getDicts(dictQuery)
    dictList.value = res.records
    dictTotal.value = res.total
  } finally {
    dictLoading.value = false
  }
}

const fetchItems = async () => {
  if (!currentDict.value) return
  itemLoading.value = true
  try {
    itemList.value = await getDictItems(currentDict.value.dictCode)
  } finally {
    itemLoading.value = false
  }
}

const handleSelectDict = (row) => {
  currentDict.value = row
  fetchItems()
}

const handleAddDict = () => {
  isDictEdit.value = false
  dictDialogTitle.value = '新增字典'
  Object.assign(dictForm, { id: null, dictName: '', dictCode: '', description: '', status: 1 })
  dictDialogVisible.value = true
}

const handleEditDict = async (row) => {
  isDictEdit.value = true
  dictDialogTitle.value = '编辑字典'
  const dict = await getDict(row.id)
  Object.assign(dictForm, dict)
  dictDialogVisible.value = true
}

const handleDictSubmit = async () => {
  if (!dictForm.dictName || !dictForm.dictCode) {
    ElMessage.warning('请填写字典名称和编码')
    return
  }
  try {
    if (isDictEdit.value) {
      await updateDict(dictForm.id, dictForm)
      ElMessage.success('更新成功')
    } else {
      await createDict(dictForm)
      ElMessage.success('创建成功')
    }
    dictDialogVisible.value = false
    fetchDicts()
  } catch (e) { /* handled */ }
}

const handleDeleteDict = async (row) => {
  await ElMessageBox.confirm(`确定要删除字典 "${row.dictName}" 吗？`, '提示', { type: 'warning' })
  await deleteDict(row.id)
  ElMessage.success('删除成功')
  if (currentDict.value?.id === row.id) currentDict.value = null
  fetchDicts()
}

const handleAddItem = () => {
  isItemEdit.value = false
  itemDialogTitle.value = '新增字典项'
  Object.assign(itemForm, { id: null, itemLabel: '', itemValue: '', sortOrder: 0, status: 1 })
  itemDialogVisible.value = true
}

const handleEditItem = (row) => {
  isItemEdit.value = true
  itemDialogTitle.value = '编辑字典项'
  Object.assign(itemForm, row)
  itemDialogVisible.value = true
}

const handleItemSubmit = async () => {
  if (!itemForm.itemLabel || !itemForm.itemValue) {
    ElMessage.warning('请填写标签和值')
    return
  }
  try {
    if (isItemEdit.value) {
      await updateDictItem(currentDict.value.id, itemForm.id, itemForm)
      ElMessage.success('更新成功')
    } else {
      await createDictItem(currentDict.value.id, itemForm)
      ElMessage.success('创建成功')
    }
    itemDialogVisible.value = false
    fetchItems()
  } catch (e) { /* handled */ }
}

const handleDeleteItem = async (row) => {
  await ElMessageBox.confirm(`确定要删除字典项 "${row.itemLabel}" 吗？`, '提示', { type: 'warning' })
  await deleteDictItem(currentDict.value.id, row.id)
  ElMessage.success('删除成功')
  fetchItems()
}

onMounted(() => {
  fetchDicts()
})
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
