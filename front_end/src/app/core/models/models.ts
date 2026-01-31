export interface User {
    username: string;
    role: 'ADMIN' | 'USER';
    token: string;
}

export interface Destination {
    id?: number;
    country: string;
    capital: string;
    region: string;
    population: number;
    currency: string;
    flagUrl: string;
    isWantToVisit?: boolean;
}

export interface AuthResponse {
    username: string;
    role: 'ADMIN' | 'USER';
    token: string;
}

export interface PaginatedResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}
export interface ApiResponse<T> {
    success: boolean;
    message: string;
    data: T;
}
