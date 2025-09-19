import { useState } from "react"
import { registerFields } from "../components/AuthFormField"
import { InputLabel } from "../components/InputLabel"
import { registerUser } from "../api/AuthService"
import { useNavigate } from "react-router-dom"


export const RegisterPage = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        "username": "",
        "email": "",
        "password": "",
        "confirmPassword": "",
        "coach": false
    });

    //const [error, setError] = useState({});

    const handleChange = (e) => {
        const { name, type, value, checked } = e.target;
        setFormData({
            ...formData,
            [name]: type === "checkbox" ? checked : value,
        });
    };

    const validateField = (field) => {
        if (field.required && !formData[field.name]) {
            return "Campo obbligatorio";
        }
        if (field.pattern) {
            const regex = new RegExp(field.pattern);
            if (!regex.test(formData[field.name])) {
                return field.errorMessage;
            }
        }
        if (field.name === "confirmPassword" && formData.confirmPassword !== formData.password) {
            return field.errorMessage;
        }
        return "";
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const newErrors = {};
        registerFields.forEach((field) => {
            const error = validateField(field);
            if (error) newErrors[field.name] = error;
        });

        //setError(newErrors);

        if (Object.keys(newErrors).length === 0) {

            registerUser(formData)
                .then(userInfo => {
                    console.log(userInfo)
                    navigate("/login")})
                .catch(err => {
                    alert(err.message)
                    console.log(err)
                })
        }
    };

    return (
        <div
            className="h-screen w-screen bg-[url(/images/risen-wang-20jX9b35r_M-unsplash.jpg)] bg-cover bg-scroll bg-center relative"
        >
            <form
                className="bg-[#f4f3ee] absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-126 rounded-lg border-2 border-[#0466c8]"
                onSubmit={handleSubmit}
            >
                <h1 className="text-center text-[#0466c8] mt-6 mb-8 font-serif text-2xl font-[700]">Registrati a Fit Planner AI</h1>

                {registerFields.map((field) => (
                    <div key={field.id}
                        className="text-center m-6"
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

                <p className="absolute text-[14px] left-4 font-serif"> Accedi con
                    <a className="text-blue-500" href="http://localhost:8080/oauth2/authorization/google"> Google </a> 
                    o
                    <a className="text-blue-500" href="http://localhost:8080/oauth2/authorization/github"> GitHub </a></p>

                <p className="absolute left-6 bottom-6 text-[14px] font-serif"> Hai già un account ? <a href="/login" className="text-blue-500">clicca qui</a></p>
            </form>
        </div>
    )
}