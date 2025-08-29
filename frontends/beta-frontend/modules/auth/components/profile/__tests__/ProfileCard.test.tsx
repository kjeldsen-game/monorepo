import { render } from '@testing-library/react'
import '@testing-library/jest-dom'
import ProfileCard from '../ProfileCard'

describe('ProfileCard', () => {
    it('renders children correctly', () => {
        const { getByText } = render(
            <ProfileCard>
                <div>Test Content</div>
            </ProfileCard>
        )

        expect(getByText('Test Content')).toBeInTheDocument()
    })
})
