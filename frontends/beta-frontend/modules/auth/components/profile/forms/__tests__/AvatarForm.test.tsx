import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import AvatarForm from '../AvatarForm';

describe('AvatarForm', () => {
    const handleAvatarChangeMock = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('renders Change picture label and Delete button', () => {
        render(<AvatarForm handleAvatarChange={handleAvatarChangeMock} />);

        const changeLabel = screen.getByText(/change picture/i);
        const deleteButton = screen.getByRole('button');

        expect(changeLabel).toBeInTheDocument();
        expect(changeLabel.tagName.toLowerCase()).toBe('label');

        expect(deleteButton).toBeInTheDocument();
    });


    test('calls handleAvatarChange on file input change', () => {
        render(<AvatarForm handleAvatarChange={handleAvatarChangeMock} />);

        const file = new File(['dummy content'], 'avatar.png', { type: 'image/png' });
        const input = screen.getByTestId('avatar-upload-input');

        fireEvent.change(input, { target: { files: [file] } });

        expect(handleAvatarChangeMock).toHaveBeenCalledTimes(1);
        // Check event argument if you want
        expect(handleAvatarChangeMock.mock.calls[0][0].target.files[0]).toEqual(file);
    });

    test('calls handleAvatarChange on delete button click', () => {
        render(<AvatarForm handleAvatarChange={handleAvatarChangeMock} />);

        // There are two buttons, get the outlined one (delete)
        // Using aria-label or better selector if possible; here fallback to role+name
        const buttons = screen.getAllByRole('button');
        const deleteButton = buttons?.find(btn => btn.querySelector('svg'));

        expect(deleteButton).toBeDefined();

        fireEvent.click(deleteButton!);

        expect(handleAvatarChangeMock).toHaveBeenCalledTimes(1);
        // The event passed here will be a MouseEvent, so no files property on target.files
    });
});
