import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import AvatarSection from './AvatarSection';
import { useProfileRepository } from '@/pages/api/profile/useProfileRepository';
import { useSession } from 'next-auth/react';
import { useError } from '@/shared/contexts/ErrorContext';

jest.mock('next-auth/react');
jest.mock('@/pages/api/profile/useProfileRepository');
jest.mock('@/shared/contexts/ErrorContext');

describe('AvatarSection', () => {
    const mockChangeAvatar = jest.fn();
    const mockSetError = jest.fn();

    beforeEach(() => {
        (useSession as jest.Mock).mockReturnValue({
            data: { accessToken: 'fake-token' }
        });

        (useProfileRepository as jest.Mock).mockReturnValue({
            changeAvatar: mockChangeAvatar
        });

        (useError as jest.Mock).mockReturnValue({
            setError: mockSetError
        });

        mockChangeAvatar.mockClear();
        mockSetError.mockClear();
    });

    it('renders avatar if provided', () => {
        render(<AvatarSection avatar="fakeBase64Image" />);
        const avatar = screen.getByAltText('Upload new avatar') as HTMLImageElement;
        expect(avatar).toBeInTheDocument();
        expect(avatar.src).toContain('data:image/jpeg;base64,fakeBase64Image');
    });

    it('renders fallback avatar if not provided', () => {
        render(<AvatarSection />);
        const avatar = screen.getByTestId('PersonIcon');
        expect(avatar).toBeInTheDocument();
    });

    it('calls changeAvatar with uploaded file', async () => {
        render(<AvatarSection />);

        const file = new File(['avatar'], 'avatar.png', { type: 'image/png' });
        const input = screen.getByTestId('avatar-upload-input') as HTMLInputElement;

        fireEvent.change(input, {
            target: { files: [file] }
        });

        await waitFor(() => {
            expect(mockChangeAvatar).toHaveBeenCalled();
        });
    });

    it('calls changeAvatar with empty file on delete click', async () => {
        render(<AvatarSection />);
        const deleteBtn = screen.getByTestId('DeleteIcon');
        fireEvent.click(deleteBtn);

        await waitFor(() => {
            expect(mockChangeAvatar).toHaveBeenCalled();
            const formArg = mockChangeAvatar.mock.calls[0][0];
            expect(formArg instanceof FormData).toBe(true);
            expect(formArg.get('file')).toBeInstanceOf(Blob);
        });
    });

    it('shows error for unsupported file type', async () => {
        render(<AvatarSection />);
        const badFile = new File(['bad'], 'avatar.txt', { type: 'text/plain' });
        const input = screen.getByTestId('avatar-upload-input') as HTMLInputElement;

        fireEvent.change(input, {
            target: { files: [badFile] }
        });

        await waitFor(() => {
            expect(mockSetError).toHaveBeenCalledWith('Please select a JPG, PNG, or GIF image.');
            expect(mockChangeAvatar).not.toHaveBeenCalled();
        });
    });
});
