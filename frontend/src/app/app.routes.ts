import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'projects',
    pathMatch: 'full'
  },
  {
    path: 'projects',
    loadComponent: () => import('./features/projects/components/project-list/project-list').then(m => m.ProjectListComponent)
  },
  {
    path: 'projects/new',
    loadComponent: () => import('./features/projects/components/project-create/project-create').then(m => m.ProjectCreateComponent)
  },
  {
    path: 'projects/:id',
    loadComponent: () => import('./features/projects/components/project-detail/project-detail').then(m => m.ProjectDetailComponent)
  },
  {
    path: '**',
    redirectTo: 'projects'
  }
];
