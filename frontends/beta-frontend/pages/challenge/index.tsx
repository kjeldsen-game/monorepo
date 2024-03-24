import type { NextPage } from 'next'
import { serverSideTranslations } from 'next-i18next/serverSideTranslations'
import { LeagueGrid } from './components/LeagueGrid'
import { Box, Tab, Tabs } from '@mui/material'
import { useState } from 'react'
import { CustomTabPanel } from '@/shared/components/Tab/CustomTabPanel'
import { useSession } from 'next-auth/react'
import { InviteGrid } from './components/InviteGrid'

const Team: NextPage = () => {
  useSession({ required: true })

  const [value, setValue] = useState(0)

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue)
  }

  return (
    <Box sx={{ width: '100%' }}>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
          <Tab label="Challenge" />
          <Tab label="Accepted challenges" />
        </Tabs>
      </Box>
      <CustomTabPanel value={value} index={0}>
        <InviteGrid />
        <LeagueGrid />
      </CustomTabPanel>
      <CustomTabPanel value={value} index={1}>
        Item Two
      </CustomTabPanel>
    </Box>
  )
}

export async function getStaticProps({ locale }: { locale: string }) {
  return {
    props: {
      ...(await serverSideTranslations(locale, ['common', 'game'])),
      // Will be passed to the page component as props
    },
  }
}

export default Team
