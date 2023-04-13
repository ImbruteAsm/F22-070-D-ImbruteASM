import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainDashboardComponent } from './main-dashboard/main-dashboard.component';
import { ScannedOutputComponent } from './scanned-output/scanned-output.component';

const routes: Routes = [{ path: '', component: MainDashboardComponent },
  {path: 'scan',component: ScannedOutputComponent}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
