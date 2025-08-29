import { render, screen } from '@testing-library/react';
import QualityText from '../QualityText';

describe('QualityText', () => {
    it('renders children text', () => {
        render(<QualityText textColor="red">High Quality</QualityText>);
        expect(screen.getByText(/high quality/i)).toBeInTheDocument();
    });

    it('applies italic and normal weight styles', () => {
        render(<QualityText textColor="blue">Styled Text</QualityText>);
        const element = screen.getByText(/styled text/i);
        expect(element).toHaveStyle({ fontStyle: 'italic', fontWeight: 'normal' });
    });

    it('applies the provided text color', () => {
        render(<QualityText textColor="#123456">Colored Text</QualityText>);
        const element = screen.getByText(/colored text/i);
        expect(element).toHaveStyle({ color: '#123456' });
    });

    it('renders non-string children (ReactNode)', () => {
        render(
            <QualityText textColor="green">
                <span>Nested Element</span>
            </QualityText>
        );
        expect(screen.getByText(/nested element/i)).toBeInTheDocument();
    });
});
