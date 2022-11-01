import Box from '@mui/material/Box'
import Image from 'next/image'

function Avatar() {
  return (
    <Box
      sx={{
        width: '120',
        height: '120',
        border: '6px solid #A4BC10',
        borderRadius: '50%',
        backgroundColor: '#D9D9D9',
      }}>
      <Image src="/img/anonymous.png" alt="me" width="120" height="120" />
    </Box>
  )
}

export default Avatar

// vs

// import Box from '@mui/material/Box'

// export default function Avatar() {
//   return (
//     <Box
//       sx={{
//         borderRadius: '50%',
//       }}></Box>
//   )
// }
