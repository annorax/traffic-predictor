#!/usr/bin/env ruby

require 'rubystats'

REQUESTS_PER_SECOND=100
RESPONSE_TIME = {'mean' => 40, 'std' => 5}

gen = Rubystats::NormalDistribution.new(RESPONSE_TIME['mean'], RESPONSE_TIME['std'])

r = Random.new
while (true) do
  File.open("/tmp/log/#{r.rand}", 'w') {|f| f.puts gen.rng}
  sleep (1.0/REQUESTS_PER_SECOND)
end

