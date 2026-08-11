import Box from '@mui/material/Box'
import Typography from '@mui/material/Typography'

type PitchPlayer = {
  id: number
  name: string
  position: string
}

type Slot = { top: string; left: string }

const FORMATION: Slot[] = [
  { top: '92%', left: '50%' }, // GK
  { top: '70%', left: '15%' }, // DEF
  { top: '70%', left: '38%' },
  { top: '70%', left: '62%' },
  { top: '70%', left: '85%' },
  { top: '45%', left: '15%' }, // MID
  { top: '45%', left: '38%' },
  { top: '45%', left: '62%' },
  { top: '45%', left: '85%' },
  { top: '18%', left: '35%' }, // FWD
  { top: '18%', left: '65%' },
]

const initials = (name: string) =>
  name
    .split(' ')
    .map((part) => part[0])
    .join('')
    .toUpperCase()

export default function Pitch({ players }: { players: PitchPlayer[] }) {
  const lineup = players.slice(0, FORMATION.length)

  return (
    <Box
      sx={{
        position: 'relative',
        width: '100%',
        maxWidth: '480px',
        aspectRatio: '2 / 3',
        background: 'linear-gradient(180deg, #2E7D32 0%, #1B5E20 100%)',
        borderRadius: '8px',
        border: '2px solid #fff',
        overflow: 'hidden',
      }}>
      {/* Pitch markings */}
      <Box sx={{ position: 'absolute', top: '50%', left: 0, right: 0, height: '2px', backgroundColor: 'rgba(255,255,255,0.5)' }} />
      <Box
        sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          width: '90px',
          height: '90px',
          border: '2px solid rgba(255,255,255,0.5)',
          borderRadius: '50%',
          transform: 'translate(-50%, -50%)',
        }}
      />
      <Box
        sx={{
          position: 'absolute',
          top: '2%',
          left: '50%',
          width: '140px',
          height: '60px',
          border: '2px solid rgba(255,255,255,0.5)',
          borderTop: 'none',
          transform: 'translateX(-50%)',
        }}
      />
      <Box
        sx={{
          position: 'absolute',
          bottom: '2%',
          left: '50%',
          width: '140px',
          height: '60px',
          border: '2px solid rgba(255,255,255,0.5)',
          borderBottom: 'none',
          transform: 'translateX(-50%)',
        }}
      />

      {lineup.map((player, index) => {
        const slot = FORMATION[index]
        return (
          <Box
            key={player.id}
            sx={{
              position: 'absolute',
              top: slot.top,
              left: slot.left,
              transform: 'translate(-50%, -50%)',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              width: '64px',
            }}>
            <Box
              sx={{
                width: '36px',
                height: '36px',
                borderRadius: '50%',
                backgroundColor: '#fff',
                border: '2px solid #FF3F84',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontWeight: 700,
                fontSize: '0.75rem',
                color: '#111',
              }}>
              {initials(player.name)}
            </Box>
            <Typography
              variant="caption"
              sx={{
                color: '#fff',
                textAlign: 'center',
                lineHeight: 1.1,
                marginTop: '2px',
                textShadow: '0 1px 2px rgba(0,0,0,0.6)',
              }}>
              {player.name}
            </Typography>
          </Box>
        )
      })}
    </Box>
  )
}
