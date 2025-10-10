import { loginFields } from '../components/FormFields';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send'
import GoogleIcon from '@mui/icons-material/Google';
import GitHubIcon from '@mui/icons-material/GitHub';
import React, { useEffect, useState } from 'react';
import { validateField } from '../components/ValidateFields';
import { login } from '../services/Authentication';
import { useNavigate } from 'react-router-dom';

export const Login = () => {
    const navigate = useNavigate();
    const [error, setError] = useState<string[]>([]);
    const [sendData, setSendData] = useState<boolean>(false);
    const [fields, setFields] = useState({ 
        email: "",
        password: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFields({
            ...fields,
            [e.target.id]: e.target.value
        })
    };

    const validateInputs = () => {
        const newErr = validateField({ email: fields.email, password: fields.password })

        console.log(newErr)

        setError(newErr);
        return newErr.length === 0;
    }


    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const isValid: boolean = validateInputs();
        setSendData(true);

        if (!isValid) {
            console.error("Validazione fallita", error);
            return;
        }

        try {
            console.log("Dati inviati:", fields);
            const response = await login(fields);
            console.log("Login avvenuto con successo:", response);
            localStorage.setItem("token", response.accessToken)
            navigate("/");

        } catch (error) {
            console.error("Login fallita:", error);

            if (error instanceof Error ){
                setError([error.message]);
            } else {
                setError(["Errore durante il login"]);
            }
        }
    }

    useEffect(() => {
        if (error.length === 0) return;

        const timer = setTimeout(() => {
            setError([]); 
            setSendData(false);
        }, 3000);

        return () => clearTimeout(timer); 
    }, [error]);

    return (
       <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-900 via-purple-900 to-black px-4">
            <div className="relative bg-white/95 backdrop-blur-lg rounded-3xl shadow-2xl w-full max-w-sm p-6 sm:p-8 animate-fadeIn">

                {/* Titolo */}
                <h1 className="text-2xl sm:text-3xl font-extrabold text-center mb-4 text-gray-900">
                    Accedi a <span className="text-indigo-600">Fit Planner AI</span>
                </h1>

                {/* Alert errori */}
                {sendData && error.length > 0 && (
                    <div
                        className="bg-red-100 border border-red-400 text-red-700 px-3 py-2 rounded-lg mb-4 text-sm"
                        role="alert"
                    >
                        <strong className="font-bold">Errore:</strong>
                        <span className="block sm:inline"> {error} </span>
                    </div>
                )}

                {/* Form */}
                <form onSubmit={handleSubmit} className="flex flex-col gap-3">
                    {loginFields.map((field) => (
                        <TextField
                            key={field.id}
                            id={field.id}
                            label={field.label}
                            type={field.type}
                            placeholder={field.placeholder}
                            required={field.required}
                            value={fields[field.id as keyof typeof fields] || ""}
                            onChange={handleChange}
                            fullWidth
                            className="bg-gray-50 rounded-xl border focus:ring-2 focus:ring-indigo-500 py-2.5"
                        />
                    ))}

                    <Button
                        type="submit"
                        variant="contained"
                        endIcon={<SendIcon />}
                        className="mt-3 bg-indigo-600 hover:bg-indigo-700 text-white py-2.5 rounded-xl font-semibold shadow-md transition-all"
                        fullWidth
                    >
                        Accedi
                    </Button>
                </form>

                {/* Divider */}
                <div className="flex items-center my-4">
                    <hr className="flex-grow border-gray-300" />
                    <span className="px-2 text-gray-500 text-xs">oppure</span>
                    <hr className="flex-grow border-gray-300" />
                </div>

                {/* Pulsanti social */}
                <a href="http://localhost:8080/api/auth/oauth2/authorize/google">
                    <div className="flex items-center justify-center gap-2 bg-red-500 hover:bg-red-600 text-white font-semibold py-2.5 rounded-xl shadow-sm transition-all text-sm">
                        <GoogleIcon />
                        <span>Google</span>
                    </div>
                </a>

                <a href="http://localhost:8080/api/auth/oauth2/authorize/github">
                    <div className="flex items-center justify-center gap-2 bg-gray-800 hover:bg-gray-900 text-white font-semibold py-2.5 rounded-xl shadow-sm transition-all mt-3 text-sm">
                        <GitHubIcon />
                        <span>GitHub</span>
                    </div>
                </a>

                {/* Link login */}
                <p className="mt-4 text-center text-gray-600 text-sm">
                    Non sei registrato?{" "}
                    <a href="/register" className="text-indigo-600 hover:underline font-medium">
                        Registrati qui
                    </a>
                </p>
            </div>
        </div>

    )
}