import type { NextPage } from 'next'
import Head from 'next/head'
import Collapsible from '@/shared/components/Collapsible'
import Box from '@mui/material/Box'
import Grid from '@/shared/components/Grid/Grid'
import { players } from '@/data/SamplePlayerTraining'
import { sampleTrainingColumn } from '@/data/sampleTrainingColumn'
import { serverSideTranslations } from 'next-i18next/serverSideTranslations'

const Training: NextPage = () => {
  return (
    <>
      <Head>
        <title>Kjeldsen</title>
        <meta name="description" content="Generated by create next app" />
      </Head>
      <>
        <Box>
          <Collapsible open title="Yesterday's Training Report">
            <Box>
              <Grid rows={players} columns={sampleTrainingColumn} />
            </Box>
          </Collapsible>
          <Collapsible title="Yesterday -1 Training Report">
            <Box>
              <Grid rows={players} columns={sampleTrainingColumn} />
            </Box>
          </Collapsible>
          <Collapsible title="Yesterday -2 Training Report">
            <Box>
              <Grid rows={players} columns={sampleTrainingColumn} />
            </Box>
          </Collapsible>
          <Collapsible title="Yesterday -3 Training Report">
            <Box>
              <Grid rows={players} columns={sampleTrainingColumn} />
            </Box>
          </Collapsible>
          <Collapsible title="Yesterday -4 Training Report">
            <Box>
              <Grid rows={players} columns={sampleTrainingColumn} />
            </Box>
          </Collapsible>
          <Collapsible title="Yesterday -5 Training Report">
            <Box>
              <Grid rows={players} columns={sampleTrainingColumn} />
            </Box>
          </Collapsible>
        </Box>
      </>
    </>
  )
}

export async function getStaticProps({ locale }: { locale: string }) {
  return {
    props: {
      ...(await serverSideTranslations(locale, ['common'])),
      // Will be passed to the page component as props
    },
  }
}

export default Training
