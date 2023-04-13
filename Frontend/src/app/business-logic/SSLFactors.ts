import { Vulnerability } from "./Vulnerability";

export class SSLFactors {
    public grade:String | undefined;
    public isSelfSigned:boolean|undefined;
    public isExpired:boolean|undefined;
    public isWeakCipher:boolean|undefined;
    public vulnerabilities:Array<Vulnerability> | undefined;
    public  issuedTo:String | undefined;
    public  issuedBy:String | undefined;
    public  cipher:String | undefined;
    public  issuedCountry:String | undefined;
    public  validFrom:String | undefined;
    public  validTill:String | undefined;

    public SSLFactors() {
        this.grade = "";
        this.isSelfSigned = false;
        this.isExpired = false;
        this.isWeakCipher = false;
        this.vulnerabilities = new Array<Vulnerability>();
        this.issuedBy = "";
        this.issuedTo = "";
        this.cipher = "";
        this.issuedCountry = "";
        this.validFrom = "";
        this.validTill = "";
    }


}
