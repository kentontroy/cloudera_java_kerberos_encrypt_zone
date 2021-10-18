# Error when writing to multiple encryption zones w/ different principals when using the same GSS Context
```
Instructions to install Java (file-based) Keystore, not recommended for production, doesn't maintain redundant key copies

https://docs.cloudera.com/cdp-private-cloud-base/7.1.7/security-encrypting-data-at-rest/topics/cm-security-enable-hdfs-encryption-using-java-keystore.html

$ sudo useradd -g root key_admin
$ sudo passwd key_admin

$ sudo groupadd trusted_app_1 
$ sudo useradd -g trusted_app_1 trusted_app_1
$ sudo passwd trusted_app_1

$ sudo groupadd trusted_app_2
$ sudo useradd -g trusted_app_2 trusted_app_2
$ sudo passwd trusted_app_2

$ sudo kadmin.local -q "addprinc -randkey key_admin/$(hostname -f)@CLOUDERA.COM"
$ sudo kadmin.local -q "xst -k key_admin.keytab key_admin/$(hostname -f)@CLOUDERA.COM"
$ sudo mv key_admin.keytab /etc/security/keytabs
$ sudo chmod 655 /etc/security/keytabs/key_admin.keytab

$ sudo kadmin.local -q "addprinc -randkey trusted_app_1/$(hostname -f)@CLOUDERA.COM"
$ sudo kadmin.local -q "xst -k trusted_app_1.keytab trusted_app_1/$(hostname -f)@CLOUDERA.COM"
$ sudo mv trusted_app_1.keytab /etc/security/keytabs
$ sudo chmod 655 /etc/security/keytabs/trusted_app_1.keytab

$ sudo kadmin.local -q "addprinc -randkey trusted_app_2/$(hostname -f)@CLOUDERA.COM"
$ sudo kadmin.local -q "xst -k trusted_app_2.keytab trusted_app_2/$(hostname -f)@CLOUDERA.COM"
$ sudo mv trusted_app_2.keytab /etc/security/keytabs
$ sudo chmod 655 /etc/security/keytabs/trusted_app_2.keytab

$ sudo kadmin.local -q list_principals
```
## Merge the keytabs
```
$ sudo ktutil
ktutil:  read_kt /etc/security/keytabs/trusted_app_1.keytab
ktutil:  read_kt /etc/security/keytabs/trusted_app_2.keytab
ktutil:  list
slot KVNO Principal
---- ---- ---------------------------------------------------------------------
   1    2 trusted_app_1/cdp.cloudera.com@CLOUDERA.COM
   2    2 trusted_app_1/cdp.cloudera.com@CLOUDERA.COM
   3    2 trusted_app_1/cdp.cloudera.com@CLOUDERA.COM
   4    2 trusted_app_2/cdp.cloudera.com@CLOUDERA.COM
   5    2 trusted_app_2/cdp.cloudera.com@CLOUDERA.COM
   6    2 trusted_app_2/cdp.cloudera.com@CLOUDERA.COM
ktutil:  write_kt merged_trusted_apps_1_2.keytab
ktutil:  quit

$ sudo chmod 655 /etc/security/keytabs/merged_trusted_apps_1_2.keytab

$ sudo kinit -kt /etc/security/keytabs/key_admin.keytab key_admin/$(hostname -f)@CLOUDERA.COM

For testing, configure the centos user as the key_admin user.
After redeploying the client configuration and restarting stale services, choose Setup HDFS Encryption in the main Actions window and Validate as the last step.
During testing, had to destroy the KKMS service from within CM. Manually removed the keystore file in /var/lib/kms.

$ sudo klist
Ticket cache: FILE:/tmp/krb5cc_0
Default principal: key_admin/cdp.cloudera.com@CLOUDERA.COM

Valid starting       Expires              Service principal
09/01/2021 22:26:09  09/02/2021 22:26:09  krbtgt/CLOUDERA.COM@CLOUDERA.COM
        renew until 09/08/2021 22:26:09

$ sudo hadoop key create mykey1
mykey1 has been successfully created with options Options{cipher='AES/CTR/NoPadding', bitLength=128, description='null', attributes=null}.
org.apache.hadoop.crypto.key.kms.LoadBalancingKMSClientProvider@a2431d0 has been updated.

$ sudo hadoop key create mykey2

$ sudo kinit -kt /var/run/cloudera-scm-agent/process/731-hdfs-NAMENODE/hdfs.keytab hdfs/$(hostname -f)@CLOUDERA.COM

$ sudo hadoop fs -mkdir /tmp/zone1
$ sudo hadoop fs -chown trusted_app_1:trusted_app_1 /tmp/zone1
$ sudo hadoop fs -chmod 777 /tmp/zone1

$ sudo hadoop fs -mkdir -p /kms_test_zone_1
$ sudo hadoop fs -chown trusted_app_1:trusted_app_1 /kms_test_zone_1
$ sudo hadoop fs -chmod 777 /kms_test_zone_1
$ sudo hdfs crypto -createZone -keyName mykey1 -path /kms_test_zone_1

$ sudo hadoop fs -mkdir -p /kms_test_zone_2
$ sudo hadoop fs -chown trusted_app_2:trusted_app_2 /kms_test_zone_2
$ sudo hadoop fs -chmod 777 /kms_test_zone_2
$ sudo hdfs crypto -createZone -keyName mykey2 -path /kms_test_zone_2

Assumes that a JDK is installed and running CDP 7.1.7 
See other notes for testing on CDH 6.2

# . env-cdp-7.1.7.sh.sh
# ./compile.sh
# ./run.sh

```
