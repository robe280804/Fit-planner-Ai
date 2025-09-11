import { useState } from "react"
import { registerFields } from "../components/AuthFormField"
import { InputLabel } from "../components/InputLabel"

export const RegisterPage = () => {

    const [formData, setFormData] = useState({
        "username": "",
        "email": "",
        "password": "",
        "confirmPassword": "",
        "coach": false
    });

    const [error, setError] = useState({});

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

        setError(newErrors);

        if (Object.keys(newErrors).length === 0) {
        console.log(formData);
        // Qui invii i dati al backend
    }
    };

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <h1>Registrati a Fit Planner AI</h1>

                {registerFields.map((field) => (
                    <div key={field.id}>
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
                        {error[field.name] && <p>{error[field.name]}</p>}
                    </div>
                ))}

                <button type="submit">Invia</button>
            </form>
        </div>
    )
}