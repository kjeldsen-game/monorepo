import React, { useState } from "react";
import {
    IconButton,
    Badge,
    Menu,
    MenuItem,
    Typography,
    ListSubheader,
    Divider,
} from "@mui/material";
import { theme } from "@/libs/material/theme";
import { useNotificationApi } from "@/shared/hooks/useNotificationApi";
import NotificationsIcon from "@mui/icons-material/Notifications";
import { NotificationResponse, NotificationType } from "@/shared/@types/responses";
import MatchEndNotification from "./notifications/MatchEndNotification";
import PlayerBidNotification from "./notifications/PlayerBidNotification";
import { NotificationProps } from "./notifications/common/CommonNotification";

interface NotificationMenuProps {

}

const NotificationMenu: React.FC<NotificationMenuProps> = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

    const { data } = useNotificationApi();

    const open = Boolean(anchorEl);

    const handleClick = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const notificationComponentTypeMap: Record<NotificationType, React.FC<NotificationProps>> = {
        [NotificationType.MATCH_END]: MatchEndNotification,
        [NotificationType.AUCTION_BID]: PlayerBidNotification,
    };

    return (
        <>
            <IconButton sx={{ marginRight: 1 }} color="inherit" onClick={handleClick}>
                <Badge badgeContent={data?.length} color="error">
                    <NotificationsIcon sx={{ color: theme.palette.secondary.main }} />
                </Badge>
            </IconButton>

            <Menu
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                slotProps={{
                    paper: {
                        sx: {
                            p: 0,
                            borderRadius: 2,
                            width: 320,
                            maxHeight: 400,
                            overflowX: "hidden",
                        },
                    },
                }}
            >
                <ListSubheader sx={{ paddingY: 1.5, display: 'flex', justifyContent: 'space-between' }}>
                    <Typography sx={{ color: 'black' }} fontWeight={'bold'}>Notifications</Typography>
                    {data?.length != 0 &&
                        <Typography sx={{ color: theme.palette.secondary.main }}>Mark all as read</Typography>
                    }
                </ListSubheader>
                <Divider></Divider>
                {data?.length === 0 ? (
                    <MenuItem disabled sx={{ display: 'flex', justifyContent: 'center' }}>
                        <Typography>No new notifications</Typography>
                    </MenuItem>
                ) : (
                    data?.map((notification: NotificationResponse) => {
                        const Component = notificationComponentTypeMap[NotificationType[notification.type]];
                        if (!Component) return null;
                        return (
                            <Component key={notification.id} notification={notification} />
                        );
                    })
                )}
            </Menu>
        </>
    );
};

export default NotificationMenu;
