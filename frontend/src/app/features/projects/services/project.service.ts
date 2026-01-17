import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import {
  Project,
  ProjectDetail,
  CreateProjectRequest,
  UpdateProjectRequest,
} from '../../../core/models/project.model';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  private readonly basePath = '/api/v1/projects';

  constructor(private api: ApiService) {}

  getProjects(): Observable<Project[]> {
    return this.api.get<Project[]>(this.basePath);
  }

  getProjectById(id: number | string): Observable<ProjectDetail> {
    return this.api.get<ProjectDetail>(`${this.basePath}/${id}`);
  }

  createProject(request: CreateProjectRequest): Observable<Project> {
    return this.api.post<Project>(this.basePath, request);
  }

  updateProject(id: number | string, request: UpdateProjectRequest): Observable<Project> {
    return this.api.put<Project>(`${this.basePath}/${id}`, request);
  }

  deleteProject(id: number | string): Observable<void> {
    return this.api.delete<void>(`${this.basePath}/${id}`);
  }
}
