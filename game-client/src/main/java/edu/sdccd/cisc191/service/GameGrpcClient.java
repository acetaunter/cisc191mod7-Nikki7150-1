package edu.sdccd.cisc191.service;

import edu.sdccd.cisc191.grpc.GameServiceGrpc;
import edu.sdccd.cisc191.grpc.JoinMatchRequest;
import edu.sdccd.cisc191.grpc.JoinMatchResponse;
import edu.sdccd.cisc191.grpc.MatchHistoryRequest;
import edu.sdccd.cisc191.grpc.MatchHistoryResponse;
import edu.sdccd.cisc191.grpc.MatchResultResponse;
import edu.sdccd.cisc191.grpc.PlayMatchRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.concurrent.Task;

public class GameGrpcClient {

    private final ManagedChannel channel;
    private final GameServiceGrpc.GameServiceBlockingStub blockingStub;

    public GameGrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        this.blockingStub = GameServiceGrpc.newBlockingStub(channel);
    }

    public Task<JoinMatchResponse> joinMatchTask(
            String playerName,
            String difficulty,
            boolean ranked
    ) {
        return new Task<>() {
            @Override
            protected JoinMatchResponse call() {
                JoinMatchRequest request = buildJoinMatchRequest(playerName, difficulty, ranked);

                return blockingStub.joinMatch(request);
            }
        };
    }

    /**
     * Complete this client-side gRPC helper, then use it from joinMatchTask.
     *
     * Requirements:
     * - Build and return a JoinMatchRequest.
     * - Use "Player" when playerName is null or blank.
     * - Use "Normal" when difficulty is null or blank.
     * - Trim playerName and difficulty.
     * - Preserve the ranked value.
     */
    public static JoinMatchRequest buildJoinMatchRequest(String playerName, String difficulty, boolean ranked) {
        if (playerName == null || playerName.isBlank()) {
            playerName = "Player";
        }
        if (difficulty == null || difficulty.isBlank()) {
            difficulty = "Normal";
        }
        playerName = playerName.trim();
        difficulty = difficulty.trim();

        return JoinMatchRequest.newBuilder()
                .setPlayerName(playerName)
                .setDifficulty(difficulty)
                .setRanked(ranked)
                .build();
    }

    public Task<MatchHistoryResponse> loadMatchHistoryTask(String playerName) {
        return new Task<>() {
            @Override
            protected MatchHistoryResponse call() {
                MatchHistoryRequest request = MatchHistoryRequest.newBuilder()
                        .setPlayerName(playerName)
                        .build();

                return blockingStub.loadMatchHistory(request);
            }
        };
    }

    public void shutdown() {
        channel.shutdown();
    }

    public Task<MatchResultResponse> playMatchTask(String matchId, String name) {
        return null;
    }
}
