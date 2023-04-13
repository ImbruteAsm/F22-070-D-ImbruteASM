import { ServiceFactors } from "./ServiceFactors";
import { SSLFactors } from "./SSLFactors";
import { SSHFactors } from "./SSHFactors";
import { Subdomain } from "./Subdomain";
export class NetworkSecurityFactors {

    public  serviceFactors:ServiceFactors|undefined;

    public  sslFactors:SSLFactors | undefined;

    public  sshFactors:SSHFactors|undefined;

    public  subdomains:Array<Subdomain>|undefined;
    public  riskFactor:number=0;


    public NetworkSecurityFactors() {
        this.serviceFactors = new ServiceFactors();
        this.sslFactors = new SSLFactors();
        this.sshFactors = new SSHFactors();
        this.subdomains = new Array<Subdomain>();
        this.riskFactor = 0;
    }

}
