import { render, screen } from '@testing-library/react';
import MatchEvents from '../MatchEvents';
import { Play } from 'modules/match/types/MatchResponses';

jest.mock('../MatchPossession', () => (props: any) => (
    <div data-testid="match-possesion">{props.possesion.length}</div>
));

describe('MatchEvents', () => {
    it('renders "No content to be displayed" when possessions is empty', () => {
        render(<MatchEvents possessions={[]} />);
        expect(screen.getByText(/No content to be displayed/i)).toBeInTheDocument();
    });

    it('renders "MATCH STARTS" and maps possessions when non-empty', () => {
        const fakePossessions: Play[][] = [
            [{ action: 'PASS' } as Play],
            [{ action: 'SHOOT' } as Play, { action: 'TACKLE' } as Play],
        ];

        render(<MatchEvents possessions={fakePossessions} />);

        // Check MATCH STARTS label
        expect(screen.getByText(/MATCH STARTS/i)).toBeInTheDocument();

        const possesionComponents = screen.getAllByTestId('match-possesion');
        expect(possesionComponents).toHaveLength(fakePossessions.length);

        expect(possesionComponents[0]).toHaveTextContent('1');
        expect(possesionComponents[1]).toHaveTextContent('2');
    });
});
