import { Component, inject } from '@angular/core';
import { ChatStore } from '../../state/chat.store';

@Component({
  selector: 'app-chat-panel',
  standalone: true,
  imports: [],
  templateUrl: './chat-panel.html',
  styleUrl: './chat-panel.scss',
})
export class ChatPanelComponent {
  readonly chatStore = inject(ChatStore);

  onSendMessage() {
    // TODO: Implement send message logic
  }

  private scrollToBottom() {
    // TODO: Implement automatic scroll to latest message
  }
}
