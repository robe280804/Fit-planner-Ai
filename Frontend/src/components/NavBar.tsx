import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import LogoutIcon from '@mui/icons-material/Logout';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const pages = ['Schedule', 'Create'];

export const NavBar = () => {
  const navigate = useNavigate();
  const [anchorElNav, setAnchorElNav] = useState<null | HTMLElement>(null);

  const handleOpenNavMenu = (e: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(e.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
    console.log('Logout');
  };

  return (
    <AppBar
      position="fixed"
      color="transparent"
      sx={{
        boxShadow: 'none',
        background:
          'linear-gradient(90deg, rgba(99,102,241,0.18) 0%, rgba(139,92,246,0.18) 50%, rgba(0,0,0,0.10) 100%)',
        backdropFilter: 'blur(8px)',
        WebkitBackdropFilter: 'blur(8px)',
        borderBottom: '1px solid rgba(255,255,255,0.12)',
        color: 'rgba(255,255,255,0.92)'
      }}
    >
      <Container maxWidth="xl">
        <Toolbar disableGutters sx={{ gap: 2, alignItems: 'center' }}>
          {/* Logo + Brand */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mr: 2 }}>
            <FitnessCenterIcon sx={{ display: { xs: 'none', md: 'inline-flex' } }} />
            <Typography
              variant="h6"
              noWrap
              component="a"
              href="/"
              sx={{
                display: { xs: 'none', md: 'inline-flex' },
                fontWeight: 700,
                letterSpacing: '.12rem',
                color: 'inherit',
                textDecoration: 'none',
              }}
            >
              FIT PLANNER AI
            </Typography>
            {/* Logo compatto per mobile */}
            <FitnessCenterIcon sx={{ display: { xs: 'inline-flex', md: 'none' } }} />
            <Typography
              variant="h6"
              noWrap
              component="a"
              href="/"
              sx={{
                display: { xs: 'inline-flex', md: 'none' },
                fontWeight: 700,
                letterSpacing: '.12rem',
                color: 'inherit',
                textDecoration: 'none',
                ml: 0.5,
              }}
            >
              FPAI
            </Typography>
          </Box>

          {/* Links sempre a sinistra */}
          <Box sx={{ display: { xs: 'none', md: 'flex' }, gap: 1 }}>
            {pages.map((page) => (
              <Button
                key={page}
                onClick={handleCloseNavMenu}
                color="inherit"
                sx={{
                  px: 2,
                  textTransform: 'none',
                  fontWeight: 600,
                  color: 'rgba(255,255,255,0.92)',
                  '&:hover': {
                    backgroundColor: 'rgba(255,255,255,0.08)'
                  }
                }}
              >
                {page}
              </Button>
            ))}
          </Box>

          {/* Hamburger per mobile */}
          <Box sx={{ display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="open navigation"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
              {/* usa icona del logo anche come trigger per coerenza visiva */}
              <FitnessCenterIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{
                display: { xs: 'block', md: 'none' },
                '& .MuiPaper-root': {
                  background:
                    'linear-gradient(180deg, rgba(99,102,241,0.22) 0%, rgba(139,92,246,0.22) 60%, rgba(0,0,0,0.12) 100%)',
                  backdropFilter: 'blur(10px)',
                  WebkitBackdropFilter: 'blur(10px)',
                  border: '1px solid rgba(255,255,255,0.12)'
                }
              }}
            >
              {pages.map((page) => (
                <MenuItem
                  key={page}
                  onClick={handleCloseNavMenu}
                  sx={{
                    color: 'rgba(255,255,255,0.92)',
                    '&:hover': { backgroundColor: 'rgba(255,255,255,0.08)' }
                  }}
                >
                  <Typography sx={{ textAlign: 'left' }}>{page}</Typography>
                </MenuItem>
              ))}
              <MenuItem
                onClick={() => { handleCloseNavMenu(); handleLogout(); }}
                sx={{ color: 'rgba(255,255,255,0.92)', '&:hover': { backgroundColor: 'rgba(255,255,255,0.08)' } }}
              >
                <LogoutIcon fontSize="small" />
                <Typography sx={{ ml: 1 }}>Logout</Typography>
              </MenuItem>
            </Menu>
          </Box>

          {/* Spaziatore per tenere tutto a sinistra */}
          <Box sx={{ flexGrow: 1 }} />

          {/* Logout a destra su desktop per accesso rapido */}
          <Box sx={{ display: { xs: 'none', md: 'flex' }, alignItems: 'center' }}>
            <Tooltip title="Logout">
              <Button
                color="inherit"
                startIcon={<LogoutIcon />}
                onClick={handleLogout}
                sx={{
                  textTransform: 'none',
                  fontWeight: 600,
                  color: 'rgba(255,255,255,0.92)',
                  '&:hover': { backgroundColor: 'rgba(255,255,255,0.08)' }
                }}
              >
                Logout
              </Button>
            </Tooltip>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}