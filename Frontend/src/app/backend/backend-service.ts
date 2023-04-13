import { GetData } from "./get-data";

export class BackendService{
    static dataget:GetData;
    static getDataOfWebsite(website:string){
        let url ='http://localhost:9901/scan?target='+website;
        //let url='https://gbfs.citibikenyc.com/gbfs/en/station_information.json';
        let jsonData = this.dataget.dataByUrl(url)
        console.log(jsonData);
        return jsonData;
    }
}