package tikape.runko.domain;

public class RaakaAine implements Comparable<RaakaAine> {
    private final Integer id;
    private final String nimi;

    public RaakaAine(Integer id, String nimi) {
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
    public int compareTo(RaakaAine o) {
        return this.getNimi().compareToIgnoreCase(o.getNimi());
    }

}