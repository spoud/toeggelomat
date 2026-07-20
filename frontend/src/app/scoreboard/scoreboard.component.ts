import {Component} from '@angular/core';
import {PlayersScoreboardComponent} from "./players-scoreboard/players-scoreboard.component";

import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-scoreboard',
  templateUrl: './scoreboard.component.html',
  styleUrls: ['./scoreboard.component.css'],
  imports: [
    RouterModule,
    PlayersScoreboardComponent
  ]
})
export class ScoreboardComponent {

}
