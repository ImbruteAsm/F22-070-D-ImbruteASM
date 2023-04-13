let data={
    
    "endPointSecurityId" : 3,
    "endpointServices" : [ 
        {
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
      "hosts" : [ 
        {
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
}

export class EndPointSecurityData{
    public endpointServices:any=new Array();
    public endpointOperatingSystem:any;
    public hosts:any;
    public total:any;
    public up:any;
    public down:any;
    public addrType:any;
    public addr:any;

    public parseJson(json:any):any{
        if(json==null)return null;
        if(json['endpointServices'])this.endpointServices=json['endpointServices']
        if(json['endpointOperatingSystem'])this.endpointOperatingSystem=json['endpointOperatingSystem'];
        if(json['endpointDevices']){
            let epd=json['endpointDevices'];
            if(epd['hosts'])this.hosts=epd['hosts']
            this.total=epd['total']
            this.up=epd['up']
            this.down=epd['down']
            this.addrType=epd['addrType']
            this.addr=epd['addr']
        }
    }
}