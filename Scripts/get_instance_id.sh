#!/usr/bin/env bash

# load up variables
source ./lab_config.sh

echo "Fetch instances IDs" | tee -a ${LOGFILE}
INSTANCES_IDS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query "Reservations[*].Instances[*].InstanceId" --output text | tr '\n' ' ')
echo "Instances IDs: $INSTANCES_IDS" | tee ${LOGFILE}

# get public IP addresses of the instances (in the public subnet)
echo "Fetch public IP addresses of the instances" | tee -a ${LOGFILE}
INSTANCES_IPS=$(aws ec2 describe-instances ${PREAMBLE} --filters Name=instance-state-name,Values=running Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} --query 'Reservations[*].Instances[*].[PublicIpAddress]' --output text | tr '\n' ' ')

echo "Public IP addresses: ${INSTANCES_IPS}" | tee ${LOGFILE}