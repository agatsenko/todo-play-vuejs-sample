import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    // {
    //   path: "/",
    //   name: "root",
    //   component: () => import(/*webpackChunkName: "simple-todo"*/ "@/components/simple-todo/todo-list"),
    // },
    {
      path: "/simple-todo.html",
      alias: "/",
      name: "simple-todo",
      component: () => import(/*webpackChunkName: "simple-todo"*/ "@/components/simple-todo/todo-list"),
    },
    {
      path: "/element-ui-todo.html",
      name: "element-ui-todo",
      component: () => import(/*webpackChunkName: "element-ui-todo"*/ "@/components/elementui-todo/todo-view"),
    },
    {
      path: "/samples.html",
      name: "samples",
      component: () => import(/*webpackChunkName: "samples"*/ "@/components/samples.vue"),
    },
    {
      path: "*",
      name: "not-found",
      component: () => import(/*webpackChunkName: "not-found"*/ "@/components/not-found.vue"),
    },
  ],
});
