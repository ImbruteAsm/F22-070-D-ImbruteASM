import { NetworkSecurityFactors } from "./NetworkSecurityFactors";
import { EndpointSecurity } from "./EndpointSecurity";
import { DNSHealth } from "./DNSHealth";
import { ApplicationSecurity } from "./ApplicationSecurity";
import { IpReputation } from "./IpReputation";
export class RiskFactors {
    public  target:String|undefined;

    public  networkSecurityFactors:NetworkSecurityFactors|undefined;

    public  endpointSecurity:EndpointSecurity|undefined;

    public  dnsHealth:DNSHealth|undefined;

    public  applicationSecurityFactors:ApplicationSecurity|undefined;

    public  ipReputation:IpReputation|undefined;
    public  riskNumber:number|undefined;


    public RiskFactors(){
        this.target="";
        this.applicationSecurityFactors=new ApplicationSecurity;
        this.endpointSecurity=new EndpointSecurity;
        this.dnsHealth=new DNSHealth;
        this.applicationSecurityFactors=new ApplicationSecurity;
        this.ipReputation=new IpReputation;
        this.riskNumber=0;
    }

    public setRiskFactorsUsingJson(p_data:any):any{
        if (p_data==null) return null;
        let ret = new RiskFactors;
        ret.applicationSecurityFactors=new ApplicationSecurity;
        ret.applicationSecurityFactors.setApplicationSecurityFactorsUsingJson(p_data['applicationSecurityFactors']);
    }

}
