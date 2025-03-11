import { ReactNode } from 'react';
import { text } from 'stream/consumers';

interface QualityTextProps {
  children: ReactNode;
  textColor: string;
}

const QualityText = ({ children, textColor }: QualityTextProps) => {
  return (
    <span
      style={{
        fontStyle: 'italic',
        fontWeight: 'normal',
        color: textColor,
      }}>
      {' '}
      {children}{' '}
    </span>
  );
};

export default QualityText;
