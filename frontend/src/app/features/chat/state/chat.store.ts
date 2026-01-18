import { signal, computed, Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Message, MessageRole } from '../../../core/models/chat.model';
import { ChatService } from '../services/chat.service';
import { ProjectStore } from '../../projects/state/project.store';

@Injectable({
  providedIn: 'root',
})
export class ChatStore {
  private chatService = inject(ChatService);
  private projectStore = inject(ProjectStore);

  // State
  private _messages = signal<Message[]>([]);
  private _conversationId = signal<number | null>(null);
  private _sending = signal<boolean>(false);
  private _error = signal<string | null>(null);

  // Selectors
  readonly messages = computed(() => this._messages());
  readonly conversationId = computed(() => this._conversationId());
  readonly sending = computed(() => this._sending());
  readonly error = computed(() => this._error());

  // Helper to generate unique IDs
  private generateId(): string {
    return `msg-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }

  // Ensure message has an ID
  private ensureMessageId(message?: Message | Partial<Message> | null): Message {
    if (!message) {
      return {
        id: this.generateId(),
        role: MessageRole.ASSISTANT,
        content: '',
        createdAt: new Date().toISOString(),
      };
    }

    return {
      id: message.id ?? this.generateId(),
      role: (message.role as MessageRole) || MessageRole.ASSISTANT,
      content: message.content || '',
      createdAt: message.createdAt || new Date().toISOString(),
    };
  }

  private normalizeMessages(messages: Array<Message | Partial<Message> | null | undefined>): Message[] {
    return messages
      .filter((m): m is Message | Partial<Message> => !!m)
      .map((m) => this.ensureMessageId(m));
  }

  private sortMessages(messages: Message[]): Message[] {
    return [...messages].sort((a, b) => {
      const aTime = a.createdAt ? new Date(a.createdAt).getTime() : null;
      const bTime = b.createdAt ? new Date(b.createdAt).getTime() : null;

      if (aTime !== null && bTime !== null && aTime !== bTime) {
        return aTime - bTime;
      }

      const aId = typeof a.id === 'number' ? a.id : Number(a.id);
      const bId = typeof b.id === 'number' ? b.id : Number(b.id);
      if (!Number.isNaN(aId) && !Number.isNaN(bId) && aId !== bId) {
        return aId - bId;
      }

      return String(a.id ?? '').localeCompare(String(b.id ?? ''));
    });
  }

  private buildMessage(role: MessageRole, content: string): Message {
    return {
      id: this.generateId(),
      role,
      content,
      createdAt: new Date().toISOString(),
    };
  }

  // Actions
  async loadConversation(projectId: number | string): Promise<void> {
    this._error.set(null);
    try {
      const conversations = await firstValueFrom(this.chatService.getConversations(projectId));
      if (conversations && conversations.length > 0) {
        const latestConversation = conversations[0];
        this._conversationId.set(latestConversation.id);

        const orderedConversations = [...conversations].reverse();
        const allMessages = orderedConversations.flatMap((conv) => conv.messages || []);
        const messagesWithIds = this.normalizeMessages(allMessages);
        this._messages.set(this.sortMessages(messagesWithIds));
      } else {
        this._conversationId.set(null);
        this._messages.set([]);
      }
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to load conversation');
      // Keep existing messages on error
    }
  }

  async sendMessage(projectId: number | string, message: string): Promise<void> {
    if (!message.trim()) return;

    this._sending.set(true);
    this._error.set(null);

    const userMessage = this.buildMessage(MessageRole.USER, message);
    this._messages.update((msgs) => [...msgs, userMessage]);

    try {
      const response = await firstValueFrom(
        this.chatService.sendMessage(projectId, {
          message,
          conversationId: this._conversationId() ?? undefined,
        })
      );

      if (response.conversationId) {
        this._conversationId.set(response.conversationId);
      }

      const assistantMessage = this.buildMessage(MessageRole.ASSISTANT, response.assistantMessage);

      this._messages.update((msgs) => this.sortMessages([...msgs, assistantMessage]));

      await this.loadConversation(projectId);
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to send message');
      this._messages.update((msgs) => msgs.filter((m) => m.id !== userMessage.id));
    } finally {
      this._sending.set(false);
    }
  }

  async extractPlan(projectId: number | string): Promise<void> {
    this._sending.set(true);
    this._error.set(null);
    try {
      const projectDetail = await firstValueFrom(this.chatService.extractPlan(projectId));
      // Update the project store with extracted plan
      this.projectStore.setSelectedProject(projectDetail);
    } catch (err) {
      this._error.set(err instanceof Error ? err.message : 'Failed to extract plan');
    } finally {
      this._sending.set(false);
    }
  }

  clearChat(): void {
    this._messages.set([]);
    this._conversationId.set(null);
    this._error.set(null);
  }

  // Internal state modifiers
  setMessages(messages: Message[]): void {
    this._messages.set(this.normalizeMessages(messages));
  }

  setConversationId(id: number | null): void {
    this._conversationId.set(id);
  }

  setSending(sending: boolean): void {
    this._sending.set(sending);
  }

  setError(error: string | null): void {
    this._error.set(error);
  }

  clearError(): void {
    this._error.set(null);
  }
}
