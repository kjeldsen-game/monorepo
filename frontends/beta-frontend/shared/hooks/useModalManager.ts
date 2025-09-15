import { useState } from 'react';

export const useModalManager = () => {
  const [open, setOpen] = useState<boolean>(false);

  const handleCloseModal = () => {
    setOpen(false);
  };

  return {
    open,
    setOpen,
    handleCloseModal,
  };
};
