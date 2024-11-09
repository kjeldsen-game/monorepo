import { ReactNode } from 'react';

interface ActionTextProps {
  children: ReactNode;
}

const ActionText = ({ children }: ActionTextProps) => {
  return (
    <span
      style={{
        fontWeight: 'bold',
      }}>
      {' '}
      {children}{' '}
    </span>
  );
};

export default ActionText;
