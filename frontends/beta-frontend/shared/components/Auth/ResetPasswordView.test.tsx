import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import ResetPasswordView from './ResetPasswordView'
import { useAuth } from 'hooks/useAuth'
import { useSearchParams } from 'next/navigation'

jest.mock('next/navigation', () => ({
    useSearchParams: jest.fn()
}))

jest.mock('hooks/useAuth')

jest.mock('../Common/CustomButton', () => (props: any) => (
    <button onClick={props.onClick}>{props.children}</button>
))

jest.mock('../Common/PasswordTextField', () => (props: any) => (
    <input
        placeholder={props.label}
        name={props.name}
        ref={props.inputRef}
        onChange={props.onChange}
    />
))

describe('ResetPasswordView', () => {
    const mockUseResetPassword = jest.fn()

    beforeEach(() => {
        (useAuth as jest.Mock).mockReturnValue({
            useResetPassword: mockUseResetPassword,
            loading: false,
        })

        const mockSearchParams = {
            get: jest.fn().mockReturnValue('mock-token')
        }
            ; (useSearchParams as jest.Mock).mockReturnValue(mockSearchParams)
    })

    afterEach(() => {
        jest.clearAllMocks()
    })

    it('renders password fields and triggers reset password', async () => {
        render(<ResetPasswordView />)

        const newPasswordInput = screen.getByPlaceholderText('New Password') as HTMLInputElement
        const confirmPasswordInput = screen.getByPlaceholderText('Confirm Password') as HTMLInputElement
        const button = screen.getByText('Reset Password')

        fireEvent.change(newPasswordInput, { target: { value: 'newPass123' } })
        fireEvent.change(confirmPasswordInput, { target: { value: 'newPass123' } })

        fireEvent.click(button)

        await waitFor(() => {
            expect(mockUseResetPassword).toHaveBeenCalledWith({
                token: 'mock-token',
                newPassword: 'newPass123',
                confirmPassword: 'newPass123'
            })
        })
    })

    it('shows loading indicator when loading is true', () => {
        (useAuth as jest.Mock).mockReturnValue({
            useResetPassword: jest.fn(),
            loading: true,
        })

        render(<ResetPasswordView />)

        expect(screen.getByRole('progressbar')).toBeInTheDocument()
    })
})
