
export class Summary {
    public riskFactor:number=-1;
    public networkSecurityFactor:number=-1;
    public DNSHealthFactor:number=-1;
    public applicationSecurityFactor:number=-1;
    public IPReputationFactor:number=-1;
    public target:string="NA"
    public malwareList:Array<any>=[];
    public highSeverityVulnerabilitiy:any;
    public vulnerabilitiesList:Array<any>=[];
    public portsCount:number=-1;
    public subdomainsCount:number=-1;
    public hostsCount:number=-1;
}