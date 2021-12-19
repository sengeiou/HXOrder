<template> 
  <dish-detail :is-edit='true'></dish-detail>
</template>
<script>
  import DishDetail from './components/DishDetail'
  export default {
    name: 'updateDish',
    components: { DishDetail }
  }
</script>
<style>
</style>
