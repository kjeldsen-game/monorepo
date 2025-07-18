import { styled } from '@mui/material';
import React from 'react';

const VisuallyHiddenInput = styled('input')({
    clip: 'rect(0 0 0 0)',
    clipPath: 'inset(50%)',
    height: 1,
    overflow: 'hidden',
    position: 'absolute',
    bottom: 0,
    left: 0,
    whiteSpace: 'nowrap',
    width: 1,
});

const HiddenInput = (props: React.InputHTMLAttributes<HTMLInputElement>) => {
    return <VisuallyHiddenInput {...props} />;
};

export default HiddenInput;
