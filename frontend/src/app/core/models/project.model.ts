import { Epic } from './epic.model';

export interface Project {
  id: number;
  name: string;
  description?: string;
  createdAt?: string;
  updatedAt?: string;
  epicCount?: number;
}

export interface ProjectDetail extends Project {
  epics: Epic[];
}

export interface CreateProjectRequest {
  name: string;
  description?: string;
}

export interface UpdateProjectRequest {
  name?: string;
  description?: string;
}
