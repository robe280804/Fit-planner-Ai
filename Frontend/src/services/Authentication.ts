type RegistrationData = {
    name: string;
    surname: string;
    email: string;
    password: string;
};

export async function registration (formData: RegistrationData) {

    const userData = {
        name: formData.name,
        surname: formData.surname,
        email: formData.email,
        password: formData.password,
    }

    const response = await fetch("http://localhost:8080/api/register", {
        method: "POST",
        headers: {
            "content-type": "application/json"
        },
        body: JSON.stringify(userData)
    })
    
    const data = await response.json();

    console.log(data);
    console.log(response);
    
}