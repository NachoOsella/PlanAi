export enum MessageRole {
  USER = 'USER',
  ASSISTANT = 'ASSISTANT',
  SYSTEM = 'SYSTEM',
}

export interface Message {
  id?: string | number;
  role: MessageRole;
  content: string;
  createdAt?: string;
}

export interface Conversation {
  id: number;
  messages: Message[];
  createdAt?: string;
}

export interface ChatRequest {
  message: string;
  conversationId?: number;
}

export interface ChatResponse {
  conversationId: number;
  userMessage: string;
  assistantMessage: string;
}
