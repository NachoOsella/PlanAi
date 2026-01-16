import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { Epic, CreateEpicRequest, UpdateEpicRequest } from '../../../core/models/epic.model';

@Injectable({
  providedIn: 'root',
})
export class EpicService {
  constructor(private api: ApiService) {}

  getEpics(projectId: string): Observable<Epic[]> {
    // TODO: Implement get epics for project
    return new Observable<Epic[]>();
  }

  createEpic(projectId: string, request: CreateEpicRequest): Observable<Epic> {
    // TODO: Implement create epic
    return new Observable<Epic>();
  }

  updateEpic(epicId: string, request: UpdateEpicRequest): Observable<Epic> {
    // TODO: Implement update epic
    return new Observable<Epic>();
  }

  deleteEpic(epicId: string): Observable<void> {
    // TODO: Implement delete epic
    return new Observable<void>();
  }
}
