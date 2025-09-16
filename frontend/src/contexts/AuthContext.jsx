import { createContext, useState } from "react";

const AuthContext = createContext();

function AuthProvider({children}) {
    const [isLogged, setIsLogged] = useState(false);
    const [user, setUser] = useState(null);

    const login = (data) => {
        
        setUser({
            "idUser": data.id,
            "email": data.email,
            "roles": data.roles,
            "provider": data.provider
        })
        setIsLogged(true)
    }

    const logOut = () => {
        localStorage.removeItem("token")

        setUser(null)
        setIsLogged(false)
    }

    const value = {
        isLogged,
        user,
        login,
        logOut
    }

    return <AuthContext.Provider value={value}> {children} </AuthContext.Provider>
}

export {AuthContext, AuthProvider}