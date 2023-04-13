import { Component } from '@angular/core';
import { SideBarComponent } from '../side-bar/side-bar.component';
import { BackendService } from '../backend/backend-service';
import { GetData } from '../backend/get-data';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-dashboard',
  templateUrl: './main-dashboard.component.html',
  styleUrls: ['./main-dashboard.component.css'
  ]
})
export class MainDashboardComponent {
  dataget:GetData=new GetData();
  isDataRecieved:boolean=false;
  fetchingData:boolean=false;
  constructor(private router:Router){
    SideBarComponent.activateComponent(0);
    SideBarComponent.dataRecievedJson=null;
    this.isDataRecieved=false;

  }

  target='';
  targetGiven:any=false;
  
  async scanWebsite(){
    let url ='http://localhost:9901/scan?target='+this.target;
    this.fetchingData=true;
    console.log("fetching")
    SideBarComponent.dataRecievedJson=null;
    let jsonData = await this.dataget.dataByUrl(url)
    //await new Promise(resolve => setTimeout(resolve, 20000));
    console.log("fetched")
    this.fetchingData=false;
    this.isDataRecieved=true;
    SideBarComponent.dataRecievedJson=jsonData;
    SideBarComponent.activateComponent(1)
    this.router.navigate(['/scan']);
    return jsonData;
  }


  public inputIntoTarget(char:any){
    
    this.target=char.target.value;
    this.targetGiven=true;
    if(this.target.length==0){this.target='';this.targetGiven=false;}
    else {SideBarComponent.target=this.target; }
  }

  value=""
  getVal(val: any){
  console.warn(val)
  console.log("val = ",val)
  this.target=val;
  this.scanWebsite()
  this.value= val
  const urlvariable="http://localhost:9901/scan?target=";
  this.value= urlvariable + this.value;
  
}
}
