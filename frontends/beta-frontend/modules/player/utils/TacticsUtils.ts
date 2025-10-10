import ShieldIcon from '@mui/icons-material/Shield';
import ManIcon from '@mui/icons-material/Man';
import HealthAndSafetyIcon from '@mui/icons-material/HealthAndSafety';

export type ModifierConfig = {
  Icon: React.ElementType;
  main: string;
  light: string;
};

export const getModifierConfig = (type: string): ModifierConfig => {
  switch (type) {
    case 'tactic':
      return {
        Icon: HealthAndSafetyIcon,
        main: '#F59E0B',
        light: '#FFF8E2',
      };
    case 'verticalPressure':
      return {
        Icon: ManIcon,
        main: '#21C55E', 
        light: '#E8F5E9',
      };
    case 'horizontalPressure':
      return {
        Icon: ShieldIcon,
        main: '#3B82F6',
        light: '#E6F3FF',
      };
  default:
      throw new Error(`Invalid modifier type: ${type}`);
  }
};
