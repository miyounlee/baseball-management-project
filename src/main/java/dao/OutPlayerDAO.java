package dao;


import dto.OutPlayerRespDTO;
import java.sql.*;
import java.util.*;

public class OutPlayerDAO {
    private static Connection connection;

    private static final OutPlayerDAO INSTANCE = new OutPlayerDAO();
    private OutPlayerDAO() {
    }
    public static OutPlayerDAO getInstance() {
        return INSTANCE;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    // 퇴출 선수 생성
    public static int createOutPlayer(int playerId, String reason) {
        String query = "INSERT INTO out_player (player_id, reason) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            statement.setString(2, reason);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    // 퇴출 선수 목록 조회
    public List<OutPlayerRespDTO> getOutPlayerList() {
        List<OutPlayerRespDTO> outPlayers = new ArrayList<>();
        String query ="SELECT p.id playerId, p.name, p.position, o.reason, o.created_at\n" +
                "FROM out_player o \n" +
                "RIGHT OUTER JOIN player p ON o.player_id = p.id";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    outPlayers.add(buildOutPlayerRespDtoFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return outPlayers;
    }

    private OutPlayerRespDTO buildOutPlayerRespDtoFromResultSet(ResultSet resultSet) {
        try {
            Integer playerId = resultSet.getInt("playerId");
            String name = resultSet.getString("name");
            String position = resultSet.getString("position");
            String reason = resultSet.getString("reason");
            Timestamp outcreatedAt= resultSet.getTimestamp("created_at");

            return OutPlayerRespDTO.builder()
                    .playerId(playerId)
                    .name(name)
                    .position(position)
                    .reason(reason)
                    .outcreatedAt(outcreatedAt)
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

