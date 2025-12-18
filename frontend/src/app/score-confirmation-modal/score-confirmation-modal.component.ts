import {Component, inject, ViewChild} from '@angular/core';
import {NgbModal, NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {Match} from "../../generated/graphql";


@Component({
  selector: 'app-score-confirmation-modal',
  templateUrl: './score-confirmation-modal.component.html',
  styleUrls: ['./score-confirmation-modal.component.css'],
  imports: [
    NgbModalModule
  ]
})
export class ScoreConfirmationModalComponent {

  private modalService = inject(NgbModal);

  @ViewChild('content')
  private content: any;

  public redWon?: boolean;
  public match?: Match;

  public confirmMatchResult(match: Match, confirmCallBack: () => void): void {
    this.match = match;
    this.redWon = (match.redScore || 0) > (match.blueScore || 0);

    const modalRef = this.modalService.open(this.content, {size: 'lg'}).result.then((result) => {
      if (result === 'save') {
        confirmCallBack();
      }
    });

  }

}
