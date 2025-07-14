import { render, screen } from "@testing-library/react"
import BillboardCardDataItem from "./BillboardCardDataItem"

describe("BillboardCardDataItem", () => {

    it("should render billboard card data item with title and string value", async () => {
        render(<BillboardCardDataItem title={"title22"} value={"string"} />)

        const titleElement = await screen.findByTestId('billboard-card-title');
        const valueElement = await screen.findByTestId('billboard-card-data');

        expect(titleElement).toHaveTextContent('title22:');
        expect(valueElement).toHaveTextContent('String');
    })

    it.each([
        [true, "1,000.00 $"],
        [false, "1000"]
    ])
        ("should render billboard card data item with title and number value", async (isMoneyInput: boolean, result: string) => {
            render(<BillboardCardDataItem title={"title22"} value={1000} isMoney={isMoneyInput} />)

            const titleElement = await screen.findByTestId('billboard-card-title');
            const valueElement = await screen.findByTestId('billboard-card-data');

            expect(titleElement).toHaveTextContent('title22:');
            expect(valueElement).toHaveTextContent(result);
        })

})