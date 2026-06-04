<template>
  <el-drawer :model-value="modelValue" title="Cart" size="520px" @update:model-value="$emit('update:modelValue', $event)">
    <div class="grid h-full gap-4">
      <el-alert v-if="message" :title="message" :type="message.startsWith('Cannot') || message.startsWith('Please') ? 'error' : 'success'" show-icon />
      <el-empty v-if="items.length === 0" description="Your cart is empty." />
      <div v-else class="grid content-start gap-3">
        <article v-for="item in items" :key="item.product.id" class="grid grid-cols-[72px_1fr] gap-3 rounded-xl border border-zinc-200 p-3">
          <img class="size-18 rounded-lg object-cover" :src="primaryImage(item.product)" :alt="item.product.name" />
          <div class="grid gap-2">
            <div>
              <strong class="block text-zinc-950">{{ item.product.name }}</strong>
              <span class="text-sm font-bold text-zinc-500">{{ formatPrice(item.product.price) }}</span>
            </div>
            <div class="flex items-center gap-2">
              <el-input-number :model-value="item.quantity" :min="1" size="small" @change="$emit('quantity-change', item.product.id, $event)" />
              <el-button type="danger" plain size="small" @click="$emit('remove', item.product.id)">Remove</el-button>
            </div>
          </div>
        </article>

        <div class="mt-2 flex items-center justify-between border-t border-zinc-200 pt-4">
          <strong>Total</strong>
          <span class="text-lg font-black">{{ formatPrice(total) }}</span>
        </div>

        <el-form label-position="top">
          <el-form-item label="Payment method">
            <el-select :model-value="paymentMethod" @change="$emit('update:paymentMethod', $event)">
              <el-option label="Cash" value="CASH" />
              <el-option label="Bank transfer" value="BANK_TRANSFER" />
              <el-option label="Card" value="CARD" />
              <el-option label="E-wallet" value="E_WALLET" />
            </el-select>
          </el-form-item>
        </el-form>

        <el-button type="primary" size="large" :loading="checkingOut" @click="$emit('checkout')">Checkout cart</el-button>
      </div>
    </div>
  </el-drawer>
</template>

<script setup>
defineProps({
  modelValue: { type: Boolean, default: false },
  items: { type: Array, default: () => [] },
  total: { type: Number, default: 0 },
  message: { type: String, default: '' },
  paymentMethod: { type: String, default: 'CASH' },
  checkingOut: { type: Boolean, default: false },
  primaryImage: { type: Function, required: true },
  formatPrice: { type: Function, required: true }
});

defineEmits(['update:modelValue', 'update:paymentMethod', 'quantity-change', 'remove', 'checkout']);
</script>
