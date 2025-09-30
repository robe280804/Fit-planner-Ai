import { useState, type ReactNode } from "react";
import { AuthContext } from "./AuthContext";

type User = {
    id: string;
    name: string;
    surname: string;
    username: string;
    email: string;
    provider: string;
    roles: string[];
};

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