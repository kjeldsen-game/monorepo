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

let me;
let opponentTeamId = 0;

refreshAuth();

$.ajaxSetup({
  beforeSend: function (xhr) {
    xhr.setRequestHeader('Authorization', '...');
  }
});

$("#generate").click(() => {
  let request = {
    data: JSON.stringify({away: {id: opponentTeamId}}),
    contentType: 'application/json',
    type: 'POST'
  }
  $.ajax("/matches", request)
  .done(report => {
    console.log(report)
    let homeTeamId = report.home.id;
    let awayTeamId = report.away.id;
    let startingTeamId = report.plays[0].initiator.teamId;
    console.log("Home team: " + homeTeamId);
    console.log("Away team: " + awayTeamId);
    console.log("Starting team: " + startingTeamId);
    let startingTeam = homeTeamId === startingTeamId ? "home" : "away"
    let narration = Narration.narrate(report.plays, startingTeamId);
    $("#narration").html(narration);
    $("#match-result").show();
    $("#final-score").html(report.homeScore + " - " + report.awayScore);
    let teamTable = $(`#${startingTeam}-team-table`).clone();
    $('#mini-table').html(teamTable);
    $("#starting-team").html(startingTeam);
    $("#end-result-fail").hide();
    $("#end-result-success").show();
  })
  .fail(data => {
    let error = JSON.parse(data.responseText);
    $("#response-message").addClass("alert-danger")
    .removeClass("alert-success").show();
    $("#response-message-text").html(error["message"]);

    // Show the plays to find out why the game failed
    let report = error["report"];
    let homeTeamId = 1;
    let awayTeamId = 6;
    let startingTeamId = report.plays[0].initiator.teamId;
    console.log("Home team: " + homeTeamId);
    console.log("Away team: " + awayTeamId);
    console.log("Starting team: " + startingTeamId);
    let startingTeam = homeTeamId === startingTeamId ? "home" : "away"
    let narration = Narration.narrate(report.plays, startingTeamId);
    $("#narration").html(narration);
    $("#match-result").show();
    $("#final-score").html(report.homeScore + " - " + report.awayScore);
    let teamTable = $(`#${startingTeam}-team-table`).clone();
    $('#mini-table').html(teamTable);
    $("#starting-team").html(startingTeam);
    $("#end-result-fail").show();
    $("#end-result-success").hide();
  });
});

$("#random-team-button").click(() => {
  $.get("/teams/random", team => {
    opponentTeamId = team.id;
    let awayRows = Teams.getTeamRows(team)
    $("#away-rows").html(awayRows);
    $("#away-tactic").html(team.tactic);
    $("#away-vertical-pressure").html(team.verticalPressure);
    $("#away-horizontal-pressure").html(team.horizontalPressure);
    $(".away-team").show();
  });
});

$("#toggle-team").on("change", function () {
  if ($(this).is(":checked")) {
    $('#mini-table').show();
  } else {
    $('#mini-table').hide();
  }
});

// Tabs

function switchTab(tab) {
  $(".nav-link").removeClass("active");
  $("#" + tab + "-tab").addClass("active");
  $(".tab").hide();
  $("#" + tab).show();
}

$("#teams-tab").click(() => {
  switchTab("teams");
});

$("#plays-tab").click(() => {
  switchTab("plays");
});

$("#analysis-tab").click(() => {
  switchTab("analysis");
});

// Pages

function switchPage(page) {
  $("#response-message").hide();
  $(".nav-button").removeClass("active");
  $("#" + page + "-page-link").addClass("active");
  $(".page").hide();
  $("#" + page + "-page").show();
}

$("#team-page-link").click(() => {
  switchPage("team");
});

$("#logout-link").click(() => {
  $.get("/logout", () => {
    window.location.href = "signin.html";
  });
});

$("#game-page-link").click(() => {
  $.get("/users/me", user => {
    me = user;

    $.get(`/team/${me.team.id}`, team => {
      let homeRows = Teams.getTeamRows(team)
      $("#home-rows").html(homeRows);
      // Modifiers
      $("#home-tactic").html(team.tactic);
      $("#home-vertical-pressure").html(team.verticalPressure);
      $("#home-horizontal-pressure").html(team.horizontalPressure);
    });
    switchPage("game");
  });
});

