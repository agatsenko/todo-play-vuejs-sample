<template>
<el-dialog
    title="Edit Task" 
    width="790px" 
    :visible.sync="dialogVisible" 
    :modal="true" 
    :append-to-body="true"
    @closed="clearData()">
  <div class="eui-todo-item-edit-dialog-content">
    <span class="eui-todo-item-edit-dialog-content-item">Description:</span>
    <el-input
        class="eui-todo-item-edit-dialog-content-item"
        width="50%"
        ref="descriptionInput"
        v-model.trim="description"
        size="mini"
        placeholder="input a description of task"
        clearable
        @keydown.native.enter="okHandler">
    </el-input>
    <el-button-group class="eui-todo-item-edit-dialog-content-item" id="eui-todo-item-edit-dialog-buttons">
      <el-button size="mini" type="primary" :disabled="!canApply" @click="okHandler()">OK</el-button>
      <el-button size="mini" type="primary" @click="closeDialog()">Cancel</el-button>
    </el-button-group>
  </div>
</el-dialog>
</template>

<script lang="ts" src="./todo-item-editor.ts">
</script>

<style lang="scss" scoped>
.eui-todo-item-edit-dialog-content {
  display: flex;
  flex-wrap: nowrap;
  justify-content: space-between;
  align-items: center;
  justify-content: space-between;
}

.eui-todo-item-edit-dialog-content-item {
  margin: 0 6px;
}

#eui-todo-item-edit-dialog-buttons {
  flex-shrink: 0;
}
</style>
