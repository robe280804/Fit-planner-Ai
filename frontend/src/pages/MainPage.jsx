import NavBar from "../components/NavBar"

export const MainPage = () => {
    return (
        <div
            className="h-screen w-screen bg-cover bg-center relative"
            style={{ backgroundImage: "url('/images/victor-freitas-Yuv-iwByVRQ-unsplash.jpg')" }}>
            <NavBar />
            < MainSection>
                {/**Varie sezioni come utente, training ...  */}
            </MainSection>
        </div>
    )
}