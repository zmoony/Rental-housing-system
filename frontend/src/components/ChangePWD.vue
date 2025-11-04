<template>
  <el-dialog title="修改密码" v-model="changePWDFlag" :width="500">
    <el-form
        ref="ruleFormRef"
        :model="formInline"
        label-width="100px"
        label-position="right"
        :rules="rules"
    >
      <el-form-item label="账号" prop="loginName">
        <el-input v-model="formInline.loginName" placeholder="" disabled/>
      </el-form-item>
      <el-form-item label="原密码" prop="loginPwd">
        <el-input
            v-model="formInline.loginPwd"
            type="password"
            placeholder=""
            maxlength="20"
        />
      </el-form-item>
      <el-form-item label="新密码" prop="newLoginPwd">
        <el-input
            v-model="formInline.newLoginPwd"
            type="password"
            placeholder=""
            maxlength="20"
        />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmLoginPwd">
        <el-input
            v-model="formInline.confirmLoginPwd"
            type="password"
            placeholder=""
            maxlength="20"
        />
      </el-form-item>
    </el-form>
    <template #footer>
              <span class="dialog-footer">
                <el-button type="primary" @click="submit">确定</el-button>
                <el-button @click="logCancel">取消</el-button>
              </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {reactive, ref, watch} from "vue";
import {ElMessage, FormInstance} from "element-plus";
import {useUserStore} from "@/store/user";
import { useRoute, useRouter } from 'vue-router'
import {changePassword} from "@/api";

let props = defineProps({
  visiable: {
    type: Boolean,
    default: false
  }
})

watch(() => props.visiable, (val) => {
  changePWDFlag.value = val;
})

const changePWDFlag = ref(props.visiable)
const ruleFormRef = ref<FormInstance>()
const $router = useRouter()

const formInline = reactive({
  loginName: useUserStore().user.realName,
  loginPwd: '',
  newLoginPwd: '',
  confirmLoginPwd: '',
})
const rules = reactive({
  loginPwd: [
    {required: true, message: '请输入原密码', trigger: 'blur'}
  ],
  newLoginPwd: [
    {required: true, message: '请输入新密码', trigger: 'blur'},
  ],
  confirmLoginPwd: [
    {required: true, message: '请输入确认密码', trigger: 'blur'},
  ],
})
const submit = async () => {
  if (formInline.newLoginPwd !== formInline.confirmLoginPwd) {
    ElMessage({
      message: "两次密码输入不一致",
      type: "error",
    })
  } else {
    if (!ruleFormRef.value) return
    ruleFormRef.value.validate(async (valid: boolean, fields: any) => {
      if (valid) {
        let param = {
          "username": useUserStore().user.username,
          "oldPassword": formInline.loginPwd,
          "newPassword": formInline.newLoginPwd,
        }
        let res = await changePassword(param);
        if (res.status === 200) {
          ElMessage({
            message: "修改密码成功",
            type: "success",
          })
          logCancel();
        } else {
          ElMessage({
            message: res.message,
            type: "error",
          })
        }
      }
    })

  }
}

const logCancel = () => {
  formInline.loginPwd = '';
  formInline.newLoginPwd = '';
  formInline.confirmLoginPwd = '';
  changePWDFlag.value = false;
  useUserStore().logoutUser();
  $router.push('/login');
}

//增加enter监听
document.onkeydown = function (e) {
  if (e.keyCode === 13) {
    submit();
  }
}
</script>

<style lang="scss" scoped>

</style>
