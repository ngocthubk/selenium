#!/bin/bash
nohup entry.sh &
java -DcfgFile=./config/config.properties -Dprofile=$CD_TEST_SCOPE -Dae-host=$CD_TEST_HOSTNAME -Dae-port=$CD_TEST_CP_PORT -Dae-system=$CD_TEST_SYSTEM -Dae-client=$CD_TEST_CLIENT -Dae-user=$CD_TEST_USERNAME -Dae-department=$CD_TEST_DEPARTMENT -Dae-password=$CD_TEST_PASSWORD -Dae-user-zero=$CD_TEST_C0_USERNAME -Dae-department-zero=$CD_TEST_C0_DEPARTMENT -Dae-password-zero=$CD_TEST_C0_PASSWORD -Dawi-conn=$CD_TEST_CONN -Dawi-url=$CD_TEST_AWI_URL -Dapm-home=/tmp/apm/bin -Dselenium-hub=http://localhost:24444/wd/hub -jar /tmp/webui-plugin-actionbuilder-test.jar
echo "Finished Test!"
sudo mkdir -p $CD_TEST_OUTPUT_DIR
sudo cp /tmp/test-output/Report.xml $CD_TEST_OUTPUT_DIR
echo "Copy report.xml to output folder!"
