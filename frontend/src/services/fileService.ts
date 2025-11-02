import api from '@/lib/axios';
import type { Attachment } from '@/types';

export const fileService = {
  uploadFile: async (ticketId: number, file: File): Promise<Attachment> => {
    const formData = new FormData();
    formData.append('ticketId', ticketId.toString());
    formData.append('file', file);

    const response = await api.post('/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  getTicketAttachments: async (ticketId: number): Promise<Attachment[]> => {
    const response = await api.get(`/files/ticket/${ticketId}`);
    return response.data;
  },

  downloadFile: async (attachmentId: number, fileName: string): Promise<void> => {
    const response = await api.get(`/files/download/${attachmentId}`, {
      responseType: 'blob',
    });

    // Create a download link
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', fileName);
    document.body.appendChild(link);
    link.click();
    link.remove();
  },
};
