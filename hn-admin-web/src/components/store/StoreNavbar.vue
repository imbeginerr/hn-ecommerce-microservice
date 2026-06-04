<template>
  <header class="sticky top-0 z-30 border-b border-zinc-200 bg-white/95 backdrop-blur">
    <div class="mx-auto grid w-full max-w-7xl grid-cols-[auto_1fr_auto] items-center gap-3 px-4 py-3 lg:px-5">
      <RouterLink class="flex items-center gap-3 text-zinc-950 no-underline" to="/shop" @click="$emit('home')">
        <span class="grid size-11 shrink-0 place-items-center rounded-lg bg-zinc-950 text-sm font-black text-white">HN</span>
        <strong class="hidden text-lg sm:block">Store</strong>
      </RouterLink>

      <div class="relative min-w-0">
        <el-input
          :model-value="searchText"
          clearable
          size="large"
          placeholder="Search product or category"
          @input="$emit('update:searchText', $event); $emit('search')"
          @clear="$emit('clear-search')"
        />
        <div v-if="searchText && searchResults.length" class="absolute left-0 right-0 top-[calc(100%+8px)] z-40 rounded-lg border border-zinc-200 bg-white p-2 shadow-xl">
          <button
            v-for="product in searchResults"
            :key="product.id"
            class="grid w-full grid-cols-[48px_1fr] items-center gap-3 rounded-lg p-2 text-left hover:bg-zinc-100"
            type="button"
            @click="$emit('select-search', product)"
          >
            <img class="size-12 rounded-lg object-cover" :src="primaryImage(product)" :alt="product.name" />
            <span class="font-semibold text-zinc-900">{{ product.name }}</span>
          </button>
        </div>
      </div>

      <div class="flex shrink-0 items-center justify-end gap-2">
        <el-badge :value="cartCount" :hidden="cartCount === 0">
          <el-button :icon="ShoppingCart" circle size="large" @click="$emit('open-cart')" />
        </el-badge>

        <el-dropdown v-if="username" trigger="click" @command="handleUserCommand">
          <button class="grid size-10 place-items-center rounded-full bg-zinc-950 text-xs font-black text-white" type="button">
            {{ avatarText }}
          </button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item disabled>{{ username }}</el-dropdown-item>
              <el-dropdown-item command="account">Account</el-dropdown-item>
              <el-dropdown-item command="logout">Sign out</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

        <el-button v-else :icon="User" size="large" @click="$emit('open-register')">Register</el-button>
        <RouterLink v-if="!username" to="/login">
          <el-button type="primary" size="large">Login</el-button>
        </RouterLink>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ShoppingCart, User } from '@element-plus/icons-vue';

defineProps({
  searchText: { type: String, default: '' },
  searchResults: { type: Array, default: () => [] },
  cartCount: { type: Number, default: 0 },
  username: { type: String, default: '' },
  avatarText: { type: String, default: 'U' },
  primaryImage: { type: Function, required: true }
});

const emit = defineEmits(['home', 'search', 'clear-search', 'select-search', 'open-cart', 'open-register', 'open-account', 'logout', 'update:searchText']);

function handleUserCommand(command) {
  if (command === 'account') emit('open-account');
  if (command === 'logout') emit('logout');
}
</script>
