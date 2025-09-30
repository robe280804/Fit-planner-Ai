import { useEffect } from "react"
import { getUser } from "../services/User";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../contexts/AuthContext";
import { useContext } from "react";

export const Main = () => {
    const { setAuth } = useContext(AuthContext)!
    const navigate =  useNavigate();
    
    useEffect(() => {
        const checkUser = async() => {
            const existToken = localStorage.getItem("token");

            if (!existToken) {
                navigate("/login");
                return;
            }
            try {
                const data = await getUser(existToken);
                setAuth(data);
                
            } catch (error) {
                console.error("Errore nel recupero utente:", error);
                navigate("/login");
            }
        }

        checkUser();
    }, [])


    return (
        <div>
            
        </div>
    )
}