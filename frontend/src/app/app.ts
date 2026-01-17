import { Component, signal, inject, OnInit, HostListener } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { ProjectStore } from './features/projects/state/project.store';
import { ProjectModalComponent, ProjectSaveEvent } from './shared/components/project-modal/project-modal.component';
import { Project } from './core/models/project.model';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, ProjectModalComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected readonly title = signal('PlanAI');
  readonly projectStore = inject(ProjectStore);

  readonly projectModalOpen = signal(false);
  readonly projectModalMode = signal<'edit' | 'delete'>('edit');
  readonly selectedProject = signal<Project | null>(null);
  readonly openMenuId = signal<number | null>(null);

  ngOnInit(): void {
    this.projectStore.loadProjects();
  }

  @HostListener('document:click')
  onDocumentClick(): void {
    this.openMenuId.set(null);
  }

  @HostListener('document:keydown.escape')
  onEscape(): void {
    this.openMenuId.set(null);
  }

  toggleMenu(projectId: number): void {
    this.openMenuId.set(this.openMenuId() === projectId ? null : projectId);
  }

  closeMenu(): void {
    this.openMenuId.set(null);
  }

  openEditProject(project: Project): void {
    this.selectedProject.set(project);
    this.projectModalMode.set('edit');
    this.projectModalOpen.set(true);
    this.closeMenu();
  }

  openDeleteProject(project: Project): void {
    this.selectedProject.set(project);
    this.projectModalMode.set('delete');
    this.projectModalOpen.set(true);
    this.closeMenu();
  }

  closeProjectModal(): void {
    this.projectModalOpen.set(false);
    this.selectedProject.set(null);
  }

  async onSaveProject(event: ProjectSaveEvent): Promise<void> {
    await this.projectStore.updateProject(event.id, {
      name: event.name,
      description: event.description,
    });
    this.closeProjectModal();
  }

  async onDeleteProject(projectId: number): Promise<void> {
    await this.projectStore.deleteProject(projectId);
    this.closeProjectModal();
  }
}
