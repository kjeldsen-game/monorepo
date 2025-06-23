import { render, screen } from "@testing-library/react";
import ColHeader from "./ColHeader";

describe('ColHeader', () => {
    it('renders the header text', () => {
        render(<ColHeader header="Test Header" />);
        expect(screen.getByText('Test Header')).toBeInTheDocument();
    });

    it.each([
        ['left', 'padding-left: 20px', 'padding-right: 20px'],
        ['right', 'padding-right: 20px', 'padding-left: 20px'],
        [undefined, '', ''],
    ])(
        'applies correct padding style when align is %s',
        (align, expectedStyle, notExpectedStyle) => {
            render(<ColHeader header="Aligned Header" align={align as any} />);
            const div = screen.getByText('Aligned Header');

            if (expectedStyle) {
                expect(div).toHaveStyle(expectedStyle);
            } else {
                expect(div).not.toHaveStyle('padding-left: 20px');
                expect(div).not.toHaveStyle('padding-right: 20px');
            }

            if (notExpectedStyle) {
                expect(div).not.toHaveStyle(notExpectedStyle);
            }
        }
    );
});