import { Route, Routes } from 'react-router-dom'
import './App.css'
import { RegisterPage } from './pages/RegisterPage'
import { LoginPage } from './pages/LoginPage'

function App() {
  
  return (
    <Routes>
      <Route path='/register' element={<RegisterPage />}></Route>
      <Route path='/login' element={<LoginPage />}></Route>
    </Routes>
  )
}

export default App
