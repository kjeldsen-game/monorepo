import { Link, LinkProps } from '@mui/material';
import React from 'react';

interface StyledLinkProps extends LinkProps { }

const StyledLink: React.FC<StyledLinkProps> = (linkProps) => {
    return (
        <Link
            sx={{ color: '#FF3F84', textDecoration: 'none', fontWeight: 'bold', fontSize: '14px' }}
            {...linkProps}
        />
    );
};

export default StyledLink;
