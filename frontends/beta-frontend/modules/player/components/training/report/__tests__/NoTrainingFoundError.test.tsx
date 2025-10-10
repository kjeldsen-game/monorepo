import { render, screen } from '@testing-library/react';
import NoTrainingFoundError from '../NoTrainingFoundError';

describe('NoTrainingFoundError', () => {
    it('renders the icon, title, and subtitle correctly', () => {
        render(<NoTrainingFoundError />);

        expect(
            screen.getByText(/No Training History Found/i)
        ).toBeInTheDocument();

        expect(
            screen.getByText(/It looks like your team hasn't completed any training yet/i)
        ).toBeInTheDocument();

        const icon = screen.getByTestId('AssignmentOutlinedIcon');
        expect(icon).toBeInTheDocument();
    });
});
