import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ProjectStore } from '../../state/project.store';

@Component({
  selector: 'app-project-create',
  standalone: true,
  imports: [],
  templateUrl: './project-create.html',
  styleUrl: './project-create.scss',
})
export class ProjectCreateComponent {
  private router = inject(Router);
  private store = inject(ProjectStore);

  async onSubmit() {
    // TODO: Implement project creation
  }

  onCancel() {
    // TODO: Navigate back to projects list
  }
}
