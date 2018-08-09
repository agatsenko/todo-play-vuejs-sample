<template>
<el-container class="stodo">
  <el-header id="stodo-main-header"><b>Simple TODO List</b></el-header>
  <el-main>
    <el-row type="flex" :justify="rowJustify" :gutter="rowGutter">
      <el-col :xs="smOuterColSpan"
              :sm="smOuterColSpan"
              :md="smOuterColSpan"
              :lg="mdOuterColSpan"
              :xl="lgOuterColSpan">
        <div class="stodo-label"><label for="stodo-new-todo">New Task:</label></div>
      </el-col>
      <el-col :xs="smMiddleColSpan"
              :sm="smMiddleColSpan"
              :md="smMiddleColSpan"
              :lg="mdMiddleColSpan"
              :xl="lgMiddleColSpan">
        <input id="stodo-new-todo" 
               type="text"
               class="stodo-new-task" 
               v-model.trim="newTaskDescription"
               @keyup.enter="addNewItem"
               placeholder="enter the description of tsak"
               autofocus>
      </el-col>
      <el-col :xs="smOuterColSpan"
              :sm="smOuterColSpan"
              :md="smOuterColSpan"
              :lg="mdOuterColSpan"
              :xl="lgOuterColSpan">
        <button class="stodo-action" @click="addNewItem()" :disabled="!canAddTask">Add</button>
      </el-col>
    </el-row>
    <simple-todo-item-component v-for="item in todoItems"
                                :key="item.id"
                                :todoItem="item"
                                :rowJustify="rowJustify"
                                :rowGutter="rowGutter"
                                :smOuterColSpan="smOuterColSpan"
                                :smMiddleColSpan="smMiddleColSpan"
                                :mdOuterColSpan="mdOuterColSpan"
                                :mdMiddleColSpan="mdMiddleColSpan"
                                :lgOuterColSpan="lgOuterColSpan"
                                :lgMiddleColSpan="lgMiddleColSpan"
                                @remove="removeItem"
    />
    <el-row type="flex" :justify="rowJustify" :gutter="rowGutter">
      <el-col :xs="smMiddleColSpan"
              :sm="smMiddleColSpan"
              :md="smMiddleColSpan"
              :lg="mdMiddleColSpan"
              :xl="lgMiddleColSpan">
        <div class="stodo-footer">
          <el-row>
            <el-col :span="8">
              <div class="stodo-footer-text"><span>{{ itemsLeft }} items left</span></div>
            </el-col>
            <el-col :span="8">
              <div class="stodo-footer-filter">
                <ul>
                  <li>
                    <a @click="filterItems(filter.all)" :class="{selected: filter.selected == filter.all}">
                      {{ filter.all }}
                    </a>
                  </li>
                  <li>
                    <a @click="filterItems(filter.active)" :class="{selected: filter.selected == filter.active}">
                      {{ filter.active }}
                    </a>
                  </li>
                  <li>
                    <a @click="filterItems(filter.completed)" :class="{selected: filter.selected == filter.completed}">
                      {{ filter.completed }}
                    </a>
                  </li>
                </ul>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stodo-footer-clear-completed"><a @click="clearCompleted">Clear of Done</a></div>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
  </el-main>
</el-container>
</template>

<script lang="ts" src="./todo-list.ts">
</script>

<style lang="scss" scoped>
@import "./../../styles/common";
@import "./../../styles/simple-todo";

#stodo-main-header {
  @extend %main-header;
}
</style>
