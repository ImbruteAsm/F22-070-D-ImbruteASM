import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RiskFactors } from '../business-logic/RiskFactors';
import { SideBarComponent } from '../side-bar/side-bar.component';
import { Summary } from '../business-logic/Summary';
import { Vulnerability } from '../business-logic/Vulnerability';
import { NetworkSecurityData } from '../business-logic/NetworkSecurityData';
import { DNSHealthData } from '../business-logic/DNSHealthData'
import { ApplicationSecurityData } from '../business-logic/ApplicationSecurityData';
import { EndPointSecurityData } from '../business-logic/EndPointSecurityData';
@Component({
  selector: 'app-scanned-output',
  templateUrl: './scanned-output.component.html',
  styleUrls:['./scanned-output.component.css']
})
export class ScannedOutputComponent {
  activeTab:number=1;
  vulnerabilities: any = ['poodle','logjam','Overload Port'];
  malware:any=[];
  highSeverityVulnerabilities: any=null;

  summary: Summary = new Summary;
  networkSecurityData: NetworkSecurityData=new NetworkSecurityData;
  DNSHealthData:DNSHealthData=new DNSHealthData;
  applicationSecurityData:ApplicationSecurityData=new ApplicationSecurityData;
  endPointSecurityData:EndPointSecurityData=new EndPointSecurityData;

  dataRecieved:any|undefined;
  constructor(){
    this.activeTab=1;
    console.log("constructor")
    SideBarComponent.activateComponent(1)
  }
  dataIsRecieved(){
    if (SideBarComponent.dataRecievedJson!=null){
      this.dataRecieved=SideBarComponent.dataRecievedJson;
      this.dataRecieved=this.targetData;
      console.log("scanned output component = ",this.dataRecieved)
      this.setSummaryData()
      this.setNetworkSecurityData()
      this.setDNSHealthData()
      this.setApplicationSecurityData()
      this.setEndPointSecurityData();
      SideBarComponent.activateComponent(1);
      return true; 
    }
    return false
  }
  activateTab(num:number){
    this.activeTab=num;
  }
  
  public backButtonClicked(){
    console.log("back clicked")
    SideBarComponent.activateComponent(0);
    SideBarComponent.dataRecievedJson=null;
  }





