import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import AvatarSection from '../AvatarSection';

// Mock hooks and components
jest.mock('@/shared/contexts/ErrorContext', () => ({
    useError: () => ({
        setError: jest.fn(),
    }),
}));

jest.mock('modules/auth/hooks/useProfile', () => ({
    useProfile: () => ({
        handleChangeAvatar: jest.fn(),
    }),
}));

jest.mock('../forms/AvatarForm', () => ({ handleAvatarChange }: { handleAvatarChange: any }) => (
    <input
        data-testid="avatar-input"
        type="file"
        onChange={handleAvatarChange}
    />
));

describe('AvatarSection', () => {
    const setErrorMock = jest.fn();
    const handleChangeAvatarMock = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();

        // Override mocks to use these spies
        jest.spyOn(require('@/shared/contexts/ErrorContext'), 'useError').mockReturnValue({
            setError: setErrorMock,
        });

        jest.spyOn(require('modules/auth/hooks/useProfile'), 'useProfile').mockReturnValue({
            handleChangeAvatar: handleChangeAvatarMock,
        });
    });

    test('renders avatar with src when avatar prop is provided', () => {
        render(<AvatarSection avatar="base64avatardata" />);
        const avatarImg = screen.getByRole('img', { name: /upload new avatar/i });
        expect(avatarImg).toHaveAttribute('src', 'data:image/jpeg;base64,base64avatardata');
    });

    test('renders avatar without src when avatar prop is not provided', () => {
        render(<AvatarSection />);
        const avatarImg = screen.getByTestId("PersonIcon");
        expect(avatarImg).not.toHaveAttribute('src');
    });

    test('calls handleChangeAvatar on valid file input', () => {
        render(<AvatarSection />);

        const file = new File(['dummy content'], 'avatar.png', { type: 'image/png' });
        const input = screen.getByTestId('avatar-input');

        fireEvent.change(input, { target: { files: [file] } });

        expect(handleChangeAvatarMock).toHaveBeenCalled();
        expect(setErrorMock).not.toHaveBeenCalled();
    });

    test('calls setError on invalid file type', () => {
        render(<AvatarSection />);

        const file = new File(['dummy content'], 'avatar.txt', { type: 'text/plain' });
        const input = screen.getByTestId('avatar-input');

        fireEvent.change(input, { target: { files: [file] } });

        expect(setErrorMock).toHaveBeenCalledWith('Please select a JPG, PNG, or GIF image.');
        expect(handleChangeAvatarMock).not.toHaveBeenCalled();
    });

    test('handles no file case by calling handleChangeAvatar with empty blob', () => {
        render(<AvatarSection />);
        const input = screen.getByTestId('avatar-input');

        fireEvent.change(input, { target: { files: [] } });

        expect(handleChangeAvatarMock).toHaveBeenCalled();
        expect(setErrorMock).not.toHaveBeenCalled();
    });
});
