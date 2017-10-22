package tikape.runko.domain;

public class Annos implements Comparable<Annos> {
    private final Integer id;
    private final String nimi;

    public Annos(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }

    public Integer getId() {
        return this.id;
    }

    public String getNimi() {
        return this.nimi;
    }

    @Override
    public int compareTo(Annos o) {
        return this.getNimi().compareToIgnoreCase(o.getNimi());
    }

}