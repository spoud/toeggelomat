import {Component} from '@angular/core';
import {PlayersScoreboardComponent} from "./players-scoreboard/players-scoreboard.component";
import {LastMatchesComponent} from "./last-matches/last-matches.component";

import {RouterModule} from "@angular/router";

@Component({
    selector: 'app-scoreboard',
    templateUrl: './scoreboard.component.html',
    styleUrls: ['./scoreboard.component.css'],
    imports: [
    RouterModule,
    PlayersScoreboardComponent,
    LastMatchesComponent
]
})
export class ScoreboardComponent {

}
