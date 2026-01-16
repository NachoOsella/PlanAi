import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { Project, ProjectDetail, CreateProjectRequest, UpdateProjectRequest } from '../../../core/models/project.model';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  constructor(private api: ApiService) {}

  getProjects(): Observable<Project[]> {
    // TODO: Implement get all projects
    return new Observable<Project[]>();
  }

  getProjectById(id: string): Observable<ProjectDetail> {
    // TODO: Implement get project by id
    return new Observable<ProjectDetail>();
  }

  createProject(request: CreateProjectRequest): Observable<Project> {
    // TODO: Implement create project
    return new Observable<Project>();
  }

  updateProject(id: string, request: UpdateProjectRequest): Observable<Project> {
    // TODO: Implement update project
    return new Observable<Project>();
  }

  deleteProject(id: string): Observable<void> {
    // TODO: Implement delete project
    return new Observable<void>();
  }
}
