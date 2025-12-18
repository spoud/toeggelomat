import {ApplicationConfig} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient} from '@angular/common/http';
import {provideStore} from "@ngrx/store";
import {playersReducer} from "./store/players/players.reducer";
import {machesReducer} from "./store/matches/matches.reducer";
import {provideEffects} from "@ngrx/effects";
import {MatchesEffect} from "./store/matches/maches.effect";
import {PlayersEffect} from "./store/players/players.effect";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    provideEffects([PlayersEffect, MatchesEffect]),
    provideStore({players: playersReducer, matches: machesReducer})
  ],
};
