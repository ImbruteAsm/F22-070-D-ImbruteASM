

export class DNSHealthData{
    public riskScore:any;
    public doesSpfRecordContainsWildcard:any;
    public doesspfRecordContainsaSoftfail:any;
    public spfRecordMissing:any;
    public spfRecordMalformed:any;
    public openDNSDetected:any;

    public ns:any = new Array<any>();
    public mx:any = new Array<any>();
    public txt:any = new Array<any>();
    public a:any = new Array<any>();
    public quadA:any = new Array<any>();
    public soa:any = new Array<any>();
    public srv:any = new Array<any>();
    public ptr:any = new Array<any>();
    public aname:any = new Array<any>();
    public cname:any = new Array<any>();

    public parseJSON(json:any):any{
        this.ns=new Array();
        this.mx=new Array();
        this.txt=new Array();
        this.a=new Array();
        this.quadA=new Array();
        this.soa=new Array();
        this.srv=new Array();
        this.ptr=new Array();
        this.aname=new Array();
        this.cname=new Array();

        if(json==null){return null}
        if(json['riskScore']!=null)this.riskScore=json['riskScore'];
        if(json['spf']!=null){
            let spf=json['spf']
            this.doesSpfRecordContainsWildcard=spf['doesSpfRecordContainsWildcard'];
            this.doesspfRecordContainsaSoftfail=spf['doesspfRecordContainsaSoftfail'];
            this.spfRecordMissing=spf['spfRecordMissing'];
            this.spfRecordMalformed=spf['spfRecordMalformed']
        }
        if(json['ns']!=null)this.ns=json['ns'];
        if(json['mx']!=null)this.mx=json['mx'];
        if(json['txt']!=null)this.txt=json['txt'];
        if(json['a']!=null)this.a=json['a'];
        if(json['quadA']!=null)this.quadA=json['quadA']
        if(json['soa']!=null)this.soa=json['soa']
        if(json['srv']!=null)this.srv=json['srv']
        if(json['ptr']!=null)this.ptr=json['ptr']
        if(json['aname']!=null)this.aname=json['aname']
        if(json['cname']!=null)this.cname=json['cname']

        if(json['openDNSDetected']!=null)this.openDNSDetected=json['openDNSDetected'];
    }
}