import {Observable} from 'rxjs';
import {Injectable, NgZone} from '@angular/core';
import {MatchEO} from '../entities/match';

@Injectable({
  providedIn: 'root'
})
export class EventApiService {

  public constructor(private zone: NgZone) {

  }

  public matchStream(): Observable<MatchEO> {
    return this.createSSEObervable('/api/v1/sse/matches');
  }

  public scoreChangeStream(): Observable<any> {
    return this.createSSEObervable('/api/v1/sse/scores');
  }

  private createSSEObervable<T>(url: string): any {
    const observable = new Observable<T>(observer => {
      const eventSource = new EventSource(url);
      eventSource.onmessage = x => this.zone.run(() => observer.next(JSON.parse(x.data)));
      eventSource.onerror = x => this.zone.run(() => observer.error(x));

      return () => {
        eventSource.close();
      };
    });

    return observable;
  }

}
