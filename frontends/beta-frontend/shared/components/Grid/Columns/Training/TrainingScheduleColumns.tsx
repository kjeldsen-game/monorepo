import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { Box } from '@mui/material';
import { formatPlayerSkills } from '@/shared/utils/ColumnUtils';
import {
  PlayerSkill,
  PlayerSkillToShortcut,
} from '@/shared/models/player/PlayerSkill';
import ColHeader from '../common/components/ColHeader';
import { PlayerNameColumn } from '../common/columns/PlayerNameColumn';
import { PlayerPositionColumn } from '../common/columns/PlayerPositionColumn';

export const trainingScheduleColumns = (
  handleCellClick: (
    skillToTrain: PlayerSkill | undefined,
    skillUnderTraining: PlayerSkill | undefined,
    playerIdToTrain: string,
  ) => void,
) => {
  const renderSkillNames = (skills: PlayerSkill[]): string => {
    if (!skills || skills.length === 0) {
      return '';
    }

    return skills
      .slice(0, 2)
      .map((skill) => PlayerSkillToShortcut[skill] || skill)
      .join(' / ');
  };

  const renderSkillCell = (
    params: GridCellParams,
    skill1: any,
    skill2: any,
  ) => {
    const skills = params.row.skills;

    let selectedSkill;
    let selectedSkillName: PlayerSkill;

    if (params.row.player.actualSkills[skill1]) {
      selectedSkill = params.row.player.actualSkills[skill1];
      selectedSkillName = skill1;
    } else if (params.row.player.actualSkills[skill2]) {
      selectedSkill = params.row.player.actualSkills[skill2];
      selectedSkillName = skill2;
    }

    return (
      <Box
        sx={{
          cursor: 'pointer',
          border: skills.includes(selectedSkillName) ? '2px solid #A4BC10' : '',
          borderRadius: '10px',
          height: '100%',
          width: '100%',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
        onClick={() => {
          handleCellClick(
            selectedSkillName,
            skills.length > 1 ? skills[0] : undefined,
            params.row.player.id,
          );
        }}>
        {formatPlayerSkills(selectedSkill)}
      </Box>
    );
  };

  const columns: GridColDef[] = [
    PlayerNameColumn((row) => row.player, "left"),
    PlayerPositionColumn(
      (row) => row.player,
      'preferredPosition',
      'Pos'
    ),
    // ...playerCommonColumns(true, false),
    {
      field: 'cs',
      renderHeader: () => <ColHeader header={'CS'} />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Box
          sx={{
            border: '2px solid #A4BC10',
            borderRadius: '10px',
            height: '100%',
            width: '100%',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}>
          {renderSkillNames(params.row.skills)}
        </Box>
      ),
    },
    // ...playerSkillsColumns(),
    // {
    //   field: 'DEFENSIVE_POSITIONING',
    //   renderHeader: () => <div>DP</div>,
    //   ...baseColumnConfig,
    //   renderCell: (params: GridCellParams) => {
    //     return renderSkillCell(
    //       params,
    //       PlayerSkill.DEFENSIVE_POSITIONING,
    //       PlayerSkill.CONTROL,
    //     );
    //   },
    // },
    // {
    //   field: 'BALL_CONTROL',
    //   renderHeader: () => <div>BC</div>,
    //   ...baseColumnConfig,
    //   renderCell: (params: GridCellParams) => {
    //     return renderSkillCell(
    //       params,
    //       PlayerSkill.BALL_CONTROL,
    //       PlayerSkill.INTERCEPTIONS,
    //     );
    //   },
    // },
    // {
    //   field: 'SCORE',
    //   renderHeader: () => <div>SC</div>,
    //   ...baseColumnConfig,
    //   renderCell: (params: GridCellParams) => {
    //     return renderSkillCell(
    //       params,
    //       PlayerSkill.SCORING,
    //       PlayerSkill.ONE_ON_ONE,
    //     );
    //   },
    // },
    // {
    //   field: 'PASSING',
    //   renderHeader: () => <div>PA</div>,
    //   ...baseColumnConfig,
    //   renderCell: (params: GridCellParams) => {
    //     return renderSkillCell(
    //       params,
    //       PlayerSkill.PASSING,
    //       PlayerSkill.ORGANIZATION,
    //     );
    //   },
    // },
    // {
    //   field: 'OFFENSIVE_POSITIONING',
    //   renderHeader: () => <div>OP</div>,
    //   ...baseColumnConfig,
    //   renderCell: (params: GridCellParams) => {
    //     return renderSkillCell(
    //       params,
    //       PlayerSkill.OFFENSIVE_POSITIONING,
    //       PlayerSkill.REFLEXES,
    //     );
    //   },
    // },
    // {
    //   field: 'TACKLING',
    //   renderHeader: () => <div>TA</div>,
    //   ...baseColumnConfig,
    //   renderCell: (params: GridCellParams) => {
    //     return renderSkillCell(
    //       params,
    //       PlayerSkill.TACKLING,
    //       PlayerSkill.REFLEXES,
    //     );
    //   },
    // },
  ];

  return columns;
};
