#!/bin/bash

kafkacat -b localhost:9092 -t employee -T -P -K: -l ./employee.json
