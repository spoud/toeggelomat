import {Component, inject, signal} from '@angular/core';
import {PlayersService} from "../services/players-service";

@Component({
  selector: 'app-manage-players',
  templateUrl: './manage-players.component.html',
  styleUrl: './manage-players.component.scss',
  imports: []
})
export class ManagePlayersComponent {

  private playersService = inject(PlayersService);

  public players = this.playersService.players;
  public newPlayerName = signal('');

  public onNewPlayerNameInput(event: Event): void {
    this.newPlayerName.set((event.target as HTMLInputElement).value);
  }

  public addPlayer(): void {
    const nickName = this.newPlayerName().trim();
    if (nickName) {
      this.playersService.createPlayer(nickName);
      this.newPlayerName.set('');
    }
  }

  public archivePlayer(uuid: string): void {
    this.playersService.deletePlayer(uuid);
  }
}
