#!/usr/bin/env bash

# fetch config from AWS for currently running infrastructure
source ./load_lab_config.sh

NOW=$(date '+%Y%m%d%H%M%S')
LOGFILE="./run-${NOW}.log"

echo "Running Full AWS infrastructure for ${APP_TAG_NAME}:${APP_TAG_VALUE}" | tee ${LOGFILE}
echo "Running run.sh at ${NOW}" | tee -a ${LOGFILE}

PROG="./hello.sh"

# get public IP addresses of the instances (in the public subnet)
INSTANCES_IPS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query 'Reservations[*].Instances[*].[PublicIpAddress]' --output text | tr '\n' ' ')
echo "Public IP addresses: ${INSTANCES_IPS}" | tee -a ${LOGFILE}

for host in ${INSTANCES_IPS}
do
	echo "Running ${PROG} at ${USER}@${host}:~/ ..." | tee -a ${LOGFILE}
	ssh -i ${KEY_FILE} ${USER}@${host} "${PROG}" | tee -a ${LOGFILE}
done

echo "Done." | tee -a ${LOGFILE}

exit 0


