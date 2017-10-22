package tikape.runko.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    private final String dbAddress;

    public Database(String dbAddress) throws ClassNotFoundException {
        this.dbAddress = dbAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbAddress);
    }

    public void init() {
        ArrayList<String> lauseet = sqliteLauseet();

        try (Connection co = this.getConnection()) {
            Statement st = co.createStatement();

            for (String l : lauseet) {
                System.out.println("Running command >> " + l);
                st.executeUpdate(l);
            }
        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }

    }

    private ArrayList<String> sqliteLauseet() {
        ArrayList<String> l = new ArrayList<>();

        l.add("CREATE TABLE Annos (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    nimi varchar(255)\n"
                + ");");
        l.add("CREATE TABLE RaakaAine (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    nimi varchar(255)\n"
                + ");");
        l.add("CREATE TABLE AnnosRaakaAine (\n"
                + "    raaka_aine_id integer,\n"
                + "    annos_id integer,\n"
                + "    jarjestys integer,\n"
                + "    maara varchar(255),\n"
                + "    ohje varchar(255),\n"
                + "    FOREIGN KEY (raaka_aine_id) REFERENCES RaakaAine(id),\n"
                + "    FOREIGN KEY (annos_id) REFERENCES Annos(id)\n"
                + ");");

        return l;
    }

}