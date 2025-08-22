import { Player } from '@/shared/models/player/Player';
import React, { createContext, useContext } from 'react';

interface LineupContextType {
    handleEdit: (
        newPlayer: Player,
        oldPlayer?: Player,
        position?: string,
        inactive?: boolean,
    ) => void;
    activePlayer: any;
    edit: boolean;
}

const LineupContext = createContext<LineupContextType | undefined>(
    undefined,
);

export const useLineupEdit = (): LineupContextType => {
    const context = useContext(LineupContext);
    if (!context) {
        throw new Error('useLineupEdit must be used within a LineupProvider');
    }
    return context;
};

interface LineupProviderProps {
    handleEdit: (
        newPlayer: Player,
        oldPlayer?: Player,
        position?: string,
        inactive?: boolean,
    ) => void;
    activePlayer: any;
    edit: boolean;
    children: React.ReactNode;
}

export const LineupProvider: React.FC<LineupProviderProps> = ({
    handleEdit,
    activePlayer,
    children,
    edit,
}) => {
    return (
        <LineupContext.Provider value={{ handleEdit, activePlayer, edit }}>
            {children}
        </LineupContext.Provider>
    );
};
