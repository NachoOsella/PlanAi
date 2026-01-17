import {
  Component,
  OnInit,
  inject,
  signal,
  DestroyRef,
  computed,
} from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule, ViewportScroller } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ProjectStore } from '../../../projects/state/project.store';
import { Epic, UserStory, Task, Priority, Status } from '../../../../core/models/epic.model';
import {
  ItemEditData,
  ItemSaveEvent,
  ItemDeleteEvent,
  ItemEditModalComponent,
} from '../../../../shared/components/item-edit-modal/item-edit-modal.component';

@Component({
  selector: 'app-plan-overview',
  standalone: true,
  imports: [CommonModule, RouterLink, ItemEditModalComponent],
  templateUrl: './plan-overview.html',
  styleUrl: './plan-overview.scss',
})
export class PlanOverviewComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);
  private viewportScroller = inject(ViewportScroller);
  readonly projectStore = inject(ProjectStore);

  // Modal State
  readonly editModalData = signal<ItemEditData | null>(null);

  // Computed Data
  readonly epics = computed(() => this.projectStore.selectedProject()?.epics ?? []);

  // Stats for Sidebar
  readonly overallProgress = computed(() => {
    const epics = this.epics();
    const allStories = epics.flatMap(e => e.stories ?? []);
    const allTasks = allStories.flatMap(s => s.tasks ?? []);
    if (!allTasks.length) return 0;
    const done = allTasks.filter(t => t.status === Status.DONE).length;
    return Math.round((done / allTasks.length) * 100);
  });

  ngOnInit(): void {
    this.route.paramMap.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.projectStore.loadProjectDetail(id);
      }
    });
  }

  scrollToEpic(epicId: number): void {
    const elementId = `epic-${epicId}`;
    const element = document.getElementById(elementId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }

  // Creation
  openCreateModal(type: 'epic' | 'story' | 'task', parentId?: number): void {
    const project = this.projectStore.selectedProject();
    if (!project) return;

    let finalParentId = parentId;

    if (type === 'epic') {
        finalParentId = project.id;
    }

    if (!finalParentId && type !== 'epic') {
        console.error('Parent ID required for story/task creation');
        return;
    }

    this.editModalData.set({
      type,
      mode: 'create',
      parentId: finalParentId,
    });
  }

  openEditModal(type: 'epic' | 'story' | 'task', item: Epic | UserStory | Task): void {
    this.editModalData.set({
      type,
      mode: 'edit',
      item,
    });
  }

  closeEditModal(): void {
    this.editModalData.set(null);
  }

  async onSaveItem(event: ItemSaveEvent): Promise<void> {
    const payload = event.data;

    if (event.mode === 'create') {
        const parentId = event.parentId!;
        switch (event.type) {
            case 'epic':
                await this.projectStore.createEpic(parentId, payload as any);
                break;
            case 'story':
                await this.projectStore.createStory(parentId, payload as any);
                break;
            case 'task':
                await this.projectStore.createTask(parentId, payload as any);
                break;
        }
    } else {
        const id = event.id!;
        switch (event.type) {
            case 'epic':
                await this.projectStore.updateEpic(id, payload as any);
                break;
            case 'story':
                await this.projectStore.updateStory(id, payload as any);
                break;
            case 'task':
                await this.projectStore.updateTask(id, payload as any);
                break;
        }
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
    const stories = epic.stories ?? [];
    const tasks = stories.flatMap((story) => story.tasks ?? []);
    if (!tasks.length) return 0;
    const done = tasks.filter((task) => task.status === Status.DONE).length;
    return Math.round((done / tasks.length) * 100);
  }
}

