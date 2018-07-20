<template>
<el-container id="app">
  <el-aside id="main-navigation" width="201px">
    <el-container>
      <el-header id="main-header"><b>Vuejs TODO</b></el-header>
      <el-aside width="200px">
        <el-menu id="main-menu" mode="vertical">
          <el-menu-item index="1">Simple TODO List</el-menu-item>
          <el-menu-item index="2">Element UI TODO List</el-menu-item>
        </el-menu>
      </el-aside>
    </el-container>
  </el-aside>

  <el-main id="main-container">
    <el-container>
      <el-header>
        <el-menu mode="horizontal">
          <el-menu-item index="1">one</el-menu-item>
          <el-menu-item index="2">two</el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
        main <el-tooltip content="solid spinner"><fa-icon :icon="['fas', 'spinner']" :spin="true"/></el-tooltip><br>
        <el-tooltip content="solid check squere"><fa-icon :icon="['fas', 'check-square']"/></el-tooltip><br>
        <el-tooltip content="regular check squere"><fa-icon :icon="['far', 'check-square']"/></el-tooltip><br>
        <el-tooltip content="solid cofee"><fa-icon :icon="['fas', 'coffee']" :spin="true" size="3x" pul="right" style="color: #b71c1c"/></el-tooltip><br>
      </el-main>
    </el-container>
  </el-main>

</el-container>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component({
  components: {
  },
})
export default class App extends Vue {
}
</script>

<style lang="scss">
$navigation-border: 1px solid #e6e6e6;

html body {
  padding: 0;
  margin: 0;
  color: #2c3e50;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei",Arial,sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.el-header {
  line-height: 60px;
}

#app {
  position: fixed;
  width: 100%;
  height: 100%;
}

#main-navigation {
  border-right: $navigation-border;
  width: 100%;
  height: 100%;
}

#main-menu {
  border: none;
}

#main-header {
  font-size: 20px;
  border-bottom: $navigation-border;
  margin: 0 20px;
}

#main-container {
  padding: 0;
  margin: 0;
}
</style>
