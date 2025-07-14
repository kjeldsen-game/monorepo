import React from 'react'
import CustomButton from '../../Common/CustomButton'
import AuctionColumnText from './AuctionColumnText'
import CustomTextField from '../../Common/CustomTextField'
import { useAuctionRepository } from '@/pages/api/market/useAuctionRepository'
import { useSession } from 'next-auth/react'
import CustomNumberField from '../../Common/CustomNumberField'

interface AuctionBidFormProps {
    auctionId: string | undefined
}

const AuctionBidForm: React.FC<AuctionBidFormProps> = ({ auctionId }) => {
    const [confirmation, setConfirmation] = React.useState(false);
    const [bid, setBid] = React.useState<number>(0);

    const { data: userData } = useSession({
        required: true,
    });


    const { updateAuction } = useAuctionRepository(
        auctionId,
        userData?.accessToken,
    );

    const handleConfirmationButtonClick = () => {
        setConfirmation(true);
    };

    const handleButtonClick = async () => {
        updateAuction(bid);
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