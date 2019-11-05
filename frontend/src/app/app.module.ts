import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';

import {NgbButtonsModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {HttpClientModule} from '@angular/common/http';
import {StoreModule} from '@ngrx/store';
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


@NgModule({
  declarations: [
    AppComponent,
    PlayersSelectionComponent,
    CurrentMatchComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(
      ROUTES
    ),

    NgbModule,
    NgbButtonsModule,

    StoreModule.forRoot({players: playersReducer, matches: machesReducer}),
    EffectsModule.forRoot([PlayersEffect]),
    StoreDevtoolsModule.instrument({
      maxAge: 25, // Retains last 25 states
      logOnly: environment.production, // Restrict extension to log-only mode
    }),

  ],
  providers: [
    PlayersApiService,
    MatchesApiService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
