<template>
  <div class="hello">
    <h1>Load test concurrency</h1>
    <vue-slider ref="slider" v-model="value" :min="1" :max="1000" @callback="reconfigureLoadTester"/>
  </div>
</template>

<script>
  import vueSlider from 'vue-slider-component'

  const debounce = function debounce (func, wait, immediate) {
    var timeout, args, context, timestamp, result
    if (wait == null) wait = 100

    function later () {
      var last = Date.now() - timestamp

      if (last < wait && last >= 0) {
        timeout = setTimeout(later, wait - last)
      } else {
        timeout = null
        if (!immediate) {
          result = func.apply(context, args)
          context = args = null
        }
      }
    }

    var debounced = function () {
      context = this
      args = arguments
      timestamp = Date.now()
      var callNow = immediate && !timeout
      if (!timeout) timeout = setTimeout(later, wait)
      if (callNow) {
        result = func.apply(context, args)
        context = args = null
      }

      return result
    }

    debounced.clear = function () {
      if (timeout) {
        clearTimeout(timeout)
        timeout = null
      }
    }

    return debounced
  }

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
      reconfigureLoadTester: function (value) {
        console.log('am I sane?')
        /* debounce(function () {
          console.log('yes!')
          // window.fetch('http://localhost:3000/loadtest?concurrency=' + value).resolve()
        }, 100) */
        debounce(console.log, 500)
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
