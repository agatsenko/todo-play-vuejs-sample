<template>
<el-container id="app">
  <el-aside id="main-navigation" width="201px">
    <el-container>
      <el-header id="main-header"><b>Vuejs TODO</b></el-header>
      <el-aside width="200px">
        <el-menu
            id="main-menu" 
            mode="vertical" 
            :router="true"
            :default-active="defaultSelectedMenuItem.path" 
            @select="selectMenu"
        >
          <el-menu-item v-for="item in mainMenu" :key="item.id" :index="item.path">
            {{ item.title }}
          </el-menu-item>
        </el-menu>
      </el-aside>
    </el-container>
  </el-aside>

  <el-main id="main-container">
    <router-view/>
  </el-main>
</el-container>
</template>

<script lang="ts" src="./app.ts">
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
