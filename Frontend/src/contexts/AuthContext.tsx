import { createContext } from "react";


// Tipizzazione dell'utente
type User = {
    id: string;
    name: string;
    surname: string;
    username: string;
    email: string;
    provider: string;
    roles: string[];
};

// Tipi del contesto
type AuthContextType = {
    logged: boolean;
    user?: User;
    setAuth: (data: User) => void;
    removeAuth: () => void;
};

//Creo il contesto che sarà di tipo: AuthcontextType o undefined, inizialmente è undefined

export const AuthContext = createContext<AuthContextType | undefined>(undefined);
