<template>
<div class="samples-root">
  <ul class="samples-list">
    <li><button @click="test">Test</button></li>
    <li>Hello World</li>
    <li><a href="#" @click="hello">Hello</a></li>
    <li>
      spinner
      <el-tooltip content="solid spinner"><fa-icon :icon="['fas', 'spinner']" :spin="true"/></el-tooltip>
    </li>
    <li>
      <el-tooltip content="solid check squere"><fa-icon :icon="['fas', 'check-square']"/></el-tooltip>
    </li>
    <li>
      <el-tooltip content="regular check squere"><fa-icon :icon="['far', 'check-square']"/></el-tooltip>
    </li>
    <li>
      <el-tooltip content="solid cofee">
        <fa-icon :icon="['fas', 'coffee']" :spin="true" size="3x" pul="right" style="color: #b71c1c"/>
      </el-tooltip>
    </li>
  </ul>
</div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import Axios, { AxiosError } from "axios";
import { HttpTodoApi } from "@/api/todo-api-http";

@Component
export default class SamplesComponent extends Vue {
  hello(): void {
    alert("Hello World !!!");
  }

  test(): void {
    function logError(err: Error): void {
      console.log("------------------- error ------------------------");
      console.log(err.stack);
      console.log("--------------------------------------------------");
    }

    const api = new HttpTodoApi("http://localhost:9000/api/todo/v2/");

    api.
      addList("new list form client").
      then(list => {
        console.log("after add list:");
        console.log(list);
        list.tasks.push(
          {
            description: "task 1",
            completed: true,
          },
          {
            description: "task 2",
            completed: true,
          },
          {
            description: "task 3",
            completed: false,
          },
        );
        console.log("before update list:");
        console.log(list);
        return api.updateList(list);
      }).
      then(list => {
        console.log("after update list:");
        console.log(list);
        return list;
      }).
      then(list => {
        return api.getLists().
          then(lists => {
            console.log("all lists:");
            console.log(lists);
            return list;
          });
      }).
      then(list => {
        console.log(`delete list: ${list.id}`);
        return api.deleteList(list.id as string).then(() => list);
      }).
      then(list => {
        return api.getLists().
          then(lists => {
            console.log("all lists:");
            console.log(lists);
            return list;
          });
      }).
      then(list => api.getList(list.id as string)).
      catch(err => logError(err));

    // create list
    // get list
    // api.
    //   getList("adf3c417-7a7b-4a33-8c3b-1e2497f3babf").
    //   then(resp => {
    //     console.log(resp);
    //   }).
    //   catch(err => logError(err));
    // // get lists
    // api.
    //   getLists().
    //   then(resp => {
    //     console.log(resp);
    //   }).
    //   catch(err => logError(err));
  }
}
</script>

<style lang="scss" scoped>
.samples-root {
  padding: 10px;
}

.samples-list {
  margin: 0;
  padding: 0;
  list-style: none;
}
</style>

