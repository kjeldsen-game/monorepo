import React, { useState } from 'react';
import { alpha, Box, Button, Card, TextField, Typography } from '@mui/material';
import { Balance } from '@mui/icons-material';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import EconomyCard from './EconomyCard';
import SellIcon from '@mui/icons-material/Sell';
import MarketButton from '../Market/MarketButton';
import WarningIcon from '@mui/icons-material/Warning';

import {
    INCOME_PERIODICITY_NAMES,
    IncomeMode,
    IncomePeriodicity,
} from '@/shared/models/Economy';
import BillboardCardDataItem from './BillboardCardDataItem';

interface SponsorCardProps {
    open: boolean;
    mode: IncomeMode | null;
    handleOpenModal: (type: IncomePeriodicity) => void;
    type: IncomePeriodicity;
}

const SponsorCard: React.FC<SponsorCardProps> = ({
    open,
    mode,
    handleOpenModal,
    type,
}) => {
    return (
        <Card sx={{ height: '45%', padding: 2 }}>
            <Box
                sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'space-between',
                }}>
                <Box
                    height={'100%'}
                    display={'flex'}
                    flexDirection={'column'}
                    justifyContent={'space-between'}>
                    <Box
                        sx={{
                            height: '100%',
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                        }}>
                        <SellIcon fontSize="large" sx={{ color: '#FF3F84' }} />
                        <Typography variant="h5" fontWeight={'900'}>
                            {INCOME_PERIODICITY_NAMES[type]} Sponsor
                        </Typography>
                        {mode === null ? (
                            <>
                                <Typography
                                    color={'#54595E99'}
                                    fontSize={'12'}
                                    sx={{
                                        height: '100%',
                                        display: 'flex',
                                        alignItems: 'center',
                                        textAlign: 'center',
                                    }}>
                                    You don't have selected Sposor, click on the
                                    button to select one!
                                </Typography>
                            </>
                        ) : (
                            <>
                                <BillboardCardDataItem
                                    title="Mode"
                                    value={mode}
                                />
                            </>
                        )}
                    </Box>
                    {mode === null ? (
                        <MarketButton onClick={() => handleOpenModal(type)}>
                            Choose Offer
                        </MarketButton>
                    ) : (
                        <></>
                    )}
                </Box>
            </Box>
        </Card>
    );
};

export default SponsorCard;
