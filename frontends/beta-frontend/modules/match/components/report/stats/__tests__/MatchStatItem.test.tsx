import { render, screen } from '@testing-library/react';
import MatchStatItem from '../MatchStatItem';

describe('MatchStatItem', () => {
    it('renders the home value, name, and away value correctly', () => {
        render(<MatchStatItem name="Goals" homeValue={3} awayValue={1} />);

        expect(screen.getByText('3')).toBeInTheDocument();
        expect(screen.getByText('Goals')).toBeInTheDocument();
        expect(screen.getByText('1')).toBeInTheDocument();
    });

    it('renders string values correctly', () => {
        render(<MatchStatItem name="Shots" homeValue="5" awayValue="7" />);

        expect(screen.getByText('5')).toBeInTheDocument();
        expect(screen.getByText('Shots')).toBeInTheDocument();
        expect(screen.getByText('7')).toBeInTheDocument();
    });
});
