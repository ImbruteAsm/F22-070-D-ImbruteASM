import { SPF } from "./SPF";
export class DNSHealth {
    public riskScore:number | undefined;
    public isOpenDNSDetected:boolean = false;

    public spf:SPF | undefined;
    public ns:Array<String> | undefined;
    public mx:Array<String> | undefined;;
    public txt:Array<String> | undefined;;
    public a:Array<String> | undefined;;
    public quadA:Array<String> | undefined;;
    public aName:Array<String> | undefined;;
    public cName:Array<String> | undefined;;
    public soa:Array<String> | undefined;;
    public srv:Array<String> | undefined;;
    public ptr:Array<String> | undefined;;

    public DNSHealth() {
        this.spf = new SPF();
        this.ns = new Array<String>;
        this.mx = new Array<String>;
        this.txt = new Array<String>;
        this.a = new Array<String>;
        this.quadA = new Array<String>;
        this.aName = new Array<String>;
        this.cName = new Array<String>;
        this.soa = new Array<String>;
        this.srv = new Array<String>;
        this.ptr = new Array<String>;

    }


}
