import {Routes} from '@angular/router';
import {PlayersSelectionComponent} from "./players-selection/players-selection.component";
import {CurrentMatchComponent} from "./current-match/current-match.component";
import {ScoreboardComponent} from "./scoreboard/scoreboard.component";
import {ManagePlayersComponent} from "./manage-players/manage-players.component";

export const routes: Routes = [
  {path: 'players-selection', component: PlayersSelectionComponent},
  {path: 'current-match', component: CurrentMatchComponent},
  {path: 'scoreboard', component: ScoreboardComponent},
  {path: 'manage-players', component: ManagePlayersComponent},
  {
    path: '',
    redirectTo: '/scoreboard',
    pathMatch: 'full'
  },
  {path: '**', component: ScoreboardComponent}
];
