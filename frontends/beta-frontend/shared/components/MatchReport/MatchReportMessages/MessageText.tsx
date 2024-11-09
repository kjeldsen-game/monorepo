import { MatchEventSide } from '@/shared/models/MatchReport';
import React, { ReactNode } from 'react';

interface MessageTextProps {
  children: ReactNode;
  matchEventSide?: MatchEventSide;
}

const colorStyles: Record<MatchEventSide, React.CSSProperties> = {
  MainEvent: {
    color: 'black',
  },
  HomeTeamEvent: {
    color: '#A4BC10',
  },
  AwayTeamEvent: {
    color: '#29B6F6',
  },
};

const MessageText = ({
  children,
  matchEventSide = 'MainEvent',
}: MessageTextProps) => {
  return (
    <span
      style={{
        fontWeight: 'bold',
        ...colorStyles[matchEventSide],
      }}>
      {' '}
      {children}{' '}
    </span>
  );
};

export default MessageText;
