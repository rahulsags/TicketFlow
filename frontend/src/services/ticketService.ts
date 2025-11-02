import api from '@/lib/axios';
import type { 
  Ticket, 
  TicketRequest, 
  TicketStatus, 
  Priority,
  Comment,
  CommentRequest,
  Rating,
  RatingRequest
} from '@/types';

export const ticketService = {
  createTicket: async (data: TicketRequest): Promise<Ticket> => {
    const response = await api.post('/tickets', data);
    return response.data;
  },

  getMyTickets: async (): Promise<Ticket[]> => {
    const response = await api.get('/tickets/my-tickets');
    return response.data;
  },

  getAssignedTickets: async (): Promise<Ticket[]> => {
    const response = await api.get('/tickets/assigned');
    return response.data;
  },

  getAllTickets: async (): Promise<Ticket[]> => {
    const response = await api.get('/tickets/all');
    return response.data;
  },

  getTicketById: async (id: number): Promise<Ticket> => {
    const response = await api.get(`/tickets/${id}`);
    return response.data;
  },

  updateTicketStatus: async (id: number, status: TicketStatus): Promise<Ticket> => {
    const response = await api.patch(`/tickets/${id}/status?status=${status}`);
    return response.data;
  },

  assignTicket: async (id: number, assigneeId: number): Promise<Ticket> => {
    const response = await api.patch(`/tickets/${id}/assign?assigneeId=${assigneeId}`);
    return response.data;
  },

  addComment: async (id: number, data: CommentRequest): Promise<Comment> => {
    const response = await api.post(`/tickets/${id}/comments`, data);
    return response.data;
  },

  getTicketComments: async (id: number): Promise<Comment[]> => {
    const response = await api.get(`/tickets/${id}/comments`);
    return response.data;
  },

  rateTicket: async (id: number, data: RatingRequest): Promise<Rating> => {
    const response = await api.post(`/tickets/${id}/rate`, data);
    return response.data;
  },

  searchTickets: async (
    keyword?: string,
    status?: TicketStatus,
    priority?: Priority
  ): Promise<Ticket[]> => {
    const params = new URLSearchParams();
    if (keyword) params.append('keyword', keyword);
    if (status) params.append('status', status);
    if (priority) params.append('priority', priority);
    
    const response = await api.get(`/tickets/search?${params.toString()}`);
    return response.data;
  },
};
