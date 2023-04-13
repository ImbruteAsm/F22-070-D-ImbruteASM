import { Injectable } from '@angular/core';
import httpClient from '../backend/http-client';

@Injectable()
export class GetData {

  async dataByUrl(url:string) {
    //url='https://gbfs.citibikenyc.com/gbfs/en/station_information.json';
    const resp = await fetch(url);
    let data;
    try{
      data = await resp.json();   
    }
    catch{
      
      data={'name':'name'};
    }
    return data;
  }
}