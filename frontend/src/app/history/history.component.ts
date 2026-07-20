import {Component} from '@angular/core';
import {LastMatchesComponent} from "../scoreboard/last-matches/last-matches.component";
import {PlayersScoreboardComponent} from "../scoreboard/players-scoreboard/players-scoreboard.component";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss',
  imports: [
    LastMatchesComponent,
    PlayersScoreboardComponent
  ]
})
export class HistoryComponent {
}