  setSummaryData(){
    this.summary.target=this.dataRecieved['target'];
      //Summary
      this.summary.riskFactor= Number(this.dataRecieved['riskNumber'].toFixed(2));
      this.summary.DNSHealthFactor=Number(this.dataRecieved['dnsHealth']['riskScore'].toFixed(2));
      this.summary.applicationSecurityFactor= Number(this.dataRecieved['applicationSecurityFactors']['riskNumber'].toFixed(2));
      this.summary.networkSecurityFactor= Number(this.dataRecieved['networkSecurityFactors']['riskFactor'].toFixed(2));
      this.summary.IPReputationFactor=0
      
      if(this.dataRecieved['networkSecurityFactors']!=null){
        console.log('found networkSecurity')
        //subdomains
        if(this.dataRecieved['networkSecurityFactors']['subdomains']!=null){
          console.log('found subdomains')
          this.summary.subdomainsCount=this.dataRecieved['networkSecurityFactors']['subdomains'].length;
        }
        else{
          this.summary.subdomainsCount=-1;
        }
        //vulnerabilities
        if(this.dataRecieved['networkSecurityFactors']['sslFactors']!=null){
          console.log('found sslFactors')
          if(this.dataRecieved['networkSecurityFactors']['sslFactors']['vulnerabilities']!=null){
            console.log('found vulnerabilities',this.summary.vulnerabilitiesList);
            let vulList=this.dataRecieved['networkSecurityFactors']['sslFactors']['vulnerabilities'];
            this.summary.vulnerabilitiesList=vulList.length;
          }
          else{
            this.summary.vulnerabilitiesList=[]
          }
        }
        //ports
        if(this.dataRecieved['networkSecurityFactors']['serviceFactors']!=null){
          console.log('found serviceFactors')
          if(this.dataRecieved['networkSecurityFactors']['serviceFactors']['ports']!=null){
            console.log('found ports')
            let portsList=this.dataRecieved['networkSecurityFactors']['serviceFactors']['ports'];
            this.summary.portsCount=portsList.length;
          }
          else{
            this.summary.portsCount=-1;
          }
        }
      }
      if (this.summary.vulnerabilitiesList.length==0){
        this.vulnerabilities=[]
      }
      else{
        //populate
      }
      //hosts
      if(this.dataRecieved['endpointSecurity']!=null){
        console.log("found endpointSecurity")
        if(this.dataRecieved['endpointSecurity']['detectedDevices']!=null){
          console.log("found detecteddevices")
          if(this.dataRecieved['endpointSecurity']['detectedDevices']['hosts']!=null){
            console.log("found hosts")
            console.log(this.dataRecieved['endpointSecurity']['detectedDevices']['hosts']);
            let hostsList=this.dataRecieved['endpointSecurity']['detectedDevices']['hosts'];
            this.summary.hostsCount=hostsList.length;
          }
          else
            this.summary.hostsCount=-1;
        }
      }
      //high severity vulnerability applicationSecurityFactors->highSeverityVul
      if(this.dataRecieved['applicationSecurityFactors']!=null){
        console.log('found applicationSecurityFactors ')
        if(this.dataRecieved['applicationSecurityFactors']['highSeverityVul']!=null){
          console.log('found highSeverityVul')
          this.summary.highSeverityVulnerabilitiy=this.dataRecieved['applicationSecurityFactors']['highSeverityVul'];
          this.highSeverityVulnerabilities=this.summary.highSeverityVulnerabilitiy;
        }
      }
      //malwares ipReputation->malwareInfection->malwares
      if(this.dataRecieved['ipReputation']!=null){
        console.log('found ipReputation')
        if(this.dataRecieved['ipReputation']['malwareInfection']!=null){
          console.log('found malwareInfection')
          if(this.dataRecieved['ipReputation']['malwareInfection']['malwares']!=null){
            console.log('malwares')
            let malList=this.dataRecieved['ipReputation']['malwareInfection']['malwares'];
            //populate this.summary.malwareList
            this.malware=malList
          }
        }
      }

      console.log(this.summary)

  }

  setNetworkSecurityData(){
    this.networkSecurityData.parseJSON(this.dataRecieved['networkSecurityFactors'])
    console.log("network security factors",this.networkSecurityFactors)
  }

  setDNSHealthData(){
    this.DNSHealthData.parseJSON(this.dataRecieved['dnsHealth']);
    console.log("dns health factors",this.DNSHealthData)
  }

  setApplicationSecurityData(){
    this.applicationSecurityData.parseJson(this.dataRecieved['applicationSecurityFactors'])
    console.log("application security factors",this.applicationSecurityData)
  }

  setEndPointSecurityData(){
    this.endPointSecurityData.parseJson(this.dataRecieved['endpointSecurity'])
    console.log("endpoint security factors",this.endPointSecurityData)
  }



