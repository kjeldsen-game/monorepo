import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { TrainingFilterContext, TrainingFilterContextType } from '../contexts/TrainingFilterContext';

const mockContextValue: TrainingFilterContextType = {
    position: PlayerPosition.GOALKEEPER,
    handlePositionChange: jest.fn(),
};

const MockTrainingFilterProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    return (
        <TrainingFilterContext.Provider value={mockContextValue}>
            {children}
        </TrainingFilterContext.Provider>
    );
};

export default MockTrainingFilterProvider