import { createTheme, ThemeProvider } from "@mui/material";
import { render, screen, fireEvent } from "@testing-library/react";
import MenuSidebar from "./MenuSidebar";

jest.mock('./MenuSidebarDrawer', () => () => <div data-testid="menu-sidebar-drawer" />);

const renderWithTheme = (ui: React.ReactElement) => {
    const theme = createTheme();
    return render(<ThemeProvider theme={theme}>{ui}</ThemeProvider>);
};

describe("MenuSidebar", () => {
    let setMobileOpenMock: jest.Mock;
    let setIsClosingMock: jest.Mock;

    const renderComponent = () =>
        renderWithTheme(
            <MenuSidebar
                mobileOpen={true}
                setMobileOpen={setMobileOpenMock}
                setIsClosing={setIsClosingMock}
            />
        );

    const getDrawerPaper = () => screen.getByRole("presentation");

    beforeEach(() => {
        setMobileOpenMock = jest.fn();
        setIsClosingMock = jest.fn();
    });

    it("renders temporary and permanent drawers with MenuSidebarDrawer inside", async () => {
        renderComponent();
        const drawers = await screen.findAllByTestId("menu-sidebar-drawer");
        expect(drawers).toHaveLength(2);
    });

    it("calls setIsClosing(false) on transition end", () => {
        renderComponent();
        fireEvent.transitionEnd(getDrawerPaper());
        expect(setIsClosingMock).toHaveBeenCalledWith(false);
    });

    it("calls setIsClosing(true) and setMobileOpen(false) when temporary drawer closes", () => {
        renderComponent();
        fireEvent.keyDown(getDrawerPaper(), { key: "Escape", code: "Escape" });
        expect(setIsClosingMock).toHaveBeenCalledWith(true);
        expect(setMobileOpenMock).toHaveBeenCalledWith(false);
    });
});
