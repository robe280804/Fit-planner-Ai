export async function getSchedule(token: string) {
    try {
        const response = await fetch("http://localhost:8080/api/training/", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        })

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "Errore lato server")
        }

        console.log("Schede ottenute" + data)
        return data;
        
    } catch (error) {
        console.error("Errore nella visualizzazione delle schede:", error);
        throw error;
    }
}