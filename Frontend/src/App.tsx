import { Routes, Route } from 'react-router-dom'
import './App.css'
import { NotFound } from './pages/NotFound'
import { Main } from './pages/Main'
import { Register } from './pages/Register'
import { Login } from './pages/Login'

function App() {

  return (
    <Routes>
      <Route path='*' element={<NotFound />}/>
      <Route path='/' element={<Main />}/>
      <Route path='/register' element={<Register />}/>
      <Route path='/login' element={<Login />}/>
    </Routes>
  )
}

export default App
