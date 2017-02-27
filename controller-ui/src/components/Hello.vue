<template>
  <div class="hello">
    <h1>Load test concurrency</h1>
    <vue-slider ref="slider" v-model="value" :min="1" :max="1000" @callback="reconfigureLoadTester"/>
  </div>
</template>

<script>
  import vueSlider from 'vue-slider-component'
  import * as _ from 'lodash'

  export default {
    name: 'hello',
    components: {
      vueSlider
    },
    data () {
      return {
        value: 1
      }
    },
    methods: {
      reconfigureLoadTester: function (sliderEvent) {
        _.debounce(function () {
          window.fetch('http://localhost:8080/loadtest?concurrency=' + sliderEvent).then()
        }, 100)()
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h1, h2 {
    font-weight: normal;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }
</style>
