export enum Priority {
  HIGH = 'HIGH',
  MEDIUM = 'MEDIUM',
  LOW = 'LOW'
}

export enum Status {
  TODO = 'TODO',
  IN_PROGRESS = 'IN_PROGRESS',
  DONE = 'DONE'
}

export interface Task {
  id: string;
  title: string;
  description?: string;
  status: Status;
  estimatedHours?: number;
  order: number;
}

export interface UserStory {
  id: string;
  title: string;
  asA?: string;
  iWant?: string;
  soThat?: string;
  priority: Priority;
  status: Status;
  order: number;
  tasks: Task[];
}

export interface Epic {
  id: string;
  title: string;
  description?: string;
  priority: Priority;
  status: Status;
  order: number;
  stories: UserStory[];
}

export interface CreateEpicRequest {
  title: string;
  description?: string;
  priority: Priority;
}

export interface UpdateEpicRequest {
  title?: string;
  description?: string;
  priority?: Priority;
  status?: Status;
  order?: number;
}
