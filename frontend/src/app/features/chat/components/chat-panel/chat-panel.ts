import {
  Component,
  inject,
  ViewChild,
  ElementRef,
  AfterViewInit,
  Input,
  Output,
  EventEmitter,
  SimpleChanges,
  OnChanges,
  effect,
  signal,
  OnDestroy,
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
export class ChatPanelComponent implements OnChanges, AfterViewInit, OnDestroy {
  @ViewChild('messagesContainer') messagesContainer!: ElementRef<HTMLDivElement>;
  @ViewChild('chatTextarea') chatTextarea!: ElementRef<HTMLTextAreaElement>;
  
  @Input() projectId: string | null = null;
  @Output() messageSent = new EventEmitter<string>();

  readonly chatStore = inject(ChatStore);
  readonly projectStore = inject(ProjectStore);
  readonly MessageRole = MessageRole;
  readonly inputValue = signal('');

  private isNearBottom = true;
  private scrollThreshold = 150; // pixels from bottom to consider "near bottom"
  private isInitialLoad = true;
  private messageCount = 0;
  private scrollDebounceTimer: ReturnType<typeof setTimeout> | null = null;

  constructor() {
    // Watch for message changes and trigger scroll
    effect(() => {
      const messages = this.chatStore.messages();
      const newCount = messages.length;
      
      // Only scroll if messages were added (not on initial empty state)
      if (newCount > this.messageCount && this.messageCount > 0) {
        // New message added - scroll with smooth animation if near bottom
        this.scheduleScroll(true);
      }
      this.messageCount = newCount;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['projectId'] && this.projectId) {
      this.isInitialLoad = true;
      this.messageCount = 0;
      this.chatStore.loadConversation(this.projectId);
    }
  }

  ngAfterViewInit(): void {
    // Set up scroll listener to track if user is near bottom
    if (this.messagesContainer?.nativeElement) {
      this.messagesContainer.nativeElement.addEventListener('scroll', this.onScroll.bind(this));
    }
    
    // Initial scroll after view is ready - use setTimeout to ensure DOM is rendered
    this.scheduleInitialScroll();
  }

  ngOnDestroy(): void {
    if (this.messagesContainer?.nativeElement) {
      this.messagesContainer.nativeElement.removeEventListener('scroll', this.onScroll.bind(this));
    }
    if (this.scrollDebounceTimer) {
      clearTimeout(this.scrollDebounceTimer);
    }
  }

  private onScroll(): void {
    if (!this.messagesContainer?.nativeElement) return;
    
    const container = this.messagesContainer.nativeElement;
    const distanceFromBottom = container.scrollHeight - container.scrollTop - container.clientHeight;
    this.isNearBottom = distanceFromBottom < this.scrollThreshold;
  }

  private scheduleInitialScroll(): void {
    // Wait for Angular to render the messages, then scroll instantly
    setTimeout(() => {
      this.scrollToBottom(false); // Instant scroll on initial load
      this.isInitialLoad = false;
    }, 100);
  }

  private scheduleScroll(smooth: boolean): void {
    // Debounce scroll calls to avoid multiple rapid scrolls
    if (this.scrollDebounceTimer) {
      clearTimeout(this.scrollDebounceTimer);
    }
    
    this.scrollDebounceTimer = setTimeout(() => {
      // Only auto-scroll if user is near the bottom (or it's initial load)
      if (this.isNearBottom || this.isInitialLoad) {
        this.scrollToBottom(smooth && !this.isInitialLoad);
      }
    }, 50);
  }

  onInputChange(event: Event): void {
    const target = event.target as HTMLTextAreaElement;
    this.inputValue.set(target.value);
    this.autoResize(target);
  }

  autoResize(textarea: HTMLTextAreaElement): void {
    textarea.style.height = 'auto';
    const maxHeight = 150;
    textarea.style.height = Math.min(textarea.scrollHeight, maxHeight) + 'px';
  }

  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  async sendMessage(): Promise<void> {
    const message = this.inputValue().trim();
    if (!message || !this.projectId) return;

    // Clear input
    this.inputValue.set('');
    if (this.chatTextarea?.nativeElement) {
      this.chatTextarea.nativeElement.style.height = 'auto';
    }

    // Force scroll to bottom when user sends a message
    this.isNearBottom = true;

    // Send to store
    await this.chatStore.sendMessage(this.projectId, message);
    
    // Refresh plan as AI might have updated it
    await this.projectStore.refreshSelectedProject();
    
    this.messageSent.emit(message);
  }

  async onSendMessage(message: string): Promise<void> {
    if (!message.trim() || !this.projectId) return;

    // Force scroll to bottom when user sends a message
    this.isNearBottom = true;

    // Send to store
    await this.chatStore.sendMessage(this.projectId, message);
    
    // Refresh plan as AI might have updated it
    await this.projectStore.refreshSelectedProject();
    
    this.messageSent.emit(message);
  }

  private scrollToBottom(smooth: boolean = true): void {
    try {
      if (this.messagesContainer?.nativeElement) {
        const container = this.messagesContainer.nativeElement;
        container.scrollTo({
          top: container.scrollHeight,
          behavior: smooth ? 'smooth' : 'instant'
        });
      }
    } catch (err) {
      // Ignore scroll errors
    }
  }

  trackByMessageId(index: number, message: any): string | number {
    return message.id || index;
  }
}
