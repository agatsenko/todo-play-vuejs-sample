<template>
<div>
  <el-card shadow="hover">
    <div slot="header">
      <b>{{ list.name }}</b>
    </div>
    <div>
      <el-row todo-list-content>
        <el-col :span="24">
          <el-input ref="newItemInput" v-model.trim="newItemDescription" size="mini" placeholder="input a description of task" clearable>
            <template slot="prepend">New Task</template>
            <el-button slot="append" :disabled="!canAddItem" @click="addItem">Add</el-button>
          </el-input>
        </el-col>
      </el-row>
      <el-row todo-list-content>
        <el-col :span="24">
          <el-table style="width: 100%" :data="items" :row-class-name="rowClassName">
            <el-table-column label="Completed" width="100">
              <template slot-scope="scope">
                <el-checkbox v-model="scope.row.completed"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="Description"></el-table-column>
            <el-table-column label="Actions" width="150">
              <template slot-scope="scope">
                <el-button size="mini" @click="editItem(scope.row)">Edit</el-button>
                <el-button size="mini" type="danger" @click="deleteItem(scope.row)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </div>
  </el-card>

  <el-dialog ref="editItemDlg" title="Edit Task" width="460px" :visible.sync="showEditDialog" :modal="false">
    <el-form :inline="true">
      <el-form-item label="Description:">
        <el-input v-model="itemToEdit" size="mini" placeholder="input a description of task" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button-group>
          <el-button size="mini" type="primary">OK</el-button>
          <el-button size="mini" type="primary">Cancel</el-button>
        </el-button-group>
      </el-form-item>
    </el-form>
  </el-dialog>
</div>
</template>

<script lang="ts" src="./todo-list.ts">
</script>

<style lang="scss" src="./todo.scss">
</style>
