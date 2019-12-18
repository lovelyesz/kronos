#!/bin/bash

ITEM=$1
SHARE_TOTAL=$2
CLAZZ=$3
EXEC_ID=$4

#某些情况下需要刷环境变量
source /etc/profile

DEPLOY_DIR=`pwd`
CONF_DIR=${DEPLOY_DIR}/conf


CONTAINER_MAIN_JAR=kronos-executor-demo-1.0.0.jar
echo $DEPLOY_DIR

PARAM=" -Xmx1g -Xms1g -XX:NewSize=512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xss1024k -Xloggc:"
PAPAM_1=${DEPLOY_DIR}/gc.log
PAPAM_2=" -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintCommandLineFlags"
PAPAM_3=" -XX:HeapDumpPath="
PAPAM_4=${DEPLOY_DIR}/dump.hprof
PAPAM_5=" -XX:+HeapDumpOnOutOfMemoryError "
JVM_PARAM=$PARAM$PAPAM_1$PAPAM_2$PAPAM_3$PAPAM_4$PAPAM_5


echo $JAVA_HOME/bin/java ${JVM_PARAM} -jar ${DEPLOY_DIR}/demo-kronos/lib/${CONTAINER_MAIN_JAR}

$JAVA_HOME/bin/java ${JVM_PARAM} -jar ${DEPLOY_DIR}/demo-kronos/lib/${CONTAINER_MAIN_JAR} --spring.profiles.active=prod

echo "executor start successfully"

