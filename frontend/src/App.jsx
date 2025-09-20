import { Route, Routes } from 'react-router-dom'
import './App.css'
import { RegisterPage } from './pages/RegisterPage'
import { LoginPage } from './pages/LoginPage'
import { MainPage } from './pages/MainPage'
import { NotFoundPage } from './pages/NotFoundPage'

function App() {
  
  return (
    <Routes>
      <Route path='/register' element={<RegisterPage />}></Route>
      <Route path='/login' element={<LoginPage />}></Route>
      <Route path='/' element={<MainPage />}></Route>
      <Route path='*' element={<NotFoundPage />}></Route>
    </Routes>
  )
  
}

export default App
