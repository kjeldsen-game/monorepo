import { ChangeEvent, ReactNode, useState } from 'react';
import { MarketFilterForm, SkillRanges } from '../types/filterForm';

export const INITIAL_MARKET_FILTER_FORM_VALUES: MarketFilterForm = {
  playerOffer: { min: '', max: '' },
  playerAge: { min: '', max: '' },
  position: '',
  skillRanges: {
    SC: { min: '', max: '', potentialMin: '', potentialMax: '' },
    OP: { min: '', max: '', potentialMin: '', potentialMax: '' },
    BC: { min: '', max: '', potentialMin: '', potentialMax: '' },
    PA: { min: '', max: '', potentialMin: '', potentialMax: '' },
    AE: { min: '', max: '', potentialMin: '', potentialMax: '' },
    CO: { min: '', max: '', potentialMin: '', potentialMax: '' },
    TA: { min: '', max: '', potentialMin: '', potentialMax: '' },
    DP: { min: '', max: '', potentialMin: '', potentialMax: '' },
  },
};

export const useMarketFilterForm = () => {
  const [formValues, setFormValues] = useState(
    INITIAL_MARKET_FILTER_FORM_VALUES,
  );

  const handlePositionChange = (
    event:
      | ChangeEvent<HTMLInputElement>
      | (Event & { target: { value: unknown; name: string } }),
    child?: ReactNode,
  ) => {
    const target = event.target as { value: string };
    const value = target.value;
    setFormValues((prev) => ({
      ...prev,
      position: value,
    }));
  };

  const handleInputChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    const { name, value } = event.target;
    let mainKey, subKey;

    if (name.includes('Potential')) {
      const parts = ([mainKey, subKey] = name.split(' '));
      [mainKey, subKey] = [
        parts[1].split('.')[0],
        `potential${parts[1].split('.')[1]}`,
      ];
    } else {
      [mainKey, subKey] = name.split('.');
    }
    if (mainKey == 'playerAge' || mainKey == 'playerOffer') {
      setFormValues({
        ...formValues,
        [mainKey]: {
          ...formValues[mainKey],
          [subKey.charAt(0).toLowerCase() + subKey.slice(1)]: value,
        },
      });
    } else {
      setFormValues({
        ...formValues,
        skillRanges: {
          ...formValues.skillRanges,
          [mainKey as keyof SkillRanges]: {
            ...formValues.skillRanges[mainKey as keyof SkillRanges],
            [subKey.charAt(0).toLowerCase() + subKey.slice(1)]: value,
          },
        },
      });
    }
  };

  const resetForm = () => setFormValues(INITIAL_MARKET_FILTER_FORM_VALUES);

  return {
    formValues,
    setFormValues,
    handleInputChange,
    handlePositionChange,
    resetForm,
  };
};
