
export class SSHFactors {
    public  sshVersion:number|undefined;
    public ciphers:Array<String> | undefined;
    public mac:Array<String> | undefined;

    public SSHFactors() {
        this.sshVersion = 0;
        this.ciphers = new Array<String>();
        this.mac = new Array<String>();
    }

}
