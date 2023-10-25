/*
 *
 *
 *
 * *****************************************************************************
 *                  IGNORE - this is for testing purpose only                  *
 * *****************************************************************************
 *
 *
 *
 */

let startingTeam;

$("#generate").click(() => {
  $("#game").show();
  $.get("/create", data => {
    let homeTeam = data.home.id.uuid;
    let awayTeam = data.away.id.uuid;
    startingTeam = data.plays[0].duel.initiator.teamId.uuid;
    console.log("Home team: " + homeTeam);
    console.log("Away team: " + awayTeam);
    console.log("Starting team: " + startingTeam);
    let team = homeTeam === startingTeam ? "home" : "away"

    let narration = Narration.narrate(data.plays, startingTeam);
    $("#narration").html(narration);

    let homeRows = Teams.getTeamRows(data.home)
    $("#home-rows").html(homeRows);
    let awayRows = Teams.getTeamRows(data.away)
    $("#away-rows").html(awayRows);

    $("#final-score").html(data.homeScore + " - " + data.awayScore);

    let teamTable = $(`#${team}-team-table`).clone();
    $('#mini-table').html(teamTable);
    $("#starting-team").html(team);
  });
});

$("#toggle-team").on("change", function () {
  if ($(this).is(":checked")) {
    $('#mini-table').show();
  } else {
    $('#mini-table').hide();
  }
});

$("#teams-tab").click(() => {
  switchTab("teams");
});

$("#plays-tab").click(() => {
  switchTab("plays");
});

$("#stats-page-link").click(() => {
  $("#stats-page").show();
  $("#stats-page-link").addClass("active");
  $("#game-page").hide();
  $("#game-page-link").removeClass("active");
});

$("#game-page-link").click(() => {
  $("#game-page").show();
  $("#game-page-link").addClass("active");
  $("#stats-page").hide();
  $("#stats-page-link").removeClass("active");
});

function switchTab(tab) {
  $(".nav-link").removeClass("active");
  $("#" + tab + "-tab").addClass("active");
  $(".tab").hide();
  $("#" + tab).show();
}

class Teams {

  static getTeamRows(team) {
    return team.players.map(player => {
      return `<tr>
                <td>${player.name}</td>
                <td>${player.position}</td>
                <td>${player.skillSet.OFFENSIVE_POSITIONING}</td>
                <td>${player.skillSet.DEFENSIVE_POSITIONING}</td>
                <td>${player.skillSet.TACKLING}</td>
                <td>${player.skillSet.BALL_CONTROL}</td>
                <td>${player.skillSet.SCORING}</td>
                <td>${player.skillSet.REFLEXES}</td>
                <td>${player.skillSet.PASSING}</td>
                <td>${player.skillSet.INTERCEPTING}</td>
                <td class="table-dark">${player.skillSet.EXPERIENCE}</td>
                <td class="table-dark">${player.skillSet.AERIAL_ABILITY}</td>
                <td class="table-dark">${player.skillSet.RATING}</td>
              </tr>`
    }).join("");
  }
}

class Narration {
  static narrate(plays, teamPerspective) {
    return "<div class='play'>"
        + plays.map(
            play => Narration.describePlay(play, teamPerspective))
        .join("</p><p>")
        + "</div>";
  }

  static areaFromPerspective(duel, teamPerspective) {
    let pitchArea = duel.pitchArea;
    if (duel.initiator.teamId.uuid !== teamPerspective) {
      if (pitchArea.includes("LEFT")) {
        pitchArea = pitchArea.replace("LEFT", "RIGHT");
      } else if (pitchArea.includes("RIGHT")) {
        pitchArea = pitchArea.replace("RIGHT", "LEFT");
      }
      if (pitchArea.includes("FORWARD")) {
        pitchArea = pitchArea.replace("FORWARD", "BACK");
      } else if (pitchArea.includes("BACK")) {
        pitchArea = pitchArea.replace("BACK", "FORWARD");
      }
    }
    return pitchArea;
  }

