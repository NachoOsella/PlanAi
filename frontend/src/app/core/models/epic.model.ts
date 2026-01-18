export enum Priority {
  HIGH = 'HIGH',
  MEDIUM = 'MEDIUM',
  LOW = 'LOW',
}

export enum Status {
  TODO = 'TODO',
  IN_PROGRESS = 'IN_PROGRESS',
  DONE = 'DONE',
}

export interface Task {
  id: number;
  title: string;
  description?: string;
  status: Status;
  estimatedHours?: number;
  orderIndex?: number;
}

export interface UserStory {
  id: number;
  title: string;
  asA?: string;
  iWant?: string;
  soThat?: string;
  priority: Priority;
  status: Status;
  orderIndex?: number;
  tasks: Task[];
}

export interface Epic {
  id: number;
  title: string;
  description?: string;
  priority: Priority;
  status: Status;
  orderIndex?: number;
  stories: UserStory[];
}

// Request DTOs
export interface CreateEpicRequest {
  title: string;
  description?: string;
  priority?: Priority;
  status?: Status;
}

export interface UpdateEpicRequest {
  title?: string;
  description?: string;
  priority?: Priority;
  status?: Status;
}

export interface CreateStoryRequest {
  title: string;
  asA?: string;
  iWant?: string;
  soThat?: string;
  priority?: Priority;
  status?: Status;
}

export interface UpdateStoryRequest {
  title?: string;
  asA?: string;
  iWant?: string;
  soThat?: string;
  priority?: Priority;
  status?: Status;
  order?: number;
}

export interface CreateTaskRequest {
  title: string;
  description?: string;
  estimatedHours?: number;
  status?: Status;
}

export interface UpdateTaskRequest {
  title?: string;
  description?: string;
  status?: Status;
  estimatedHours?: number;
  order?: number;
}
