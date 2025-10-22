import { defineStore } from "pinia";
import { ref, computed } from "vue";
import type { User } from "@/types/user";
import { login, logout, getUserInfo } from "@/api";

export const useUserStore = defineStore("user", () => {
  const user = ref<User | null>(null);
  const token = ref<string | null>(localStorage.getItem("token"));

  const isLoggedIn = computed(() => !!token.value && !!user.value);

  const loginUser = async (username: string, password: string) => {
    try {
      const response = await login(username, password);
      token.value = response.token;
      user.value = response.user;
      localStorage.setItem("token", response.token);
      return response;
    } catch (error) {
      throw error;
    }
  };

  const logoutUser = async () => {
    try {
      await logout();
    } finally {
      token.value = null;
      user.value = null;
      localStorage.removeItem("token");
    }
  };

  const initUser = async () => {
    if (token.value) {
      try {
        const userInfo = await getUserInfo();
        user.value = userInfo;
      } catch (error) {
        // Token 无效，清除本地存储
        token.value = null;
        localStorage.removeItem("token");
      }
    }
  };

  return {
    user,
    token,
    isLoggedIn,
    loginUser,
    logoutUser,
    initUser
  };
});
