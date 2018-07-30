<template>
<div id="eui-todo-view">
  <el-container>
    <el-header id="eui-main-header">
      <b>ElementUI TODO</b>
    </el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col :span="12">
          <el-input
              ref="newListInput" 
              v-model.trim="newListName" 
              size="mini" 
              placeholder="input a name of list"
              autofocus
              clearable
              @keydown.native.enter="addList">
            <template slot="prepend">New List</template>
            <el-button slot="append" :disabled="!canAddList" @click="addList">Add</el-button>
          </el-input>
        </el-col>
      </el-row>

      <el-row v-for="list in lists" :key="list.id" class="todo-list" type="flex" justify="center" todo-list>
        <el-col :span="12">
          <eui-todo-list :list="list" @deleteList="deleteList"/>
        </el-col>
      </el-row>
    </el-main>
  </el-container>

  <el-dialog title="Add New List">

  </el-dialog>
</div>
</template>

<script lang="ts" src="./todo-view.ts">
</script>

<style lang="scss" scoped src="./todo.scss">
</style>
