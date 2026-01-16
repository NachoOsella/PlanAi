import { signal, computed, Injectable, inject } from '@angular/core';
import { Message } from '../../../core/models/chat.model';
import { ChatService } from '../services/chat.service';

@Injectable({
  providedIn: 'root'
})
export class ChatStore {
  private chatService = inject(ChatService);

  // State
  private _messages = signal<Message[]>([]);
  private _conversationId = signal<string | null>(null);
  private _sending = signal<boolean>(false);
  private _error = signal<string | null>(null);

  // Selectors
  readonly messages = computed(() => this._messages());
  readonly conversationId = computed(() => this._conversationId());
  readonly sending = computed(() => this._sending());
  readonly error = computed(() => this._error());

  // Actions
  async loadConversation(projectId: string) {
    // TODO: Implement load conversation history
  }

  async sendMessage(projectId: string, message: string) {
    // TODO: Implement send message and update state
  }

  async extractPlan(projectId: string) {
    // TODO: Implement extract plan and trigger project refresh
  }

  clearChat() {
    this._messages.set([]);
    this._conversationId.set(null);
    this._error.set(null);
  }

  // Internal state modifiers
  setMessages(messages: Message[]) {
    this._messages.set(messages);
  }

  setConversationId(id: string | null) {
    this._conversationId.set(id);
  }

  setSending(sending: boolean) {
    this._sending.set(sending);
  }

  setError(error: string | null) {
    this._error.set(error);
  }
}
