import { createContext, useState, type ReactNode } from "react";


// Tipizzazione dell'utente
type User = {
    id: string;
    name: string;
    surname: string;
    username: string;
    email: string;
    token: string;
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
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthContextProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [logged, setLogged] = useState<boolean>(false);
    const [user, setUser] = useState<User | undefined>(undefined);

    const setAuth = (userData: User) => {
        setLogged(true);
        setUser(userData);
    }

    const removeAuth = () => {
        setLogged(false);
        setUser(undefined);
    }

    return (
        <AuthContext.Provider value={{logged, user, setAuth, removeAuth}}>
            {children}
        </AuthContext.Provider>
    );
}
