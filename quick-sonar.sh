#!/bin/bash

echo ">>>>>>>>>>>>>>>代码静态检查,并生成测试报告"
mvn sonar:sonar -Dsonar.language=java -Dsonar.sourceEncoding=UTF-8 -Dsonar.host.url=http://sonar.weichedao.com/ 
