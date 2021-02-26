#!/usr/bin/env bash

# load up variables
source ./lab_config.sh
SN_TAG_NAME=Subnet-Name
SN_TAG_VALUE=Public-Subnet

VPC_ID=$(aws ec2 describe-vpcs --filters Name=cidr,Values=${VPC_CDR} --query "Vpcs[0].VpcId" --output text)
echo ${VPC_ID}
SN_ID_PUBLIC=$(aws ec2 describe-subnets ${PREAMBLE} --filter Name=vpc-id,Values=${VPC_ID} Name=tag:${APP_TAG_NAME},Values=${APP_TAG_VALUE} Name=tag:${SN_TAG_NAME},Values=${SN_TAG_VALUE} --query "Subnets[0].SubnetId" --output text)
echo ${SN_ID_PUBLIC}

NOW=$(date '+%Y%m%d%H%M%S')
LOGFILE="./create_ec2_in_vpc-${NOW}.log"
echo "Creating Full AWS infrastructure for ${APP_TAG_NAME}:${APP_TAG_VALUE}" | tee ${LOGFILE}

echo "Running create_ec2_in_vpc.sh at ${NOW}" | tee -a ${LOGFILE}

# create the security group for SSH on port 22
echo "Create the security group for SSH on port 22" | tee -a ${LOGFILE}
SEC_GROUP_ID=$(aws ec2 create-security-group ${PREAMBLE} --group-name SSHAccess --description "Security group for SSH access" --vpc-id ${VPC_ID} --tag-specifications "ResourceType=security-group,Tags=[{Key=${APP_TYPE},Value=${APP_TYPE_NAME}},{Key=${APP_TAG_NAME},Value=${APP_TAG_VALUE}}]" | jq '.GroupId' | tr -d '"')

# set the security group for ingress from the Internet
echo "Set the security group for ingress from the Internet" | tee -a ${LOGFILE}
aws ec2 authorize-security-group-ingress ${PREAMBLE} --group-id ${SEC_GROUP_ID} --protocol tcp --port 22 --cidr 0.0.0.0/0 | tee ${LOGFILE}

# create the instance(s) with tags
#INSTANCE_ID=$(aws ec2 run-instances ${PREAMBLE} --image-id ${AMI_ID} --count ${INSTANCES} --instance-type t2.micro --key-name ${KEY_NAME} --security-group-ids ${SEC_GROUP_ID} --subnet-id ${SN_ID_PUBLIC} | jq '.Instances[0].InstanceId' | tr -d '"')
echo "Create ${INSTANCES_COUNT} instances" | tee -a ${LOGFILE}
aws ec2 run-instances ${PREAMBLE} --image-id ${AMI_ID} --count ${INSTANCES_COUNT} --instance-type ${INSTANCE_TYPE} --key-name ${KEY_NAME} --security-group-ids ${SEC_GROUP_ID} --subnet-id ${SN_ID_PUBLIC} \
   --tag-specifications "ResourceType=instance,Tags=[{Key=${APP_TYPE},Value=${APP_TYPE_NAME}},{Key=${APP_TAG_NAME},Value=${APP_TAG_VALUE}}]" \
   "ResourceType=volume,Tags=[{Key=${APP_TYPE},Value=${APP_TYPE_NAME}},{Key=${APP_TAG_NAME},Value=${APP_TAG_VALUE}}]" | tee ${LOGFILE}

exit 0