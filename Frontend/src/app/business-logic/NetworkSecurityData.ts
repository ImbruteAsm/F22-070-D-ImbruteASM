
/*let data = {
"networkSecurityFactors" : {
    "networkSecurityFactorsId" : 3,
    "serviceFactors" : {
      "mongoDiscovered" : false,
      "cassandraDiscovered" : false,
      "msSQLDiscovered" : false,
      "mySQLDiscovered" : false,
      "redisDiscovered" : false,
      "vncDiscovered" : false,
      "rdpDiscovered" : true,
      "rsyncDiscovered" : false,
      "imapDiscovered" : false,
      "ftpDiscovered" : false,
      "smbDiscovered" : false,
      "telnetDiscovered" : false,
      "pop3Discovered" : false,
      "serviceDict" : {
        "mongo" : false,
        "imap" : false,
        "ftp" : false,
        "smb" : false,
        "vnc" : false,
        "redis" : false,
        "rsync" : false,
        "cassandra" : false,
        "telnet" : false,
        "pop3" : false,
        "mySQL" : false,
        "microsoftSQL" : false,
        "rdp" : true
      },
      "ports" : [ 
        {
        "portId" : 1,
        "name" : "http",
        "port" : "80",
        "state" : "open"
      }, {
        "portId" : 2,
        "name" : "ident",
        "port" : "113",
        "state" : "closed"
      }, {
        "portId" : 3,
        "name" : "https",
        "port" : "443",
        "state" : "open"
      }, {
        "portId" : 4,
        "name" : "ms-wbt-server",
        "port" : "3389",
        "state" : "open"
      } ],
      "serviceFactorsId" : 3
    },
    "sslFactors" : {
      "grade" : "B",
      "isSelfSigned" : false,
      "isExpired" : false,
      "isWeakCipher" : false,
      "vulnerabilities" : [ 
      {
        "vulnerabilityId" : 1,
        "name" : "poodle",
        "isDiscovered" : false
      }, {
        "vulnerabilityId" : 2,
        "name" : "heartbeat",
        "isDiscovered" : false
      }, {
        "vulnerabilityId" : 3,
        "name" : "logjam",
        "isDiscovered" : false
      }, {
        "vulnerabilityId" : 4,
        "name" : "freak",
        "isDiscovered" : false
      }, {
        "vulnerabilityId" : 5,
        "name" : "heartbleed",
        "isDiscovered" : false
      }, {
        "vulnerabilityId" : 6,
        "name" : "drownVulnerable",
        "isDiscovered" : false
      } ],
      "issuedTo" : "",
      "issuedBy" : "DigiCert, Inc.",
      "cipher" : "sha256WithRSAEncryption",
      "issuedCountry" : "US",
      "validFrom" : "2022-11-16",
      "validTill" : "2023-12-17",
      "sslfactorsId" : 1
    },
    "sshFactors" : null,
    "subdomains" : [ 
    {
      "subdomainId" : 1,
      "url" : "admin.nu.edu.pk",
      "ip" : "203.124.44.78"
    }, {
      "subdomainId" : 2,
      "url" : "ftp.nu.edu.pk",
      "ip" : "65.163.26.246"
    }, {
      "subdomainId" : 3,
      "url" : "sip.nu.edu.pk",
      "ip" : "52.113.64.147"
    }, {
      "subdomainId" : 4,
      "url" : "sip.nu.edu.pk",
      "ip" : "2603:1047::10:0:0:0:a"
    }, {
      "subdomainId" : 5,
      "url" : "sip.nu.edu.pk",
      "ip" : "2603:1047::1:0:0:0:b"
    }, {
      "subdomainId" : 6,
      "url" : "sip.nu.edu.pk",
      "ip" : "2603:1047::2:0:0:0:b"
    }, {
      "subdomainId" : 7,
      "url" : "sip.nu.edu.pk",
      "ip" : "2603:1047::6:0:0:0:b"
    }, {
      "subdomainId" : 8,
      "url" : "sip.nu.edu.pk",
      "ip" : "2603:1047::8:0:0:0:f"
    }, {
      "subdomainId" : 9,
      "url" : "sip.nu.edu.pk",
      "ip" : "2603:1047::9:0:0:0:f"
    }, {
      "subdomainId" : 10,
      "url" : "mail.nu.edu.pk",
      "ip" : "172.217.22.19"
    }, {
      "subdomainId" : 11,
      "url" : "mail.nu.edu.pk",
      "ip" : "2a00:1450:4028:809::2013"
    }, {
      "subdomainId" : 12,
      "url" : "backup.nu.edu.pk",
      "ip" : "202.165.229.3"
    }, {
      "subdomainId" : 13,
      "url" : "mssql.nu.edu.pk",
      "ip" : "203.124.44.78"
    }, {
      "subdomainId" : 14,
      "url" : "www.nu.edu.pk",
      "ip" : "203.124.44.78"
    } ],
    "riskFactor" : 0.36
  }}*/
