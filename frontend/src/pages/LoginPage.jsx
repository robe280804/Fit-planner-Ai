import { InputLabel } from "../components/InputLabel"
import { loginField } from "../components/AuthFormField" 
import { useContext, useState } from "react";
import { loginUser } from "../api/AuthService";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../contexts/AuthContext";

export const LoginPage = () => {
    const navigate = useNavigate();
    const {login} = useContext(AuthContext);

    const [formData, setFormData] = useState({
            "email": "",
            "password": "",
        });
    
    //const [error, setError] = useState({});

    const handleChange = (e) => {
        const {name, value} = e.target

        setFormData({
            ...formData,
            [name]: value
        })
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        loginUser(formData)
        .then(loginInfo => {
            console.log(loginInfo)
            localStorage.setItem("token", loginInfo.token)
            login(loginInfo)
            navigate("/")
        })
        .catch(err => {
            alert(err)
        })
    }

    return (
        <div
            className="h-screen w-screen bg-cover bg-center relative"
            style={{ backgroundImage: "url('/images/risen-wang-20jX9b35r_M-unsplash.jpg')" }}
        >
            <form
                className="bg-[#f4f3ee] absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-126 rounded-lg border-2  border-[#0466c8] "
                onSubmit={handleSubmit}
            >
                <h1 className="text-center text-[#0466c8] mt-6 mb-8 font-serif text-2xl font-[700]">Accedi a Fit Planner AI</h1>

                {loginField.map((field) => (
                    <div key={field.id}
                        className="text-center m-10"
                    >
                        <InputLabel
                            htmlFor={field.id}
                            labelName={field.labelName}
                            type={field.type}
                            name={field.name}
                            value={formData[field.name]}
                            checked={(field.type === "checkbox" ? formData[field.name] : undefined)}
                            onChange={handleChange}
                            placeholder={field.placeholder}
                            required={field.required}
                            pattern={field.pattern}
                            error={field.error}
                        />
                        {/**error[field.name] && <p>{error[field.name]}</p>*/}
                    </div>
                ))}

                <button
                    className="absolute right-10 bottom-12 bg-blue-500 hover:bg-blue-400 text-white font-bold py-2 px-4 border-b-4 border-blue-700 hover:border-blue-500 rounded"
                    type="submit"
                >
                    Invia
                </button>

                <p className="absolute text-[14px] left-6 bottom-20 font-serif"> Accedi con
                    <a className="text-blue-500" href="http://localhost:8080/oauth2/authorization/google"> Google </a> 
                    o
                    <a className="text-blue-500" href="http://localhost:8080/oauth2/authorization/github"> GitHub </a></p>

                <p className="absolute left-6 bottom-6 text-[14px] font-serif"> Non hai un account? <a href="/register" className="text-blue-500">clicca qui</a></p>

            </form>
        </div>
    )
}