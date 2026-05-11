package edu.sdccd.cisc191.model;

import java.util.concurrent.atomic.AtomicInteger;

public class MatchViewModel {
    private String matchId;
    private final Player player = new Player("Player");
    private final Player opponent = new Player("Opponent");
    private boolean matchOver;
    private String winnerName = "";

    // Make this shared counter thread-safe.
    // Use either an AtomicInteger field or synchronized methods so background tasks cannot lose updates.
    private final AtomicInteger completedMatchCount = new AtomicInteger(0);

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public boolean isMatchOver() {
        return matchOver;
    }

    public void setMatchOver(boolean matchOver) {
        this.matchOver = matchOver;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName == null ? "" : winnerName;
    }

    public int getCompletedMatchCount() {
        return completedMatchCount.get();
    }

    /**
     * Complete this method using thread-safe programming.
     *
     * This model may be updated after JavaFX background tasks finish. Make sure concurrent
     * calls do not lose completed-match updates. You may use synchronized methods or an
     * AtomicInteger.
     *
     * Requirements:
     * - Increase the completed match count exactly once per call.
     * - Store the winner name using the existing null-safe setter.
     * - Mark the match as over.
     * - Protect shared state from race conditions.
     */
    public void recordCompletedMatchThreadSafely(String winnerName) {
        completedMatchCount.incrementAndGet();
        setWinnerName(winnerName);
        matchOver = true;
    }

    public boolean hasJoinedMatch() {
        return matchId != null && !matchId.isBlank();
    }

    public boolean canPlayMatch() {
        return hasJoinedMatch() && !matchOver;
    }

    /**
     * Complete this MVC helper.
     *
     * Return a short summary for the bottom of the JavaFX screen.
     * Expected format:
     * Match match-001: Ada vs Bot (Hard, ranked)
     *
     * Requirements:
     * - Use "No match" when matchId is null or blank.
     * - Use the current player and opponent names from this model.
     * - Use "Normal" when difficulty is null or blank.
     * - Use "ranked" when ranked is true, otherwise "casual".
     */
    public String buildMatchSummary(String difficulty, boolean ranked) {
        if (matchId == null && matchId.isBlank()) {
            return "No match found";
        }
        if (difficulty == null && difficulty.isBlank()) {
            return "Normal";
        }

        String matchType = ranked ? "ranked" : "casual";

        return "Match" + matchId + ": "
                + player.getName() + " vs "
                + opponent.getName() + " ("
                + difficulty + ", "
                + matchType + ")";
    }

    public void resetLocalState() {
        matchId = null;
        player.setName("Player");
        opponent.setName("Opponent");
        matchOver = false;
        winnerName = "";
        completedMatchCount.set(0);
    }
}
