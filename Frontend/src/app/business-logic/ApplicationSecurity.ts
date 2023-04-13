import {Cookies} from "./Cookies"
import { General } from "./General";
import { HighSeverityVul } from "./HighSeverityVul";

export class ApplicationSecurity {
    public cookies:Cookies | undefined;

    public general:General | undefined;

    public highSeverityVul:HighSeverityVul | undefined;
    public riskNumber:number | undefined;


    public ApplicationSecurity( cookies:any, general:any, highSeverityVul:any) {
        this.cookies = cookies;
        this.general = general;
        this.highSeverityVul = highSeverityVul;
        this.riskNumber = 0;
    }

    public setApplicationSecurityFactorsUsingJson(p_data:any):any{
        if(p_data==null)return null;
        let ret = new ApplicationSecurity;
        let cookie=new Cookies;
        let general=new General;
        let highSeverityVul=new HighSeverityVul;
        if(p_data['cookies']!=null){
            
            cookie.httpOnly=p_data['cookies']['httpOnly'];
            cookie.secure=p_data['cookies']['secure'];
            
        }
        ret.cookies?.httpOnly
    }

}
