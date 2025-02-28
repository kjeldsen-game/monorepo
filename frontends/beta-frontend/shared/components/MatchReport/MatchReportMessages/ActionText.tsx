import { ReactNode } from 'react';

interface ActionTextProps {
  children: ReactNode;
}

const ActionText = ({ children }: ActionTextProps) => {
  const text = children === 'SHOOT' ? 'SHOT' : children;
  return <span style={{ textTransform: 'lowercase' }}> {text} </span>;
};

export default ActionText;
