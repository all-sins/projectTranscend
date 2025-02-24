#!/bin/bash
for i in $(seq -w 0 $1); do
    curl -X POST http://localhost:8080/postBlockData \
        -H "Content-Type: application/json" \
        -d "{
          \"itemName\": \"minecraft:$i\",
          \"itemAmount\": 1,
          \"itemDamage\": 2
        }"
done
