<template> 
  <store-detail :is-edit='true'></store-detail>
</template>
<script>
  import StoreDetail from './components/StoreDetail'
  export default {
    name: 'update',
    components: { StoreDetail }
  }
</script>
<style>
</style>
