import { Component, Input, Output, EventEmitter, HostListener, ElementRef, AfterViewInit, OnDestroy, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './modal.component.html',
})
export class ModalComponent implements AfterViewInit, OnDestroy {
  @ViewChild('modalContent') modalContent!: ElementRef<HTMLDivElement>;

  @Input() title = 'Modal';
  @Input() showFooter = true;
  @Input() showDelete = false;
  @Input() showSave = true;
  @Input() saveLabel = 'Save';
  @Input() deleteLabel = 'Delete';

  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<void>();
  @Output() delete = new EventEmitter<void>();

  private previouslyFocusedElement: HTMLElement | null = null;
  private focusableElements: HTMLElement[] = [];

  ngAfterViewInit(): void {
    // Store the previously focused element to restore later
    this.previouslyFocusedElement = document.activeElement as HTMLElement;
    
    // Get all focusable elements within the modal
    this.updateFocusableElements();
    
    // Focus the first focusable element
    if (this.focusableElements.length > 0) {
      setTimeout(() => this.focusableElements[0]?.focus(), 0);
    }
  }

  ngOnDestroy(): void {
    // Restore focus to the previously focused element
    if (this.previouslyFocusedElement) {
      this.previouslyFocusedElement.focus();
    }
  }

  @HostListener('document:keydown.escape')
  onEscapeKey(): void {
    this.close.emit();
  }

  @HostListener('document:keydown', ['$event'])
  onKeyDown(event: KeyboardEvent): void {
    if (event.key !== 'Tab') return;
    
    this.updateFocusableElements();
    
    if (this.focusableElements.length === 0) return;

    const firstElement = this.focusableElements[0];
    const lastElement = this.focusableElements[this.focusableElements.length - 1];

    if (event.shiftKey) {
      // Shift + Tab: if on first element, go to last
      if (document.activeElement === firstElement) {
        event.preventDefault();
        lastElement.focus();
      }
    } else {
      // Tab: if on last element, go to first
      if (document.activeElement === lastElement) {
        event.preventDefault();
        firstElement.focus();
      }
    }
  }

  private updateFocusableElements(): void {
    if (!this.modalContent?.nativeElement) return;
    
    const focusableSelectors = [
      'button:not([disabled])',
      'input:not([disabled])',
      'textarea:not([disabled])',
      'select:not([disabled])',
      'a[href]',
      '[tabindex]:not([tabindex="-1"])',
    ].join(', ');

    this.focusableElements = Array.from(
      this.modalContent.nativeElement.querySelectorAll<HTMLElement>(focusableSelectors)
    );
  }

  onBackdropClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.close.emit();
    }
  }
}
