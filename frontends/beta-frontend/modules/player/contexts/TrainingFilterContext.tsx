import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { SelectChangeEvent } from '@mui/material';
import React, { createContext, useContext } from 'react';

export interface TrainingFilterContextType {
    position: PlayerPosition;
    handlePositionChange: (event: SelectChangeEvent) => void;
}

export const TrainingFilterContext = createContext<TrainingFilterContextType | undefined>(
    undefined,
);

export const useTrainingFilter = (): TrainingFilterContextType => {
    const context = useContext(TrainingFilterContext);
    if (!context) {
        throw new Error('useTrainingFilter must be used within a TrainingFilterProvider');
    }
    return context;
};

interface TrainingFilterProviderProps {
    position: PlayerPosition;
    handlePositionChange: (event) => void;
    children: React.ReactNode;
}

export const TrainingFilterProvider: React.FC<TrainingFilterProviderProps> = ({
    position,
    handlePositionChange,
    children
}) => {
    return (
        <TrainingFilterContext.Provider value={{ position, handlePositionChange }}>
            {children}
        </TrainingFilterContext.Provider>
    );
};
