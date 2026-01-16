import { signal, computed, Injectable, inject } from '@angular/core';
import { Project, ProjectDetail, CreateProjectRequest, UpdateProjectRequest } from '../../../core/models/project.model';
import { ProjectService } from '../services/project.service';

@Injectable({
  providedIn: 'root'
})
export class ProjectStore {
  private projectService = inject(ProjectService);

  // State
  private _projects = signal<Project[]>([]);
  private _selectedProject = signal<ProjectDetail | null>(null);
  private _loading = signal<boolean>(false);
  private _error = signal<string | null>(null);

  // Selectors
  readonly projects = computed(() => this._projects());
  readonly selectedProject = computed(() => this._selectedProject());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());

  // Actions
  async loadProjects() {
    // TODO: Implement load all projects using projectService
  }

  async loadProjectDetail(id: string) {
    // TODO: Implement load project detail by id
  }

  async createProject(request: CreateProjectRequest) {
    // TODO: Implement create project
  }

  async updateProject(id: string, request: UpdateProjectRequest) {
    // TODO: Implement update project
  }

  async deleteProject(id: string) {
    // TODO: Implement delete project
  }

  // Internal state modifiers (optional to use or keep)
  setProjects(projects: Project[]) {
    this._projects.set(projects);
  }

  setSelectedProject(project: ProjectDetail | null) {
    this._selectedProject.set(project);
  }

  setLoading(loading: boolean) {
    this._loading.set(loading);
  }

  setError(error: string | null) {
    this._error.set(error);
  }
}
