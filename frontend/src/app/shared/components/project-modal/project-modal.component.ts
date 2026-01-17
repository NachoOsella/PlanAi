import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ModalComponent } from '../modal/modal.component';
import { Project } from '../../../core/models/project.model';

export interface ProjectSaveEvent {
  id: number;
  name: string;
  description?: string;
}

@Component({
  selector: 'app-project-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalComponent],
  templateUrl: './project-modal.component.html',
})
export class ProjectModalComponent {
  @Input() project: Project | null = null;
  @Input() mode: 'edit' | 'delete' = 'edit';
  @Input() title = 'Edit Project';

  get isDeleteMode(): boolean {
    return this.mode === 'delete';
  }

  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<ProjectSaveEvent>();
  @Output() delete = new EventEmitter<number>();

  formData: Record<string, any> = {
    name: '',
    description: '',
  };

  get showDelete(): boolean {
    return this.mode === 'delete' || this.mode === 'edit';
  }

  ngOnChanges(): void {
    if (this.project) {
      this.formData['name'] = this.project.name;
      this.formData['description'] = this.project.description ?? '';
    }
  }

  onSave(): void {
    if (!this.project) return;
    const name = String(this.formData['name'] ?? '').trim();
    if (!name) return;

    this.save.emit({
      id: this.project.id,
      name,
      description: String(this.formData['description'] ?? '').trim() || undefined,
    });
  }

  onDelete(): void {
    if (!this.project) return;
    this.delete.emit(this.project.id);
  }
}
