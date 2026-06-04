<template>
  <section class="grid gap-5">
    <div class="flex items-center justify-between gap-4">
      <h2 class="m-0 text-2xl font-black text-zinc-950">Products</h2>
      <span class="text-sm font-bold text-zinc-500">{{ products.length }} items</span>
    </div>

    <el-alert v-if="error" :title="error" type="error" show-icon :closable="false" />
    <div v-else-if="loading" class="grid grid-cols-1 gap-4 lg:grid-cols-2 2xl:grid-cols-3">
      <el-skeleton v-for="index in 6" :key="index" animated>
        <template #template>
          <el-skeleton-item variant="image" class="!h-48 !w-full" />
          <el-skeleton-item variant="h3" class="mt-4 !w-2/3" />
          <el-skeleton-item variant="text" class="mt-2 !w-1/3" />
        </template>
      </el-skeleton>
    </div>

    <div v-else class="grid grid-cols-1 gap-4 lg:grid-cols-2 2xl:grid-cols-3">
      <article v-for="product in products" :key="product.id" class="overflow-hidden rounded-xl border border-zinc-200 bg-white shadow-sm transition hover:-translate-y-0.5 hover:shadow-lg">
        <button class="block aspect-[4/3] w-full overflow-hidden bg-zinc-100" type="button" @click="$emit('detail', product)">
          <img class="h-full w-full object-cover transition duration-300 hover:scale-105" :src="primaryImage(product)" :alt="product.name" />
        </button>
        <div class="grid gap-3 p-4">
          <div>
            <span class="text-xs font-black uppercase text-emerald-700">{{ product.category || 'General' }}</span>
            <h3 class="mt-1 line-clamp-2 text-lg font-black text-zinc-950">{{ product.name }}</h3>
          </div>
          <p class="m-0 text-base font-black text-zinc-900">{{ formatPrice(product.price) }}</p>
          <div class="grid grid-cols-2 gap-2">
            <el-button :icon="ShoppingCart" @click="$emit('add-cart', product)">Add</el-button>
            <el-button type="primary" :icon="ShoppingBag" @click="$emit('detail', product)">Buy</el-button>
          </div>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
import { ShoppingBag, ShoppingCart } from '@element-plus/icons-vue';

defineProps({
  products: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  error: { type: String, default: '' },
  primaryImage: { type: Function, required: true },
  formatPrice: { type: Function, required: true }
});

defineEmits(['detail', 'add-cart']);
</script>
