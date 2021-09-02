import java.io.*;
import java.security.PrivilegedExceptionAction;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.security.*;

class HdfsFileTest {
  public static void main(final String[] args) throws IOException, FileNotFoundException, InterruptedException {
    String principal = "joe_analyst/cdp.cloudera.com@CLOUDERA.COM";
    String keytab = "/etc/security/keytabs/joe_analyst.keytab";

    Configuration conf = new Configuration();
    conf.set("fs.defaultFS", "hdfs://cdp.cloudera.com:8020");
    conf.set("hadoop.security.authentication", "kerberos");
    conf.set("hadoop.security.authorization", "true");

    UserGroupInformation.setConfiguration(conf);
    UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal, keytab);

    ugi.doAs(new PrivilegedExceptionAction() {
      public Void run() throws Exception {
        FileSystem fs = FileSystem.get(conf);
        String dirName = "/";
        FileStatus[] status = fs.listStatus(new Path(dirName));
        System.out.println("File Count: " + status.length);

        return null;
      }
   });
  }
}
