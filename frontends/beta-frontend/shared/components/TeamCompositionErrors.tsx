import styled from '@emotion/styled'
import { useTranslation } from 'react-i18next'
import { CompositionError } from '../models/CompositionError'

export const ErrorsContainer = styled.div`
  display: flex;
  flex-direction: column;
  font-size: 1.2rem;
  min-width: 500px;
`

type TeamCompositionErrorsProps = {
  errors: CompositionError[]
}

const TeamCompositionErrors: React.FC<TeamCompositionErrorsProps> = ({ errors }: TeamCompositionErrorsProps) => {
  const { t } = useTranslation(['game'])
  return (
    <ErrorsContainer>
      {errors.map((error) => (
        <div key={error.error}>- {t(`game:teamCompositionRules.${error.error}`, { value: error.value })}</div>
      ))}
    </ErrorsContainer>
  )
}

export default TeamCompositionErrors
