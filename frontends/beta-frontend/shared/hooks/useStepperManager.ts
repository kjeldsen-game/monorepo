import React from 'react';

export const useStepperManager = (stepContent) => {
  const handleStep = (step: number) => () => {
    setActiveStep(step);
  };
  const [activeStep, setActiveStep] = React.useState(0);
  const [completed, setCompleted] = React.useState<{
    [k: number]: boolean;
  }>({});

  const completedSteps = () => {
    return Object.keys(completed).length;
  };

  const totalSteps = () => {
    return stepContent.length;
  };

  const isLastStep = () => {
    return activeStep === totalSteps() - 1;
  };

  const allStepsCompleted = () => {
    return completedSteps() === totalSteps();
  };

  const handleNext = () => {
    const newActiveStep =
      isLastStep() && !allStepsCompleted()
        ? stepContent.findIndex((step, i) => !(i in completed))
        : activeStep + 1;
    setActiveStep(newActiveStep);
  };

  return { activeStep, handleNext, handleStep, completed };
};
