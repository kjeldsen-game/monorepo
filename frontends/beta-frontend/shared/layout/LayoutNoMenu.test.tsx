import { render, screen } from '@testing-library/react';
import LayoutNoMenu from './LayoutNoMenu';

describe('LayoutNoMenu', () => {
    it('renders children correctly inside the mocked layout', () => {
        render(
            <LayoutNoMenu>
                <div data-testid="child-element">Hello World</div>
            </LayoutNoMenu>
        );

        expect(screen.getByTestId('child-element')).toBeInTheDocument();
        expect(screen.getByText('Hello World')).toBeInTheDocument();
    });
});
