#!/bin/bash
curl -X POST http://localhost:8080/postBlockData \
    -H "Content-Type: application/json" \
    -d "{
          \"itemName\": \"$1\",
          \"itemAmount\": 1,
          \"itemDamage\": 2
        }"
