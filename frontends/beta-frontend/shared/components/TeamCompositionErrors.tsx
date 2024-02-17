import styled from '@emotion/styled'
import { useTranslation } from 'react-i18next'

export const ErrorsContainer = styled.div`
  display: flex;
  flex-direction: column;
  font-size: 1.2rem;
  min-width: 500px;
`

type TeamCompositionErrorsProps = {
  errors: string[]
}

const TeamCompositionErrors: React.FC<TeamCompositionErrorsProps> = ({ errors }: TeamCompositionErrorsProps) => {
  const { t } = useTranslation(['game'])
  return (
    <ErrorsContainer>
      {errors.map((error) => (
        <div key={error}>- {t(`game:teamCompositionRules.${error}`)}</div>
      ))}
    </ErrorsContainer>
  )
}

export default TeamCompositionErrors
