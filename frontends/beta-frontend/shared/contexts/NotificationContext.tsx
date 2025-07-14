import React, { createContext, useContext, useState } from 'react';

interface NotificationContextType {
    notification: string | null | undefined;
    setNotification: (msg: string | null) => void;
}

const NotificationContext = createContext<NotificationContextType>({
    notification: null,
    setNotification: () => { },
});

export const NotificationProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [notification, setNotification] = useState<string | null>(null);

    return (
        <NotificationContext.Provider value={{ notification, setNotification }}>
            {children}
        </NotificationContext.Provider>
    );
};

export const useNotification = () => useContext(NotificationContext);
