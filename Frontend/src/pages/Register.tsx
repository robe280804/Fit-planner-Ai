import { registerFields } from '../components/FormFields';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send'
import GoogleIcon from '@mui/icons-material/Google';
import GitHubIcon from '@mui/icons-material/GitHub';
import React, { useEffect, useState } from 'react';
import { validateField } from '../components/ValidateFields';
import { registration } from '../services/Authentication';
import { useNavigate } from 'react-router-dom';

const socialButtonClasses = "flex items-center gap-3 justify-center py-3 px-6 rounded-lg cursor-pointer text-white font-medium transition-all duration-200 shadow-md hover:shadow-lg";

export const Register = () => {
    const navigate = useNavigate();
    const [error, setError] = useState<string[]>([]);
    const [sendData, setSendData] = useState<boolean>(false);
    const [fields, setFields] = useState({
        name: "",
        surname: "",
        email: "",
        password: "",
        confirmPassword: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFields({
            ...fields,
            [e.target.id]: e.target.value
        })
    };

    const validateInputs = () => {
        const newErr = validateField({ email: fields.email, password: fields.password })

        if (fields.password !== fields.confirmPassword) {
            newErr.push("Le password non coincidono");
        }

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
            const response = await registration(fields);
            console.log("Registrazione avvenuta con successo:", response);
            navigate("/login");

        } catch (error) {
            console.error("Registrazione fallita:", error);

            if (error instanceof Error ){
                setError([error.message]);
            } else {
                setError(["Errore durante la registrazione"]);
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
        <div className='h-screen w-screen flex items-center justify-center bg-gradient-to-br from-gray-900 via-gray-800 to-black'>
            <div className='relative bg-white rounded-2xl shadow-2xl w-full max-w-md p-10'>

                <h1 className='text-3xl font-bold text-center mb-8 text-gray-800'>Registrati a Fit Planner Ai</h1>

                {sendData && error.length > 0 && (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                        <strong className="font-bold"> Errore: </strong>
                        <span className="block sm:inline"> {error} </span>
                    </div>
                )}


                <form
                    onSubmit={handleSubmit}
                    className='flex flex-col gap-4'
                >
                    {registerFields.map((field) => (
                        <TextField
                            key={field.id}
                            id={field.id}
                            label={field.label}
                            type={field.type}
                            placeholder={field.placeholder}
                            required={field.required}
                            value={fields[field.id as keyof typeof fields] || ""}
                            onChange={handleChange}
                            variant="outlined"
                            fullWidth
                            className="bg-gray-50 rounded-lg"
                        />
                    ))}

                    <Button
                        type="submit"
                        variant='contained'
                        endIcon={<SendIcon />}
                        className='mt-4 bg-blue-600 hover:bg-blue-700 shadow-lg'
                        fullWidth
                    >
                        Invia
                    </Button>
                </form>

                {/* Pulsante Google */}
                <a href='http://localhost:8080/api/auth/oauth2/authorize/google'>
                <div className={`${socialButtonClasses} bg-red-500 hover:bg-red-600 mt-6`}>
                    <GoogleIcon />
                    <span>Accedi con Google</span>
                </div>
                </a>

                {/* Pulsante GitHub */}
                <a href='http://localhost:8080/api/auth/oauth2/authorize/github'>
                <div className={`${socialButtonClasses} bg-gray-800 hover:bg-gray-900 mt-4`}>
                    <GitHubIcon />
                    <span>Accedi con GitHub</span>
                </div>
                </a>

                <p className='mt-6 text-center text-gray-600'>
                    Sei già registrato? <a href='/login' className='text-blue-600 hover:underline'>Accedi qui</a>
                </p>

            </div>
        </div>
    )
}