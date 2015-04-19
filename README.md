# Introduction #
Below is simple instruction how to setup DB2 HADR on one machine using two DB2 instances

# Prerequisites #
HADR option is not available in free DB2 Express Edition. But can be setup with trial (90 days) DB2 version.

http://www-01.ibm.com/software/data/db2/linux-unix-windows/download.html

# Step 1 Install DB2 and two instance #
Install DB2 and create two instances db2had1 and db2had2. Assume that db2had1 will be the primary and db2had2 the secondary instance. After installation pay attention to /etc/services file. On my linux (ubuntu) box the last two lines are:
```
db2c_db2had1    50008/tcp
db2c_db2had2    50009/tcp
```
Port 50008 is a connection port for db2had1 and 50009 is a connection port for 50009. But both instances participating in HADR need additional port to communicate between them. So let's pick up first two free port starting from 60000 - on my machine they are 60004 for db2had1 and 60005 for db2had2. Although it is not necessary it is a good practise to add them also to /etc/services file - just preventing for reusing them by another software.
```
DB2_db2had1     60004/tcp
DB2_db2had2     60005/tcp
```
# Step2 Create SAMPLE database on db2had1 instance #
Log in to db2had1 instance and create SAMPLE database (assume the host name for the DB2 box is 'think')
```
ssh -X db2had2@think
db2start
db2sampl
```
# Step 3 Change logging method on instance db2had1 to archival #
Now we want to replicate SAMPLE database to instance db2had2 and set up HADR configuration having SAMLE database on db2had1 instance acting as primary and SAMPLE on db2had2 instance as secondary database

But before doing anything we have to change logging (transactional) method from circular which is default to archival ([more about different logging method in DB2](http://publib.boulder.ibm.com/infocenter/db2luw/v9r7/index.jsp?topic=%2Fcom.ibm.db2.luw.admin.ha.doc%2Fdoc%2Fc0051343.html))
```
mkdir tranlog
db2 "UPDATE DATABASE CONFIGURATION for sample USING logarchmeth1 disk:$PWD/tranlog"
```
Restart instance just in case
```
db2 terminate
db2stop
db2start
```
# Step 4 Make a backup copy of SAMPLE database #
```
mkdir /tmp/backup
db2 backup database sample to /tmp/backup
```
Give access to backup for instance db2had2
```
chmod 755 /tmp/backup
chmod 644 /tmp/backup/*
```
# Step 5 Restore backup on instance db2had2 #
Login to instance db2had2 and create folder for database.
```
ssh -X db2had2@think
mkdir db2had2
```
Restore database from backup to folder just created.
```
db2 restore database sample from /tmp/backup on $PWD/db2had2
```
# Step 6 Change logging method on db2had2 instance #
mkdir tranlog
```
db2 "UPDATE DATABASE CONFIGURATION for sample USING logarchmeth1 disk:$PWD/tranlog"
```
Restart instance
```
db2 terminate
db2stop
db2start
```
# Step 7 Configure HADR on db2had1 instance #
Log in to db2had1
```
ssh -X db2had1@think
db2 update db cfg for SAMPLE using HADR_LOCAL_HOST localhost
db2 update db cfg for SAMPLE using HADR_REMOTE_HOST localhost
db2 update db cfg for SAMPLE using HADR_LOCAL_SVC 60004
db2 update db cfg for SAMPLE using HADR_REMOTE_SVC 60005
db2 update db cfg for SAMPLE using HADR_REMOTE_INST db2had2
```
# Step 8 Set up ACR on db2had1 instance #
ACR stands for Automatic Client Reroute. It allows to switch client automatically to secondary server after primary failure.
```
db2 UPDATE ALTERNATE SERVER FOR DATABASE SAMPLE USING HOSTNAME think PORT 50009
```
# Step 9 Configure HADR on db2had2 instance #
```
ssh -X db2had2@think
db2 update db cfg for SAMPLE using HADR_LOCAL_HOST localhost
db2 update db cfg for SAMPLE using HADR_REMOTE_HOST localhost
db2 update db cfg for SAMPLE using HADR_LOCAL_SVC 60005
db2 update db cfg for SAMPLE using HADR_REMOTE_SVC 60004
db2 update db cfg for SAMPLE using HADR_REMOTE_INST db2had1
```
# Step 10 Set up ACR in db2had2 instance #
```
db2 UPDATE ALTERNATE SERVER FOR DATABASE SAMPLE USING HOSTNAME think PORT 50008
```
# Step 11 Start db2had2 as secondary server #
It is recommended to start standby before primary.
```
db2 start HADR on database SAMPLE as STANDBY
```
# Step 12 Start db2had1 as primary server #
```
ssh -X db2had1@think
db2 start HADR on database SAMPLE as PRIMARY
```
# Step 13 Configure command line client #
Log in to the client machine and catalog db2had1 as remote node.
```
db2 catalog tcpip node DB2HAD1 remote think server 50008
```
Catalog database SAMPLE
```
db2 catalog database SAMPLE at node db2had1 
```
# Summary #
At this moment we have SAMPLE database running on db2had1 server running as a primary database and SAMPLE database (being an exact copy of SAMPLE on db2had1) running as a secondary. The client is ready for connecting to SAMPLE database on primary server. Pay attention that there is no need to catalog access to SAMPLE database on db2had2. All changes (DDL and DML) commited on primary server will be replicated to secondary. Any time we can switch the roles between primary and secondary.
# Important #
Switching role can be executed only from command line, on demand. Recognizing node failure and activate secondary server as primary is the role of cluster management system.
