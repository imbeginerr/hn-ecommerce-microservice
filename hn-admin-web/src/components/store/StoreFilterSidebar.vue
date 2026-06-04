<template>
  <aside class="grid gap-0 rounded-xl border border-zinc-200 bg-white">
    <div class="border-b border-zinc-200 p-4">
      <h2 class="mb-3 text-xs font-black uppercase text-zinc-500">Categories</h2>
      <div class="grid gap-2">
        <el-button class="!ml-0 !w-full" :type="selectedCategory === '' ? 'primary' : 'default'" @click="$emit('select-category', '')">All products</el-button>
        <el-button
          v-for="category in categories"
          :key="category"
          class="!ml-0 !w-full"
          :type="selectedCategory === category ? 'primary' : 'default'"
          @click="$emit('select-category', category)"
        >
          {{ category }}
        </el-button>
      </div>
    </div>

    <div class="border-b border-zinc-200 p-4">
      <h2 class="mb-3 text-xs font-black uppercase text-zinc-500">Sort price</h2>
      <div class="grid gap-2 sm:grid-cols-2 lg:grid-cols-1">
        <el-button class="!ml-0 !w-full" :icon="SortDown" :type="sortMode === 'desc' ? 'primary' : 'default'" @click="$emit('update:sortMode', 'desc')">High to low</el-button>
        <el-button class="!ml-0 !w-full" :icon="SortUp" :type="sortMode === 'asc' ? 'primary' : 'default'" @click="$emit('update:sortMode', 'asc')">Low to high</el-button>
      </div>
    </div>

    <div class="p-4">
      <h2 class="mb-3 text-xs font-black uppercase text-zinc-500">Price range</h2>
      <div class="grid gap-3">
        <el-slider :model-value="priceRange" range :min="0" :max="maxPrice" :step="100000" @input="$emit('update:priceRange', $event)" />
        <div class="flex items-center justify-between text-xs font-bold text-zinc-500">
          <span>{{ formatPrice(priceRange[0]) }}</span>
          <span>{{ formatPrice(priceRange[1]) }}</span>
        </div>
        <el-button class="!ml-0 !w-full" type="primary" plain @click="$emit('apply-price')">Filter price</el-button>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { SortDown, SortUp } from '@element-plus/icons-vue';

defineProps({
  categories: { type: Array, default: () => [] },
  selectedCategory: { type: String, default: '' },
  sortMode: { type: String, default: 'desc' },
  priceRange: { type: Array, default: () => [0, 0] },
  maxPrice: { type: Number, default: 0 },
  formatPrice: { type: Function, required: true }
});

defineEmits(['select-category', 'update:sortMode', 'update:priceRange', 'apply-price']);
</script>
