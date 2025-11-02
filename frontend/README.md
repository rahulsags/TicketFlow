# TicketFlow Frontend

Next.js frontend application for the TicketFlow ticketing system.

## Setup

### 1. Install Dependencies

```bash
npm install
# or
yarn install
```

### 2. Environment Configuration

Create a `.env.local` file:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

### 3. Run Development Server

```bash
npm run dev
# or
yarn dev
```

Open [http://localhost:3000](http://localhost:3000) in your browser.

## Default Users

Login with these default accounts:

- **Admin**: `admin` / `admin123`
- **Support Agent**: `agent` / `agent123`
- **User**: `user` / `user123`

## Features

- User authentication and authorization
- Create and manage support tickets
- Comment on tickets
- Upload file attachments
- Search and filter tickets
- Admin panel for user management
- Support agent dashboard
- Rate ticket resolutions

## Tech Stack

- **Next.js 14** - React framework
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling
- **Zustand** - State management
- **React Hook Form** - Form handling
- **Axios** - HTTP client
- **React Hot Toast** - Notifications

## Build for Production

```bash
npm run build
npm start
```

## Project Structure

```
src/
├── app/              # Next.js pages
│   ├── dashboard/    # Dashboard pages
│   ├── admin/        # Admin pages
│   ├── login/        # Auth pages
│   └── layout.tsx    # Root layout
├── components/       # Reusable components
├── services/         # API services
├── store/            # State management
├── types/            # TypeScript types
└── lib/              # Utilities
```

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm start` - Start production server
- `npm run lint` - Run ESLint

## API Integration

The frontend communicates with the backend API running on `http://localhost:8080/api`.

All API requests include JWT authentication token in the Authorization header.

## Troubleshooting

### Module Not Found

```bash
rm -rf node_modules package-lock.json
npm install
```

### API Connection Issues

- Verify backend is running on port 8080
- Check `.env.local` configuration
- Verify CORS settings in backend
