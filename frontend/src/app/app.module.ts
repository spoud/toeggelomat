import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';

import {NgbButtonsModule, NgbModalModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {HttpClientModule} from '@angular/common/http';
import {ActionReducer, ActionReducerMap, MetaReducer, StoreModule} from '@ngrx/store';
import {playersReducer} from './store/players/players.reducer';
import {PlayersSelectionComponent} from './players-selection/players-selection.component';
import {RouterModule} from '@angular/router';
import {ROUTES} from './app.router';
import {EffectsModule} from '@ngrx/effects';
import {PlayersEffect} from './store/players/players.effect';
import {PlayersApiService} from './services/players-api.service';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {environment} from '../environments/environment';
import {CurrentMatchComponent} from './current-match/current-match.component';
import {machesReducer} from './store/matches/matches.reducer';
import {MatchesApiService} from './services/matches-api.service';
import {MatchesEffect} from './store/matches/maches.effect';
import {ScoreboardComponent} from './scoreboard/scoreboard.component';
import {SpoudAvatarComponent} from './spoud-avatar/spoud-avatar.component';
import {ScoreConfirmationModalComponent} from './score-confirmation-modal/score-confirmation-modal.component';
import {localStorageSync} from 'ngrx-store-localstorage';
import {EventApiService} from './services/event-api.service';
import {LastMatchesComponent} from './scoreboard/last-matches/last-matches.component';
import {PlayersScoreboardComponent} from './scoreboard/players-scoreboard/players-scoreboard.component';


const reducers: ActionReducerMap<any> = {players: playersReducer, matches: machesReducer};

export function localStorageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  if (environment.production || true) {
    return reducer;
  }
  return localStorageSync({
    keys: ['players', 'matches'],
    rehydrate: true
  })(reducer);
}

const metaReducers: Array<MetaReducer<any, any>> = [localStorageSyncReducer];

@NgModule({
  declarations: [
    AppComponent,
    PlayersSelectionComponent,
    CurrentMatchComponent,
    ScoreboardComponent,
    SpoudAvatarComponent,
    ScoreConfirmationModalComponent,
    LastMatchesComponent,
    PlayersScoreboardComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(
      ROUTES
    ),

    NgbModule,
    NgbButtonsModule,
    NgbModalModule,

    StoreModule.forRoot(reducers, {metaReducers}),
    EffectsModule.forRoot([PlayersEffect, MatchesEffect]),
    StoreDevtoolsModule.instrument({
      maxAge: 25, // Retains last 25 states
      logOnly: environment.production, // Restrict extension to log-only mode
    }),

  ],
  providers: [
    EventApiService,
    PlayersApiService,
    MatchesApiService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
