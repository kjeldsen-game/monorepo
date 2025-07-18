import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import HiddenInput from './HiddenInput';

describe('HiddenInput', () => {
    it('should render a visually hidden input element', () => {
        render(<HiddenInput type="file" data-testid="hidden-file-input" />);

        const input = screen.getByTestId('hidden-file-input');
        expect(input).toBeInTheDocument();
        expect(input).toHaveAttribute('type', 'file');

        const styles = window.getComputedStyle(input);
        expect(styles.position).toBe('absolute');
        expect(styles.width).toBe('1px');
        expect(styles.overflow).toBe('hidden');
    });

    it('should call onChange handler when file selected', () => {
        const handleChange = jest.fn();

        render(<HiddenInput type="file" data-testid="hidden-file-input" onChange={handleChange} />);

        const input = screen.getByTestId('hidden-file-input');

        fireEvent.change(input, {
            target: { files: [new File(['test'], 'test.txt', { type: 'text/plain' })] }
        });

        expect(handleChange).toHaveBeenCalled();
    });
});
