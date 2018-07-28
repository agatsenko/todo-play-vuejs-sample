<template>
<el-container id="app">
  <el-aside id="main-navigation" width="201px">
    <el-container>
      <el-header id="main-header"><b>Vuejs TODO</b></el-header>
      <el-aside width="200px">
        <el-menu id="main-menu" mode="vertical" :default-active="defaultSelectedMenuItem.id" @select="selectMenu">
          <el-menu-item v-for="item in mainMenu" :key="item.id" :index="item.id">
            {{ item.title }}
          </el-menu-item>
        </el-menu>
      </el-aside>
    </el-container>
  </el-aside>

  <el-main id="main-container">
    <component v-if="selectedMenuItem !== null" :is="selectedMenuItem.componentName"/>
  </el-main>
</el-container>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import SimpleTodoListComponent from "@/components/simple-todo/todo-list";
import SamplesComponent from "@/components/samples.vue";

import { MenuItem } from "@/components/menu.ts";

@Component({
  components: {
    SimpleTodoListComponent,
    SamplesComponent,
  },
})
export default class App extends Vue {
  mainMenu: MenuItem[] = [
    new MenuItem("1", "Simple TODO List", "simple-todo-list-component"),
    new MenuItem("2", "ElementUI TODO List", "samples-component"),
    new MenuItem("3", "Samples", "samples-component"),
  ];

  selectedMenuItem: MenuItem | null = this.defaultSelectedMenuItem;

  get defaultSelectedMenuItem(): MenuItem {
    return this.mainMenu[0];
  }

  selectMenu(index: string): void {
    const foundItem = this.mainMenu.find(item => item.id === index);
    this.selectedMenuItem = foundItem === undefined ? null : foundItem;
  }
}
</script>

<style lang="scss" scoped>
@import "./styles/common";

#app {
  position: fixed;
  width: 100%;
  height: 100%;
}

#main-navigation {
  border-right: $nav-border;
  width: 100%;
  height: 100%;
}

#main-menu {
  border: none;
}

#main-header {
  @extend %main-header;
}

#main-container {
  padding: 0;
  margin: 0;
}
</style>
