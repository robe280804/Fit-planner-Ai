
export async function getUser(token: string) {
    try {
        const response = await fetch("http://localhost:8080/api/user/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token,
            }
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || "Errore durante la chiamata")
        }

        return data;
        
    } catch (error) {
        console.error("Errore nel recupero dell'utente:", error);
        throw error;
    }
}