  static describePlay(play, teamPerspective) {
    let pitchArea = Narration.areaFromPerspective(play.duel, teamPerspective);

    const meta = Narration.getMeta(play, pitchArea);
    const initiator = play.duel.initiator;
    const field = Narration.getField(pitchArea);
    let attempt = `<b>${initiator.name}</b>
                         <span class="badge bg-secondary">${initiator.position}</span>
                         ${Narration.describeInitiatorAction(play)}`;

    if (play.duel.receiver !== null) {
      const receiver = play.duel.receiver;
      attempt += ` to <b>${receiver.name}</b>
                  <span class="badge bg-secondary">${receiver.position}</span>`;
    }
    const challenger = play.duel.challenger;
    const response = `Duel challenger:
                            <b>${challenger.name}</b>
                            <span class="badge bg-secondary">${challenger.position}</span>
                            ${Narration.describeChallengerAction(play)}`;

    let outcome;
    if (play.action === "POSITION") {
      outcome = Narration.describePositionalOutcome(
          play, initiator, challenger);
    } else {
      outcome = Narration.describeOutcome(play, initiator, challenger);
    }

    const stats = Narration.getDuelStats(play.duel);
    return `<p>${meta}</p>
            <div class="d-flex">
              <div>
                ${field}
              </div>
              <div>
                <p>${attempt}</p>
                <p>${response}</p>
                <hr>
                <i class="text-primary">${outcome}</i>
              </div>
            </div>
            <p>${stats}</p>`;
  }

  static getMeta(play, pitchArea) {
    return `<div class="d-flex justify-content-center">
              <div class="text-bg-primary text-center play-divider">
              <p class="meta">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-clock-fill" viewBox="0 0 16 16">
                  <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71V3.5z"/>
                </svg>
                ${play.minute} min
              </p>
              <p class="meta">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-play-fill" viewBox="0 0 16 16">
                  <path d="m11.596 8.697-6.363 3.692c-.54.313-1.233-.066-1.233-.697V4.308c0-.63.692-1.01 1.233-.696l6.363 3.692a.802.802 0 0 1 0 1.393z"/>
                </svg>
                Action: ${play.action}
              </p>
              <p class="meta">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-geo-alt-fill" viewBox="0 0 16 16">
                  <path d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10zm0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6z"/>
                </svg>
                ${pitchArea}
              </p>
            </div>
          </div>`
  }

  static getField(field) {
    const areas = {
      LEFT_FORWARD: 'top: 0; left: 0; width: 33.33%; height: 33.33%;',
      CENTER_FORWARD: 'top: 0; left: 33.33%; width: 33.33%; height: 33.33%;',
      RIGHT_FORWARD: 'top: 0; left: 66.66%; width: 33.33%; height: 33.33%;',
      LEFT_MIDFIELD: 'top: 33.33%; left: 0; width: 33.33%; height: 33.33%;',
      CENTER_MIDFIELD: 'top: 33.33%; left: 33.33%; width: 33.33%; height: 33.33%;',
      RIGHT_MIDFIELD: 'top: 33.33%; left: 66.66%; width: 33.33%; height: 33.33%;',
      LEFT_BACK: 'top: 66.66%; left: 0; width: 33.33%; height: 33.33%;',
      CENTER_BACK: 'top: 66.66%; left: 33.33%; width: 33.33%; height: 33.33%;',
      RIGHT_BACK: 'top: 66.66%; left: 66.66%; width: 33.33%; height: 33.33%;',
    };
    const highlightStyle = areas[field] || '';
    return `
    <div style="position: relative; margin-right: 2em">
      <img alt="field" src="./field.png" style="width: 100%;">
      <div style="position: absolute; background-color: rgba(67,148,255,0.68); ${highlightStyle}"></div>
    </div>
  `;
  }