export class NetworkSecurityData{
    public serviceFactors:any = new Array<any>();
    public serviceDict:any = new Array<any>();
    public ports:any = new Array();
    public sslFactors:any=null;
    public vulnerabilities:any = new Array();
    public sshFactors:any=null;
    public subdomains:any= new Array();
    public ciphers:any = new Array();
    public macs:any = new Array();
    public parseJSON(json:any):any{
        this.serviceFactors=new Array();
        this.serviceDict=new Array();
        this.ports=new Array();
        this.sslFactors=null;
        this.vulnerabilities=new Array();
        this.sshFactors=null;
        this.subdomains=new Array();
        this.ciphers=new Array();
        this.macs=new Array();
        if (json==null)
        {
            return null;
        }
        if(json['serviceFactors']!=null){
            let serveFact=json['serviceFactors'];
            if(serveFact['mongoDiscovered']){this.serviceFactors.push('Mongo')}
            if(serveFact['cassandraDiscovered']){this.serviceFactors.push('Cassandra')}
            if(serveFact['msSQLDiscovered']){this.serviceFactors.push('MSSQL')}
            if(serveFact['mySQLDiscovered']){this.serviceFactors.push('MySQL')}
            if(serveFact['redisDiscovered']){this.serviceFactors.push('Redis')}
            if(serveFact['vncDiscovered']){this.serviceFactors.push('VNC')}
            if(serveFact['rdpDiscovered']){this.serviceFactors.push('RDP')}
            if(serveFact['rsyncDiscovered']){this.serviceFactors.push('Rsync')}
            if(serveFact['imapDiscovered']){this.serviceFactors.push('IMAP')}
            if(serveFact['ftpDiscovered']){this.serviceFactors.push('FTP')}
            if(serveFact['smbDiscovered']){this.serviceFactors.push('SMB')}
            if(serveFact['telnetDiscovered']){this.serviceFactors.push('Telnet')}
            if(serveFact['pop3Discovered']){this.serviceFactors.push('Pop3')}
            if(serveFact['serviceDict']!=null){
                let servDict=serveFact['serviceDict'];
                if(servDict['mongo']){this.serviceDict.push('Mongo')}
                if(servDict['imap']){this.serviceDict.push('IMAP')}
                if(servDict['ftp']){this.serviceDict.push('FTP')}
                if(servDict['smb']){this.serviceDict.push('SMB')}
                if(servDict['vnc']){this.serviceDict.push('VNC')}
                if(servDict['redis']){this.serviceDict.push('Redis')}
                if(servDict['rsync']){this.serviceDict.push('Rsync')}
                if(servDict['cassandra']){this.serviceDict.push('Cassandra')}
                if(servDict['telnet']){this.serviceDict.push('Telnet')}
                if(servDict['pop3']){this.serviceDict.push('Pop3')}
                if(servDict['mySQL']){this.serviceDict.push('MySQL')}
                if(servDict['microsoftSQL']){this.serviceDict.push('MSSQL')}
                if(servDict['rdp']){this.serviceDict.push('RDP')}
            }
            if(serveFact['ports']!=null){
                this.ports=serveFact['ports'];
            }
        }
        if(json['sslFactors']!=null){
            let sslFact=json['sslFactors'];
            //debugger
            this.sslFactors={
                "grade" : sslFact['grade'],
                "isSelfSigned" : sslFact['isSelfSigned'],
                "isExpired" : sslFact['isExpired'],
                "isWeakCipher" : sslFact['isWeakCipher'],
                "issuedTo" : sslFact['issuedTo'],
                "issuedBy" : sslFact['issuedBy'],
                "cipher" : sslFact['cipher'],
                "issuedCountry" : sslFact['issuedCountry'],
                "validFrom" : sslFact['validFrom'],
                "validTill" : sslFact['validTill'],
                "sslfactorsId" : sslFact['sslfactorsId']
            };
            this.vulnerabilities=sslFact['vulnerabilities'];
        }
        if(json['sshFactors']!=null){
            let sshFact=json['sshFactors'];
            this.sshFactors={"sshVersion":sshFact['sshVersion']}
            if(sshFact['ciphers']!=null){
                this.ciphers=sshFact['ciphers']
            }
            if(sshFact['macs']!=null){
                this.macs=sshFact['macs']
            }
        }
        if(json['subdomains']!=null){
            this.subdomains=json['subdomains'];
        }
    }

}