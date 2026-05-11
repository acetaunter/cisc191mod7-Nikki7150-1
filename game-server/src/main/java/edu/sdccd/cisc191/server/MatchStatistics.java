package edu.sdccd.cisc191.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Tracks server-wide match statistics shared by many gRPC request threads.
 */
public class MatchStatistics {

    // Replace these counters with a thread-safe design.
    // Recommended: AtomicInteger joinedMatchCount and AtomicInteger completedMatchCount.
    private final AtomicInteger joinedMatchCount = new AtomicInteger(0);
    private final AtomicInteger completedMatchCount = new AtomicInteger(0);

    private final AtomicInteger rankedJoinCount = new AtomicInteger(0);
    private final AtomicInteger casualJoinCount = new AtomicInteger(0);

    private final ConcurrentHashMap<String, AtomicInteger> difficultyMap = new ConcurrentHashMap<>();

    /**
     * Make this update thread-safe.
     */

    public void recordJoin() {
        recordJoin("Normal", false);
    }

    public void recordJoin(String difficulty, boolean ranked) {
        joinedMatchCount.incrementAndGet();

        if (ranked) {
            rankedJoinCount.incrementAndGet();
        }
        else {
            casualJoinCount.incrementAndGet();
        }

        difficultyMap
                .computeIfAbsent(difficulty == null ? "Unknown" : difficulty,
                        k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    /**
     * Make this update thread-safe.
     */
    public void recordCompletion() {
        completedMatchCount.incrementAndGet();
    }

    public int getTotalJoins(){
        return joinedMatchCount.get();
    }

    public int getRankedJoins(){
        return rankedJoinCount.get();
    }

    public int getCasualJoins(){
        return casualJoinCount.get();
    }

    public int getJoinCountForDifficulty(String difficulty){
        AtomicInteger difficultyCount = difficultyMap.get(difficulty);
        return difficultyCount == null ? 0 : difficultyCount.get();
    }

    public int getJoinedMatchCount() {
        return joinedMatchCount.get();
    }

    public int getCompletedMatchCount() {
        return completedMatchCount.get();
    }

    /**
     * Return a readable, thread-safe statistics summary.
     *
     * Expected format:
     * Server stats: 3 joined, 2 completed
     */
    public String buildStatusLine() {
        return "Server stats: "
                + joinedMatchCount.get() + " joined, "
                + completedMatchCount.get() + " completed.";
    }
}
