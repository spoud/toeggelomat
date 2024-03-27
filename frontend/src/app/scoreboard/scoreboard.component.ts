import {Component} from '@angular/core';
import {PlayersScoreboardComponent} from "./players-scoreboard/players-scoreboard.component";
import {LastMatchesComponent} from "./last-matches/last-matches.component";
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";

@Component({
  standalone: true,
  selector: 'app-scoreboard',
  templateUrl: './scoreboard.component.html',
  styleUrls: ['./scoreboard.component.css'],
  imports: [
    CommonModule,
    RouterModule,
    PlayersScoreboardComponent,
    LastMatchesComponent
  ]
})
export class ScoreboardComponent {

}
