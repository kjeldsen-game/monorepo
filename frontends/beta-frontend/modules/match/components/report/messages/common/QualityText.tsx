import { ReactNode } from 'react';

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
