import { signal, computed, Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import {
  Project,
  ProjectDetail,
  CreateProjectRequest,
  UpdateProjectRequest,
} from '../../../core/models/project.model';
import { ProjectService } from '../services/project.service';
import { EpicService } from '../../plans/services/epic.service';
import {
  UpdateEpicRequest,
  UpdateStoryRequest,
  UpdateTaskRequest,
  CreateEpicRequest,
  CreateStoryRequest,
  CreateTaskRequest,
} from '../../../core/models/epic.model';

@Injectable({
  providedIn: 'root',
})
export class ProjectStore {
  private projectService = inject(ProjectService);
  private epicService = inject(EpicService);

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
  async loadProjects(): Promise<void> {
    this._loading.set(true);
    this._error.set(null);
    try {
      const projects = await firstValueFrom(this.projectService.getProjects());
      this._projects.set(projects);
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to load projects');
    } finally {
      this._loading.set(false);
    }
  }

  async loadProjectDetail(id: number | string): Promise<void> {
    this._loading.set(true);
    this._error.set(null);
    try {
      const project = await firstValueFrom(this.projectService.getProjectById(id));
      this._selectedProject.set(project);
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to load project details');
    } finally {
      this._loading.set(false);
    }
  }

  async createProject(request: CreateProjectRequest): Promise<Project | null> {
    this._loading.set(true);
    this._error.set(null);
    try {
      const project = await firstValueFrom(this.projectService.createProject(request));
      this._projects.update((projects) => [project, ...projects]);
      return project;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to create project');
      return null;
    } finally {
      this._loading.set(false);
    }
  }

  async updateProject(id: number | string, request: UpdateProjectRequest): Promise<Project | null> {
    this._loading.set(true);
    this._error.set(null);
    try {
      const updated = await firstValueFrom(this.projectService.updateProject(id, request));
      this._projects.update((projects) =>
        projects.map((p) => (p.id === Number(id) ? { ...p, ...updated } : p))
      );
      if (this._selectedProject()?.id === Number(id)) {
        this._selectedProject.update((current) => (current ? { ...current, ...updated } : null));
      }
      return updated;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to update project');
      return null;
    } finally {
      this._loading.set(false);
    }
  }

  async deleteProject(id: number | string): Promise<boolean> {
    this._loading.set(true);
    this._error.set(null);
    try {
      await firstValueFrom(this.projectService.deleteProject(id));
      this._projects.update((projects) => projects.filter((p) => p.id !== Number(id)));
      if (this._selectedProject()?.id === Number(id)) {
        this._selectedProject.set(null);
      }
      return true;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to delete project');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  async createEpic(projectId: number, request: CreateEpicRequest): Promise<boolean> {
    this._loading.set(true);
    this._error.set(null);
    try {
      await firstValueFrom(this.epicService.createEpic(projectId, request));
      await this.refreshSelectedProject();
      return true;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to create epic');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  async createStory(epicId: number, request: CreateStoryRequest): Promise<boolean> {
    this._loading.set(true);
    this._error.set(null);
    try {
      await firstValueFrom(this.epicService.createStory(epicId, request));
      await this.refreshSelectedProject();
      return true;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to create story');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  async createTask(storyId: number, request: CreateTaskRequest): Promise<boolean> {
    this._loading.set(true);
    this._error.set(null);
    try {
      await firstValueFrom(this.epicService.createTask(storyId, request));
      await this.refreshSelectedProject();
      return true;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to create task');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  async updateEpic(epicId: number, request: UpdateEpicRequest): Promise<boolean> {
    this._loading.set(true);
    this._error.set(null);
    try {
      await firstValueFrom(this.epicService.updateEpic(epicId, request));
      await this.refreshSelectedProject();
      return true;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to update epic');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  async deleteEpic(epicId: number): Promise<boolean> {
    this._loading.set(true);
    this._error.set(null);
    try {
      await firstValueFrom(this.epicService.deleteEpic(epicId));
      await this.refreshSelectedProject();
      return true;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to delete epic');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  async updateStory(storyId: number, request: UpdateStoryRequest): Promise<boolean> {
    this._loading.set(true);
    this._error.set(null);
    try {
      await firstValueFrom(this.epicService.updateStory(storyId, request));
      await this.refreshSelectedProject();
      return true;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to update story');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  async deleteStory(storyId: number): Promise<boolean> {
    this._loading.set(true);
    this._error.set(null);
    try {
      await firstValueFrom(this.epicService.deleteStory(storyId));
      await this.refreshSelectedProject();
      return true;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to delete story');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  async updateTask(taskId: number, request: UpdateTaskRequest): Promise<boolean> {
    this._loading.set(true);
    this._error.set(null);
    try {
      await firstValueFrom(this.epicService.updateTask(taskId, request));
      await this.refreshSelectedProject();
      return true;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to update task');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  async deleteTask(taskId: number): Promise<boolean> {
    this._loading.set(true);
    this._error.set(null);
    try {
      await firstValueFrom(this.epicService.deleteTask(taskId));
      await this.refreshSelectedProject();
      return true;
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to delete task');
      return false;
    } finally {
      this._loading.set(false);
    }
  }

  // Helper method to refresh the selected project (e.g., after plan extraction)
  async refreshSelectedProject(): Promise<void> {
    const currentId = this._selectedProject()?.id;
    if (currentId) {
      await this.loadProjectDetail(currentId);
    }
  }

  // Internal state modifiers
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

  clearError() {
    this._error.set(null);
  }
}
