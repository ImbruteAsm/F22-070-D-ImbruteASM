import { Port } from "./Port";
export class ServiceFactors {
    public mongoDiscovered:boolean = false;
    public  cassandraDiscovered:boolean = false;
    public  msSQLDiscovered:boolean = false;
    public  mySQLDiscovered:boolean = false;
    public  redisDiscovered:boolean = false;
    public  vncDiscovered:boolean = false;
    public  rdpDiscovered:boolean = false;
    public  rsyncDiscovered:boolean = false;
    public  imapDiscovered:boolean = false;
    public  ftpDiscovered:boolean = false;
    public  smbDiscovered:boolean = false;
    public  telnetDiscovered:boolean = false;
    public  pop3Discovered:boolean = false;
    public serviceDict:Map<String, Boolean> | undefined;
    public ports:Array<Port> | undefined;

}
