<template>
  <el-dialog
    :model-value="Boolean(product)"
    class="product-detail-dialog"
    width="min(1040px, 94vw)"
    destroy-on-close
    @close="$emit('close')"
  >
    <template v-if="product">
      <div class="grid max-h-[82vh] min-h-0 overflow-hidden">
        <div class="grid min-h-0 gap-6 overflow-hidden md:grid-cols-[minmax(260px,500px)_1fr]">
          <div class="grid content-start gap-3 overflow-hidden">
            <img class="h-auto max-h-[42vh] w-full rounded-xl bg-zinc-100 object-cover md:aspect-square md:max-h-[54vh]" :src="currentImage" :alt="product.name" />
            <div class="flex items-center justify-between">
              <el-button @click="$emit('previous')">Prev</el-button>
              <span class="text-sm font-bold text-zinc-500">{{ imageIndex + 1 }} / {{ images.length }}</span>
              <el-button @click="$emit('next')">Next</el-button>
            </div>
          </div>

          <div class="grid content-start gap-4">
            <p class="m-0 text-xs font-black uppercase text-emerald-700">{{ product.category || 'Product' }}</p>
            <h2 class="m-0 text-3xl font-black leading-tight text-zinc-950 md:text-4xl">{{ product.name }}</h2>
            <strong class="text-xl">{{ formatPrice(product.price) }}</strong>
            <div class="prose max-h-[32vh] max-w-none overflow-y-auto rounded-xl border border-zinc-100 bg-white p-4 text-zinc-700 md:max-h-[38vh]" v-html="safeDescription"></div>
          </div>
        </div>

        <div class="mt-4 border-t border-zinc-200 bg-white pt-4">
          <el-form label-position="top">
            <div class="grid gap-3 sm:grid-cols-[150px_1fr]">
              <el-form-item label="Quantity">
                <el-input-number :model-value="quantity" :min="1" @change="$emit('update:quantity', $event)" />
              </el-form-item>
              <el-form-item label="Payment method">
                <el-select :model-value="paymentMethod" @change="$emit('update:paymentMethod', $event)">
                  <el-option label="Cash" value="CASH" />
                  <el-option label="Bank transfer" value="BANK_TRANSFER" />
                  <el-option label="Card" value="CARD" />
                  <el-option label="E-wallet" value="E_WALLET" />
                </el-select>
              </el-form-item>
            </div>
          </el-form>

          <el-button type="primary" size="large" :loading="checkingOut" @click="$emit('checkout')">Checkout</el-button>
          <el-alert v-if="message" :title="message" :type="message.startsWith('Cannot') || message.startsWith('Please') ? 'error' : 'success'" show-icon />
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
defineProps({
  product: { type: Object, default: null },
  images: { type: Array, default: () => [] },
  currentImage: { type: String, default: '' },
  imageIndex: { type: Number, default: 0 },
  safeDescription: { type: String, default: '' },
  quantity: { type: Number, default: 1 },
  paymentMethod: { type: String, default: 'CASH' },
  checkingOut: { type: Boolean, default: false },
  message: { type: String, default: '' },
  formatPrice: { type: Function, required: true }
});

defineEmits(['close', 'previous', 'next', 'checkout', 'update:quantity', 'update:paymentMethod']);
</script>

<style scoped>
:deep(.product-detail-dialog .el-dialog__body) {
  padding-top: 12px;
  padding-bottom: 16px;
  overflow: hidden;
}

@media (max-width: 767px) {
  :deep(.product-detail-dialog) {
    margin-top: 12px;
    margin-bottom: 12px;
    width: 94vw;
  }

  :deep(.product-detail-dialog .el-dialog__body) {
    padding-left: 14px;
    padding-right: 14px;
  }
}
</style>
