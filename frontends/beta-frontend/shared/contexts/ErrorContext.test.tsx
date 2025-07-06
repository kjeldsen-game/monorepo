import { render, screen, fireEvent } from '@testing-library/react';
import { ErrorProvider, useError } from './ErrorContext';

const TestComponent = () => {
    const { error, setError } = useError();

    return (
        <div>
            <p data-testid="error">{error}</p>
            <button onClick={() => setError('Test error')}>Set Error</button>
            <button onClick={() => setError(null)}>Clear Error</button>
        </div>
    );
};

describe('ErrorContext', () => {
    it('provides initial value', () => {
        render(
            <ErrorProvider>
                <TestComponent />
            </ErrorProvider>
        );
        expect(screen.getByTestId('error').textContent).toBe('');
    });

    it('updates the error value using setError', () => {
        render(
            <ErrorProvider>
                <TestComponent />
            </ErrorProvider>
        );

        fireEvent.click(screen.getByText('Set Error'));
        expect(screen.getByTestId('error').textContent).toBe('Test error');

        fireEvent.click(screen.getByText('Clear Error'));
        expect(screen.getByTestId('error').textContent).toBe('');
    });
});
