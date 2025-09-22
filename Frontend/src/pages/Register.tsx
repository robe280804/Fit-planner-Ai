import { registerFields } from '../components/FormFields';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send'
import GoogleIcon from '@mui/icons-material/Google';
import GitHubIcon from '@mui/icons-material/GitHub';
import React, { useState } from 'react';
import { validateField } from '../components/ValidateFields';
import { registration } from '../services/Authentication';

const socialButtonClasses = "flex items-center gap-3 justify-center py-3 px-6 rounded-lg cursor-pointer text-white font-medium transition-all duration-200 shadow-md hover:shadow-lg";

export const Register = () => {
    const [error, setError] = useState<string[]>([]);
    const [fields, setFields] = useState({
        name: "",
        surname: "",
        email: "",
        password: "",
        comfirmPassword: "",  
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFields({
            ...fields,
            [e.target.id]: e.target.value
        })
    }; 

    const validateInputs = () => {
        const newErr = validateField({email: fields.email, password: fields.password})
        
        if (fields.password !== fields.comfirmPassword){
            newErr.push("Le password non coincidono");
        }

        setError(newErr);
        return newErr.length === 0;
    }

    const handleSubmit = (e: React.ChangeEvent<HTMLFormElement>) => {
        e.preventDefault();

        const isValid: boolean = validateInputs();

        if (!isValid){
            console.log(error);
        }

        console.log(fields);

        const response = registration(fields);
        console.log(response);
        //Valido i field
        //Se sono sbagliati invio un alert
        //Se sono corretti eseguo la fech
        
    }

    return (
        <div className='h-screen w-screen flex items-center justify-center bg-gradient-to-br from-gray-900 via-gray-800 to-black'>
            <div className='relative bg-white rounded-2xl shadow-2xl w-full max-w-md p-10'>
                
                <h1 className='text-3xl font-bold text-center mb-8 text-gray-800'>Registrati a Fit Planner Ai</h1>
                
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
                <div className={`${socialButtonClasses} bg-red-500 hover:bg-red-600 mt-6`}>
                    <GoogleIcon />
                    <span>Accedi con Google</span>
                </div>

                {/* Pulsante GitHub */}
                <div className={`${socialButtonClasses} bg-gray-800 hover:bg-gray-900 mt-4`}>
                    <GitHubIcon />
                    <span>Accedi con GitHub</span>
                </div>

                <p className='mt-6 text-center text-gray-600'>
                    Sei già registrato? <a href='/login' className='text-blue-600 hover:underline'>Accedi qui</a>
                </p>

            </div>
        </div>
    )
}