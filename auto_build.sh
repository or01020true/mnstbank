#!/bin/bash
current_path=$(pwd)
webwas_container_name=$1
if [ -z "$webwas_container_name" ]; then # not use docker
    sudo echo "$0 로컬 tomcat 이용"
    local_tomcat_path=/usr/local/tomcat/apache-tomcat-8.0.53
    sudo ${local_tomcat_path}/bin/shutdown.sh
    gradle build
    sudo cp -f ${current_path}/build/libs/hacking02_sk-0.0.1-SNAPSHOT-plain.war ${local_tomcat_path}/webapps/ROOT.war
    sudo rm -rf ${local_tomcat_path}/webapps/ROOT
    sudo ${local_tomcat_path}/bin/startup.sh
else # use docker
    echo "$0 Docker tomcat 이용"
    current_path=$(pwd)
    docker_tomcat_path=/usr/local/tomcat
    gradle build
    docker cp ${current_path}/build/libs/hacking02_sk-0.0.1-SNAPSHOT-plain.war ${webwas_container_name}:${docker_tomcat_path}/webapps/ROOT.war
fi