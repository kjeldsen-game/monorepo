import { PlayerStats } from '@/data/SamplePlayer';
import router from 'next/router';
import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react';

export interface PlayerContextType {
  selectedPlayer: PlayerStats | null;
  setSelectedPlayer: React.Dispatch<React.SetStateAction<PlayerStats | null>>;
}

export const PlayerContext = createContext<PlayerContextType | undefined>(undefined);

interface PlayerProviderProps {
  children: ReactNode;
}

export const PlayerProvider: React.FC<PlayerProviderProps> = ({ children }) => {
  const [selectedPlayer, setSelectedPlayer] = useState<PlayerStats | null>(null);

  useEffect(() => {
    if (selectedPlayer) {
      console.log('Player state has been updated:', selectedPlayer);
      // Perform your desired action using the updated 'selectedPlayer' state here
      // For example, navigate to the player's page using router.push
      router.push({ pathname: `/player/${selectedPlayer.id}` });
    }
  }, [selectedPlayer]);

  return (
    <PlayerContext.Provider value={{ selectedPlayer, setSelectedPlayer }}>
      {children}
    </PlayerContext.Provider>
  );
};
