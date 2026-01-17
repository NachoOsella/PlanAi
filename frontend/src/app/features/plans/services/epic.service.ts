import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import {
  Epic,
  UserStory,
  Task,
  CreateEpicRequest,
  UpdateEpicRequest,
  CreateStoryRequest,
  UpdateStoryRequest,
  CreateTaskRequest,
  UpdateTaskRequest,
} from '../../../core/models/epic.model';

@Injectable({
  providedIn: 'root',
})
export class EpicService {
  constructor(private api: ApiService) {}

  // Epic operations
  getProjectEpics(projectId: number): Observable<Epic[]> {
    return this.api.get<Epic[]>(`/api/v1/projects/${projectId}/epics`);
  }

  createEpic(projectId: number, request: CreateEpicRequest): Observable<Epic> {
    return this.api.post<Epic>(`/api/v1/projects/${projectId}/epics`, request);
  }

  updateEpic(epicId: number, request: UpdateEpicRequest): Observable<Epic> {
    return this.api.put<Epic>(`/api/v1/epics/${epicId}`, request);
  }

  deleteEpic(epicId: number): Observable<void> {
    return this.api.delete<void>(`/api/v1/epics/${epicId}`);
  }

  // User Story operations
  getEpicStories(epicId: number): Observable<UserStory[]> {
    return this.api.get<UserStory[]>(`/api/v1/epics/${epicId}/stories`);
  }

  createStory(epicId: number, request: CreateStoryRequest): Observable<UserStory> {
    return this.api.post<UserStory>(`/api/v1/epics/${epicId}/stories`, request);
  }

  updateStory(storyId: number, request: UpdateStoryRequest): Observable<UserStory> {
    return this.api.put<UserStory>(`/api/v1/stories/${storyId}`, request);
  }

  deleteStory(storyId: number): Observable<void> {
    return this.api.delete<void>(`/api/v1/stories/${storyId}`);
  }

  // Task operations
  getStoryTasks(storyId: number): Observable<Task[]> {
    return this.api.get<Task[]>(`/api/v1/stories/${storyId}/tasks`);
  }

  createTask(storyId: number, request: CreateTaskRequest): Observable<Task> {
    return this.api.post<Task>(`/api/v1/stories/${storyId}/tasks`, request);
  }

  updateTask(taskId: number, request: UpdateTaskRequest): Observable<Task> {
    return this.api.put<Task>(`/api/v1/tasks/${taskId}`, request);
  }

  deleteTask(taskId: number): Observable<void> {
    return this.api.delete<void>(`/api/v1/tasks/${taskId}`);
  }
}
