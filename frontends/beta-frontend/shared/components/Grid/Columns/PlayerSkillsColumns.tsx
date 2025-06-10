import { GridColDef } from '@mui/x-data-grid';
import { baseColumnConfig } from './ColumnsConfig';
import CustomTooltip from '../../MatchReport/Tooltips/CustomTooltip';
import { PlayerSkillShortcuts } from '@/shared/models/player/PlayerSkill';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Box } from '@mui/material';

export const playerSkillsColumns = (showPotential: boolean = false) => {
  const getValue = (
    field: string,
    params,
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
      renderCell: (params) => {
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
      renderHeader: () => (
        <CustomTooltip
          tooltipContent={
            <Box sx={{ fontSize: '14px' }}>
              <span>
                {convertSnakeCaseToTitleCase(PlayerSkillShortcuts[headerName])}
              </span>
              {headerNameSecondary && (
                <>
                  <span style={{ color: '#FF3F84' }}> / </span>
                  <span>
                    {convertSnakeCaseToTitleCase(
                      PlayerSkillShortcuts[headerNameSecondary],
                    )}
                  </span>
                </>
              )}
            </Box>
          }>
          <div>
            {headerName}
            {headerNameSecondary && (
              <sup style={{ color: '#FF3F84' }}>{headerNameSecondary}</sup>
            )}
          </div>
        </CustomTooltip>
      ),
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
