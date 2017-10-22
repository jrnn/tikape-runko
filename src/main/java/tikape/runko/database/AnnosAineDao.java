package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.AnnosAine;

public class AnnosAineDao implements Dao<AnnosAine, Integer> {
    private final Database db;

    public AnnosAineDao(Database db) {
        this.db = db;
    }

    @Override
    public AnnosAine findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not applicable.");
    }

    public AnnosAine findOne(Integer aineId, Integer annosId) throws SQLException {
        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "SELECT * FROM AnnosRaakaAine WHERE raaka_aine_id=? AND annos_id=?;"
            );
            ps.setInt(1, aineId);
            ps.setInt(2, annosId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return this.create(rs);
            }

        }

    }

    @Override
    public List<AnnosAine> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not applicable.");
    }

    public List<AnnosAine> findAllFor(Integer annosId) throws SQLException {
        List<AnnosAine> a = new ArrayList<>();

        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "SELECT * FROM AnnosRaakaAine WHERE annos_id=?;"
            );
            ps.setInt(1, annosId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                a.add(this.create(rs));
            }

        }

        return a;
    }

    @Override
    public AnnosAine saveOrUpdate(AnnosAine object) throws SQLException {
        Integer aineId = object.getAineId();
        Integer annosId = object.getAnnosId();
        AnnosAine a = this.findOne(aineId, annosId);

        if (a != null) {
            return a;
        }

        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement("INSERT INTO AnnosRaakaAine "
                    + "(raaka_aine_id, annos_id, jarjestys, maara, ohje) "
                    + "VALUES (?, ?, ?, ?, ?);"
            );
            ps.setInt(1, aineId);
            ps.setInt(2, annosId);
            ps.setInt(3, object.getJarjestys());
            ps.setString(4, object.getMaara());
            ps.setString(5, object.getOhje());
            ps.executeUpdate();
        }

        return this.findOne(aineId, annosId);
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not applicable.");
    }

    public void delete(Integer aineId, Integer annosId) throws SQLException {
        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "DELETE FROM AnnosRaakaAine WHERE raaka_aine_id=? AND annos_id=?;"
            );
            ps.setInt(1, aineId);
            ps.setInt(2, annosId);
            ps.executeUpdate();
        }

    }

    public void deleteFor(Integer annosId) throws SQLException {
        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "DELETE FROM AnnosRaakaAine WHERE annos_id=?;"
            );
            ps.setInt(1, annosId);
            ps.executeUpdate();
        }

    }

    private AnnosAine create(ResultSet rs) throws SQLException {
        return new AnnosAine(
                rs.getInt("raaka_aine_id"),
                "",
                rs.getInt("annos_id"),
                rs.getInt("jarjestys"),
                rs.getString("maara"),
                rs.getString("ohje")
        );

    }

}