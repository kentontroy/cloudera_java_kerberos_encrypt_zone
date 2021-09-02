# Test integration of Java and Kerberos, cloudera_solix_kerberos_int

```
Instructions to install Java (file-based) Keystore, not recommended for production, doesn't maintain redundant key copies

https://docs.cloudera.com/cdp-private-cloud-base/7.1.7/security-encrypting-data-at-rest/topics/cm-security-enable-hdfs-encryption-using-java-keystore.html

$ sudo useradd -g root key_admin
$ sudo passwd key_admin

$ sudo kadmin.local -q "addprinc -randkey key_admin/$(hostname -f)@CLOUDERA.COM"
$ sudo kadmin.local -q "xst -k key_admin.keytab key_admin/$(hostname -f)@CLOUDERA.COM"
$ sudo mv key_admin.keytab /etc/security/keytabs

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
$ sudo hadoop fs -mkdir /tmp/zone2
$ sudo hdfs crypto -createZone -keyName mykey1 -path /tmp/zone1
Added encryption zone /tmp/zone1
$ sudo hdfs crypto -createZone -keyName mykey2 -path /tmp/zone2
Added encryption zone /tmp/zone2



```

