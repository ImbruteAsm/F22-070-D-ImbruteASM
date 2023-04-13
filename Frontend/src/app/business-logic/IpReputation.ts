import { Malware } from "./Malware";
export class IpReputation {

    public  malwareInfection:Malware|undefined;
    public riskFactor:number|undefined;

    public IpReputation() {
        this.malwareInfection = new Malware();
        this.riskFactor = 0;
    }

}
