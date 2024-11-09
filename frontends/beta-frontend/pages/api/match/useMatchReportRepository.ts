import useSWR from 'swr';
import { MatchReport } from '@/shared/models/MatchReport';
import { MatchReportResponse } from './models/MatchReportresponse';
import { connectorAPI } from '@/libs/fetcher';
import { South } from '@mui/icons-material';

const API = '/match/';

const useMatchReportRepository = (matchId: string | undefined) => {
    if (!matchId) {
        return { report: undefined, isLoading: true };
    }

    const { data, isLoading } = useSWR<MatchReport>(
        API + matchId,
        connectorAPI,
    );

    return { report: data, isLoading };
};

export { useMatchReportRepository };
