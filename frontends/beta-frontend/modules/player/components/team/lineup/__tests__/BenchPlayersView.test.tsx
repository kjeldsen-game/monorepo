import { mockPlayers } from "__mocks__/player";
import BenchPlayersView from "../BenchPlayersView";
import { render, screen } from "@testing-library/react";

jest.mock('../LineupButton', () => (props: any) => (
    <div data-testid="lineup-button">{props.player?.name || 'Empty'}</div>
));

describe("BenchPlayersView", () => {

    it('renders 7 LineupButtons with 2 players', () => {
        render(<BenchPlayersView bench={mockPlayers} />);

        const buttons = screen.getAllByTestId('lineup-button');
        expect(buttons).toHaveLength(7);
        expect(buttons[0]).not.toBe("");
        expect(buttons[1]).not.toBe("");
        expect(buttons[2]).toHaveTextContent("Empty");
    });

    it('renders empty slots when bench is undefined', () => {
        render(<BenchPlayersView bench={undefined} />);

        const buttons = screen.getAllByTestId('lineup-button');
        expect(buttons).toHaveLength(7);
        buttons.forEach((btn) => {
            expect(btn).toHaveTextContent('Empty');
        });
    });

})