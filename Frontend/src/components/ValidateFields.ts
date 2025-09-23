export function validateField (field: { email: string; password: string  }) {
    const error: string[] = [];

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,}$/;

    if (!emailRegex.test(field.email)){
        error.push("Email non valida")
    }

    if (!passwordRegex.test(field.password)){
        error.push("Password deve avere almeno 8 caratteri, 1 maiuscola e 1 numero")
    }

    return  error;
}