import {Routes} from '@angular/router';
import {PlayersSelectionComponent} from './players-selection/players-selection.component';

export const ROUTES: Routes = [
  { path: 'players-selection', component: PlayersSelectionComponent },
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
