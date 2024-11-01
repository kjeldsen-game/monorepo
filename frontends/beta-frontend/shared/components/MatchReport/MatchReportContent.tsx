import Box from '@mui/material/Box';
import MatchReportItem from './MatchReportItem';
import { MatchReport } from '@/shared/models/MatchReport';
import { Typography } from '@mui/material';
import vs from '../../../public/img/vs.png';
import Image from 'next/image';

interface MatchReportContentProps {
    report: MatchReport;
    sx?: React.CSSProperties;
}

export const MatchReportContent: React.FC<MatchReportContentProps> = ({
    report,
    sx,
}) => {
    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'start',
                height: '100%',
                overflow: 'auto',
                gap: '15px',
                padding: '0 5px 0 5px',
                ...sx,
            }}>
            <Box
                sx={{
                    width: '100%',
                    height: '900px',
                    display: 'flex',
                    justifyContent: 'center',
                    paddingY: '50px',
                }}>
                <Image src={vs} width={300} alt="vsimage"></Image>
            </Box>
            <Box
                display={'flex'}
                flexDirection={'column'}
                alignItems={'center'}
                width={'100%'}
                sx={{ background: '#F8F8F8', padding: '20px' }}>
                <Box
                    sx={{
                        background: '#3D3D3D',
                        width: '200px',
                        height: '20px',
                        color: 'white',
                        borderRadius: '5px',
                        textAlign: 'center',
                        fontSize: '12px',
                    }}>
                    MATCH STARTS
                </Box>
                {report.matchReport.plays.map((event, index) => (
                    <MatchReportItem
                        key={index}
                        event={event}
                        homeId={report.home.id}
                        awayId={report.away.id}
                    />
                ))}
            </Box>
        </Box>
    );
};

export default MatchReportContent;
