<template>
<el-row type="flex" justify="center" :gutter="rowGutter">
  <el-col :xs="smOuterColSpan"
          :sm="smOuterColSpan"
          :md="smOuterColSpan"
          :lg="mdOuterColSpan"
          :xl="lgOuterColSpan">
    <div align="right"><input type="checkbox" v-model="completed" :true-value="true" :false-value="false"></div>
  </el-col>
  <el-col :xs="smMiddleColSpan"
          :sm="smMiddleColSpan"
          :md="smMiddleColSpan"
          :lg="mdMiddleColSpan"
          :xl="lgMiddleColSpan">
    <div :class="{ 'stodo-task-completed': completed }">{{ todoItem.description }}</div>
  </el-col>
  <el-col :xs="smOuterColSpan"
          :sm="smOuterColSpan"
          :md="smOuterColSpan"
          :lg="mdOuterColSpan"
          :xl="lgOuterColSpan">
    <button class="stodo-action" @click="remove">Remove</button>
  </el-col>
</el-row>
</template>

<script lang="ts" src="./todo-list-item.ts">
</script>

<style lang="scss" scoped>
@import "./../../styles/common";
@import "./../../styles/simple-todo";
</style>

