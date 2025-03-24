import React, { useState } from 'react';
import {
    Paper,
    Box,
    TextField,
    Button,
    List,
    ListItem,
    IconButton,
    Typography,
    Tooltip,
    CircularProgress
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import HelpOutlineIcon from '@mui/icons-material/HelpOutline';

const ScenarioForm = ({ onSubmit, isLoading }) => {
    const [scenario, setScenario] = useState('');
    const [constraints, setConstraints] = useState(['']);

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit({
            scenario,
            constraints: constraints.filter(c => c.trim())
        });
    };

    const handleAddConstraint = () => {
        setConstraints([...constraints, '']);
    };

    const handleRemoveConstraint = (index) => {
        setConstraints(constraints.filter((_, i) => i !== index));
    };

    const handleConstraintChange = (index, value) => {
        const newConstraints = [...constraints];
        newConstraints[index] = value;
        setConstraints(newConstraints);
    };

    return (
        <Paper 
            elevation={0} 
            sx={{ 
                p: 3,
                borderRadius: 2,
                backgroundColor: 'white',
                border: '1px solid rgba(0, 0, 0, 0.1)'
            }}
        >
            <Box component="form" onSubmit={handleSubmit}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                    <Typography variant="h6" sx={{ mr: 1 }}>
                        Describe Your Scenario
                    </Typography>
                    <Tooltip title="Describe the situation or problem you want to analyze">
                        <HelpOutlineIcon color="action" sx={{ fontSize: 20 }} />
                    </Tooltip>
                </Box>

                <TextField
                    fullWidth
                    multiline
                    rows={4}
                    label="Scenario"
                    value={scenario}
                    onChange={(e) => setScenario(e.target.value)}
                    sx={{ mb: 3 }}
                    placeholder="Example: I need to develop a mobile app for a small business..."
                />

                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Typography variant="h6" sx={{ mr: 1 }}>
                        Constraints
                    </Typography>
                    <Tooltip title="Add any limitations or requirements">
                        <HelpOutlineIcon color="action" sx={{ fontSize: 20 }} />
                    </Tooltip>
                </Box>

                <List sx={{ mb: 2 }}>
                    {constraints.map((constraint, index) => (
                        <ListItem 
                            key={index} 
                            disableGutters
                            sx={{ 
                                mb: 1,
                                transition: 'all 0.2s',
                                '&:hover': {
                                    transform: 'translateX(5px)'
                                }
                            }}
                        >
                            <TextField
                                fullWidth
                                label={`Constraint ${index + 1}`}
                                value={constraint}
                                onChange={(e) => handleConstraintChange(index, e.target.value)}
                                sx={{ mr: 1 }}
                                placeholder="Example: Budget: $10,000"
                            />
                            <IconButton 
                                onClick={() => handleRemoveConstraint(index)}
                                disabled={constraints.length === 1}
                                color="error"
                                sx={{ opacity: constraints.length === 1 ? 0.5 : 1 }}
                            >
                                <DeleteIcon />
                            </IconButton>
                        </ListItem>
                    ))}
                </List>

                <Box sx={{ display: 'flex', gap: 2, justifyContent: 'space-between' }}>
                    <Button 
                        onClick={handleAddConstraint}
                        startIcon={<AddCircleOutlineIcon />}
                        variant="outlined"
                        color="primary"
                    >
                        Add Constraint
                    </Button>
                    <Button 
                        type="submit"
                        variant="contained"
                        disabled={isLoading || !scenario.trim()}
                        sx={{ 
                            minWidth: 150,
                            position: 'relative'
                        }}
                    >
                        {isLoading ? (
                            <>
                                <CircularProgress 
                                    size={24} 
                                    sx={{ 
                                        position: 'absolute',
                                        left: '50%',
                                        marginLeft: '-12px'
                                    }}
                                />
                                <span style={{ opacity: 0 }}>Analyze</span>
                            </>
                        ) : 'Analyze Scenario'}
                    </Button>
                </Box>
            </Box>
        </Paper>
    );
};

export default ScenarioForm; 