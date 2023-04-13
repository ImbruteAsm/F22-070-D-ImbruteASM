import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SideBarComponent } from './side-bar/side-bar.component';
import { MainDashboardComponent } from './main-dashboard/main-dashboard.component';
import { ScannedOutputComponent } from './scanned-output/scanned-output.component';
import { NgCircleProgressModule } from 'ng-circle-progress';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FooterComponent } from './footer/footer.component';
import { CDBFreeModule } from 'ng-cdbangular';

@NgModule({
  declarations: [
    AppComponent,
    SideBarComponent,
    MainDashboardComponent,
    ScannedOutputComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatProgressSpinnerModule,
    CDBFreeModule,
    NgCircleProgressModule.forRoot({
      // set defaults here
      radius: 100,
      outerStrokeWidth: 16,
      innerStrokeWidth: 1,
      outerStrokeColor: "#78C000",
      innerStrokeColor: "#070F06",
      animationDuration: 700,
      animation: true,
      subtitleFontSize: '15px',
      titleFontSize: '15px',
      titleColor: 'white',
      unitsColor: 'white',
      subtitleColor: 'white'
      
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
