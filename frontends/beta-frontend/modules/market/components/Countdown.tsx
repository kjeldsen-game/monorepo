import React, { useEffect, useState } from 'react';

interface CountdownProps {
  endedAt: string;
}

const Countdown: React.FC<CountdownProps> = ({ endedAt }) => {
  const [timeLeft, setTimeLeft] = useState('');

  useEffect(() => {
    const endTime = new Date(endedAt).getTime();

    const interval = setInterval(() => {
      const now = new Date().getTime();
      const distance = endTime - now;

      if (distance <= 0) {
        setTimeLeft('Ended');
        clearInterval(interval);
        return;
      }

      const hours = Math.floor(distance / (1000 * 60 * 60));
      const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
      const seconds = Math.floor((distance % (1000 * 60)) / 1000);

      setTimeLeft(`${hours}h ${minutes}m ${seconds}s`);
    }, 1000);

    return () => clearInterval(interval);
  }, [endedAt]);

  return <span>{timeLeft}</span>;
};

export default Countdown;