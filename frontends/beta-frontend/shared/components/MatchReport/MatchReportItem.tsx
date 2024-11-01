import {
    MatchActionType,
    MatchEventSide,
    Play,
} from '@/shared/models/MatchReport';
import {
    SportsSoccer,
    ArrowForward,
    ControlCamera,
    KeyboardDoubleArrowDown,
} from '@mui/icons-material';
import { Typography } from '@mui/material';
import Box from '@mui/material/Box';
interface MatchReportItemProps {
    event: Play;
    sx?: React.CSSProperties;
    homeId: string;
    awayId: string;
}

const justifyStyles: Record<MatchEventSide, React.CSSProperties> = {
    MainEvent: {
        justifyContent: 'center',
    },
    HomeTeamEvent: {
        justifyContent: 'left',
    },
    AwayTeamEvent: {
        justifyContent: 'right',
    },
};

const colorStyles: Record<MatchEventSide, React.CSSProperties> = {
    MainEvent: {
        color: 'white',
    },
    HomeTeamEvent: {
        color: 'green',
    },
    AwayTeamEvent: {
        color: 'red',
    },
};

const iconByAction: Record<MatchActionType, React.ReactNode> = {
    PASSING: <ArrowForward />,
    POSITIONAL: <ControlCamera />,
    TACKLE: <KeyboardDoubleArrowDown />,
    SHOT: <SportsSoccer />,
};

export const MatchReportItem: React.FC<MatchReportItemProps> = ({
    sx,
    event,
    homeId,
    awayId,
}) => {
    console.log(homeId);
    return (
        <Box
            sx={{
                width: '100%',
                display: 'flex',
                flexDirection: 'column',
            }}>
            <Box
                alignSelf={'center'}
                fontSize={'8px'}
                textAlign={'center'}
                sx={{ background: '#A3A3A3', width: '40px' }}
                borderRadius={'5px'}>
                {event.clock}:00
            </Box>
            {/* event.duel.initiator.id == homeId justifyconent start else end justifyContent*/}
            <Box
                paddingY={'10px'}
                display={'flex'}
                justifyContent={
                    event.duel.initiator.id === homeId ? 'start' : 'end'
                }>
                <span
                    style={{
                        color: 'black',
                        fontWeight: 'bold',
                        marginRight: '5px',
                        ...colorStyles[event.duel.side],
                    }}>
                    {event.duel.initiator.name}
                    {event.duel.type}
                </span>
                <span>{event.duel.challenger?.name}</span>
                <span>{event.duel.receiver?.name}</span>
            </Box>
        </Box>
    );
};

export default MatchReportItem;
