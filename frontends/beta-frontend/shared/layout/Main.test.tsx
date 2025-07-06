import React from 'react';
import { render, screen } from '@testing-library/react';
import { Main } from './Main';

describe('Main component', () => {
    it('renders children inside the Box', () => {
        render(
            <Main>
                <div data-testid="test-child">Hello from child</div>
            </Main>
        );

        const child = screen.getByTestId('test-child');
        expect(child).toBeInTheDocument();
        expect(child).toHaveTextContent('Hello from child');
    });
});
