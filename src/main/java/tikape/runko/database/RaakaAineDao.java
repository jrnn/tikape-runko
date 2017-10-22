package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tikape.runko.domain.RaakaAine;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {
    private final Database db;

    public RaakaAineDao(Database db) {
        this.db = db;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "SELECT * FROM RaakaAine WHERE id=?;"
            );
            ps.setInt(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return this.create(rs);
            }

        }

    }

    public RaakaAine findByName(String nimi) throws SQLException {
        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "SELECT * FROM RaakaAine WHERE nimi=?;"
            );
            ps.setString(1, nimi);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return this.create(rs);
            }

        }

    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {
        List<RaakaAine> r = new ArrayList<>();

        try (Connection co = this.db.getConnection();
                ResultSet rs = co.prepareStatement(
                        "SELECT * FROM RaakaAine;"
                ).executeQuery()) {

            while (rs.next()) {
                r.add(this.create(rs));
            }

        }

        Collections.sort(r);
        return r;
    }

    @Override
    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException {
        String nimi = object.getNimi();
        RaakaAine r = this.findByName(nimi);

        if (r != null) {
            return r;
        }

        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "INSERT INTO RaakaAine (nimi) VALUES (?);"
            );
            ps.setString(1, nimi);
            ps.executeUpdate();
        }

        return this.findByName(nimi);
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "DELETE FROM RaakaAine WHERE id=?;"
            );
            ps.setInt(1, key);
            ps.executeUpdate();
        }

    }

    private RaakaAine create(ResultSet rs) throws SQLException {
        return new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
    }

}