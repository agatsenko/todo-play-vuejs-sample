<template>
<div class="samples-root">
  <ul class="samples-list">
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

@Component
export default class SamplesComponent extends Vue {
  hello(): void {
    alert("Hello World !!!");
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

