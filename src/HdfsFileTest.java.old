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
    conf.set("fs.defaultFS", "hdfs://dturnau-802475-2.gce.cloudera.com:8022");
    conf.set("hadoop.security.authentication", "kerberos");
    conf.set("hadoop.security.authorization", "true");

    UserGroupInformation.setConfiguration(conf);

    String keytab1 = "/etc/security/keytabs/trusted_app_1.keytab";

    UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI("trusted_app_1@ADV.SEC.CLOUDERA.COM", keytab1);
    ugi.doAs(new PrivilegedExceptionAction() {
      public Void run() throws Exception {
        FileSystem fs = FileSystem.get(conf);
        fs.create(new Path("/kms_test_zone_1/testFile"));
        return null;
      }
    });

    String keytab2 = "/etc/security/keytabs/trusted_app_2.keytab";

    UserGroupInformation ugi2 = UserGroupInformation.loginUserFromKeytabAndReturnUGI("trusted_app_2@ADV.SEC.CLOUDERA.COM", keytab2);
    ugi2.doAs(new PrivilegedExceptionAction() {
      public Void run() throws Exception {
        FileSystem fs = FileSystem.get(conf);
        fs.create(new Path("/kms_test_zone_2/testFile"));
        return null;
      }
    });
  }
}