$("#stats-page-link").click(() => {
  switchPage("stats");
});

$("#manual-page-link").click(() => {
  switchPage("manual");
});

class Teams {

  static getTeamRows(team) {
    let positions = ["GOALKEEPER", "LEFT_BACK", "CENTER_BACK", "RIGHT_BACK",
      "RIGHT_WINGER", "LEFT_WINGER", "LEFT_MIDFIELDER", "CENTER_MIDFIELDER",
      "RIGHT_MIDFIELDER", "FORWARD", "STRIKER"]
    let orders =
        ["PASS_FORWARD", "LONG_SHOT", "PASS_TO_AREA", "CHANGE_FLANK", "SHOOT",
          "DRIBBLE"];

    return team.players.map(player => {
      let positionDropdown = `${positions.map(position => {
        return `<option>${position}</option>`
      }).join("")}`;

      let ordersDropdown = `${orders.map(order => {
        return `<option>${order}</option>`
      }).join("")}`;
      return `<tr>
                <td>${player.name}</td>
                <td>
                  <div class="form-group">
                    <select id="${player.id}" class="form-control player-position-select">
                      <option>${player.position}</option>
                      ${positionDropdown}
                    </select>
                  </div>
                </td>
                <td>
                  <div class="form-group">
                    <select id="${player.id}" class="form-control player-order-select">
                      <option>${player.playerOrder}</option>
                      ${ordersDropdown}
                    </select>
                  </div>
                </td>
                <td>${player.skills.SCORING}</td>
                <td>${player.skills.OFFENSIVE_POSITIONING}</td>
                <td>${player.skills.BALL_CONTROL}</td>
                <td>${player.skills.PASSING}</td>
                <td>${player.skills.AERIAL_ABILITY}</td>
                <td>${player.skills.TACKLING}</td>
                <td>${player.skills.DEFENSIVE_POSITIONING}</td>
                <td>${player.skills.REFLEXES}</td>
                <td>${player.skills.INTERCEPTING}</td>
                <td class="table-dark">${player.skills.EXPERIENCE}</td>
                <td class="table-dark">${player.skills.RATING}</td>
              </tr>`
    }).join("");
  }
}

$(document).on('change', '.player-position-select', function () {
  let playerId = $(this).attr("id");
  let position = $(this).val();
  let player = {
    position: position,
  };
  console.log(player)
  let request = {
    data: JSON.stringify(player),
    contentType: 'application/json',
    type: 'PATCH'
  }
  $.ajax(`/players/${playerId}`, request)
  .fail(data => {
    let error = JSON.parse(data.responseText);
    $("#response-message").addClass("alert-danger")
    .removeClass("alert-success").show();
    $("#response-message-text").html(error["message"]);
  })
});

$(document).on('change', '.player-order-select', function () {
  let playerId = $(this).attr("id");
  let order = $(this).val();
  let player = {
    playerOrder: order,
  };
  let request = {
    data: JSON.stringify(player),
    contentType: 'application/json',
    type: 'PATCH'
  }
  $.ajax(`/players/${playerId}`, request)
  .fail(data => {
    let error = JSON.parse(data.responseText);
    $("#response-message").addClass("alert-danger")
    .removeClass("alert-success").show();
    $("#response-message-text").html(error["message"]);
  })
});

class Narration {
  static narrate(plays, teamPerspective) {
    return "<div class='play'>"
        + plays.map(
            play => Narration.describePlay(play, teamPerspective))
        .join("</p><p>")
        + "</div>";
  }

