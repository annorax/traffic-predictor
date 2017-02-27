#!/usr/bin/env ruby

require 'rubystats'

REQUESTS_PER_SECONDS=10
RESPONSE_TIME = {'mean' => 10, 'std' => 2}

gen = Rubystats::NormalDistribution.new(RESPONSE_TIME['mean'], RESPONSE_TIME['std'])

while (true) do
  puts gen.rng
  sleep (1.0/REQUESTS_PER_SECONDS)
end

