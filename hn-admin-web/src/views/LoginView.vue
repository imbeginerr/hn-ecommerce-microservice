<template>
  <main class="login-shell">
    <section class="login-panel">
      <div>
        <p class="eyebrow">HN Ecommerce</p>
        <h1>Sign in</h1>
      </div>

      <form class="form-stack" @submit.prevent="submit">
        <label>
          Username
          <input v-model="form.username" autocomplete="username" required />
        </label>

        <label>
          Password
          <input v-model="form.password" autocomplete="current-password" type="password" required />
        </label>

        <p v-if="error" class="error-text">{{ error }}</p>
        <button class="primary-button" type="submit" :disabled="loading">
          {{ loading ? 'Signing in...' : 'Sign in' }}
        </button>
        <div class="login-link-row">
          <button class="secondary-link" type="button" @click="showRegister = true">Register</button>
          <button class="secondary-link" type="button" @click="forgotPassword">Forgot password?</button>
        </div>
      </form>
    </section>
    <RegisterDialog v-model="showRegister" :form="registerForm" :loading="registering" @submit="register" />
  </main>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { login } from '../api/authApi';
import { registerAccount } from '../api/accountApi';
import RegisterDialog from '../components/store/RegisterDialog.vue';
import { getHomePath } from '../utils/authScope';
import { setTokens } from '../utils/tokenStorage';
import { notifySuccess } from '../utils/notify';

const router = useRouter();
const loading = ref(false);
const registering = ref(false);
const showRegister = ref(false);
const error = ref('');
const form = reactive({
  username: '',
  password: ''
});
const registerForm = reactive({ username: '', fullName: '', password: '', dob: '' });

async function submit() {
  loading.value = true;
  error.value = '';
  try {
    const response = await login(form);
    setTokens(response);
    router.push(getHomePath());
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
}

function forgotPassword() {
  ElMessage.info('Password recovery will be available after email or phone verification is configured.');
}

async function register() {
  registering.value = true;
  try {
    await registerAccount({ ...registerForm });
    notifySuccess('Account registered. You can login now.');
    showRegister.value = false;
    registerForm.username = '';
    registerForm.fullName = '';
    registerForm.password = '';
    registerForm.dob = '';
  } finally {
    registering.value = false;
  }
}
</script>