  static describeOutcome(play, initiator, challenger) {
    const initiatorName = initiator.name;
    const challengerName = challenger.name;
    if (play.duel.result === "WIN") {
      switch (play.action) {
        case "PASS":
          return "The pass was successful (interceptions disabled)";
        case "TACKLE":
          return `${initiatorName} stole the ball`;
        case "SHOOT":
          return `${initiatorName} scored a goal`;
      }
    }

    switch (play.action) {
      case "PASS":
        return `${challengerName} intercepted the ball`;
      case "TACKLE":
        return `${challengerName} controlled the ball`;
      case "SHOOT":
        return `${challengerName} saved the ball`;
    }
  }

  static describeInitiatorAction(play) {
    const action = play.action;
    const denomination =
        Narration.describePerformance(
            play.duel.initiatorStats.performance
            + play.duel.initiatorStats.skillPoints);
    switch (action) {
      case "PASS":
        return `made ${denomination} pass`;
      case "TACKLE":
        return `attempted ${denomination} tackle`;
      case "SHOOT":
        return `made ${denomination} shot`;
      case "POSITION":
        return `tried with ${denomination} effort to get free`;
    }
  }

  static describeChallengerAction(play) {
    const action = play.action;
    const denomination =
        Narration.describePerformance(
            play.duel.challengerStats.performance
            + play.duel.challengerStats.skillPoints);
    switch (action) {
      case "PASS":
        return `made ${denomination} attempt to intercept the ball`;
      case "TACKLE":
        return `made ${denomination} dribble`;
      case "SHOOT":
        return `attempted ${denomination} save`;
      case "POSITION":
        return `made ${denomination} attempt to stay close`;
    }
  }

  static getDuelStats(duel) {
    const initiatorStats = duel.initiatorStats;
    const challengerStats = duel.challengerStats;
    return `${Narration.getPlayerStats(
        duel.type,
        duel.initiator.name,
        initiatorStats,
        duel.challenger.name,
        challengerStats)}`;
  }

  static getPlayerStats(
      duelType,
      initiatorName,
      initiatorStats,
      challengerName,
      challengerStats) {
    let stats = "<table class='table table-striped-columns'>";
    // put stats in a black pill with white text
    stats += `<tr>
                <th> 
                  <span>
                  ${duelType} duel stats
                  </span>
                </th>
                <th>${initiatorName}</th>
                <th>${challengerName}</th>
                </tr>`;
    if (initiatorStats.skillPoints !== null) {
      stats += `<tr>
                  <td style="text-align: right">Skill contribution</td>
                  <td>${initiatorStats.skillPoints}</td>
                  <td>${challengerStats.skillPoints}</td>
                </tr>`;
    }
    if (initiatorStats.performance !== null) {
      stats += `<tr>
                  <td style="text-align: right">Performance</td>
                  <td>${initiatorStats.performance}</td>
                  <td>${challengerStats.performance}</td>
                </tr>`;
    }
    if (initiatorStats.assistance !== null) {
      let initiatorId = Math.random().toString(36).slice(2, 10)
      let initiatorAssistance =
          `<div class="modal fade" id="${initiatorId}" tabindex="-1">
            <div class="modal-dialog">
              <div class="modal-content">
                <div class="modal-header">
                  <h1 class="modal-title fs-5" id="exampleModalLabel">Assistance</h1>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    ${Object.keys(initiatorStats.teamAssistance).map(key => `
                              <ul>
                                <b>${key}</b>            
                                <span>${initiatorStats.teamAssistance[key]}</span>
                              </ul>`).join("")}
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
              </div>
            </div>
          </div>`;

      let challengerId = Math.random().toString(36).slice(2, 10)
      let challengerAssistance =
          `<div class="modal fade" id="${challengerId}" tabindex="-1">
            <div class="modal-dialog">
              <div class="modal-content">
                <div class="modal-header">
                  <h1 class="modal-title fs-5" id="exampleModalLabel">Assistance</h1>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    ${Object.keys(challengerStats.teamAssistance).map(key => `
                              <ul>
                                <b>${key}</b>            
                                <span>${challengerStats.teamAssistance[key]}</span>
                              </ul>`).join("")}
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
              </div>
            </div>
          </div>`;

      stats += `<tr>
                  <td style="text-align: right">Assistance</td>
                  <td><u data-bs-toggle="modal" data-bs-target="#${initiatorId}">${initiatorStats.assistance}</u></td>
                  <td><u data-bs-toggle="modal" data-bs-target="#${challengerId}">${challengerStats.assistance}</u></td>
                </tr>` + initiatorAssistance + challengerAssistance;
    }
    if (initiatorStats.carryover !== null) {
      stats += `<tr>
                 <td style="text-align: right">Carryover</td>    
                 <td>${initiatorStats.carryover}</td>
                 <td>n/a</td>
               </tr>`;
    }
    if (initiatorStats.total !== null) {
      stats += `<tr>
                  <td style="text-align: right"><b>Total</b></td>    
                  <td><b>${initiatorStats.total}</b></td>
                  <td><b>${challengerStats.total}</b></td>
                </tr>`;
    }
    return stats + "</table>"

  }

