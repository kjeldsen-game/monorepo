import { Button } from '@mui/material';

export const containedSecondaryButton = () => {
    return <Button variant="contained" color="secondary"></Button>;
};

export function outlinedSecondaryButton(buttonText: string) {
    return (
        <Button variant="outlined" color="secondary">
            {buttonText}
        </Button>
    );
}
