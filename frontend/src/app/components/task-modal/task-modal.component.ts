import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center p-4">
      <div class="bg-white rounded-lg max-w-md w-full p-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">{{ task ? 'Edit Task' : 'Create Task' }}</h2>
          <button
            (click)="onClose()"
            class="text-gray-400 hover:text-gray-500"
          >
            <span class="sr-only">Close</span>
            <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <form [formGroup]="taskForm" (ngSubmit)="onSubmit()" class="space-y-4">
          <div *ngIf="errorMessage" class="mt-2 text-sm text-red-600">
            {{ errorMessage }}
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">Title</label>
            <input
              type="text"
              formControlName="title"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            />
            <div *ngIf="taskForm.get('title')?.touched && taskForm.get('title')?.invalid" class="mt-1 text-sm text-red-600">
              Title is required
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Description</label>
            <textarea
              formControlName="description"
              rows="3"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            ></textarea>
            <div *ngIf="taskForm.get('description')?.touched && taskForm.get('description')?.invalid" class="mt-1 text-sm text-red-600">
              Description is required
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Priority</label>
            <select
              formControlName="priority"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            >
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Status</label>
            <select
              formControlName="status"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            >
              <option value="PENDING">Pending</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="COMPLETED">Completed</option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Due Date</label>
            <input
              type="date"
              formControlName="due_date"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            />
            <div *ngIf="taskForm.get('due_date')?.touched && taskForm.get('due_date')?.invalid" class="mt-1 text-sm text-red-600">
              Due date is required
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Category (Optional)</label>
            <input
              type="text"
              formControlName="category"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              placeholder="e.g., Work, Personal, Shopping"
            />
          </div>

          <div class="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              (click)="onClose()"
              class="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Cancel
            </button>
            <button
              type="submit"
              [disabled]="taskForm.invalid || isSubmitting"
              class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
            >
              {{ isSubmitting ? 'Saving...' : (task ? 'Update' : 'Create') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `
})
export class TaskModalComponent implements OnInit {
  @Input() task: Task | null = null;
  @Input() category: String | null = null;
  @Input() errorMessage: string | null = null;
  @Input() isSubmitting = false;
  @Output() save = new EventEmitter<any>();
  @Output() close = new EventEmitter<void>();

  taskForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.taskForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      priority: ['MEDIUM', Validators.required],
      status: ['PENDING', Validators.required],
      due_date: ['', Validators.required],
      category: ['']
    });
  }

  ngOnInit(): void {
    if (this.task) {
      this.taskForm.patchValue({
        title: this.task.title,
        description: this.task.description,
        priority: this.task.priority,
        status: this.task.status,
        due_date: this.task.due_date.split('T')[0],
      });
    }

    if(this.category) {
      this.taskForm.patchValue({
        category: this.category
      })
    }
  }

  onSubmit(): void {
    if (this.taskForm.valid) {
      this.save.emit(this.taskForm.value);
    }
  }

  onClose(): void {
    this.close.emit();
  }
}