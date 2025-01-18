export interface TaskCategory {
  id: number;
  title: string;
  tasks: Task[]
}

export interface Task {
  id: number;
  title: string;
  description: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';
  due_date: string;
  created_at: string;
  updated_at: string;
}

export interface CreateTaskRequest {
  title: string;
  description: string;
  priority: string;
  status: string;
  due_date: string;
  category?: string;
}