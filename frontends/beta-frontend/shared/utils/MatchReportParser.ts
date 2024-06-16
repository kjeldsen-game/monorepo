import { MatchReportPlayer, MatchReportType } from '../models/MatchReport'
import { PlayerPosition } from '../models/PlayerPosition'
import { MatchActionType, MatchEvent, MatchEventSide, PlayerEventStats } from '../models/MatchEvent'
import { PitchArea } from '../models/PitchArea'

const parsePlayer = (player: string): MatchReportPlayer => {
  const playerData = player.split(/\[|\]/)
  return {
    name: playerData[0].trim(),
    position: PlayerPosition[playerData[1] as PlayerPosition],
  }
}

const parseEvent = (stringData: string[], playerName: string): PlayerEventStats => {
  const result: PlayerEventStats = { playerName }
  stringData.forEach((row) => {
    switch (row.split(':')[0]) {
      case 'Skill contribution':
        result.skillContribution = parseInt(row.split(': ')[1], 10)
        break
      case 'Performance':
        result.performance = parseInt(row.split(': ')[1], 10)
        break
      case 'Assistance':
        result.assistance = parseInt(row.split(': ')[1], 10)
        break
      case 'Total':
        result.total = parseInt(row.split(': ')[1], 10)
        break
      case 'Carry over':
        result.carryOver = parseInt(row.split(': ')[1], 10)
        break

      default:
        break
    }
  })

  return result
}

const parseMatchReport = (event: string, homePlayers: MatchReportPlayer[]): MatchEvent => {
  const eventData = event.split('\n')
  const clock = parseInt(eventData[0].split(' ')[1], 10)
  const area = eventData[1].split(' ')[1] as PitchArea
  const action = eventData[2].split(' ')[1] as MatchActionType
  const eventStart = eventData[3]
  const eventResponse = eventData[4]
  const eventResult = eventData[5]

  const player1StatsString = []
  const player2StatsString = []

  const player1Name = eventData[7].split('─────────────────')[1].trim()
  let player2Name = ''

  let player1Done = false
  let lastIndex = 0
  for (let index = 8; !player1Done; index++) {
    if (eventData[index].startsWith('────')) {
      lastIndex = index
      player2Name = eventData[index].split('─────────────────')[1].trim()
      player1Done = true
    } else player1StatsString.push(eventData[index])
  }

  for (let index = lastIndex + 1; index < eventData.length; index++) {
    player2StatsString.push(eventData[index])
  }

  const player1Stats = parseEvent(player1StatsString, player1Name)
  const player2Stats = parseEvent(player2StatsString, player2Name)

  const eventSide: MatchEventSide = homePlayers.map((player) => player.name).includes(player1Name) ? 'HomeTeamEvent' : 'AwayTeamEvent'

  return {
    clock,
    area,
    action,
    eventStart,
    eventResponse,
    eventResult,
    eventSide,
    actionStats: {
      player1: player1Stats,
      player2: player2Stats,
    },
  }
}

export const parseReport = (report: string | undefined): MatchReportType => {
  const result: MatchReportType = {
    homePlayers: [],
    awayPlayers: [],
    events: [],
  }

  if (!report) return result

  const reportData = report.split('\n')
  const matchEventsString = report.split('\n\n')

  // Home team
  for (let index = 2; index < 13; index++) {
    result.homePlayers.push(parsePlayer(reportData[index]))
  }

  // Away team
  for (let index = 16; index < 27; index++) {
    result.awayPlayers.push(parsePlayer(reportData[index]))
  }

  for (let index = 6; index < matchEventsString.length - 3; index++) {
    result.events.push(parseMatchReport(matchEventsString[index], result.homePlayers))
  }

  return result
}