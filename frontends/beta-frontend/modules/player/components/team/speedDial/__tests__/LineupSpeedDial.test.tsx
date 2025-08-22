import { render, screen, fireEvent } from '@testing-library/react';
import LineupSpeedDial from '../LineupSpeedDial';
import * as material from '@mui/material';

jest.mock('@mui/icons-material/VerifiedUser', () => () => <div>VerifiedUserIcon</div>);
jest.mock('@mui/icons-material/GppMaybe', () => () => <div>GppMaybeIcon</div>);
jest.mock('@mui/icons-material/Edit', () => () => <div>EditIcon</div>);
jest.mock('@mui/icons-material/Close', () => () => <div>CloseIcon</div>);
jest.mock('@mui/icons-material/Autorenew', () => () => <div>AutorenewIcon</div>);


jest.mock('@mui/material', () => {
    const actual = jest.requireActual('@mui/material');
    return {
        ...actual,
        useMediaQuery: jest.fn(),
    };
});

(material.useMediaQuery as jest.Mock).mockReturnValue(false);


describe('LineupSpeedDial', () => {
    const mockIsEditOnClick = jest.fn();
    const mockOpenModalValidationOnClick = jest.fn();
    const mockToogleIsXsPlayers = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('renders main SpeedDial and actions', () => {
        (material.useMediaQuery as jest.Mock).mockReturnValue(false);

        render(
            <LineupSpeedDial
                isEdit={false}
                isFormationValid={true}
                isEditOnClick={mockIsEditOnClick}
                openModalValidationOnClick={mockOpenModalValidationOnClick}
                toogleIsXsPlayers={mockToogleIsXsPlayers}
            />
        );

        expect(screen.getByLabelText('SpeedDial basic example')).toBeInTheDocument();
        expect(screen.getByText('EditIcon')).toBeInTheDocument();
        expect(screen.getByText('VerifiedUserIcon')).toBeInTheDocument();
        expect(screen.queryByText('AutorenewIcon')).not.toBeInTheDocument();
    });

    it('renders switch lineup button on xs screen', () => {
        (material.useMediaQuery as jest.Mock).mockReturnValue(true);

        render(
            <LineupSpeedDial
                isEdit={false}
                isFormationValid={false}
                isEditOnClick={mockIsEditOnClick}
                openModalValidationOnClick={mockOpenModalValidationOnClick}
                toogleIsXsPlayers={mockToogleIsXsPlayers}
            />
        );

        expect(screen.getByText('AutorenewIcon')).toBeInTheDocument();
        expect(screen.getByText('EditIcon')).toBeInTheDocument();
        expect(screen.getByText('GppMaybeIcon')).toBeInTheDocument();
    });

    it('calls the correct handlers when actions are clicked', () => {
        (material.useMediaQuery as jest.Mock).mockReturnValue(true);

        render(
            <LineupSpeedDial
                isEdit={true}
                isFormationValid={true}
                isEditOnClick={mockIsEditOnClick}
                openModalValidationOnClick={mockOpenModalValidationOnClick}
                toogleIsXsPlayers={mockToogleIsXsPlayers}
            />
        );

        fireEvent.click(screen.getByText('AutorenewIcon'));
        fireEvent.click(screen.getByText('CloseIcon'));
        fireEvent.click(screen.getByText('VerifiedUserIcon'));

        expect(mockToogleIsXsPlayers).toHaveBeenCalled();
        expect(mockIsEditOnClick).toHaveBeenCalled();
        expect(mockOpenModalValidationOnClick).toHaveBeenCalled();
    });
});
