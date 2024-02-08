#!/bin/bash
sudo echo "자동 배포 진행"
local_tomcat_path=/usr/local/tomcat
sudo -E ${local_tomcat_path}/bin/shutdown.sh
git remote update
git pull origin main
sudo rm -rf ${local_tomcat_path}/webapps/ROOT*
sudo cp -f ./ROOT.war ${local_tomcat_path}/webapps/ROOT.war
sudo -E ${local_tomcat_path}/bin/startup.sh

