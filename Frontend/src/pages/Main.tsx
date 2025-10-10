import { useEffect } from "react"
import { getUser } from "../services/User";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../contexts/AuthContext";
import { useContext } from "react";
import { NavBar } from "../components/NavBar";

export const Main = () => {
    const { setAuth } = useContext(AuthContext)!
    const navigate =  useNavigate();

    useEffect(() => {
        const checkUser = async() => {
            const existToken = localStorage.getItem("token");

            console.log(`Access token ${existToken}`);

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
        <div className="min-h-screen bg-gradient-to-br from-indigo-900 via-purple-900 to-black">
            <NavBar />
            {/* offset per AppBar fixed MUI (~64px mobile, ~72px desktop). Usando Toolbar come spacer */}
            <div className="px-4">
                <div style={{ height: 64 }} className="md:hidden" />
                <div style={{ height: 72 }} className="hidden md:block" />
                <div className="flex items-center justify-center">
                    {/* Contenuto principale */}
                </div>
            </div>
        </div>
    )
}