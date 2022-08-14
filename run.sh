#!/bin/sh
# Compile and run a demo project

set -e
cd "$(dirname $0)"

sh ./mvnw spring-boot:run

# EOF
