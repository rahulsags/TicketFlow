import api from '@/lib/axios';
import type { User, RegisterRequest } from '@/types';

export const userService = {
  getAllUsers: async (): Promise<User[]> => {
    const response = await api.get('/admin/users');
    return response.data;
  },

  getUserById: async (id: number): Promise<User> => {
    const response = await api.get(`/admin/users/${id}`);
    return response.data;
  },

  getSupportAgents: async (): Promise<User[]> => {
    const response = await api.get('/users/support-agents');
    return response.data;
  },

  createUser: async (data: RegisterRequest): Promise<User> => {
    const response = await api.post('/admin/users', data);
    return response.data;
  },

  updateUserRole: async (id: number, role: string): Promise<User> => {
    const response = await api.patch(`/admin/users/${id}/role?role=${role}`);
    return response.data;
  },

  deleteUser: async (id: number): Promise<void> => {
    await api.delete(`/admin/users/${id}`);
  },

  toggleUserStatus: async (id: number): Promise<User> => {
    const response = await api.patch(`/admin/users/${id}/toggle-status`);
    return response.data;
  },
};
