import {
  Component,
  OnInit,
  inject,
  signal,
  DestroyRef,
} from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ProjectStore } from '../../state/project.store';
import { ChatStore } from '../../../chat/state/chat.store';
import {
  ItemEditData,
  ItemSaveEvent,
  ItemDeleteEvent,
  ItemEditModalComponent,
} from '../../../../shared/components/item-edit-modal/item-edit-modal.component';
import { Epic, UserStory, Task } from '../../../../core/models/epic.model';
import { ChatPanelComponent } from '../../../chat/components/chat-panel/chat-panel';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, ItemEditModalComponent, ChatPanelComponent],
  templateUrl: './project-detail.html',
  styleUrl: './project-detail.scss',
})
export class ProjectDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);
  readonly projectStore = inject(ProjectStore);
  readonly chatStore = inject(ChatStore);

  readonly editModalData = signal<ItemEditData | null>(null);

  ngOnInit(): void {
    this.route.paramMap.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.projectStore.loadProjectDetail(id);
      }
    });
  }

  async onRefreshPlan(): Promise<void> {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      await this.chatStore.extractPlan(id);
    }
  }

  openEditModal(type: 'epic' | 'story' | 'task', item: Epic | UserStory | Task): void {
    this.editModalData.set({ type, item, mode: 'edit' });
  }

  closeEditModal(): void {
    this.editModalData.set(null);
  }

  async onSaveItem(event: ItemSaveEvent): Promise<void> {
    const payload = event.data;
    if (event.mode === 'create') return; // Not handled here yet

    const id = event.id!;

    switch (event.type) {
      case 'epic':
        await this.projectStore.updateEpic(id, {
          title: (payload as Epic).title,
          description: (payload as Epic).description,
          priority: (payload as Epic).priority,
        });
        break;
      case 'story':
        await this.projectStore.updateStory(id, {
          title: (payload as UserStory).title,
          asA: (payload as UserStory).asA,
          iWant: (payload as UserStory).iWant,
          soThat: (payload as UserStory).soThat,
          priority: (payload as UserStory).priority,
        });
        break;
      case 'task':
        await this.projectStore.updateTask(id, {
          title: (payload as Task).title,
          description: (payload as Task).description,
          estimatedHours: (payload as Task).estimatedHours,
        });
        break;
    }

    this.closeEditModal();
  }

  async onDeleteItem(event: ItemDeleteEvent): Promise<void> {
    switch (event.type) {
      case 'epic':
        await this.projectStore.deleteEpic(event.id);
        break;
      case 'story':
        await this.projectStore.deleteStory(event.id);
        break;
      case 'task':
        await this.projectStore.deleteTask(event.id);
        break;
    }

    this.closeEditModal();
  }

  getEpicProgress(epic: Epic): number {
    const stories = epic.stories || [];
    const tasks = stories.flatMap((story) => story.tasks || []);
    if (tasks.length === 0) return 0;
    const doneCount = tasks.filter((task) => task.status === 'DONE').length;
    return Math.round((doneCount / tasks.length) * 100);
  }
}
