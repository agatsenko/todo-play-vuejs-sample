<template>
<div id="not-found-view">
  <h1>404</h1>
  <p><b>Page not found</b></p>
  <p>The site configured at this address does not contain the requested page.</p>
</div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

export default class NotFoundView extends Vue {
}
</script>

<style lang="scss" scoped>
#not-found-view {
  text-align: center;
}
</style>
