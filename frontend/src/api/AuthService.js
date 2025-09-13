export async function registerUser (formData) {
    const userData = {
        "userName" : formData.username,
        "email" : formData.email,
        "password" : formData.password,
        "roles" : (formData.coach) ? ["TRAINER"] : ["USER"]
    }
    console.log("dati da inviare ", userData)

    const response = await fetch("http://localhost:8080/api/auth/register", {
        "method" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    })
    const data = await response.json()

    if (!response.ok){
        throw new Error(data.message || "Errore generico")
    }
    return data;
}

export async function loginUser (formData) {
    const userData = {
        "email" : formData.email,
        "password" : formData.password,
    }
    console.log("dati da inviare ", userData)

    const response = await fetch("http://localhost:8080/api/auth/login", {
        "method" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    })
    const data = await response.json()

    if (!response.ok){
        throw new Error(data.message || "Errore generico")
    }
    return data;
}