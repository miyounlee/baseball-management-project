package service;

import db.DBConnection;
import dto.OutPlayerRespDTO;
import dao.OutPlayerDAO;
import dao.PlayerDAO;

import java.sql.*;
import java.util.List;

public class OutPlayerService {
    private PlayerDAO playerDao;
    private OutPlayerDAO outPlayerDao;
    private Connection connection;

    public OutPlayerService(PlayerDAO playerDao, OutPlayerDAO outPlayerDao, Connection connection) {
        this.playerDao = playerDao;
        this.outPlayerDao = outPlayerDao;
        this.connection = DBConnection.getInstance();
    }

    // 퇴출 선수 등록
    public String createOutPlayer(int playerId, String reason) throws SQLException {
        connection.setAutoCommit(false);
        int outPlayerResult = outPlayerDao.createOutPlayer(playerId, reason);
        int playerResult = playerDao.updatePlayer(playerId);

        if (outPlayerResult == 1 && playerResult == 1) {
            connection.commit();
            return "성공";
        }
        connection.rollback();
        return "실패";
    }

    // 퇴출 선수 목록 조회
    public List<OutPlayerRespDTO>  getOutPlayerList() {
        List<OutPlayerRespDTO> outPlayers = outPlayerDao.getOutPlayerList();
        if (outPlayers == null) {
            throw new RuntimeException("퇴출선수 목록 조회 오류발생");
        }
        return outPlayers;
    }
}

