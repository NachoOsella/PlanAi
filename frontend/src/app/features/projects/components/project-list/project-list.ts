import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ProjectStore } from '../../state/project.store';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [],
  templateUrl: './project-list.html',
  styleUrl: './project-list.scss',
})
export class ProjectListComponent implements OnInit {
  private router = inject(Router);
  readonly store = inject(ProjectStore);

  ngOnInit() {
    this.loadProjects();
  }

  loadProjects() {
    // TODO: Call store.loadProjects()
  }

  goToProject(projectId: string) {
    // TODO: Navigate to /projects/:id
  }

  deleteProject(projectId: string) {
    // TODO: Confirm and call store.deleteProject()
  }
}
