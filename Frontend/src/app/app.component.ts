import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="row" style="height:100%;">
      <div class="column-side-bar"><app-side-bar></app-side-bar></div>
      <div class="column-main-dashboard"><router-outlet></router-outlet></div>
    </div>
  `,
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Dashboard';
}
