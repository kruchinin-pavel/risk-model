#!/bin/sh
export JAVA="/opt/jdk/bin/java"
mvn clean package
${JAVA} -jar target/portfolio-risk-manager-jar-with-dependencies.jar 0.15 0.075 250 10 500

