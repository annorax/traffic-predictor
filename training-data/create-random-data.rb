#!/usr/bin/env ruby

require 'rubystats'

REQUESTS_PER_10 = {'mean' => 100, 'std' => 5}
AVG_RESPONSE_PER_10 = { 'mean' => 10, 'std' => 2}

LOAD_REQUESTS_PER_10 = {'mean' => 1000, 'std' => 200}
LOAD_AVG_RESPONSE_PER_10 = { 'mean' => 40, 'std' => 5}

DATA_SIZE=200

reqs = Rubystats::NormalDistribution.new(REQUESTS_PER_10['mean'], REQUESTS_PER_10['std'])
resps = Rubystats::NormalDistribution.new(AVG_RESPONSE_PER_10['mean'], AVG_RESPONSE_PER_10['std'])

DATA_SIZE.times { puts "#{reqs.rng} #{resps.rng} 0" }

reqs = Rubystats::NormalDistribution.new(LOAD_REQUESTS_PER_10['mean'], LOAD_REQUESTS_PER_10['std'])
resps = Rubystats::NormalDistribution.new(LOAD_AVG_RESPONSE_PER_10['mean'], LOAD_AVG_RESPONSE_PER_10['std'])

DATA_SIZE.times { puts "#{reqs.rng} #{resps.rng} 1" }

