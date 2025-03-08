import { GridColDef, GridValueGetterParams } from '@mui/x-data-grid';
import { baseColumnConfig } from './ColumnsConfig';

export const playerSkillsColumns = (showPotential: boolean = false) => {
  const getValue = (
    field: string,
    params: GridValueGetterParams,
    skillType: 'actual' | 'potential',
    fieldSecondary?: string,
    headerNameSecondary?: string,
  ) => {
    return fieldSecondary &&
      headerNameSecondary &&
      params.row.actualSkills[fieldSecondary]?.PlayerSkills?.[skillType] !==
        undefined
      ? params.row.actualSkills[fieldSecondary]?.PlayerSkills?.[skillType]
      : params.row.actualSkills[field]?.PlayerSkills?.[skillType] !== undefined
        ? params.row.actualSkills[field]?.PlayerSkills?.[skillType]
        : '';
  };

  const createSkillColumnConfig = (
    field: string,
    headerName: string,
    fieldSecondary?: string,
    headerNameSecondary?: string,
  ): GridColDef => {
    return {
      ...baseColumnConfig,
      field,
      renderHeader: () => (
        <div>
          {headerName}
          <sup style={{ color: '#FF3F84' }}>{headerNameSecondary}</sup>
        </div>
      ),
      valueGetter: (params: GridValueGetterParams) => {
        const actual = getValue(
          field,
          params,
          'actual',
          fieldSecondary,
          headerNameSecondary,
        );
        const potential = getValue(
          field,
          params,
          'potential',
          fieldSecondary,
          headerNameSecondary,
        );

        return actual === '' && potential === ''
          ? ''
          : showPotential
            ? `${actual}/${potential}`
            : `${actual}`;
      },
    };
  };

  const columns: GridColDef[] = [
    createSkillColumnConfig('SCORING', 'SC', 'REFLEXES', 'RE'),
    createSkillColumnConfig(
      'OFFENSIVE_POSITIONING',
      'OP',
      'GOALKEEPER_POSITIONING',
      'GP',
    ),
    createSkillColumnConfig('BALL_CONTROL', 'BC', 'INTERCEPTIONS', 'IN'),
    createSkillColumnConfig('PASSING', 'PA', 'CONTROL', 'CT'),
    createSkillColumnConfig('AERIAL', 'AE', 'ORGANIZATION', 'OR'),
    createSkillColumnConfig('CONSTITUTION', 'CO', 'ONE_ON_ONE', 'OO'),
    createSkillColumnConfig('TACKLING', 'TA'),
    createSkillColumnConfig('DEFENSIVE_POSITIONING', 'DP'),
  ];

  return columns;
};
