import {Routes} from '@angular/router';
import {PlayersSelectionComponent} from './players-selection/players-selection.component';
import {CurrentMatchComponent} from './current-match/current-match.component';

export const ROUTES: Routes = [
  { path: 'players-selection', component: PlayersSelectionComponent },
  { path: 'current-match', component: CurrentMatchComponent },
  // { path: 'hero/:id',      component: HeroDetailComponent },
  // {
  //   path: 'heroes',
  //   component: HeroListComponent,
  //   data: { title: 'Heroes List' }
  // },
  { path: '',
    redirectTo: '/players-selection',
    pathMatch: 'full'
  },
  { path: '**', component: PlayersSelectionComponent }
];
