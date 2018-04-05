#!/usr/bin/env bash

mvn clean install
java -jar ./aspectj-service/target/aspectj-service-1.0-SNAPSHOT.jar
