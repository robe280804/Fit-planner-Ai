import { useContext, useEffect } from "react";
import { AuthContext } from "../contexts/AuthContext";
import { useNavigate } from "react-router-dom";
import { getUser } from "../api/UserService";
import  NavBar from "../components/NavBar";


export const MainPage = () => {
    const navigate = useNavigate()
    const { user } = useContext(AuthContext)
    const { login } = useContext(AuthContext)

    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token == null || token == "") {
            navigate("/login")
        }

        if (user == null) {
            getUser(token)
                .then(userData => {
                    console.log(userData)
                    login(userData)
                }
                )
                .catch(err => {
                    navigate("/login")
                    console.log(err)
                }
            )
        }
    }, [])

    return (
       <div
        className="h-screen w-screen bg-cover bg-center relative"
        style={{ backgroundImage: "url('/images/victor-freitas-Yuv-iwByVRQ-unsplash.jpg')" }}
       >
        <NavBar />
       </div>
    )
    
}