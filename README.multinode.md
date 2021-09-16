## Test on multi-node cluster created by Cloudera Support team
```
# Accidentally, executed the below on CM node
# It lacks a process directory so have to ssh elsewhere later
# ssh -u root 172.31.113.31

# ktutil
ktutil: addent -password -p trusted_app_1@ADV.SEC.CLOUDERA.COM -k 1 -e RC4-HMAC
Password for trusted_app_1@ADV.SEC.CLOUDERA.COM: 
ktutil: wkt /tmp/trusted_app_1.keytab
ktutil: q

# ktutil
ktutil: addent -password -p trusted_app_2@ADV.SEC.CLOUDERA.COM -k 1 -e RC4-HMAC
Password for trusted_app_2@ADV.SEC.CLOUDERA.COM: 
ktutil: wkt /tmp/trusted_app_2.keytab
ktutil: q

# mv /tmp/*keytab /etc/security/keytabs

# kinit key_admin@ADV.SEC.CLOUDERA.COM
# klist
Ticket cache: FILE:/tmp/krb5cc_0
Default principal: key_admin@ADV.SEC.CLOUDERA.COM

Valid starting       Expires              Service principal
09/15/2021 12:16:28  09/15/2021 22:16:28  krbtgt/ADV.SEC.CLOUDERA.COM@ADV.SEC.CLOUDERA.COM
	renew until 09/22/2021 12:16:25

# hadoop key create mykey1
mykey1 has been successfully created with options Options{cipher='AES/CTR/NoPadding', bitLength=128, description='null', attributes=null}.
org.apache.hadoop.crypto.key.kms.LoadBalancingKMSClientProvider@76a4ebf2 has been updated.

# hadoop key create mykey2
mykey2 has been successfully created with options Options{cipher='AES/CTR/NoPadding', bitLength=128, description='null', attributes=null}.
org.apache.hadoop.crypto.key.kms.LoadBalancingKMSClientProvider@76a4ebf2 has been updated.

# Switch to a Data Node
ssh -u root 172.31.113.18  

# kinit -kt /var/run/cloudera-scm-agent/process/84-hdfs-DATANODE/hdfs.keytab hdfs/dturnau-802475-2.gce.cloudera.com@ADV.SEC.CLOUDERA.COM
# hadoop fs -mkdir -p /kms_test_zone_1
# hadoop fs -chown trusted_app_1:trusted_app_1 /kms_test_zone_1
# hadoop fs -chmod 777 /kms_test_zone_1
# hdfs crypto -createZone -keyName mykey1 -path /kms_test_zone_1
# Added encryption zone /kms_test_zone_1

# hadoop fs -mkdir -p /kms_test_zone_2
# hadoop fs -chown trusted_app_2:trusted_app_2 /kms_test_zone_2
# hadoop fs -chmod 777 /kms_test_zone_2
# hdfs crypto -createZone -keyName mykey2 -path /kms_test_zone_2
Added encryption zone /kms_test_zone_2
```
```
# yum install java-1.8.0-openjdk-devel

# pwd
/root/troubleshooting

# yum install git
# git clone https://github.com/kentontroy/cloudera_java_kerberos_encrypt_zone

# cd cloudera_java_kerberos_encrypt_zone/scripts
# . env-cdh-6.2.sh
# ./compile.sh
# ./run.sh

If compile fails, use script to search Cloudera jar directory
# ./search.sh -q "com/google/common/base/Preconditions"
Searching for com/google/common/base/Preconditions

Copy keytabs created on the CM node
# mkdir /etc/security/keytabs; scp root@dturnau-802475-1:/etc/security/keytabs/* /etc/security/keytabs

```


