export interface User {
  id: string;
  first_name: string;
  last_name: string;
  email_address: string;
  access_token: string;
}

export interface SignupRequest {
  first_name: string;
  last_name: string;
  email_address: string;
  password: string;
}

export interface LoginRequest {
  email_address: string;
  password: string;
}