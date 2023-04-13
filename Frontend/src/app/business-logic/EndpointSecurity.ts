import { Devices } from "./Devices";
import { Services } from "./Services";
import { OperatingSytem } from "./OperatingSytem";
export class EndpointSecurity {
    public  detectedDevices:Devices|undefined;
    public  detectedServices:Array<Services>|undefined;

    public  endpointOperatingSystem:Array<OperatingSytem>|undefined;
//    public List<Devices> endpointDevices;
//    public List<Serives> endpointServices;
//
//    public EndpointSecurity(List<OperatingSytem> endpointOS, List<Devices> endpointDevices, List<Serives> endpointServices){
//        this.endpointDevices = endpointDevices;
//        this.endpointOS= endpointOS;
//        this.endpointDevices = endpointDevices;
//
//    }


}
