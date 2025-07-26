import React from 'react'
import AuctionColumnText from './AuctionColumnText'
import CustomNumberField from '@/shared/components/Common/CustomNumberField'
import CustomButton from '@/shared/components/Common/CustomButton'
import { useMarketApi } from 'modules/market/hooks/useMarketApi'

interface AuctionBidFormProps {
    auctionId: string | undefined
}

const AuctionBidForm: React.FC<AuctionBidFormProps> = ({ auctionId }) => {
    const [confirmation, setConfirmation] = React.useState(false);
    const [bid, setBid] = React.useState<number>(0);

    const { handlePlaceBid } = useMarketApi();

    const handleConfirmationButtonClick = () => {
        setConfirmation(true);
    };

    const handleButtonClick = async () => {
        handlePlaceBid(auctionId, { amount: bid })
        setConfirmation(false)
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setBid(Number(e.target.value));
    };

    return (
        <>
            {!confirmation ?
                <CustomNumberField
                    placeholder={'Your bid here...'}
                    onChange={handleInputChange}
                    value={bid || ''}
                />
                : <AuctionColumnText
                    title="Your Bid"
                    value={`${bid} $`}
                    sx={{
                        color: '#FF3F84',
                        fontSize: '30px',
                        fontWeight: '900',
                    }}
                />}

            <CustomButton
                sx={{ mt: 1, width: '100%' }}
                onClick={
                    !confirmation
                        ? handleConfirmationButtonClick
                        : handleButtonClick
                }>
                {!confirmation ? 'PLACE MY BID' : 'YES, I AM SURE'}
            </CustomButton>
        </>
    )
}

export default AuctionBidForm