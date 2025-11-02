export interface User {
  id: number;
  username: string;
  email: string;
  fullName: string;
  role: 'USER' | 'SUPPORT_AGENT' | 'ADMIN';
  enabled: boolean;
  createdAt: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
  role: 'USER' | 'SUPPORT_AGENT' | 'ADMIN';
}

export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  email: string;
  fullName: string;
  role: string;
}

export type TicketStatus = 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED';
export type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';

export interface Ticket {
  id: number;
  subject: string;
  description: string;
  status: TicketStatus;
  priority: Priority;
  createdBy: User;
  assignedTo?: User;
  createdAt: string;
  updatedAt: string;
  resolvedAt?: string;
  closedAt?: string;
  commentCount: number;
  attachmentCount: number;
  rating?: Rating;
}

export interface TicketRequest {
  subject: string;
  description: string;
  priority: Priority;
}

export interface Comment {
  id: number;
  content: string;
  user: User;
  createdAt: string;
}

export interface CommentRequest {
  content: string;
}

export interface Rating {
  id: number;
  stars: number;
  feedback?: string;
  ratedBy: User;
  createdAt: string;
}

export interface RatingRequest {
  stars: number;
  feedback?: string;
}

export interface Attachment {
  id: number;
  fileName: string;
  fileType: string;
  fileSize: number;
  uploadedBy: User;
  uploadedAt: string;
}
