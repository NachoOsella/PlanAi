import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnInit,
  ViewChild,
  ElementRef,
  AfterViewInit,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ModalComponent } from '../modal/modal.component';
import { Epic, UserStory, Task, Priority } from '../../../core/models/epic.model';

export type ItemType = 'epic' | 'story' | 'task';

export interface ItemEditData {
  type: ItemType;
  mode: 'create' | 'edit';
  item?: Epic | UserStory | Task;
  parentId?: number; // projectId for Epic, epicId for Story, storyId for Task
}

export interface ItemSaveEvent {
  type: ItemType;
  mode: 'create' | 'edit';
  id?: number;
  parentId?: number;
  data: Partial<Epic | UserStory | Task>;
}

export interface ItemDeleteEvent {
  type: ItemType;
  id: number;
}

@Component({
  selector: 'app-item-edit-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalComponent],
  templateUrl: './item-edit-modal.component.html',
})
export class ItemEditModalComponent implements OnInit, AfterViewInit {
  @ViewChild('titleInput') titleInput!: ElementRef<HTMLInputElement>;

  @Input() data: ItemEditData | null = null;

  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<ItemSaveEvent>();
  @Output() delete = new EventEmitter<ItemDeleteEvent>();

  priorities = Object.values(Priority);

  formData: Record<string, any> = {
    title: '',
    description: '',
    asA: '',
    iWant: '',
    soThat: '',
    priority: 'MEDIUM',
    estimatedHours: null,
  };

  ngOnInit(): void {
    if (this.data?.item) {
      this.formData = { ...this.data.item };
      if (this.data.type === 'task') {
        delete this.formData['status'];
      }
    } else {
        // Reset form for create mode
        this.formData = {
            title: '',
            description: '',
            asA: '',
            iWant: '',
            soThat: '',
            priority: 'MEDIUM',
            estimatedHours: null,
        };
    }
  }

  ngAfterViewInit(): void {
      setTimeout(() => {
          this.titleInput?.nativeElement?.focus();
      }, 100);
  }

  getTitle(): string {
    if (!this.data) return 'Edit Item';
    const action = this.data.mode === 'create' ? 'Create' : 'Edit';
    const typeLabels: Record<ItemType, string> = {
      epic: 'Epic',
      story: 'User Story',
      task: 'Task',
    };
    return `${action} ${typeLabels[this.data.type]}`;
  }

  onClose(): void {
    this.close.emit();
  }

  onSave(): void {
    if (!this.data) return;
    if (!this.formData['title']) return; // Basic validation

    this.save.emit({
      type: this.data.type,
      mode: this.data.mode,
      id: this.data.item?.id,
      parentId: this.data.parentId,
      data: this.formData,
    });
  }

  onDelete(): void {
    if (!this.data || !this.data.item) return;
    if (confirm(`Are you sure you want to delete this ${this.data.type}?`)) {
      this.delete.emit({
        type: this.data.type,
        id: this.data.item.id,
      });
    }
  }
}
