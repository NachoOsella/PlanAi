import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectStore } from '../../state/project.store';
import { ChatStore } from '../../../chat/state/chat.store';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [],
  templateUrl: './project-detail.html',
  styleUrl: './project-detail.scss',
})
export class ProjectDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  readonly projectStore = inject(ProjectStore);
  readonly chatStore = inject(ChatStore);

  ngOnInit() {
    this.loadProject();
  }

  loadProject() {
    // TODO: Extract ID from route and call store.loadProjectDetail()
  }

  onRefreshPlan() {
    // TODO: Implement plan refresh/extraction
  }
}
