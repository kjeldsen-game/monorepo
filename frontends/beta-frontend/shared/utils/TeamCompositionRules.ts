import { CompositionError } from '../models/CompositionError'
import { Player } from '../models/Player'
import { PlayerPosition } from '../models/PlayerPosition'

const checkTeamComposition = (teamPlayers: Player[]): CompositionError[] => {
  const errors: CompositionError[] = []
  // Size of the team = 11
  if (teamPlayers.length !== 11) {
    errors.push({ error: 'totalActivePlayers', value: teamPlayers.length })
  }

  // Defense between 3 and 5
  const totalDefenders = teamPlayers.filter((player) =>
    [PlayerPosition.SWEEPER, PlayerPosition.LEFT_BACK, PlayerPosition.RIGHT_BACK, PlayerPosition.CENTRE_BACK].includes(player.position),
  ).length
  if (totalDefenders > 5 || totalDefenders < 3) {
    errors.push({ error: 'totalDefense', value: totalDefenders })
  }

  // Sweepers between 0 and 1
  const totalSweepers = teamPlayers.filter((player) => player.position === PlayerPosition.SWEEPER).length
  if (totalSweepers > 1) {
    errors.push({ error: 'totalSweepers', value: totalSweepers })
  }

  // At least 1 left (wing) back defense
  const totalLeftBacks = teamPlayers.filter((player) => player.position === PlayerPosition.LEFT_BACK).length
  const totalLeftWingBacks = teamPlayers.filter((player) => player.position === PlayerPosition.LEFT_WINGBACK).length
  if (totalLeftBacks > 0 && totalLeftWingBacks > 0) {
    errors.push({ error: 'totalLeftBacks' })
  }

  // At least 1 right (wing) back defense
  const totalRightBacks = teamPlayers.filter((player) => player.position === PlayerPosition.RIGHT_BACK).length
  const totalRightWingBacks = teamPlayers.filter((player) => player.position === PlayerPosition.RIGHT_WINGBACK).length
  if (totalRightBacks > 0 && totalRightWingBacks > 0) {
    errors.push({ error: 'totalRightBacks' })
  }

  // Midfielders between 3 and 6
  const totalMidfielders = teamPlayers.filter((player) =>
    [
      PlayerPosition.LEFT_MIDFIELDER,
      PlayerPosition.RIGHT_MIDFIELDER,
      PlayerPosition.CENTRE_MIDFIELDER,
      PlayerPosition.DEFENSIVE_MIDFIELDER,
      PlayerPosition.OFFENSIVE_MIDFIELDER,
    ].includes(player.position),
  ).length
  if (totalMidfielders > 6 || totalMidfielders < 3) {
    errors.push({ error: 'totalMidfielders', value: totalMidfielders })
  }

  // At least one center
  const totalAllCenterMidfielders = teamPlayers.filter((player) =>
    [PlayerPosition.DEFENSIVE_MIDFIELDER, PlayerPosition.CENTRE_MIDFIELDER, PlayerPosition.OFFENSIVE_MIDFIELDER].includes(player.position),
  ).length
  if (totalAllCenterMidfielders < 1) {
    errors.push({ error: 'totalAllCenterMidfielders', value: totalAllCenterMidfielders })
  }

  // No more than one Defensive / Offensive Midfielder
  const totalDefensiveOffensiveMidfielders = teamPlayers.filter((player) =>
    [PlayerPosition.DEFENSIVE_MIDFIELDER, PlayerPosition.OFFENSIVE_MIDFIELDER].includes(player.position),
  ).length
  if (totalDefensiveOffensiveMidfielders > 1) {
    errors.push({ error: 'totalDefensiveOffensiveMidfielders', value: totalDefensiveOffensiveMidfielders })
  }

  // No more than 3 center midfielders
  const totalCenterMidfielders = teamPlayers.filter((player) => player.position === PlayerPosition.CENTRE_MIDFIELDER).length
  if (totalCenterMidfielders > 3) {
    errors.push({ error: 'totalCenterMidfielders', value: totalCenterMidfielders })
  }

  // Between 1 and 3 Center Backs
  const totalCenterBacks = teamPlayers.filter((player) => player.position === PlayerPosition.CENTRE_BACK).length
  if (totalCenterBacks > 3 || totalCenterBacks < 1) {
    errors.push({ error: 'totalCenterBacks', value: totalCenterBacks })
  }

  // At least 1 left midfield or winger
  const totalLeftMidfielders = teamPlayers.filter((player) => player.position === PlayerPosition.LEFT_MIDFIELDER).length
  const totalLeftWingers = teamPlayers.filter((player) => player.position === PlayerPosition.LEFT_WINGER).length
  if (totalLeftMidfielders > 0 && totalLeftWingers > 0) {
    errors.push({ error: 'totalLeftMidfielders', value: totalLeftMidfielders })
  }

  // At least 1 right midfield or winger
  const totalRightMidfielders = teamPlayers.filter((player) => player.position === PlayerPosition.RIGHT_MIDFIELDER).length
  const totalRightWingers = teamPlayers.filter((player) => player.position === PlayerPosition.RIGHT_WINGER).length
  if (totalRightMidfielders > 0 && totalRightWingers > 0) {
    errors.push({ error: 'totalRightMidfielders' })
  }

  // Between 1 and 3 Forwards
  const totalForwards = teamPlayers.filter((player) => player.position === PlayerPosition.FORWARD).length
  if (totalForwards > 3 || totalForwards < 1) {
    errors.push({ error: 'totalForwards', value: totalForwards })
  }

  // Max 1 striker
  const totalStrikers = teamPlayers.filter((player) => player.position === PlayerPosition.STRIKER).length
  if (totalStrikers > 1) {
    errors.push({ error: 'totalStrikers', value: totalStrikers })
  }

  return errors
}

export default checkTeamComposition