  networkSecurityFactors:any;
  endpointSecurity:any;
  dnsHealth:any;
  applicationSecurityFactors:any;
  ipReputation:any;
  totalRiskNumber=0;
  target = null;
  targetData={
    "target" : "flexstudent.nu.edu.pk",
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
        "ports" : [ {
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
        "vulnerabilities" : [ {
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
      "subdomains" : [ {
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
    },
    "endpointSecurity" : {
      "endPointSecurityId" : 3,
      "endpointServices" : [ {
        "servicesId" : 1,
        "protocol" : null,
        "product" : "Microsoft IIS httpd",
        "method" : "probed",
        "name" : "http",
        "conf" : 10,
        "version" : 10,
        "portID" : 80,
        "ostype" : "Windows"
      }, {
        "servicesId" : 2,
        "protocol" : null,
        "product" : null,
        "method" : "table",
        "name" : "ident",
        "conf" : 3,
        "version" : 0,
        "portID" : 113,
        "ostype" : null
      }, {
        "servicesId" : 3,
        "protocol" : null,
        "product" : "Microsoft HTTPAPI httpd",
        "method" : "probed",
        "name" : "http",
        "conf" : 10,
        "version" : 2,
        "portID" : 443,
        "ostype" : "Windows"
      }, {
        "servicesId" : 4,
        "protocol" : null,
        "product" : "Microsoft Terminal Services",
        "method" : "probed",
        "name" : "ms-wbt-server",
        "conf" : 10,
        "version" : 0,
        "portID" : 3389,
        "ostype" : "Windows"
      } ],
      "endpointOperatingSystem" : null,
      "endpointDevices" : {
        "deviceId" : 3,
        "hosts" : [ {
          "hostNameId" : 1,
          "hostName" : "flexstudent.nu.edu.pk",
          "hostType" : "user"
        }, {
          "hostNameId" : 2,
          "hostName" : "wtl.worldcall.net.pk",
          "hostType" : "PTR"
        } ],
        "total" : 1,
        "up" : 1,
        "down" : 0,
        "addrType" : "ipv4",
        "addr" : "115.186.60.85"
      }
    },
    "dnsHealth" : {
      "dnsHealthId" : 3,
      "riskScore" : 0.0,
      "spf" : {
        "doesSpfRecordContainsWildcard" : false,
        "doesspfRecordContainsaSoftfail" : false,
        "target" : "flexstudent.nu.edu.pk",
        "spfid" : 3,
        "spfRecordMissing" : false,
        "spfRecordMalformed" : false
      },
      "ns" : [ ],
      "mx" : [ ],
      "txt" : [ ],
      "a" : [ ],
      "quadA" : [ ],
      "soa" : [ ],
      "srv" : [ ],
      "ptr" : [ ],
      "aname" : [ ],
      "cname" : [ ],
      "openDNSDetected" : false
    },
    "applicationSecurityFactors" : {
      "applicationSecurityId" : 3,
      "cookies" : {
        "cookiesId" : 3,
        "httpOnly" : null,
        "secure" : null
      },
      "general" : {
        "generalId" : 3,
        "enforceHttps" : null,
        "csp" : null,
        "hsts" : null,
        "unencryptedPwd" : null,
        "xframeOptions" : null,
        "xcontentTypeOptions" : null,
        "corspolicy" : null
      },
      "highSeverityVul" : {
        "highSeverityVulId" : 3,
        "xss" : null,
        "csrf" : null,
        "sqlI" : null
      },
      "riskNumber" : 0.0
    },
    "ipReputation" : null,
    "riskNumber" : 0.36,
    "riskFactorsId" : 3
  };
  RiskFactor:RiskFactors|undefined;

  setData(recData:any){
    recData=this.targetData;
    this.networkSecurityFactors=recData["networkSecurityFactors"];
    this.endpointSecurity=recData["endpointSecurity"];
    this.dnsHealth=recData["dnsHealth"];
    this.applicationSecurityFactors=recData["applicationSecurityFactors"];
    this.ipReputation=recData["ipReputation"];
    this.totalRiskNumber=recData["riskNumber"];
    this.target=recData["target"]

    console.log(this.target,this.totalRiskNumber,this.ipReputation,this.applicationSecurityFactors,this.networkSecurityFactors);

  }

  rgbToHex(red: number, green: number, blue: number): string {
    // Ensure that the input values are within the valid range of 0-255
    red = Math.max(0, Math.min(255, red));
    green = Math.max(0, Math.min(255, green));
    blue = Math.max(0, Math.min(255, blue));
  
    // Convert the RGB values to hex
    const hexRed = red.toString(16).padStart(2, '0');
    const hexGreen = green.toString(16).padStart(2, '0');
    const hexBlue = blue.toString(16).padStart(2, '0');
  
    // Return the combined hex string
    //console.log(`#${hexRed}${hexGreen}${hexBlue}`);
    return `#${hexRed}${hexGreen}${hexBlue}`;
  }
  generateColour(num:number){
    let r=0;
    let g=0;
    let b=0;
    r=Math.round((250*(num))/100);
    g=Math.round(((100-num)*250)/100);
    b=20;
    //console.log(r,g,b);
    return this.rgbToHex(r,g,b);
  }
  getConditionByHealth(num:number){
    if(num<33)
      return "Low"
    if(num>33 && num<66)
      return "Medium"
    else
      return "High"
  }
}
