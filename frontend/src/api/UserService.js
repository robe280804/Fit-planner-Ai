export async function getUser(token) {
    console.log(token)

    const response = await fetch("http://localhost:8080/api/user/", {
        "method": "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        }
    })

    const data = await response.json();

    if (!response.ok){
        throw new Error(data.message || "Errore lato server")
    }

    return data;
}