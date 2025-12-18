import {ApplicationConfig, inject, provideZonelessChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient} from '@angular/common/http';
import {provideApollo} from "apollo-angular";
import {HttpLink} from "apollo-angular/http";
import {InMemoryCache} from "@apollo/client/core";

export const appConfig: ApplicationConfig = {
  providers: [
    provideZonelessChangeDetection(),
    provideRouter(routes),
    provideHttpClient(),

    provideApollo(() => {
      const httpLink = inject(HttpLink);

      return {
        link: httpLink.create({uri: '/graphql', }),
        cache: new InMemoryCache({
          typePolicies: {
            // Kind of hack to resolve scalar properly...
            // found here: https://community.apollographql.com/t/custom-scalars-on-the-client-side/6587/3
            Player:{
              fields: {
                lastMatchTime: {
                  read: (str)=> new Date(str)
                }
              }
            },
            Match: {
              fields: {
                matchTime: {
                  read: (str)=> new Date(str)
                }
              }
            }
          }
        }),
        defaultOptions: {
          query: {fetchPolicy: 'network-only'},
        },
      };
    })

  ],
};
