import {
  Component,
  inject,
  ViewChild,
  ElementRef,
  AfterViewChecked,
  Input,
  Output,
  EventEmitter,
  SimpleChanges,
  OnChanges,
  effect,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatStore } from '../../state/chat.store';
import { ProjectStore } from '../../../projects/state/project.store';
import { MessageRole } from '../../../../core/models/chat.model';
import { MarkdownPipe } from '../../../../shared/pipes/markdown.pipe';

@Component({
  selector: 'app-chat-panel',
  standalone: true,
  imports: [CommonModule, MarkdownPipe],
  templateUrl: './chat-panel.html',
  styleUrl: './chat-panel.scss',
})
export class ChatPanelComponent implements OnChanges, AfterViewChecked {
  @ViewChild('messagesContainer') messagesContainer!: ElementRef<HTMLDivElement>;
  
  @Input() projectId: string | null = null;
  @Output() messageSent = new EventEmitter<string>();

  readonly chatStore = inject(ChatStore);
  readonly projectStore = inject(ProjectStore);
  readonly MessageRole = MessageRole;

  private shouldScrollToBottom = false;

  constructor() {
    effect(() => {
      this.chatStore.messages();
      this.shouldScrollToBottom = true;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['projectId'] && this.projectId) {
      this.chatStore.loadConversation(this.projectId);
    }
  }

  ngAfterViewChecked(): void {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  async onSendMessage(message: string): Promise<void> {
    if (!message.trim() || !this.projectId) return;

    // Send to store
    await this.chatStore.sendMessage(this.projectId, message);
    
    // Refresh plan as AI might have updated it
    await this.projectStore.refreshSelectedProject();
    
    this.messageSent.emit(message);
    this.shouldScrollToBottom = true;
  }

  private scrollToBottom(): void {
    try {
      if (this.messagesContainer?.nativeElement) {
        const container = this.messagesContainer.nativeElement;
        container.scrollTop = container.scrollHeight;
      }
    } catch (err) {
      // Ignore scroll errors
    }
  }

  trackByMessageId(index: number, message: { id: string }): string {
    return message.id;
  }
}
