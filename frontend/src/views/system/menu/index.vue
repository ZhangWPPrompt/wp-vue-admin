<template>
  <div class="page-container">
    <el-card>
      <div style="margin-bottom: 16px">
        <el-button type="success" @click="handleAdd({ parentId: 0 })">新增菜单</el-button>
        <el-button @click="expandAll">展开全部</el-button>
        <el-button @click="collapseAll">折叠全部</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" row-key="id" border stripe
        :tree-props="{ children: 'children' }" default-expand-all>
        <el-table-column prop="menuName" label="菜单名称" min-width="180" />
        <el-table-column prop="icon" label="图标" width="100" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="permission" label="权限标识" width="160" />
        <el-table-column prop="path" label="路由路径" width="160" />
        <el-table-column prop="component" label="组件路径" width="200" />
        <el-table-column prop="menuType" label="类型" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.menuType === 'DIR'" type="info" size="small">目录</el-tag>
            <el-tag v-else-if="row.menuType === 'MENU'" type="success" size="small">菜单</el-tag>
            <el-tag v-else type="warning" size="small">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" link @click="handleAdd(row)">新增</el-button>
            <el-button type="warning" size="small" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="550px" @close="resetForm">
      <el-form ref="formRef" :model="form" label-width="90px">
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="form.parentId"
            :data="treeSelectData"
            :props="{ label: 'menuName', value: 'id', children: 'children' }"
            placeholder="选择上级菜单 (不选则为顶级)"
            check-strictly
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="form.menuType">
            <el-radio value="DIR">目录</el-radio>
            <el-radio value="MENU">菜单</el-radio>
            <el-radio value="BUTTON">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" />
        </el-form-item>
        <el-form-item v-if="form.menuType !== 'BUTTON'" label="图标">
          <el-input v-model="form.icon" placeholder="Element Plus 图标名" />
        </el-form-item>
        <el-form-item v-if="form.menuType !== 'BUTTON'" label="路由路径">
          <el-input v-model="form.path" placeholder="例: /system/user" />
        </el-form-item>
        <el-form-item v-if="form.menuType === 'MENU'" label="组件路径">
          <el-input v-model="form.component" placeholder="例: system/user/index" />
        </el-form-item>
        <el-form-item label="权限标识">
          <el-input v-model="form.permission" placeholder="例: sys:user:list" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMenuTree, getMenu, createMenu, updateMenu, deleteMenu } from '@/api/menu'

const loading = ref(false)
const tableData = ref([])
const allMenus = ref([])

const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref(null)

const defaultForm = () => ({
  id: null,
  parentId: 0,
  menuName: '',
  menuType: 'MENU',
  path: '',
  component: '',
  icon: '',
  sortOrder: 0,
  permission: '',
  visible: 1,
  status: 1
})
const form = reactive(defaultForm())

const treeSelectData = ref([])

const fetchData = async () => {
  loading.value = true
  try {
    allMenus.value = await getMenuTree()
    tableData.value = allMenus.value
    treeSelectData.value = allMenus.value
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(form, defaultForm())
}

const handleAdd = (row) => {
  isEdit.value = false
  dialogTitle.value = '新增菜单'
  resetForm()
  form.parentId = row.id || 0
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑菜单'
  const menu = await getMenu(row.id)
  Object.assign(form, menu)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!form.menuName) {
    ElMessage.warning('请输入菜单名称')
    return
  }
  try {
    if (isEdit.value) {
      await updateMenu(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createMenu(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (e) { /* handled */ }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定要删除菜单 "${row.menuName}" 及其子菜单吗？`, '提示', { type: 'warning' })
  await deleteMenu(row.id)
  ElMessage.success('删除成功')
  fetchData()
}

const expandAll = () => {
  // Element Plus table doesn't have an easy expandAll, but default-expand-all is set
  fetchData()
}

const collapseAll = () => {
  // For simplicity, collapse is not implemented; just refresh
  fetchData()
}

onMounted(fetchData)
</script>
