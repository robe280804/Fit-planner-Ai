type RegistrationData = {
    name: string;
    surname: string;
    email: string;
    password: string;
};

export async function registration(formData: RegistrationData) {

    const userData = {
        name: formData.name,
        surname: formData.surname,
        email: formData.email,
        password: formData.password,
    }
    try {
        const response = await fetch("http://localhost:8080/api/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json", 
            },
            body: JSON.stringify(userData),
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || "Errore durante la registrazione");
        }

        return data;

    } catch (error) {
        console.error("Errore nella registrazione:", error);
        throw error;
    }
}

type LoginData = {
    email: string,
    password: string
}


export async function login(formData: LoginData) {
    const userData = {
        email: formData.email,
        password: formData.password
    };

    try {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json", // standard
            },
            body: JSON.stringify(userData),
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || "Errore durante il login");
        }

        return data;

    } catch (error) {
        console.error("Errore nella registrazione:", error);
        throw error;
    }
}
