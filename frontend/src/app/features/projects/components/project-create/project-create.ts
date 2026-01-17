import { Component, inject, ViewChild, ElementRef } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ProjectStore } from '../../state/project.store';

@Component({
  selector: 'app-project-create',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './project-create.html',
  styleUrl: './project-create.scss',
})
export class ProjectCreateComponent {
  @ViewChild('nameInput') nameInput!: ElementRef<HTMLInputElement>;
  @ViewChild('descInput') descInput!: ElementRef<HTMLTextAreaElement>;

  private router = inject(Router);
  private store = inject(ProjectStore);

  async onSubmit(): Promise<void> {
    const name = this.nameInput?.nativeElement?.value?.trim();
    const description = this.descInput?.nativeElement?.value?.trim();

    if (!name) {
      this.nameInput?.nativeElement?.focus();
      return;
    }

    const project = await this.store.createProject({
      name,
      description: description || undefined,
    });

    if (project) {
      this.router.navigate(['/projects', project.id]);
    }
  }

  onCancel(): void {
    this.router.navigate(['/projects']);
  }
}
