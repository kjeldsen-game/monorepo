import { render, screen } from '@testing-library/react';
import TeamValidationDialog from '../TeamValidationDialog';
import { lineupValidationFalseMock } from 'modules/player/__fixtures__/lineupValidation.fixture';


describe('TeamValidationDialog', () => {
    const handleClose = jest.fn();

    it('renders the correct icon and color for each validation item', () => {
        render(
            <TeamValidationDialog
                open={true}
                handleClose={handleClose}
                teamValidation={lineupValidationFalseMock}
            />
        );

        expect(screen.getByText('Player 1 is valid')).toBeInTheDocument();
        expect(screen.getByText('Player 2 is invalid')).toBeInTheDocument();

        const doneIcons = screen.getAllByTestId('DoneIcon');
        const closeIcons = screen.getAllByTestId('CloseIcon');

        expect(doneIcons.length).toBe(1);
        expect(closeIcons.length).toBe(1);

        const doneIconWrapper = doneIcons[0].parentElement;
        const closeIconWrapper = closeIcons[0].parentElement;

        expect(doneIconWrapper).toHaveStyle('color: green');
        expect(closeIconWrapper).toHaveStyle('color: red');
    });
});
