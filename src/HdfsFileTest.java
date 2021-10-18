import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.security.*;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.TokenIdentifier;

public class HdfsFileTest {
  public static void main(final String[] args) throws Exception {
    Configuration conf = new Configuration();
    conf.set("fs.defaultFS", "hdfs://cdp.cloudera.com:8020");
    conf.set("hadoop.security.authentication", "kerberos");
    conf.set("hadoop.security.authorization", "true");
    conf.set("fs.hdfs.impl.disable.cache", "true");

    UserGroupInformation.setConfiguration(conf);

    String keytab = "/etc/security/keytabs/merged_trusted_apps_1_2.keytab";

    UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI( "trusted_app_1/cdp.cloudera.com@CLOUDERA.COM", keytab);
    ugi.doAs(new PrivilegedExceptionAction() {
      public Void run() throws Exception {
        FileSystem fs = FileSystem.get(conf);
        fs.create(new Path("/kms_test_zone_1/testFile"));
        return null;
      }
    });

    UserGroupInformation ugi2 = UserGroupInformation.loginUserFromKeytabAndReturnUGI("trusted_app_2/cdp.cloudera.com@CLOUDERA.COM", keytab);
    ugi2.doAs(new PrivilegedExceptionAction() {
      public Void run() throws Exception {
        FileSystem fs = FileSystem.get(conf);
        fs.create(new Path("/kms_test_zone_2/testFile"));
        return null;
      }
    });
  }
}
