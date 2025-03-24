import React from 'react';
import { 
    Paper, 
    Typography, 
    List, 
    ListItem, 
    ListItemIcon,
    ListItemText,
    Divider,
    Box,
    Fade
} from '@mui/material';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import LightbulbOutlinedIcon from '@mui/icons-material/LightbulbOutlined';
import MenuBookIcon from '@mui/icons-material/MenuBook';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';

const AnalysisResult = ({ result }) => {
    if (!result) return null;

    return (
        <Fade in={true} timeout={500}>
            <Paper 
                elevation={0} 
                sx={{ 
                    p: 3,
                    mt: 4,
                    borderRadius: 2,
                    backgroundColor: 'white',
                    border: '1px solid rgba(0, 0, 0, 0.1)'
                }}
            >
                <Box sx={{ mb: 3 }}>
                    <Typography 
                        variant="h6" 
                        gutterBottom
                        sx={{ 
                            color: 'primary.main',
                            fontWeight: 600
                        }}
                    >
                        Analysis Summary
                    </Typography>
                    <Typography 
                        variant="body1"
                        sx={{ 
                            backgroundColor: 'rgba(0, 0, 0, 0.02)',
                            p: 2,
                            borderRadius: 1
                        }}
                    >
                        {result.scenarioSummary}
                    </Typography>
                </Box>

                <Divider sx={{ my: 3 }} />

                <Box sx={{ mb: 3 }}>
                    <Typography 
                        variant="h6" 
                        gutterBottom
                        sx={{ color: '#d32f2f', fontWeight: 600 }}
                    >
                        Potential Pitfalls
                    </Typography>
                    <List>
                        {result.potentialPitfalls.map((pitfall, index) => (
                            <ListItem 
                                key={index}
                                sx={{
                                    backgroundColor: index % 2 === 0 ? 'rgba(211, 47, 47, 0.05)' : 'transparent',
                                    borderRadius: 1
                                }}
                            >
                                <ListItemIcon>
                                    <ErrorOutlineIcon color="error" />
                                </ListItemIcon>
                                <ListItemText primary={pitfall} />
                            </ListItem>
                        ))}
                    </List>
                </Box>

                <Box sx={{ mb: 3 }}>
                    <Typography 
                        variant="h6" 
                        gutterBottom
                        sx={{ color: '#2e7d32', fontWeight: 600 }}
                    >
                        Proposed Strategies
                    </Typography>
                    <List>
                        {result.proposedStrategies.map((strategy, index) => (
                            <ListItem 
                                key={index}
                                sx={{
                                    backgroundColor: index % 2 === 0 ? 'rgba(46, 125, 50, 0.05)' : 'transparent',
                                    borderRadius: 1
                                }}
                            >
                                <ListItemIcon>
                                    <LightbulbOutlinedIcon color="success" />
                                </ListItemIcon>
                                <ListItemText primary={strategy} />
                            </ListItem>
                        ))}
                    </List>
                </Box>

                <Box sx={{ mb: 3 }}>
                    <Typography 
                        variant="h6" 
                        gutterBottom
                        sx={{ color: '#1976d2', fontWeight: 600 }}
                    >
                        Recommended Resources
                    </Typography>
                    <List>
                        {result.recommendedResources.map((resource, index) => (
                            <ListItem 
                                key={index}
                                sx={{
                                    backgroundColor: index % 2 === 0 ? 'rgba(25, 118, 210, 0.05)' : 'transparent',
                                    borderRadius: 1
                                }}
                            >
                                <ListItemIcon>
                                    <MenuBookIcon color="primary" />
                                </ListItemIcon>
                                <ListItemText primary={resource} />
                            </ListItem>
                        ))}
                    </List>
                </Box>

                <Divider sx={{ my: 3 }} />

                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <InfoOutlinedIcon color="action" />
                    <Typography 
                        variant="body2" 
                        color="text.secondary"
                        sx={{ fontStyle: 'italic' }}
                    >
                        {result.disclaimer}
                    </Typography>
                </Box>
            </Paper>
        </Fade>
    );
};

export default AnalysisResult; 