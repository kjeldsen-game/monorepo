# This R script plots the cumulative duel wins between two players with different skills.
# If you're using IntelliJ there is an R plugin to run this script directly from the IDE.

p_win <- function(p1_skill, p2_skill) {
  (p1_skill - p2_skill + 100) / 200
}

# No adjustments made for losing many times in a row
plot_trials <- function(p1_skill, p2_skill, lim) {
  x <- 1:lim
  results <- sapply(x, function(x) {
    p <- p_win(p1_skill, p2_skill)
    if (p > runif(1)) {
      return(1)
    } else {
      return(0)
    }
  })
  y <- cumsum(results)
  plot(x, y, type = "l")
}

# Increase the probability of winning by x% for each loss
plot_adjusted <- function(p1_skill, p2_skill, lim) {
  x <- 1:lim
  p <- p_win(p1_skill, p2_skill)
  y <- NULL
  increase <- abs(p1_skill - p2_skill) / 1000

  for (i in 1:lim) {
    if (p > runif(1)) {
      y <- c(y, 1)
      p <- p_win(p1_skill, p2_skill)
    } else {
      y <- c(y, 0)
      p <- p + increase
    }
  }
  y <- cumsum(y)
  plot(x, y, type = "l")
}

p1_skill <- 30
p2_skill <- 70
trials <- 15
plot_trials(p1_skill, p2_skill, trials)
plot_adjusted(p1_skill, p2_skill, trials)

player_performance <- function(previous_performance) {
  perf <- runif(1, min_performance, max_performance) + (previous_performance / 2)
  max(perf, max_performance)
}

max_performance <- 15
min_performance <- -15
max_assistance <- 25
min_assistance <- -25

# Adjusting performance by limiting the range of the random number
plot_performance_adjustments <- function(p1_skill, p2_skill, lim) {
  x <- 1:lim
  y <- NULL
  p1_peformance <- 0
  p2_peformance <- 0
  for (i in 1:lim) {
    p1_performance <- player_performance(p1_peformance)
    p1_total <- p1_performance +
      runif(1, min_assistance, max_assistance) +
      p1_skill

    p2_performance <- player_performance(p2_peformance)
    p2_total <- p2_performance +
      runif(1, min_assistance, max_assistance) +
      p2_skill

    if (p1_total > p2_total) {
      y <- c(y, 1)
    } else {
      y <- c(y, 0)
    }
  }
  y <- cumsum(y)
  plot(x, y, type = "l",
       main = paste0("Player 1 (", p1_skill, ") vs Player 2 (", p2_skill, ")"),
       ylim = c(0, trials),
       xlab = "Trials",
       ylab = "Wins")
  grid(nx = NULL, ny = NULL, lty = 2, col = "gray", lwd = 2)
}

p1_skill <- 70
p2_skill <- 30
trials <- 100
plot_performance_adjustments(p1_skill, p2_skill, trials)