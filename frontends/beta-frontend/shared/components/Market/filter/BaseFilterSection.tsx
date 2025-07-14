import { Grid } from '@mui/material'
import React from 'react'
import CustomSelectInput from '../../Common/CustomSelectInput'
import { MarketFilterForm } from 'hooks/useMarketFilterForm'
import { PlayerPosition } from '@/shared/models/player/PlayerPosition'
import MinMaxInput from './MinMaxInput'

interface BaseFilterSectionProps {
    formValues: MarketFilterForm
    handleInputChange: (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
    ) => void;
    handlePositionChange: (event: any) => void
}

const BaseFilterSection: React.FC<BaseFilterSectionProps> = ({ formValues, handleInputChange, handlePositionChange }) => {

    return (
        <Grid
            container
            spacing={1}
            justifyContent={{ md: 'flex-start', sm: 'center', xs: 'center' }}
            alignItems={'left'}>
            <Grid>
                <MinMaxInput
                    minMaxFormValues={formValues.playerOffer}
                    inputName={'playerOffer'}
                    handleInputChange={handleInputChange}
                />
            </Grid>
            <Grid>
                <MinMaxInput
                    minMaxFormValues={formValues.playerAge}
                    inputName={'playerAge'}
                    handleInputChange={handleInputChange}
                />
            </Grid>
            <Grid>
                <CustomSelectInput
                    value={formValues.position}
                    title={'Position'}
                    values={PlayerPosition}
                    onChange={handlePositionChange}
                />
            </Grid>
        </Grid>
    )
}

export default BaseFilterSection