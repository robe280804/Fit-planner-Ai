type RegisterFields = {
    id: "name" | "surname" | "email" | "password" | "confirmPassword",
    label: string
    type: string,
    placeholder: string,
    required: boolean  
}

export const registerFields: RegisterFields[] = [
   {
    id: "name",
    label: "Nome",
    type: "text",
    placeholder: "Inserisci il nome",
    required: true,
  },
  {
    id: "surname",
    label: "Cognome",
    type: "text",
    placeholder: "Inserisci il cognome",
    required: true,
  },
  {
    id: "email",
    label: "Email",
    type: "email",
    placeholder: "Inserisci l'email",
    required: true,
  },
  {
    id: "password",
    label: "Password",
    type: "password",
    placeholder: "Insersci la password",
    required: true,
  },
  {
    id: "confirmPassword",
    label: "Conferma la password",
    type: "password",
    placeholder: "Conferma la password",
    required: true,
  },
]

export const loginFields = [
    {
    id: "email",
    label: "Email",
    type: "email",
    placeholder: "Enter your email",
    required: true,
  },
  {
    id: "password",
    label: "Password",
    type: "password",
    placeholder: "Enter your password",
    required: true,
    
  },
]

