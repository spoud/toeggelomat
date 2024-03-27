import {Component} from '@angular/core';
import {RouterModule} from "@angular/router";

@Component({
  standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [
    RouterModule
  ]
})
export class AppComponent {
}
