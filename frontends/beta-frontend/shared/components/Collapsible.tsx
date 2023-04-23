import React, { useEffect, useRef, useState } from 'react'
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp'
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'
import { css } from '@emotion/react'
import Typography from '@mui/material/Typography'

interface IProps {
  open?: boolean
  title: string
  children: React.ReactNode
}

const card = css`
  border-radius: 0.5rem;
  border: 1px solid #e0e0e0;
  margin-bottom: 1rem;
  padding: 1rem;
`
const cardHeader = css`
  display: flex;
  justify-content: space-between;
  align-items: center;
`
const marginRight = css`
  margin-right: 1rem;
`

const content = css`
  transition: all 2s ease-in-out;
`

const Collapsible: React.FC<IProps> = ({ open, children, title }) => {
  const [isOpen, setIsOpen] = useState(open)

  const handleFilterOpening = () => {
    setIsOpen((prev) => !prev)
  }

  return (
    <>
      <div css={card}>
        <div css={cardHeader}>
          <Typography variant="h6" css={marginRight}>
            {title}
          </Typography>
          <button type="button" className="btn" onClick={handleFilterOpening}>
            {isOpen ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </button>
        </div>

        <div css={content}>
          <div>{isOpen && <div>{children}</div>}</div>
        </div>
      </div>
    </>
  )
}

export default Collapsible
