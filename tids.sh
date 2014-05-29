#!/bin/bash
for line in `cat $1`;do
  echo $1 | cut -d '=' -f2 | xargs printf '%d\n' >> decids
done
