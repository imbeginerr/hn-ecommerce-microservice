<template>
  <el-dialog :model-value="modelValue" title="Create account" width="min(520px, 94vw)" @close="$emit('update:modelValue', false)">
    <el-form label-position="top" @submit.prevent>
      <el-form-item label="Username">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="Full name">
        <el-input v-model="form.fullName" />
      </el-form-item>
      <el-form-item label="Password">
        <el-input v-model="form.password" type="password" show-password />
      </el-form-item>
      <el-form-item label="Date of birth">
        <el-date-picker v-model="form.dob" class="!w-full" type="date" value-format="DD/MM/YYYY" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:modelValue', false)">Cancel</el-button>
      <el-button type="primary" :loading="loading" @click="$emit('submit')">Register</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
defineProps({
  modelValue: { type: Boolean, default: false },
  form: { type: Object, required: true },
  loading: { type: Boolean, default: false }
});

defineEmits(['update:modelValue', 'submit']);
</script>
