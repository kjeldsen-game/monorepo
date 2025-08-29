import { render, screen } from '@testing-library/react';
import MessageText from '../MessageText';

describe('MessageText', () => {
    it('renders children text', () => {
        render(<MessageText>Hello</MessageText>);
        expect(screen.getByText(/hello/i)).toBeInTheDocument();
    });

    it('applies MainEvent style by default', () => {
        render(<MessageText>Hello</MessageText>);
        const element = screen.getByText(/hello/i);
        expect(element).toHaveStyle({ color: 'black', fontWeight: 'bold' });
    });

    it('applies HomeTeamEvent style when passed', () => {
        render(<MessageText matchEventSide="HomeTeamEvent">Home</MessageText>);
        const element = screen.getByText(/home/i);
        expect(element).toHaveStyle({ color: '#29B6F6', fontWeight: 'bold' });
    });

    it('applies AwayTeamEvent style when passed', () => {
        render(<MessageText matchEventSide="AwayTeamEvent">Away</MessageText>);
        const element = screen.getByText(/away/i);
        expect(element).toHaveStyle({ color: '#A4BC10', fontWeight: 'bold' });
    });
});