  static describePosition(positionalDuelPerformance) {
    if (positionalDuelPerformance <= -21) {
      return 'very far';
    }
    if (positionalDuelPerformance <= -16) {
      return 'far';
    }
    if (positionalDuelPerformance <= -11) {
      return 'in the vicinity';
    }
    if (positionalDuelPerformance <= -6) {
      return 'close';
    }
    if (positionalDuelPerformance <= -1) {
      return 'almost there';
    }
    if (positionalDuelPerformance <= 0) {
      return 'there';
    }
    if (positionalDuelPerformance <= 5) {
      return 'just about';
    }
    if (positionalDuelPerformance <= 10) {
      return 'near';
    }
    if (positionalDuelPerformance <= 15) {
      return 'within reach';
    }
    if (positionalDuelPerformance <= 20) {
      return 'ready';
    }
    return 'in perfect position';
  }

  static describePerformance(performance) {
    if (performance < 15) {
      return 'an awful';
    }
    if (performance < 30) {
      return 'a poor';
    }
    if (performance < 40) {
      return 'a weak';
    }
    if (performance < 50) {
      return 'a decent';
    }
    if (performance < 60) {
      return 'a good';
    }
    if (performance < 70) {
      return 'an excellent';
    }
    if (performance < 80) {
      return 'a superb';
    }
    if (performance < 90) {
      return 'a brilliant';
    }
    if (performance < 100) {
      return 'an awesome';
    }
    if (performance < 110) {
      return 'a masterful';
    }
    return 'an unbelievable';
  }

  static describeAssistance(assistance) {
    if (assistance <= -21) {
      return 'awful';
    }
    if (assistance <= -16) {
      return 'poor';
    }
    if (assistance <= -11) {
      return 'weak';
    }
    if (assistance <= -6) {
      return 'decent';
    }
    if (assistance <= -1) {
      return 'good';
    }
    if (assistance <= 0) {
      return 'excellent';
    }
    if (assistance <= 5) {
      return 'superb';
    }
    if (assistance <= 10) {
      return 'brilliant';
    }
    if (assistance <= 15) {
      return 'awesome';
    }
    if (assistance <= 20) {
      return 'masterful';
    }
    return 'unbelievable';
  }

  static describePositionalOutcome(play, initiator, challenger) {

    let initiatorAssistance =
        Narration.describeAssistance(play.duel.initiatorStats.assistance);
    let challengerAssistance =
        Narration.describeAssistance(play.duel.challengerStats.assistance);

    let initiatorOutcome =
        `<b>${initiator.name}</b> had <i>${initiatorAssistance}</i> spacing help`;

    let challengerOutcome =
        `<b>${challenger.name}</b> received <i>${challengerAssistance}</i> defensive support`;

    let difference =
        play.duel.initiatorStats.total - play.duel.challengerStats.total;
    let position = Narration.describePosition(-difference);
    let won = difference > 0;
    let outcome = won
        ? `<p>${initiator.name} won, ${challenger.name} was ${position}`
        : `<p>${initiator.name} lost, ${challenger.name} was ${position}`;
    return `<div class="text-primary">
              <p>${initiatorOutcome}</p>
              <p>${challengerOutcome}</p>
              <i>${outcome}</i>
            </div>`;
  }
}

