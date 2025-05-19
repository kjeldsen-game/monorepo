import React, { createContext, useContext } from 'react';
import { Player } from '../models/player/Player';

interface LineupEditContextType {
  handleEdit: (
    newPlayer: Player,
    oldPlayer?: Player,
    position?: string,
    inactive?: boolean,
  ) => void;
  activePlayer: any;
  edit: boolean;
}

const LineupEditContext = createContext<LineupEditContextType | undefined>(
  undefined,
);

export const useLineupEdit = (): LineupEditContextType => {
  const context = useContext(LineupEditContext);
  if (!context) {
    throw new Error('useLineupEdit must be used within a LineupEditProvider');
  }
  return context;
};

interface LineupEditProviderProps {
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

export const LineupEditProvider: React.FC<LineupEditProviderProps> = ({
  handleEdit,
  activePlayer,
  children,
  edit,
}) => {
  return (
    <LineupEditContext.Provider value={{ handleEdit, activePlayer, edit }}>
      {children}
    </LineupEditContext.Provider>
  );
};
