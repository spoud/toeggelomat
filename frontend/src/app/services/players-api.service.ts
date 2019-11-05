import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {PlayerEO} from '../store/players/players.model';
import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';

@Injectable()
export class PlayersApiService {
  constructor(private http: HttpClient) {
  }

  public getAll(): Observable<PlayerEO[]> {
    return of([
      {
        uuid: '65691fb6-0056-4a78-b3ea-2aa58130ea98',
        nickName: 'anna',
        email: 'anna.knifely@spoud.io',
        points: 1111
      },
      {
        uuid: 'cc07cf47-59db-4e3b-b430-a3deacc5e603',
        nickName: 'gaetan',
        email: 'gaetan.however_that_is_spelled@spoud.io',
        points: 2222
      },
      {
        uuid: '3b01e01f-56f4-48a8-aad1-b0db90444498',
        nickName: 'marcel',
        email: 'marcelo.lastname@spoud.io',
        points: 3333
      },
      {
        uuid: '9e021eb1-9bcf-4e66-a4a4-e5e28cd55414',
        nickName: 'julian',
        email: 'julian.thegreatest@spoud.io',
        points: 9999
      }
    ]);
    //
    // return this.http
    //   .get<PlayerEO[]>(`/api/v1/players`, {
    //     observe: 'response'
    //   })
    //   .pipe(
    //     map((res: HttpResponse<PlayerEO[]>) => res.body)
    //   );

  }
}
