export CLOUDERA_JAR_PATH=/opt/cloudera/parcels/CDH/jars
export CLASSPATH=`(printf %s: ${CLOUDERA_JAR_PATH}/hadoop*.jar)`:${CLOUDERA_JAR_PATH}/commons-configuration2-2.1.1.jar
export CLASSPATH=${CLASSPATH}:${CLOUDERA_JAR_PATH}/slf4j-api-1.7.30.jar:${CLOUDERA_JAR_PATH}/slf4j-simple-1.7.30.jar
export CLASSPATH=${CLASSPATH}:${CLOUDERA_JAR_PATH}/log4j-1.2-api-2.13.3.jar:${CLOUDERA_JAR_PATH}/guava-30.1.1-jre.jar
export CLASSPATH=${CLASSPATH}:${CLOUDERA_JAR_PATH}/commons-lang-2.6.jar:${CLOUDERA_JAR_PATH}/woodstox-core-5.0.3.jar
export CLASSPATH=${CLASSPATH}:${CLOUDERA_JAR_PATH}/stax2-api-3.1.4.jar:${CLOUDERA_JAR_PATH}/commons-collections-3.2.2.jar
export CLASSPATH=${CLASSPATH}:${CLOUDERA_JAR_PATH}/commons-logging-1.2.jar:${CLOUDERA_JAR_PATH}/protobuf-java-3.12.0.jar
export CLASSPATH=${CLASSPATH}:${CLOUDERA_JAR_PATH}/re2j-1.2.jar:${CLOUDERA_JAR_PATH}/failureaccess-1.0.1.jar:../classes/:.
