import React from 'react';
import { render, screen } from '@testing-library/react';
import PlayerSkillText from './PlayerSkillText'; // Adjust path as needed
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { ActualSkill, PlayerSkillRelevance } from '@/shared/models/player/Player';
import useMediaQuery from '@mui/material/useMediaQuery';

jest.mock('@mui/material/useMediaQuery');

const mockSkills: ActualSkill = {
    PlayerSkills: {
        actual: 78,
        potential: 91,
        playerSkillRelevance: PlayerSkillRelevance.CORE
    },
};

const renderWithTheme = (ui: React.ReactElement) => {
    const theme = createTheme();
    return render(<ThemeProvider theme={theme}>{ui}</ThemeProvider>);
};

describe('PlayerSkillText', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('renders nothing if skills is undefined', () => {
        const { container } = renderWithTheme(<PlayerSkillText />);
        expect(container.firstChild).toBeNull();
    });

    it('renders actual and potential skill values', () => {
        (useMediaQuery as jest.Mock).mockReturnValue(false);
        renderWithTheme(<PlayerSkillText skills={mockSkills} />);
        expect(screen.getByText('78')).toBeInTheDocument();
        expect(screen.getByText('91')).toBeInTheDocument();
    });

    // it('shows slash (/) when not on XS screen', () => {
    //     (useMediaQuery as jest.Mock).mockReturnValue(false);
    //     renderWithTheme(<PlayerSkillText skills={mockSkills} />);
    //     expect(screen.getByText('/')).toBeInTheDocument();
    // });

    it('hides slash (/) on XS screen', () => {
        (useMediaQuery as jest.Mock).mockReturnValue(true);
        renderWithTheme(<PlayerSkillText skills={mockSkills} />);
        expect(screen.queryByText('/')).not.toBeInTheDocument();
    });
});
