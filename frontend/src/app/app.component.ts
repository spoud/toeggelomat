import {Component} from '@angular/core';
import {RouterModule} from "@angular/router";
import {NavComponent} from "./nav/nav.component";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    imports: [
        RouterModule,
        NavComponent
    ]
})
export class AppComponent {
}
