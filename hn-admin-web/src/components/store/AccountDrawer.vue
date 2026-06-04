<template>
  <el-drawer :model-value="modelValue" title="Account" size="420px" @open="$emit('load')" @update:model-value="$emit('update:modelValue', $event)">
    <el-skeleton v-if="loading" animated :rows="6" />
    <el-form v-else label-position="top">
      <div class="mb-5 flex items-center gap-3">
        <div class="grid size-14 place-items-center rounded-full bg-zinc-950 text-base font-black text-white">{{ avatarText }}</div>
        <div>
          <strong class="block text-zinc-950">{{ form.username }}</strong>
          <span class="text-sm text-zinc-500">{{ form.fullName || 'No name' }}</span>
        </div>
      </div>
      <el-form-item label="Username">
        <el-input v-model="form.username" disabled />
      </el-form-item>
      <el-form-item label="Full name">
        <el-input v-model="form.fullName" />
      </el-form-item>
      <el-form-item label="Date of birth">
        <el-date-picker v-model="form.dob" class="!w-full" type="date" value-format="DD/MM/YYYY" />
      </el-form-item>
      <el-form-item label="Address">
        <el-input v-model="form.address" type="textarea" :rows="3" placeholder="Shipping address" />
      </el-form-item>
      <el-button type="primary" class="!w-full" :loading="saving" @click="$emit('save')">Save account</el-button>
    </el-form>
  </el-drawer>
</template>

<script setup>
defineProps({
  modelValue: { type: Boolean, default: false },
  form: { type: Object, required: true },
  avatarText: { type: String, default: 'U' },
  loading: { type: Boolean, default: false },
  saving: { type: Boolean, default: false }
});

defineEmits(['update:modelValue', 'load', 'save']);
</script>
