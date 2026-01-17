import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ProjectStore } from '../../state/project.store';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './project-list.html',
  styleUrl: './project-list.scss',
})
export class ProjectListComponent implements OnInit {
  private router = inject(Router);
  readonly projectStore = inject(ProjectStore);

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.projectStore.loadProjects();
  }

  goToProject(projectId: string): void {
    this.router.navigate(['/projects', projectId]);
  }

  async deleteProject(projectId: string, event: Event): Promise<void> {
    event.stopPropagation();
    if (confirm('Are you sure you want to delete this project?')) {
      await this.projectStore.deleteProject(projectId);
    }
  }
}
