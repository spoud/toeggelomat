import {Component} from '@angular/core';
import {LastMatchesComponent} from "../scoreboard/last-matches/last-matches.component";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss',
  imports: [
    LastMatchesComponent
  ]
})
export class HistoryComponent {
}
