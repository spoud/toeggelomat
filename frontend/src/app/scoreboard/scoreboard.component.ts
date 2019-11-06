import {Component, OnInit} from '@angular/core';
import {select, Store} from '@ngrx/store';
import {PlayerEO} from '../entities/playersl';

@Component({
  selector: 'app-scoreboard',
  templateUrl: './scoreboard.component.html',
  styleUrls: ['./scoreboard.component.css']
})
export class ScoreboardComponent implements OnInit {

  public players: PlayerEO[];

  constructor(private store: Store<{ count: number }>) {
  }

  ngOnInit() {
    // FIXME unsubscribe
    this.store.pipe(select('players'), select('list'))
      .subscribe((list: PlayerEO[]) => {
        this.players = list.sort((l, r) => r.points - l.points);
      });
  }

}
