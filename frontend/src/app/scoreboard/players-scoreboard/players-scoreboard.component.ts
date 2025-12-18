import {Component, inject} from '@angular/core';
import {SpoudAvatarComponent} from "../../spoud-avatar/spoud-avatar.component";
import {LastMatchTimePipe} from "./last-match-time.pipe";
import {PlayersService} from "../../services/players-service";


@Component({
  selector: 'app-players-scoreboard',
  templateUrl: './players-scoreboard.component.html',
  styleUrls: ['./players-scoreboard.component.css'],
  imports: [
    SpoudAvatarComponent,
    LastMatchTimePipe
  ]
})
export class PlayersScoreboardComponent {

  private playersService = inject(PlayersService);

  public players = this.playersService.players;

}
