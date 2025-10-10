import { getSchedule } from "../services/Schedule"
import { useEffect, useState } from "react"

// Interfaccia per gli esercizi
interface Exercise {
    name: string;
    series: number;
    repetition: number;
    weight: number;
    restForSeries: number;
    description?: string;
}

// Interfaccia per i training giornalieri
interface DailyTraining {
    day: string;
    exercises: Exercise[];
    trainingDuration: number;
}

// Interfaccia per le schede di allenamento
interface TrainingCard {
    id: string;
    days: number;
    goals: string[];
    trainingLevel: string;
    type: string[];
    userId: string;
    dailyTrainings: DailyTraining[];
}

export const Schedule = () => {
    const [schedule, setSchedule] = useState<TrainingCard[]>([])
    const [selectedCard, setSelectedCard] = useState<TrainingCard | null>(null)
    const [isModalOpen, setIsModalOpen] = useState(false)

    const openModal = (card: TrainingCard) => {
        setSelectedCard(card)
        setIsModalOpen(true)
    }

    const closeModal = () => {
        setSelectedCard(null)
        setIsModalOpen(false)
    }

    useEffect(() => {
            const checkUser = async() => {
                const existToken = localStorage.getItem("token");

                if (existToken == null){
                    return
                }
    
                try {
                    const data = await getSchedule(existToken);
                    setSchedule(data);
                    console.log(data);
                    
                } catch (error) {
                    console.error("Errore nel recupero delle schede:", error);
                }
            }
    
            checkUser();
        }, [])
    return (
        <div style={{ 
            padding: '40px 20px', 
            backgroundColor: '#f8f9fa', 
            minHeight: '100vh',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center'
        }}>
            {/* Header sezione */}
            <div style={{ 
                textAlign: 'center', 
                marginBottom: '40px',
                maxWidth: '1200px',
                width: '100%'
            }}>
                <h1 style={{ 
                    fontSize: '32px', 
                    fontWeight: 'bold',
                    color: '#2c3e50',
                    margin: '0 0 10px 0',
                    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                    WebkitBackgroundClip: 'text',
                    WebkitTextFillColor: 'transparent',
                    backgroundClip: 'text'
                }}>
                    Le Mie Schede di Allenamento
                </h1>
                <p style={{ 
                    color: '#7f8c8d', 
                    fontSize: '16px', 
                    margin: '0',
                    fontStyle: 'italic'
                }}>
                    Clicca su una scheda per visualizzare tutti i dettagli
                </p>
            </div>
            
            {/* Container delle schede */}
            <div style={{ 
                maxWidth: '1200px', 
                width: '100%'
            }}>
                {!schedule || schedule.length === 0 ? (
                    <div style={{ 
                        textAlign: 'center', 
                        padding: '60px 20px', 
                        backgroundColor: 'white',
                        borderRadius: '15px',
                        boxShadow: '0 4px 15px rgba(0,0,0,0.1)',
                        color: '#7f8c8d',
                        fontSize: '18px'
                    }}>
                        <div style={{ fontSize: '48px', marginBottom: '20px' }}>🏋️‍♂️</div>
                        <p style={{ margin: '0', fontSize: '18px', fontWeight: '500' }}>
                            Nessuna scheda di allenamento trovata
                        </p>
                        <p style={{ margin: '10px 0 0 0', fontSize: '14px', opacity: '0.7' }}>
                            Crea la tua prima scheda per iniziare ad allenarti!
                        </p>
                    </div>
                ) : (
                    <div style={{ 
                        display: 'flex', 
                        flexWrap: 'wrap', 
                        gap: '20px',
                        justifyContent: 'center'
                    }}>
                    {schedule?.map((card) => (
                        <div 
                            key={card.id}
                            onClick={() => openModal(card)}
                            style={{
                                backgroundColor: 'white',
                                borderRadius: '15px',
                                boxShadow: '0 6px 20px rgba(0,0,0,0.08)',
                                padding: '25px',
                                border: '1px solid #e8ecf0',
                                cursor: 'pointer',
                                transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                                position: 'relative',
                                width: '300px',
                                minHeight: '200px',
                                display: 'flex',
                                flexDirection: 'column',
                                justifyContent: 'space-between'
                            }}
                            onMouseEnter={(e) => {
                                e.currentTarget.style.transform = 'translateY(-8px) scale(1.02)'
                                e.currentTarget.style.boxShadow = '0 12px 30px rgba(0,0,0,0.15)'
                                e.currentTarget.style.borderColor = '#667eea'
                            }}
                            onMouseLeave={(e) => {
                                e.currentTarget.style.transform = 'translateY(0) scale(1)'
                                e.currentTarget.style.boxShadow = '0 6px 20px rgba(0,0,0,0.08)'
                                e.currentTarget.style.borderColor = '#e8ecf0'
                            }}
                        >
                            {/* Header della scheda compatta */}
                            <div style={{ 
                                borderBottom: '2px solid #667eea', 
                                paddingBottom: '15px',
                                marginBottom: '15px'
                            }}>
                                <h2 style={{ 
                                    margin: '0 0 12px 0', 
                                    color: '#2c3e50', 
                                    fontSize: '18px',
                                    fontWeight: 'bold',
                                    lineHeight: '1.3'
                                }}>
                                    Scheda {card.id.slice(-8)}
                                </h2>
                                <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
                                    <span style={{ 
                                        background: 'linear-gradient(135deg, #28a745, #20c997)', 
                                        color: 'white', 
                                        padding: '4px 10px', 
                                        borderRadius: '12px',
                                        fontSize: '10px',
                                        fontWeight: '600',
                                        textTransform: 'uppercase',
                                        letterSpacing: '0.5px'
                                    }}>
                                        {card.trainingLevel}
                                    </span>
                                    <span style={{ 
                                        background: 'linear-gradient(135deg, #17a2b8, #6f42c1)', 
                                        color: 'white', 
                                        padding: '4px 10px', 
                                        borderRadius: '12px',
                                        fontSize: '10px',
                                        fontWeight: '600',
                                        textTransform: 'uppercase',
                                        letterSpacing: '0.5px'
                                    }}>
                                        {card.days} giorni
                                    </span>
                                </div>
                            </div>

                            {/* Preview dei giorni e obiettivi */}
                            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                                {/* Giorni */}
                                <div style={{ marginBottom: '15px' }}>
                                    <p style={{ 
                                        margin: '0 0 8px 0', 
                                        color: '#5a6c7d', 
                                        fontSize: '12px',
                                        fontWeight: '600',
                                        textTransform: 'uppercase',
                                        letterSpacing: '0.5px'
                                    }}>
                                        Giorni di allenamento
                                    </p>
                                    <div style={{ display: 'flex', gap: '4px', flexWrap: 'wrap' }}>
                                        {card.dailyTrainings?.slice(0, 4).map((training, index) => (
                                            <span key={index} style={{ 
                                                background: 'linear-gradient(135deg, #667eea, #764ba2)', 
                                                color: 'white', 
                                                padding: '3px 8px', 
                                                borderRadius: '10px',
                                                fontSize: '10px',
                                                fontWeight: '600'
                                            }}>
                                                {training.day.slice(0, 3)}
                                            </span>
                                        ))}
                                        {card.dailyTrainings && card.dailyTrainings.length > 4 && (
                                            <span style={{ 
                                                backgroundColor: '#e9ecef', 
                                                color: '#6c757d', 
                                                padding: '3px 8px', 
                                                borderRadius: '10px',
                                                fontSize: '10px',
                                                fontWeight: '600'
                                            }}>
                                                +{card.dailyTrainings.length - 4}
                                            </span>
                                        )}
                                    </div>
                                </div>

                                {/* Obiettivi e tipi */}
                                <div>
                                    <p style={{ 
                                        margin: '0 0 6px 0', 
                                        color: '#5a6c7d', 
                                        fontSize: '12px',
                                        fontWeight: '600',
                                        textTransform: 'uppercase',
                                        letterSpacing: '0.5px'
                                    }}>
                                        Obiettivi & Tipi
                                    </p>
                                    <div style={{ display: 'flex', gap: '4px', flexWrap: 'wrap' }}>
                                        {card.goals.slice(0, 2).map((goal, index) => (
                                            <span key={index} style={{ 
                                                background: 'linear-gradient(135deg, #ffc107, #fd7e14)', 
                                                color: 'white', 
                                                padding: '3px 8px', 
                                                borderRadius: '10px',
                                                fontSize: '10px',
                                                fontWeight: '600'
                                            }}>
                                                {goal}
                                            </span>
                                        ))}
                                        {card.type.slice(0, 2).map((type, index) => (
                                            <span key={index} style={{ 
                                                background: 'linear-gradient(135deg, #6f42c1, #e83e8c)', 
                                                color: 'white', 
                                                padding: '3px 8px', 
                                                borderRadius: '10px',
                                                fontSize: '10px',
                                                fontWeight: '600'
                                            }}>
                                                {type}
                                            </span>
                                        ))}
                                    </div>
                                </div>
                            </div>

                            {/* Indicatore click */}
                            <div style={{ 
                                position: 'absolute',
                                top: '20px',
                                right: '20px',
                                color: '#667eea',
                                fontSize: '20px',
                                opacity: '0.7',
                                transition: 'all 0.3s ease'
                            }}>
                                👁️
                            </div>
                        </div>
                    ))}
                    </div>
                )}
            </div>

            {/* Modal per visualizzazione completa */}
            {isModalOpen && selectedCard && (
                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    backgroundColor: 'rgba(0, 0, 0, 0.7)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    zIndex: 1000,
                    padding: '20px'
                }}
                onClick={closeModal}
                >
                    <div 
                        style={{
                            backgroundColor: 'white',
                            borderRadius: '15px',
                            boxShadow: '0 20px 40px rgba(0,0,0,0.3)',
                            padding: '30px',
                            width: '100%',
                            maxWidth: '900px',
                            maxHeight: '90vh',
                            overflowY: 'auto',
                            position: 'relative'
                        }}
                        onClick={(e) => e.stopPropagation()}
                    >
                        {/* Header del modal */}
                        <div style={{ 
                            display: 'flex', 
                            justifyContent: 'space-between', 
                            alignItems: 'center',
                            marginBottom: '25px',
                            paddingBottom: '15px',
                            borderBottom: '2px solid #007bff'
                        }}>
                            <h2 style={{ 
                                margin: '0', 
                                color: '#007bff', 
                                fontSize: '24px',
                                fontWeight: 'bold'
                            }}>
                                Scheda di Allenamento {selectedCard.id}
                            </h2>
                            <button 
                                onClick={closeModal}
                                style={{
                                    background: 'none',
                                    border: 'none',
                                    fontSize: '24px',
                                    cursor: 'pointer',
                                    color: '#666',
                                    padding: '5px',
                                    borderRadius: '50%',
                                    width: '35px',
                                    height: '35px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center'
                                }}
                                onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#f5f5f5'}
                                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                            >
                                ✕
                            </button>
                        </div>

                        {/* Badge informativi */}
                        <div style={{ margin: '15px 0 25px 0', display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
                            <span style={{ 
                                backgroundColor: '#28a745', 
                                color: 'white', 
                                padding: '6px 12px', 
                                borderRadius: '6px',
                                fontSize: '13px',
                                fontWeight: 'bold'
                            }}>
                                {selectedCard.trainingLevel}
                            </span>
                            <span style={{ 
                                backgroundColor: '#17a2b8', 
                                color: 'white', 
                                padding: '6px 12px', 
                                borderRadius: '6px',
                                fontSize: '13px',
                                fontWeight: 'bold'
                            }}>
                                {selectedCard.days} giorni
                            </span>
                            {selectedCard.goals.map((goal, index) => (
                                <span key={index} style={{ 
                                    backgroundColor: '#ffc107', 
                                    color: '#333', 
                                    padding: '6px 12px', 
                                    borderRadius: '6px',
                                    fontSize: '13px',
                                    fontWeight: 'bold'
                                }}>
                                    {goal}
                                </span>
                            ))}
                            {selectedCard.type.map((type, index) => (
                                <span key={index} style={{ 
                                    backgroundColor: '#6f42c1', 
                                    color: 'white', 
                                    padding: '6px 12px', 
                                    borderRadius: '6px',
                                    fontSize: '13px',
                                    fontWeight: 'bold'
                                }}>
                                    {type}
                                </span>
                            ))}
                        </div>

                        {/* Programma completo */}
                        <div>
                            <h3 style={{ 
                                color: '#333', 
                                marginBottom: '20px',
                                fontSize: '20px',
                                fontWeight: '600'
                            }}>
                                Programma Settimanale Completo:
                            </h3>
                            
                            {!selectedCard.dailyTrainings || selectedCard.dailyTrainings.length === 0 ? (
                                <p style={{ color: '#999', fontStyle: 'italic' }}>
                                    Nessun training giornaliero in questa scheda
                                </p>
                            ) : (
                                <div style={{ display: 'flex', flexDirection: 'column', gap: '25px' }}>
                                    {selectedCard.dailyTrainings?.map((dailyTraining, dayIndex) => (
                                        <div 
                                            key={dayIndex}
                                            style={{
                                                backgroundColor: '#f8f9fa',
                                                padding: '20px',
                                                borderRadius: '10px',
                                                border: '1px solid #e9ecef',
                                                borderLeft: '4px solid #007bff'
                                            }}
                                        >
                                            <div style={{ 
                                                display: 'flex', 
                                                justifyContent: 'space-between', 
                                                alignItems: 'center',
                                                marginBottom: '15px'
                                            }}>
                                                <h4 style={{ 
                                                    margin: '0', 
                                                    color: '#007bff',
                                                    fontSize: '18px',
                                                    fontWeight: 'bold'
                                                }}>
                                                    {dailyTraining.day}
                                                </h4>
                                                <span style={{ 
                                                    backgroundColor: '#007bff', 
                                                    color: 'white', 
                                                    padding: '6px 12px', 
                                                    borderRadius: '20px',
                                                    fontSize: '12px',
                                                    fontWeight: 'bold'
                                                }}>
                                                    {dailyTraining.trainingDuration} min
                                                </span>
                                            </div>
                                            
                                            {!dailyTraining.exercises || dailyTraining.exercises.length === 0 ? (
                                                <p style={{ color: '#999', fontStyle: 'italic', margin: '0' }}>
                                                    Nessun esercizio per questo giorno
                                                </p>
                                            ) : (
                                                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                                                    {dailyTraining.exercises?.map((exercise, exerciseIndex) => (
                                                        <div 
                                                            key={exerciseIndex}
                                                            style={{
                                                                backgroundColor: 'white',
                                                                padding: '12px',
                                                                borderRadius: '6px',
                                                                border: '1px solid #dee2e6'
                                                            }}
                                                        >
                                                            <h5 style={{ 
                                                                margin: '0 0 8px 0', 
                                                                color: '#333',
                                                                fontSize: '14px',
                                                                fontWeight: '600'
                                                            }}>
                                                                {exerciseIndex + 1}. {exercise.name}
                                                            </h5>
                                                            
                                                            {exercise.description && (
                                                                <p style={{ 
                                                                    margin: '0 0 8px 0', 
                                                                    color: '#666',
                                                                    fontSize: '12px',
                                                                    fontStyle: 'italic'
                                                                }}>
                                                                    {exercise.description}
                                                                </p>
                                                            )}
                                                            
                                                            <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
                                                                <span style={{ 
                                                                    backgroundColor: '#007bff', 
                                                                    color: 'white', 
                                                                    padding: '3px 6px', 
                                                                    borderRadius: '3px',
                                                                    fontSize: '11px',
                                                                    fontWeight: 'bold'
                                                                }}>
                                                                    {exercise.series} serie
                                                                </span>
                                                                
                                                                <span style={{ 
                                                                    backgroundColor: '#28a745', 
                                                                    color: 'white', 
                                                                    padding: '3px 6px', 
                                                                    borderRadius: '3px',
                                                                    fontSize: '11px',
                                                                    fontWeight: 'bold'
                                                                }}>
                                                                    {exercise.repetition} rip
                                                                </span>
                                                                
                                                                <span style={{ 
                                                                    backgroundColor: '#ffc107', 
                                                                    color: '#333', 
                                                                    padding: '3px 6px', 
                                                                    borderRadius: '3px',
                                                                    fontSize: '11px',
                                                                    fontWeight: 'bold'
                                                                }}>
                                                                    {exercise.weight} kg
                                                                </span>
                                                                
                                                                <span style={{ 
                                                                    backgroundColor: '#6c757d', 
                                                                    color: 'white', 
                                                                    padding: '3px 6px', 
                                                                    borderRadius: '3px',
                                                                    fontSize: '11px',
                                                                    fontWeight: 'bold'
                                                                }}>
                                                                    Riposo: {exercise.restForSeries}s
                                                                </span>
                                                            </div>
                                                        </div>
                                                    ))}
                                                </div>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}