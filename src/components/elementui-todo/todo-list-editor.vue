<template>
<el-dialog
    title="Rename List" 
    width="460px" 
    :visible.sync="dialogVisible" 
    :modal="true" 
    :append-to-body="true"
    @closed="clearData()">
  <el-form :inline="true" @submit.native.prevent="okHandler">
    <el-form-item label="List Name:">
      <el-input
          ref="listNameInput"
          v-model.trim="listName"
          size="mini"
          placeholder="input a name of list"
          clearable
          @keydown.native.enter="okHandler">
      </el-input>
    </el-form-item>
    <el-form-item>
      <el-button-group>
        <el-button size="mini" type="primary" :disabled="!canApply" @click="okHandler">OK</el-button>
        <el-button size="mini" type="primary" @click="closeDialog">Cancel</el-button>
      </el-button-group>
    </el-form-item>
  </el-form>
</el-dialog>
</template>

<script lang="ts" src="./todo-list-editor.ts">
</script>

<style lang="scss" scoped>
</style>
