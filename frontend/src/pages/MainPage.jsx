import { useContext, useEffect } from "react";
import NavBar from "../components/NavBar"
import { AuthContext } from "../contexts/AuthContext";
import { useNavigate } from "react-router-dom";
import { getUser } from "../api/UserService"

export const MainPage = () => {
    const navigate = useNavigate()
    const {user} = useContext(AuthContext)
    const {login} = useContext(AuthContext)

    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token == null || token == ""){
            navigate("/login")
        }

        if (user == null){
            getUser(token)
            .then(userData => {
                login(userData)}
            )
            .catch(err => console.log(err))
        }

        console.log(user)
    }, [])
    
    return (
        <div
            className="h-screen w-screen bg-cover bg-center relative"
            style={{ backgroundImage: "url('/images/victor-freitas-Yuv-iwByVRQ-unsplash.jpg')" }}>
            <NavBar />
            
        </div>
    )
}