// Stats

// Defaults
let max_performance = 15;
let min_performance = -15;
let max_assistance = 25;
let min_assistance = -25;
let p1_skill = 50;
let p2_skill = 50;
let trials = 100;

$("#stats-button, #stats-button-two").click(() => {
  updateParams();
  plot_performance_adjustments(p1_skill, p2_skill, trials);
  $("#plot-placeholder").hide();
});

function updateParams() {
  let max_p = $("#performance-max").val();
  if (max_p !== "") {
    max_performance = parseInt(max_p);
  }
  let min_p = $("#performance-min").val();
  if (min_p !== "") {
    min_performance = parseInt(min_p);
  }
  let max_a = $("#assistance-max").val();
  if (max_a !== "") {
    max_assistance = parseInt(max_a);
  }
  let min_a = $("#assistance-min").val();
  if (min_a !== "") {
    min_assistance = parseInt(min_a);
  }
  let p1 = $("#p1-skill").val();
  if (p1 !== "") {
    p1_skill = parseInt(p1);
  }
  let p2 = $("#p2-skill").val();
  if (p2 !== "") {
    p2_skill = parseInt(p2);
  }
  let t = $("#trials").val();
  if (t !== "") {
    trials = parseInt(t);
  }

  console.log(`max_performance: ${max_performance}`);
  console.log(`min_performance: ${min_performance}`);
  console.log(`max_assistance: ${max_assistance}`);
  console.log(`min_assistance: ${min_assistance}`);
  console.log(`p1_skill: ${p1_skill}`);
  console.log(`p2_skill: ${p2_skill}`);
  console.log(`trials: ${trials}`);
}

function player_performance(prev_performance) {
  let perf = Math.random() * (max_performance - min_performance);
  let adjustment = prev_performance / 3;
  return perf - adjustment;
}

function player_assistance() {
  return Math.random() * (max_assistance - min_assistance) + min_assistance;
}

function plot_performance_adjustments(p1_skill, p2_skill, lim, trials) {
  const x = Array.from({length: lim}, (_, i) => i + 1);
  const y = [];
  let p1_performance = 0;
  let p2_performance = 0;

  for (let i = 0; i < lim; i++) {
    p1_performance = player_performance(p1_performance);
    const p1_total = p1_performance
        + player_assistance()
        + p1_skill;

    p2_performance = player_performance(p2_performance);
    const p2_total = p2_performance
        + player_assistance()
        + p2_skill;

    if (p1_total > p2_total) {
      y.push(1);
    } else {
      y.push(0);
    }
  }

  const trace = {
    x: x,
    y: y.reduce((acc, val) => {
      acc.push(acc[acc.length - 1] + val);
      return acc;
    }, [0]),
    type: 'scatter',
    mode: 'lines',
    name: `Player 1 (${p1_skill}) vs Player 2 (${p2_skill})`,
  };

  const layout = {
    title: `Player 1 (${p1_skill}) vs Player 2 (${p2_skill})`,
    xaxis: {title: 'Trials'},
    yaxis: {title: 'Wins'},
  };

  const data = [trace];

  Plotly.newPlot('plot', data, layout);
}

const tooltipTriggerList =
    document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList =
    [...tooltipTriggerList].map(
        tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))

// setTimeout(() => {
//   $("#generate").trigger("click");
//   setTimeout(() => {
//     $("#plays-tab").trigger("click");
//   }, 50);
// }, 50);
