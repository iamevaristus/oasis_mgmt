export interface Response<T> {
  status: string;
  code: number;
  message: string;
  data: T;
}

export class ApiResponse<T> implements Response<T> {
  status: string;
  code: number;
  message: string;
  data: T;

  constructor(status: string, code: number, message: string, data: T) {
    this.status = status;
    this.code = code;
    this.message = message;
    this.data = data;
  }

  // Getter for successful status (between 200 and 299)
  get isSuccessful(): boolean {
    return this.code >= 200 && this.code <= 299;
  }

  // Getter for error status (any 4xx or 5xx status code)
  get isError(): boolean {
    return this.code >= 400 && this.code <= 599;
  }

  // Check if the error data is "O999"
  get isInvalidSession(): boolean {
    return this.data === "O999";
  }

  // Method to create an ApiResponse from a plain JavaScript object (Deserialization)
  static fromJson<T>(json: any): ApiResponse<T> {
    const data = json.data;
    return new ApiResponse<T>(json.status, json.code, json.message, data);
  }

  // Method to convert an ApiResponse instance to a plain JavaScript object (Serialization)
  toJson(): any {
    return {
      "status": this.status,
      "code": this.code,
      "message": this.message,
      "data": this.data
    };
  }
}