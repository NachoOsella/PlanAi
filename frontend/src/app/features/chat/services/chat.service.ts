import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { ChatRequest, ChatResponse, Message } from '../../../core/models/chat.model';
import { ProjectDetail } from '../../../core/models/project.model';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  constructor(private api: ApiService) {}

  sendMessage(projectId: string, request: ChatRequest): Observable<ChatResponse> {
    // TODO: Implement send message
    return new Observable<ChatResponse>();
  }

  getConversation(projectId: string): Observable<Message[]> {
    // TODO: Implement get conversation history
    return new Observable<Message[]>();
  }

  extractPlan(projectId: string): Observable<ProjectDetail> {
    // TODO: Implement extract structured plan from chat
    return new Observable<ProjectDetail>();
  }
}
