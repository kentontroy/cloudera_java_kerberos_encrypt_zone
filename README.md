# Test integration of Java and Kerberos, cloudera_solix_kerberos_int

```
Instructions to install Java (file-based) Keystore, not recommended for production, doesn't maintain redundant key copies

https://docs.cloudera.com/cdp-private-cloud-base/7.1.7/security-encrypting-data-at-rest/topics/cm-security-enable-hdfs-encryption-using-java-keystore.html

useradd -g etl key_admin
sudo passwd key_admin

sudo kadmin.local -q "addprinc -randkey key_admin/$(hostname -f)@CLOUDERA.COM"
sudo kadmin.local -q "xst -k key_admin.keytab key_admin/$(hostname -f)@CLOUDERA.COM"
sudo mv key_admin.keytab /etc/security/keytabs

For testing, configure the centos user as the key_admin user.
After redeploying the client configuration and restarting stale services, choose Setup HDFS Encryption in the main Actions window and Validate as the last step.



```

