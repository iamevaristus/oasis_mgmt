import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TaskService } from '../../services/task.service';
import { Task, TaskCategory } from '../../models/task.model';
import { TaskModalComponent } from '../../components/task-modal/task-modal.component';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, TaskModalComponent, FormsModule],
  template: `
    <div class="min-h-screen bg-gray-100">
      <nav class="bg-white shadow-sm">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex justify-between h-16">
            <div class="flex">
              <div class="flex-shrink-0 flex items-center">
                <h1 class="text-xl font-bold">Task Manager</h1>
              </div>
            </div>
            <div class="flex items-center">
              <a routerLink="/profile" class="text-gray-700 hover:text-gray-900">Profile</a>
            </div>
          </div>
        </div>
      </nav>

      <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-2xl font-bold text-gray-900">Your Tasks</h2>
          <button
              (click)="openCreateTaskModal()"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Create Task
          </button>
        </div>
        <div class="px-4 py-6 sm:px-0">
          <!-- Search Bar -->
          <div class="search-bar mb-4">
            <input
                type="text"
                class="border rounded-md px-4 py-2 w-full"
                [(ngModel)]="searchQuery"
                (ngModelChange)="searchTasks()"
                placeholder="Search tasks..."
            />
          </div>

          <!-- Category Tabs -->
          <div class="tabs mb-4">
            <div class="flex space-x-4">
              <button
                  *ngFor="let category of taskCategories"
                  (click)="selectCategory(category.id)"
                  [class.bg-indigo-600]="selectedCategory !== null && category.id === selectedCategory.id"
                  [class.text-white]="selectedCategory !== null && category.id === selectedCategory.id"
                  [class.bg-gray-300]="selectedCategory !== null && category.id !== selectedCategory.id"
                  [class.text-black]="selectedCategory !== null && category.id !== selectedCategory.id"
                  class="px-4 py-2 rounded-md text-white font-medium">
                {{ category.title }}
              </button>
            </div>
          </div>

          <!-- Task List -->
          <div class="bg-white shadow overflow-hidden sm:rounded-md">
            <ul role="list" class="divide-y divide-gray-200">
              <li *ngFor="let task of tasks">
                <div class="px-4 py-4 sm:px-6">
                  <div class="flex items-center justify-between">
                    <h3 class="text-lg font-medium text-gray-900">{{ task.title }}</h3>
                    <div class="flex space-x-2">
                      <button
                          (click)="editTask(task)"
                          class="text-indigo-600 hover:text-indigo-900"
                      >
                        Edit
                      </button>
                      <button
                          (click)="deleteTask(task.id)"
                          class="text-red-600 hover:text-red-900"
                      >
                        Delete
                      </button>
                    </div>
                  </div>
                  <div class="mt-2">
                    <p class="text-sm text-gray-600">{{ task.description }}</p>
                    <div class="mt-2 flex items-center text-sm text-gray-500">
                      <span class="mr-2">Priority: {{ task.priority }}</span>
                      <span class="mr-2">Status: {{ task.status }}</span>
                      <span>Due: {{ task.due_date | date }}</span>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </main>
    </div>

    <app-task-modal
        *ngIf="showTaskModal"
        [task]="selectedTask"
        [errorMessage]="errorMessage"
        [isSubmitting]="isSubmitting"
        [category]="selectedTaskCategory"
        (save)="onTaskSave($event)"
        (close)="closeTaskModal()"
    ></app-task-modal>
  `
})
export class DashboardComponent implements OnInit {
  taskCategories: TaskCategory[] = [];
  tasks: Task[] = [];
  selectedCategory: TaskCategory | null = null;
  searchQuery: string = '';
  showTaskModal = false;
  errorMessage: string | null = null;
  isSubmitting = false;
  selectedTask: Task | null = null;
  selectedTaskCategory: string | null = null;

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.loadTaskCategories();
  }

  loadTaskCategories(): void {
    this.taskService.getAllTasks().subscribe({
      next: (categories) => {
        console.log(categories)
        this.taskCategories = categories;
        // Set the first category as selected
        if (this.taskCategories.length > 0) {
          this.selectedCategory = this.taskCategories[0];
          this.tasks = [...this.selectedCategory.tasks];
        }
      },
      error: (error) => {
        console.error('Failed to load task categories:', error);
      }
    });
  }

  selectCategory(categoryId: number): void {
    const selectedCategory = this.taskCategories.find(category => category.id === categoryId);
    if (selectedCategory) {
      this.selectedCategory = selectedCategory;
      this.tasks = this.selectedCategory.tasks;
    }
  }

  searchTasks(): void {
    if (this.searchQuery.trim() !== '') {
      this.taskService.searchTasks(this.searchQuery.trim(), this.selectedCategory?.title).subscribe({
        next: (tasks) => {
          if(tasks.length > 0) {
            this.tasks = tasks;
          } else if(this.selectedCategory) {
            this.tasks = this.selectedCategory.tasks;
          }
        },
        error: (error) => {
          if(this.selectedCategory) {
            this.tasks = this.selectedCategory.tasks;
          }

          console.error('Failed to update task:', error);
        }
      });
    }
  }

  openCreateTaskModal(): void {
    this.selectedTask = null;
    this.showTaskModal = true;
  }

  editTask(task: Task): void {
    this.selectedTask = task;

    if(this.selectedCategory !== null) {
      this.selectedTaskCategory = this.selectedCategory.title;
    }

    this.showTaskModal = true;
  }

  closeTaskModal(): void {
    this.showTaskModal = false;
    this.selectedTask = null;
    this.selectedTaskCategory = null;
  }

  onTaskSave(taskData: any): void {
    this.errorMessage = null;
    this.isSubmitting = true;

    if (this.selectedTask) {
      this.taskService.updateTask(this.selectedTask.id, taskData).subscribe({
        next: () => {
          this.loadTaskCategories();
          this.closeTaskModal();
          this.isSubmitting = false;
        },
        error: (error) => {
          this.isSubmitting = false;
          this.errorMessage = error;
          console.error('Failed to update task:', error);
        }
      });
    } else {
      this.taskService.createTask(taskData).subscribe({
        next: () => {
          this.loadTaskCategories();
          this.closeTaskModal();
          this.isSubmitting = false;
        },
        error: (error) => {
          this.isSubmitting = false;
          this.errorMessage = error;
          console.error('Failed to create task:', error);
        }
      });
    }
  }

  deleteTask(id: number): void {
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          this.loadTaskCategories();
        },
        error: (error) => {
          console.error('Failed to delete task:', error);
        }
      });
    }
  }
}
