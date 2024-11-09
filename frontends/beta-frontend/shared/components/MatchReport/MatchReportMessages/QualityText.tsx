import { ReactNode } from 'react';

interface QualityTextProps {
  children: ReactNode;
}

const QualityText = ({ children }: QualityTextProps) => {
  return (
    <span
      style={{
        fontWeight: 'normal',
        fontStyle: 'italic',
      }}>
      {' '}
      {children}{' '}
    </span>
  );
};

export default QualityText;
