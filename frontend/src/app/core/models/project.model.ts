export interface Project {
  id: string;
  name: string;
  description?: string;
  createdAt: string;
  updatedAt: string;
  epicCount?: number;
}

export interface ProjectDetail extends Project {
  epics: any[]; // Will be typed as Epic[]
}

export interface CreateProjectRequest {
  name: string;
  description?: string;
}

export interface UpdateProjectRequest {
  name?: string;
  description?: string;
}