  static areaFromPerspective(play, teamPerspective) {
    let pitchArea = play.pitchArea;
    if (play.initiator.teamId !== teamPerspective) {
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
    let pitchArea = Narration.areaFromPerspective(play, teamPerspective);

    const meta = Narration.getMeta(play, pitchArea);
    const initiator = play.initiator;
    const field = Narration.getField(pitchArea);
    let attempt = `<b>${initiator.name}</b>
                         <span class="badge bg-secondary">${initiator.position}</span>
                         ${Narration.describeInitiatorAction(play)}`;

    if (play.receiver != null) {
      const receiver = play.receiver;
      attempt += ` to <b>${receiver.name}</b>
                  <span class="badge bg-secondary">${receiver.position}</span>`;
    }
    const challenger = play.challenger;
    const response = `Challenger:
                            <b>${challenger?.name}</b>
                            <span class="badge bg-secondary">${challenger?.position}</span>
                            ${Narration.describeChallengerAction(play)}`;

    let outcome;
    if (play.action === "POSITION") {
      outcome = Narration.describePositionalOutcome(
          play, initiator, challenger);
    } else {
      outcome = Narration.describeOutcome(play, initiator, challenger);
    }

    const stats = Narration.getDuelStats(play);
    return `<p>${meta}</p>
            <div class="row">
              <div class="col-2"> ${field} </div>
              <div class="col-8">
                <div>
                  <p>${attempt}</p>
                  <p>${response}</p>
                  <i class="text-primary">${outcome}</i>
                </div>
                <br>
                <div>${stats}</div>
              </div>
            </div>`;
  }

  static getMeta(play, pitchArea) {
    return `<div class="d-flex justify-content-center">
              <div class="text-bg-primary text-center play-divider">
              <p class="meta">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-clock-fill" viewBox="0 0 16 16">
                  <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71V3.5z"/>
                </svg>
                ${play.clock} min
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
              <p class="meta">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-info-circle" viewBox="0 0 16 16">
                  <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                  <path d="m8.93 6.588-2.29.287-.082.38.45.083c.294.07.352.176.288.469l-.738 3.468c-.194.897.105 1.319.808 1.319.545 0 1.178-.252 1.465-.598l.088-.416c-.2.176-.492.246-.686.246-.275 0-.375-.193-.304-.533L8.93 6.588zM9 4.5a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"/>
                </svg>
                ${play.origin}
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
    <div style="position: relative">
      <img alt="field" src="./field.png" style="width: 100%;">
      <div style="position: absolute; background-color: rgba(67,148,255,0.68); ${highlightStyle}"></div>
    </div>
  `;
  }

  static describeOutcome(play, initiator, challenger) {
    const initiatorName = initiator.name;
    const challengerName = challenger.name;
    if (play.result === "WIN") {
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
            play.initiatorStats.performance
            + play.initiatorStats.skillPoints);
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
            play.challengerStats.performance
            + play.challengerStats.skillPoints);
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

  static getDuelStats(play) {
    const initiatorStats = play.initiatorStats;
    const challengerStats = play.challengerStats;
    return `${Narration.getPlayerStats(
        play.duelType,
        play.initiator.name,
        initiatorStats,
        play.challenger?.name,
        challengerStats)}`;
  }

  static getPlayerStats(
      duelType,
      initiatorName,
      initiatorStats,
      challengerName,
      challengerStats) {
    let stats = "<table class='table table-striped-columns'>";
    stats += `<tr>
                <th></th>
                <th>${initiatorName}</th>
                <th>${challengerName}</th>
                </tr>`;
    if (initiatorStats.skillPoints != null) {
      stats += `<tr>
                  <td style="text-align: right">Skill contribution</td>
                  <td>${initiatorStats.skillPoints}</td>
                  <td>${challengerStats.skillPoints}</td>
                </tr>`;
    }
    if (initiatorStats.performance != null) {
      stats += `<tr>
                  <td style="text-align: right">Performance</td>
                  <td>${initiatorStats.performance}</td>
                  <td>${challengerStats.performance}</td>
                </tr>`;
    }
    if (initiatorStats.assistance != null) {
      let initiatorId = Math.random().toString(36).slice(2, 10)
      let initiatorTotal =
          Object.values(initiatorStats.teamAssistance)
          .reduce((acc, val) => acc + val, 0);
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
                              </ul>`).join("") + "<hr> <b>Total = </b> "
          + initiatorTotal}
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
              </div>
            </div>
          </div>`;

      let challengerId = Math.random().toString(36).slice(2, 10)
      let challengerTotal =
          Object.values(challengerStats.teamAssistance)
          .reduce((acc, val) => acc + val, 0);
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
                              </ul>`).join("") + "<hr> <b>Total = </b> "
          + challengerTotal}
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
    if (initiatorStats.carryover != null) {
      stats += `<tr>
                 <td style="text-align: right">Carryover</td>    
                 <td>${initiatorStats.carryover}</td>
                 <td>${challengerStats.carryover}</td>
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
    if (assistance <= 4) {
      return 'awful';
    }
    if (assistance <= 9) {
      return 'poor';
    }
    if (assistance <= 14) {
      return 'weak';
    }
    if (assistance <= 19) {
      return 'decent';
    }
    if (assistance <= 24) {
      return 'good';
    }
    if (assistance <= 25) {
      return 'excellent';
    }
    if (assistance <= 30) {
      return 'superb';
    }
    if (assistance <= 35) {
      return 'brilliant';
    }
    if (assistance <= 40) {
      return 'awesome';
    }
    if (assistance <= 45) {
      return 'masterful';
    }
    return 'unbelievable';
  }

  static describePositionalOutcome(play, initiator, challenger) {

    let initiatorAssistance =
        Narration.describeAssistance(play.initiatorStats.assistance);
    let challengerAssistance =
        Narration.describeAssistance(play.challengerStats.assistance);

    let initiatorOutcome =
        `<b>${initiator.name}</b> had <i>${initiatorAssistance}</i> spacing help`;

    let challengerOutcome =
        `<b>${challenger?.name}</b> received <i>${challengerAssistance}</i> defensive support`;

    let difference =
        play.initiatorStats.total - play.challengerStats.total;
    let position = Narration.describePosition(-difference);
    let won = difference > 0;
    let outcome = won
        ? `<p>${initiator.name} won, ${challenger?.name} was ${position}`
        : `<p>${initiator.name} lost, ${challenger?.name} was ${position}`;
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

const tooltipTriggerList = document.querySelectorAll(
    '[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(
    tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))
const popoverTriggerList = document.querySelectorAll(
    '[data-bs-toggle="popover"]')
const popoverList = [...popoverTriggerList].map(
    popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl))

// setTimeout(() => {
//   $("#generate").trigger("click");
// setTimeout(() => {
//   $("#plays-tab").trigger("click");
// }, 50);
// }, 50);

$("#sign-up-form").on("submit", event => {
  event.preventDefault();
  let data = {
    email: $("#email").val(),
    password: $("#password").val()
  };
  let request = {
    data: JSON.stringify(data),
    contentType: 'application/json',
    type: 'POST'
  }
  $.ajax("/users", request)
  .done(res => {
    $.ajax("/auth/token", request)
    .done(data => {
      refreshAuth();
    });

    $("#response-message")
    .addClass("alert-success")
    .removeClass("alert-danger").show();
    $("#response-message-text").html("Account created").show();
    $("#create-team-button").show();
  })
  .fail(data => {
    let error = JSON.parse(data.responseText);
    $("#response-message").addClass("alert-danger")
    .removeClass("alert-success").show();
    $("#response-message-text").html(error["message"]);
  })
});

$("#sign-in-form").on("submit", event => {
  event.preventDefault();
  let data = {
    email: $("#email").val(),
    password: $("#password").val()
  };
  let request = {
    data: JSON.stringify(data),
    contentType: 'application/json',
    type: 'POST'
  }
  $.ajax("/auth/token", request)
  .done(data => {
    $("#response-message-text").html("You are signed in")
    .addClass("alert-success")
    .removeClass("alert-danger")
    .show();

    refreshAuth();
    window.location.href = "home.html";
  })
  .fail(data => {
    let error = JSON.parse(data.responseText);
    $("#response-message").addClass("alert-danger")
    .removeClass("alert-success").show();
    $("#response-message-text").html(error["message"]);
  })
});

$("#availablePositions li").draggable({
  helper: "clone"
});

$("#generate-default-team").click(() => {
  let players = ["GOALKEEPER", "LEFT_BACK", "CENTER_BACK", "RIGHT_BACK",
    "RIGHT_WINGER", "LEFT_WINGER", "LEFT_MIDFIELDER", "CENTER_MIDFIELDER",
    "RIGHT_MIDFIELDER", "FORWARD", "STRIKER"]

  $("#selected-positions").empty();
  $.each(players, function (key, value) {
    $('#selected-positions').append(
        `<h5><li class="badge text-bg-secondary" data-position="${value}">${value}</li></h5>`
    );
  });
});

$("#clear-team").click(() => {
  $("#selected-positions").empty();
  $('#selected-positions').append(
      `<h5><li class="badge text-bg-secondary" data-position="GOALKEEPER">GOALKEEPER</li></h5>`
  );
});

$("#selected-positions-box").droppable({
  accept: "#availablePositions li",
  drop: function (event, ui) {
    const position = ui.helper.data("position");
    $("#selected-positions").append(
        `<h5><li class="badge text-bg-secondary" data-position="${position}">${position}</li></h5>`
    );
  }
});

$("#generate-new-team").click(() => {
  let players = [];

  $("#selected-positions").children().each(function () {
    players.push({
      position: $(this).text()
    });
  });
  let data = {
    players: players
  }
  let request = {
    data: JSON.stringify(data),
    contentType: 'application/json',
    type: 'POST'
  }
  $.ajax(`/teams`, request)
  .done(function (data) {
    $("#response-message")
    .addClass("alert-success").removeClass("alert-danger").show();
    $("#response-message-text").html("Team created")

  })
  .fail(function (data) {
    let error = JSON.parse(data.responseText);
    $("#response-message").addClass("alert-danger")
    .removeClass("alert-success").show();
    $("#response-message-text").html(error["message"]);
  });
});

$("#dismiss-alert").click(() => {
  $("#response-message").hide();
});

$("#tacticForm").submit(function (event) {
  event.preventDefault();

  let selectedTactic = $("#tacticDropdown").val();
  let selectedVerticalPressure = $("#verticalPressureDropdown").val();
  let selectedHorizontalPressure = $("#horizontalPressureDropdown").val();

  let request = {
    type: 'PATCH',
    data: JSON.stringify({
      tactic: selectedTactic,
      verticalPressure: selectedVerticalPressure,
      horizontalPressure: selectedHorizontalPressure
    }),
    contentType: 'application/json'
  }
  $.ajax(`/teams/${me.team.id}`, request)
  .done(function (data) {
    $("#home-tactic").html(data.tactic);
    $("#home-vertical-pressure").html(data.verticalPressure);
    $("#home-horizontal-pressure").html(data.horizontalPressure);
  });
});

// Heatmap

let heatmap = h337.create({
  radius: 100,
  container: document.querySelector('.heatmap')
});

let points = [];
let max = 20; // make dynamic
let width = 512;
let height = 512;
let positions = {
  "LEFT_BACK": 5,
  "CENTER_BACK": 15,
  "RIGHT_BACK": 19,
  "LEFT_MIDFIELD": 15,
  "CENTER_MIDFIELD": 17,
  "RIGHT_MIDFIELD": 25,
  "LEFT_FORWARD": 7,
  "CENTER_FORWARD": 8,
  "RIGHT_FORWARD": 10
};

for (const [key, value] of Object.entries(positions)) {
  let file = key.split("_")[0];
  let x_val;
  if (file === "LEFT") {
    x_val = 0.25;
  } else if (file === "CENTER") {
    x_val = 0.5;
  } else if (file === "RIGHT") {
    x_val = 0.75;
  }
  let rank = key.split("_")[1];
  let y_val;
  if (rank === "BACK") {
    y_val = 0.25;
  } else if (rank === "MIDFIELD") {
    y_val = 0.5;
  } else if (rank === "FORWARD") {
    y_val = 0.75;
  }
  let point = {
    x: x_val * height,
    y: y_val * width,
    value: value
  };
  points.push(point);
}
let data = {
  max: max,
  data: points
};
heatmap.setData(data);

function refreshAuth() {
  $.get("/users/me", user => {
    me = user;
  });
}