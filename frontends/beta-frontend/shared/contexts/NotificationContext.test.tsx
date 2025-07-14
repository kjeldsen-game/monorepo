import { render, screen, fireEvent } from '@testing-library/react';
import { ErrorProvider, useError } from './ErrorContext';
import { NotificationProvider, useNotification } from './NotificationContext';

const TestComponent = () => {
    const { notification, setNotification } = useNotification();

    return (
        <div>
            <p data-testid="notification">{notification}</p>
            <button onClick={() => setNotification('Test Notification')}>Set Notification</button>
            <button onClick={() => setNotification(null)}>Clear Notification</button>
        </div>
    );
};

describe('NotificationContext', () => {
    it('provides initial value', () => {
        render(
            <NotificationProvider>
                <TestComponent />
            </NotificationProvider>
        );
        expect(screen.getByTestId('notification').textContent).toBe('');
    });

    it('updates the error value using setError', () => {
        render(
            <NotificationProvider>
                <TestComponent />
            </NotificationProvider>
        );

        fireEvent.click(screen.getByText('Set Notification'));
        expect(screen.getByTestId('notification').textContent).toBe('Test Notification');

        fireEvent.click(screen.getByText('Clear Notification'));
        expect(screen.getByTestId('notification').textContent).toBe('');
    });
});
