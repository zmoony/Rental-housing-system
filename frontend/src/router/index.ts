import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/store/user";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/login",
      name: "Login",
      component: () => import("@/views/Login.vue"),
      meta: { requiresAuth: false }
    },
    {
      path: "/",
      component: () => import("@/views/Layout.vue"),
      meta: { requiresAuth: true },
      children: [
        {
          path: "",
          name: "Dashboard",
          component: () => import("@/views/Dashboard.vue"),
          meta: { title: "仪表盘" }
        },
        {
          path: "/houses",
          name: "Houses",
          component: () => import("@/views/Houses.vue"),
          meta: { title: "房屋管理" }
        },
        {
          path: "/rooms",
          name: "Rooms",
          component: () => import("@/views/Rooms.vue"),
          meta: { title: "房间管理" }
        },
        {
          path: "/tenants",
          name: "Tenants",
          component: () => import("@/views/Tenants.vue"),
          meta: { title: "租户管理" }
        },
        {
          path: "/contracts",
          name: "Contracts",
          component: () => import("@/views/Contracts.vue"),
          meta: { title: "合同管理" }
        },
        {
          path: "/utilities",
          name: "Utilities",
          component: () => import("@/views/Utilities.vue"),
          meta: { title: "水电费管理" }
        },
        {
          path: "/finance",
          name: "Finance",
          component: () => import("@/views/Finance.vue"),
          meta: { title: "财务管理" }
        },
        {
          path: "/profile",
          name: "Profile",
          component: () => import("@/views/Profile.vue"),
          meta: { title: "个人中心" }
        }
      ]
    }
  ]
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();

  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next("/login");
  }  else {
    next();
  }
});

export default router;
