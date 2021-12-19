<template> 
  <dish-detail :is-edit='false'></dish-detail>
</template>
<script>
  import DishDetail from './components/DishDetail'
  export default {
    name: 'addProduct',
    components: { DishDetail }
  }
</script>
<style>
</style>
