import React, { createContext, useContext, useState } from 'react';

interface ErrorContextType {
    error: string | null | undefined;
    setError: (msg: string | null) => void;
}

const ErrorContext = createContext<ErrorContextType>({
    error: null,
    setError: () => { },
});

export const ErrorProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [error, setError] = useState<string | null>(null);

    return (
        <ErrorContext.Provider value={{ error, setError }}>
            {children}
        </ErrorContext.Provider>
    );
};

export const useError = () => useContext(ErrorContext);
