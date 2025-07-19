import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import ForgetPasswordView from './ForgetPasswordView'
import { useAuth } from 'hooks/useAuth'

jest.mock('hooks/useAuth')
jest.mock('../Common/CustomButton', () => (props: any) => (
    <button onClick={props.onClick}>{props.children}</button>
))
jest.mock('../Common/CustomTextField', () => (props: any) => (
    <input
        placeholder={props.label}
        name={props.name}
        ref={props.inputRef}
        onChange={props.onChange}
    />
))

describe('ForgetPasswordView', () => {
    const mockUseForgetPassword = jest.fn()

    beforeEach(() => {
        (useAuth as jest.Mock).mockReturnValue({
            useForgetPassword: mockUseForgetPassword,
            loading: false,
        })
    })

    afterEach(() => {
        jest.clearAllMocks()
    })

    it('renders form and submits email correctly', async () => {
        render(<ForgetPasswordView />)

        const emailInput = screen.getByPlaceholderText('Email') as HTMLInputElement
        const button = screen.getByText('Send Reset Link')

        fireEvent.change(emailInput, { target: { value: 'user@example.com' } })
        fireEvent.click(button)

        await waitFor(() => {
            expect(mockUseForgetPassword).toHaveBeenCalledWith({
                email: 'user@example.com',
            })
        })
    })

    it('shows loading indicator when loading is true', () => {
        (useAuth as jest.Mock).mockReturnValue({
            useForgetPassword: jest.fn(),
            loading: true,
        })

        render(<ForgetPasswordView />)

        expect(screen.getByRole('progressbar')).toBeInTheDocument()
    })
})
