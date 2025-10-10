export async function refreshToken() {
    const response = await fetch("http://localhost:8080/api/auth/refresh");

    const data = await response.json();

    console.log(data)

    if (!response.ok){
        throw new Error(data.message || "Errore")
    }

    console.log(data);
}