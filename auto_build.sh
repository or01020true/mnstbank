#!/bin/bash
webwas_container_name=$1

if [ -z "$webwas_container_name" ]; then
    echo "오류: 도커 컨테이너 이름을 지정해주세요."
    echo "사용법: $0 <도커컨테이너이름>"
    exit 1
fi

gradle build
sesac_tomcat_path=/usr/local/tomcat
docker cp ./build/libs/hacking02_sk-0.0.1-SNAPSHOT-plain.war ${webwas_container_name}:${sesac_tomcat_path}/webapps/ROOT.war