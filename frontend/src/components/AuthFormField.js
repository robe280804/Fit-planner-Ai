export const registerFields = [
  {
    id: "username",
    labelName: "Username",
    type: "text",
    name: "username",
    placeholder: "Inserisci il tuo username",
    required: true,
  },
  {
    id: "email",
    labelName: "Email",
    type: "email",
    name: "email",
    placeholder: "esempio@mail.com",
    required: true,
    pattern: "^[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",
    errorMessage: "Inserisci una email valida"
  },
  {
    id: "password",
    labelName: "Password",
    type: "password",
    name: "password",
    placeholder: "Inserisci la password",
    required: true,
    pattern:  "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+]).{8,}$",
    errorMessage: "La password deve avere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale"
  },
  {
    id: "confirmPassword",
    labelName: "Conferma Password",
    type: "password",
    name: "confirmPassword",
    placeholder: "Conferma la password",
    required: true,
    errorMessage: "Le password devono coincidere",
  },
  {
    id: "coach",
    labelName: "Sei un allenatore?",
    type: "checkbox",
    name: "coach",
    required: false,
  },
];