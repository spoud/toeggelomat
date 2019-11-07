import {Routes} from '@angular/router';
import {PlayersSelectionComponent} from './players-selection/players-selection.component';
import {CurrentMatchComponent} from './current-match/current-match.component';
import {ScoreboardComponent} from './scoreboard/scoreboard.component';

export const ROUTES: Routes = [
  { path: 'players-selection', component: PlayersSelectionComponent },
  { path: 'current-match', component: CurrentMatchComponent },
  { path: 'scoreboard', component: ScoreboardComponent },
  { path: '',
    redirectTo: '/scoreboard',
    pathMatch: 'full'
  },
  { path: '**', component: ScoreboardComponent }
];
