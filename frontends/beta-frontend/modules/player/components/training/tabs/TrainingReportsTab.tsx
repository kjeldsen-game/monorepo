import React from "react";
import { Box, CircularProgress } from "@mui/material";
import TrainingReport from "../report/TrainingReport";
import { TrainingEventResponse } from "modules/player/types/TrainingResponses";
import { theme } from "@/libs/material/theme";
import TrainingFilter from "../filter/TrainingFilter";
import { TrainingFilterProvider, useTrainingFilter } from "modules/player/contexts/TrainingFilterContext";

interface TrainingReportsTabProps {
    trainings: Record<string, TrainingEventResponse[]>;
    loading: boolean;
}

const TrainingReportsTab: React.FC<TrainingReportsTabProps> = ({ trainings, loading }) => {

    return (
        <>
            {loading || !trainings ? (
                <Box padding={1} display={'flex'} justifyContent={"center"}>
                    <CircularProgress sx={{ color: theme.palette.secondary.main }} />
                </Box>
            ) : (
                <>
                    <TrainingFilter />
                    {Object.entries(trainings)
                        .sort(([dateA], [dateB]) => {
                            const [dayA, monthA, yearA] = dateA.split("-").map(Number);
                            const [dayB, monthB, yearB] = dateB.split("-").map(Number);

                            const parsedA = new Date(yearA, monthA - 1, dayA);
                            const parsedB = new Date(yearB, monthB - 1, dayB);

                            return parsedB.getTime() - parsedA.getTime();
                        })

                        .map(([date, trainingList]: [string, TrainingEventResponse[]]) => (
                            <TrainingReport
                                key={date}
                                date={date}
                                trainings={trainingList}
                            />
                        ))
                    }
                </>
            )}
        </>

    );
};

export default TrainingReportsTab;
