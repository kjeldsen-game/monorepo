import { Box } from '@mui/material';
import TwitterIcon from '@mui/icons-material/Twitter';
import InstagramIcon from '@mui/icons-material/Instagram';
import LinkedInIcon from '@mui/icons-material/LinkedIn';
import SocialMediaLink from './SocialMediaLink';

const SocialMediaButtons = () => {
    return (
        <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            gap={2}
            mt={1}
        >
            <SocialMediaLink link="/">
                <LinkedInIcon />
            </SocialMediaLink>
            <SocialMediaLink link="/">
                <InstagramIcon />
            </SocialMediaLink>
            <SocialMediaLink link="/">
                <TwitterIcon />
            </SocialMediaLink>
        </Box>
    );
};

export default SocialMediaButtons;
