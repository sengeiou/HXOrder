<template> 
  <store-detail :is-edit='false'></store-detail>
</template>
<script>
  import StoreDetail from './components/StoreDetail'
  export default {
    name: 'addProduct',
    components: { StoreDetail }
  }
</script>
<style>
</style>
