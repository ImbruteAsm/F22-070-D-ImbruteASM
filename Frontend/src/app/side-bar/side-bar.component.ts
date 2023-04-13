import { Component } from '@angular/core';
import { RiskFactors } from '../business-logic/RiskFactors';
import { ScannedOutputComponent } from '../scanned-output/scanned-output.component';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css'
  ]
})
export class SideBarComponent {
  
getSidebarActiveComponent():number {
  return SideBarComponent.activeComponent;
}
sideBarActivateComponent(arg0: number) {
  SideBarComponent.activateComponent(arg0);
}


  public static dataRecieved:RiskFactors=new RiskFactors;
  public static dataRecievedJson:any=null;
  public static target:any=null;
  public static activeComponent:any;
  public static setData(json:any){
    this.dataRecievedJson=json;
    SideBarComponent.dataRecieved=new RiskFactors;
    SideBarComponent.dataRecieved.target=json["target"];
    //SideBarComponent.dataRecieved.networkSecurityFactors?.riskFactor=json["networkSecurityFactor"]["riskFactor"];

  }

  public static activateComponent(arg0: number) {
    if(arg0==1){
      if(this.target!=null)
      this.activeComponent=1
    }
    if(arg0==0){
      this.target=null;
      this.activeComponent=0;

    }
  }
}
