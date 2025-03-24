import React, { useState } from 'react';
import { 
  Container, 
  Typography, 
  Box, 
  Alert, 
  Paper,
  ThemeProvider,
  createTheme,
  CssBaseline
} from '@mui/material';
import ScenarioForm from './components/ScenarioForm';
import AnalysisResult from './components/AnalysisResult';
import axios from 'axios';

// Create a custom theme
const theme = createTheme({
  palette: {
    primary: {
      main: '#2196f3',
    },
    secondary: {
      main: '#ff4081',
    },
    background: {
      default: '#f5f5f5',
    }
  },
  typography: {
    h4: {
      fontWeight: 600,
      color: '#1a237e',
    }
  },
  components: {
    MuiPaper: {
      styleOverrides: {
        root: {
          padding: '24px',
          boxShadow: '0 3px 10px rgb(0 0 0 / 0.2)',
        }
      }
    }
  }
});

function App() {
    const [result, setResult] = useState(null);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (data) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axios.post('http://localhost:8081/api/analyze-scenario', data);
            setResult(response.data);
        } catch (err) {
            setError(err.response?.data?.message || 'An error occurred while analyzing the scenario');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Container maxWidth="md">
                <Box sx={{ 
                    my: 6,
                    display: 'flex',
                    flexDirection: 'column',
                    gap: 4
                }}>
                    <Typography 
                        variant="h4" 
                        component="h1" 
                        gutterBottom 
                        align="center"
                        sx={{
                            mb: 4,
                            position: 'relative',
                            '&:after': {
                                content: '""',
                                position: 'absolute',
                                bottom: '-10px',
                                left: '50%',
                                transform: 'translateX(-50%)',
                                width: '60px',
                                height: '4px',
                                backgroundColor: 'primary.main',
                                borderRadius: '2px'
                            }
                        }}
                    >
                        AI Scenario Analysis Tool
                    </Typography>

                    {error && (
                        <Alert 
                            severity="error" 
                            sx={{ 
                                mb: 2,
                                borderRadius: 2
                            }}
                            variant="filled"
                        >
                            {error}
                        </Alert>
                    )}

                    <Paper 
                        elevation={0}
                        sx={{
                            borderRadius: 2,
                            backgroundColor: 'white',
                            transition: 'transform 0.2s',
                            '&:hover': {
                                transform: 'translateY(-4px)'
                            }
                        }}
                    >
                        <ScenarioForm onSubmit={handleSubmit} isLoading={isLoading} />
                    </Paper>

                    {result && (
                        <Paper 
                            elevation={0}
                            sx={{
                                borderRadius: 2,
                                backgroundColor: 'white',
                                mt: 4
                            }}
                        >
                            <AnalysisResult result={result} />
                        </Paper>
                    )}
                </Box>
            </Container>
        </ThemeProvider>
    );
}

export default App; 