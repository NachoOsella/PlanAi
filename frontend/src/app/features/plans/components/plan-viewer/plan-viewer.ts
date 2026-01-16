import { Component, Input } from '@angular/core';
import { Epic } from '../../../../core/models/epic.model';

@Component({
  selector: 'app-plan-viewer',
  standalone: true,
  imports: [],
  templateUrl: './plan-viewer.html',
  styleUrl: './plan-viewer.scss',
})
export class PlanViewerComponent {
  @Input() epics: Epic[] = [];

  toggleEpic(epicId: string) {
    // TODO: Implement epic visibility toggle
  }

  toggleStory(storyId: string) {
    // TODO: Implement user story visibility toggle
  }
}
