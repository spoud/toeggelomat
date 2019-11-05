import {Component, OnInit} from '@angular/core';
import {select, Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {PlayerEO} from '../store/players/players.model';

@Component({
  selector: 'app-players-selection',
  templateUrl: './players-selection.component.html',
  styleUrls: ['./players-selection.component.css']
})
export class PlayersSelectionComponent implements OnInit {


  public players$: Observable<PlayerEO[]>;

  constructor(private store: Store<{ count: number }>) {
    this.players$ = store.pipe(select('players'), select('list'));
  }

  ngOnInit() {
  }

}
