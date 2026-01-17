import { Component, Input, Output, EventEmitter, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './modal.component.html',
})
export class ModalComponent {
  @Input() title = 'Modal';
  @Input() showFooter = true;
  @Input() showDelete = false;
  @Input() showSave = true;
  @Input() saveLabel = 'Save';
  @Input() deleteLabel = 'Delete';

  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<void>();
  @Output() delete = new EventEmitter<void>();

  @HostListener('document:keydown.escape')
  onEscapeKey(): void {
    this.close.emit();
  }

  onBackdropClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.close.emit();
    }
  }
}
