import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { ChatRequest, ChatResponse, Conversation, Message } from '../../../core/models/chat.model';
import { ProjectDetail } from '../../../core/models/project.model';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  constructor(private api: ApiService) {}

  sendMessage(projectId: number | string, request: ChatRequest): Observable<ChatResponse> {
    return this.api.post<ChatResponse>(`/api/v1/projects/${projectId}/chat`, request);
  }

  getConversations(projectId: number | string): Observable<Conversation[]> {
    return this.api.get<Conversation[]>(`/api/v1/projects/${projectId}/conversations`);
  }

  getConversationMessages(
    projectId: number | string,
    conversationId: number | string
  ): Observable<Message[]> {
    return this.api.get<Message[]>(
      `/api/v1/projects/${projectId}/conversations/${conversationId}/messages`
    );
  }

  extractPlan(projectId: number | string): Observable<ProjectDetail> {
    return this.api.post<ProjectDetail>(`/api/v1/projects/${projectId}/extract-plan`, {});
  }
}
