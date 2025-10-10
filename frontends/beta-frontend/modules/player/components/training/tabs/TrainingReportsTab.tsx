import React from "react";
import { Box, CircularProgress } from "@mui/material";
import TrainingReport from "../report/TrainingReport";
import { TrainingEventResponse } from "modules/player/types/TrainingResponses";
import { theme } from "@/libs/material/theme";
import TrainingFilter from "../filter/TrainingFilter";
import { sortDatesDesc } from "modules/player/utils/TrainingUtils";
import { isObjectEmpty } from "modules/player/utils/GeneralUtils";
import NoTrainingFoundError from "../report/NoTrainingFoundError";

interface TrainingReportsTabProps {
    trainings: Record<string, TrainingEventResponse[]>;
    loading: boolean;
}

const TrainingReportsTab: React.FC<TrainingReportsTabProps> = ({ trainings, loading }) => {
    return (
        <>
            <TrainingFilter />
            {loading || !trainings ? (
                <Box padding={2} display="flex" justifyContent="center">
                    <CircularProgress sx={{ color: theme.palette.secondary.main }} />
                </Box>
            ) : isObjectEmpty(trainings) ? (
                <NoTrainingFoundError />
            ) : (
                <>
                    {Object.entries(trainings)
                        .sort(sortDatesDesc)
                        .map(([date, trainingList]: [string, TrainingEventResponse[]]) => (
                            <TrainingReport
                                key={date}
                                date={date}
                                trainings={trainingList}
                            />
                        ))}
                </>
            )}
        </>
    );
};

export default TrainingReportsTab;
