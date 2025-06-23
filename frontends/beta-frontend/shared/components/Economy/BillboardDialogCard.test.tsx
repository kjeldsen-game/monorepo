import { fireEvent, render, screen } from "@testing-library/react"
import BillboardDialogCard from "./BillboardDialogCard"

describe('BillboardDialogCard', () => {
    const mockHandleButtonClick = jest.fn()

    it.each(["Short", "Medium", "Long"])("should render card with correct type", (type) => {
        render(<BillboardDialogCard type={type} handleButtonClick={mockHandleButtonClick} />)
        expect(screen.getByRole("heading", { name: type })).toBeInTheDocument();
    })

    it('calls handleButtonClick with the correct type when the button is clicked', () => {
        const mockType = "Short"
        render(<BillboardDialogCard type={mockType} handleButtonClick={mockHandleButtonClick} />)

        const button = screen.getByRole('button', { name: /choose offer/i })
        fireEvent.click(button)
        expect(mockHandleButtonClick).toHaveBeenCalledWith(mockType)
    })
})