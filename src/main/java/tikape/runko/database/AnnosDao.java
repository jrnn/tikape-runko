package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tikape.runko.domain.Annos;

public class AnnosDao implements Dao<Annos, Integer> {
    private final Database db;

    public AnnosDao(Database db) {
        this.db = db;
    }

    @Override
    public Annos findOne(Integer key) throws SQLException {
        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "SELECT * FROM Annos WHERE id=?;"
            );
            ps.setInt(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return this.create(rs);
            }

        }

    }

    public Annos findByName(String nimi) throws SQLException {
        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "SELECT * FROM Annos WHERE nimi=?;"
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
    public List<Annos> findAll() throws SQLException {
        List<Annos> a = new ArrayList<>();

        try (Connection co = this.db.getConnection();
                ResultSet rs = co.prepareStatement(
                        "SELECT * FROM Annos;"
                ).executeQuery()) {

            while (rs.next()) {
                a.add(this.create(rs));
            }

        }

        // Listataan aakkosjärjestyksessä selkeyden vuoksi
        Collections.sort(a);
        return a;
    }

    @Override
    public Annos saveOrUpdate(Annos object) throws SQLException {
        String nimi = object.getNimi();
        if (nimi.isEmpty()) {
            return null;    // Hylätään nimettömät annokset
        }

        Annos a = this.findByName(nimi);
        if (a != null) {
            return a;
        }

        try (Connection co = this.db.getConnection()) {
            PreparedStatement ps = co.prepareStatement(
                    "INSERT INTO Annos (nimi) VALUES (?);"
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
                    "DELETE FROM Annos WHERE id=?;"
            );
            ps.setInt(1, key);
            ps.executeUpdate();
        }

    }

    // Luo ResultSetin rivistä Annos-olion
    private Annos create(ResultSet rs) throws SQLException {
        return new Annos(rs.getInt("id"), rs.getString("nimi"));
    }

}