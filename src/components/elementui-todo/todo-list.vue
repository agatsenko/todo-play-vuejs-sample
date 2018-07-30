<template>
<div>
  <el-card shadow="hover">
    <div slot="header" class="eui-todo-list-header">
      <b>{{ list.name }}</b>
      <div>
        <el-button size="mini" type="text" @click="renameList">Rename</el-button>
        <el-button size="mini" type="text" @click="deleteList">Delete</el-button>
      </div>
    </div>
    <div>
      <el-row todo-list-content>
        <el-col :span="24">
          <el-input 
              ref="newItemInput"
              v-model.trim="newItemDescription"
              size="mini"
              placeholder="input a description of task"
              clearable
              @keydown.native.enter="addItem"
              @>
            <template slot="prepend">New Task</template>
            <el-button slot="append" :disabled="!canAddItem" @click="addItem">Add</el-button>
          </el-input>
        </el-col>
      </el-row>
      <el-row todo-list-content>
        <el-col :span="24">
          <el-table style="width: 100%" :data="items" :row-class-name="rowClassName">
            <el-table-column
                label="Completed"
                width="140"
                prop="completed"
                sortable
                :filters="[{text: 'Active', value: false}, {text: 'Completed', value: true}]"
                :filter-method="filterItems">
              <template slot-scope="scope">
                <el-checkbox v-model="scope.row.completed"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="Description" sortable></el-table-column>
            <el-table-column label="Actions" width="160">
              <template slot-scope="scope">
                <el-button size="mini" @click="editItem(scope.row)">Edit</el-button>
                <el-button size="mini" type="danger" @click="deleteItem(scope.row)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
      <el-row todo-list-content>
        <el-col :span="24">
          <span id="eui-todo-list-table-footer">{{ activeItemsCount }} items left</span>
        </el-col>
      </el-row>
    </div>
  </el-card>

  <EuiTodoListEditDialog ref="renameListDialog"/>
  <eui-todo-item-edit-dialog ref="itemEditDialog"/>
</div>
</template>

<script lang="ts" src="./todo-list.ts">
</script>

<style lang="scss">
@import "./todo.scss";

.eui-todo-list-header:after {
  clear: both;
}

.eui-todo-list-header div {
  float: right;
}

#eui-todo-list-table-footer {
  font-size: 0.8em;
  color: #909399;
}
</style>
