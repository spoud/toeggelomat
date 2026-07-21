import {Component, signal} from '@angular/core';
import {RouterModule} from "@angular/router";
import {NgbCollapseModule} from "@ng-bootstrap/ng-bootstrap";

interface NavLink {
  label: string;
  path: string;
}

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.scss',
  imports: [
    RouterModule,
    NgbCollapseModule
  ]
})
export class NavComponent {

  public links: NavLink[] = [
    {label: 'Scoreboard', path: '/scoreboard'},
    {label: 'History', path: '/history'},
    {label: 'Stats', path: '/stats'},
    {label: 'Manage players', path: '/manage-players'},
    {label: 'Manage seasons', path: '/manage-seasons'},
  ];

  public isCollapsed = signal(true);
}
