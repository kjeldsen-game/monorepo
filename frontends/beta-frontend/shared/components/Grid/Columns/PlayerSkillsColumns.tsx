import { GridColDef, GridValueGetterParams } from '@mui/x-data-grid';
import { baseColumnConfig } from './ColumnsConfig';

export const playerSkillsColumns = () => {
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
      minWidth: 50,
      valueGetter: (params: GridValueGetterParams) => {
        const actual =
          fieldSecondary &&
          headerNameSecondary &&
          params.row.actualSkills[fieldSecondary]?.PlayerSkills?.actual !==
            undefined
            ? params.row.actualSkills[fieldSecondary]?.PlayerSkills?.actual
            : params.row.actualSkills[field]?.PlayerSkills?.actual !== undefined
              ? params.row.actualSkills[field]?.PlayerSkills?.actual
              : '';

        const potential =
          fieldSecondary &&
          headerNameSecondary &&
          params.row.actualSkills[fieldSecondary]?.PlayerSkills?.potential !==
            undefined
            ? params.row.actualSkills[fieldSecondary]?.PlayerSkills?.potential
            : params.row.actualSkills[field]?.PlayerSkills?.potential !==
                undefined
              ? params.row.actualSkills[field]?.PlayerSkills?.potential
              : '';

        return actual === '' && potential === ''
          ? ''
          : `${actual}/${potential}`;
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
    createSkillColumnConfig('CONSTITUTION', 'CO'),
    createSkillColumnConfig('TACKLING', 'TA'),
    createSkillColumnConfig('DEFENSIVE_POSITIONING', 'DP'),
  ];

  return columns;
};
