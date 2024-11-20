import {
  GridCellParams,
  GridColDef,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import {
  PlayerPosition,
  PlayerPositionColorNew,
} from '@/shared/models/PlayerPosition';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import ProgressBar from '../../Training/ProgressBar';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import { Box, Typography } from '@mui/material';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { baseColumnConfig, leftColumnConfig } from './ColumnsConfig';
import { formatPlayerSkills } from '@/shared/utils/ColumnUtils';
import {
  PlayerSkill,
  PlayerSkillToShortcut,
} from '@/shared/models/PlayerSkill';

export const trainingScheduleColumns = (
  handleCellClick: (
    skillToTrain: PlayerSkill | undefined,
    skillUnderTraining: PlayerSkill | undefined,
    playerIdToTrain: string,
  ) => void,
) => {
  console.log(['PASSING'].includes('PASSING'));
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
    {
      ...leftColumnConfig,
      field: 'name',
      renderHeader: () => <div>Name</div>,
      minWidth: 130,
      renderCell: (params: GridCellParams) => (
        <Link
          style={{ textDecoration: 'none', color: '#000000' }}
          passHref
          href={`/player/${params.row.player.id}`}>
          {params.row.player.name}
        </Link>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'age',
      renderHeader: () => <div>Age</div>,
      minWidth: 70,
      valueGetter: (params: GridValueGetterParams) =>
        params.row.player.age.years,
    },
    {
      ...baseColumnConfig,
      field: 'playerPosition',
      renderHeader: () => <div>Position</div>,
      renderCell: (params) => {
        const position = params.row.player
          .position as keyof typeof PlayerPosition;
        const initials = getPositionInitials(position);

        return (
          <div
            style={{
              color: '#FFFFFF',
              padding: '2px 8px 2px 8px',
              width: '42px',
              height: '24px',
              borderRadius: '5px',
              textAlign: 'center',
              background: PlayerPositionColorNew[position],
            }}>
            {initials}
          </div>
        );
      },
      minWidth: 50,
      flex: 1,
    },
    {
      field: 'cs',
      renderHeader: () => <div>CS</div>,
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
    {
      field: 'DEFENSIVE_POSITIONING',
      renderHeader: () => <div>DP</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => {
        return renderSkillCell(
          params,
          PlayerSkill.DEFENSIVE_POSITIONING,
          PlayerSkill.CONTROL,
        );
      },
    },
    {
      field: 'BALL_CONTROL',
      renderHeader: () => <div>BC</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => {
        return renderSkillCell(
          params,
          PlayerSkill.BALL_CONTROL,
          PlayerSkill.INTERCEPTIONS,
        );
      },
    },
    {
      field: 'SCORE',
      renderHeader: () => <div>SC</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => {
        return renderSkillCell(
          params,
          PlayerSkill.SCORING,
          PlayerSkill.ONE_ON_ONE,
        );
      },
    },
    {
      field: 'PASSING',
      renderHeader: () => <div>PA</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => {
        return renderSkillCell(
          params,
          PlayerSkill.PASSING,
          PlayerSkill.ORGANIZATION,
        );
      },
    },
    {
      field: 'OFFENSIVE_POSITIONING',
      renderHeader: () => <div>OP</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => {
        return renderSkillCell(
          params,
          PlayerSkill.OFFENSIVE_POSITIONING,
          PlayerSkill.POSITIONING,
        );
      },
    },
    {
      field: 'TACKLING',
      renderHeader: () => <div>TA</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => {
        return renderSkillCell(
          params,
          PlayerSkill.TACKLING,
          PlayerSkill.REFLEXES,
        );
      },
    },
  ];

  return columns;
};
