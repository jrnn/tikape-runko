package tikape.runko.domain;

public class AnnosAine {
    private final Integer aineId;
    private String aineNimi;
    private final Integer annosId;
    private final Integer jarjestys;
    private final String maara;
    private final String ohje;

    public AnnosAine(Integer aineId, String aineNimi, Integer annosId, Integer jarjestys, String maara, String ohje) {
        this.aineId = aineId;
        this.aineNimi = aineNimi;
        this.annosId = annosId;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }

    public Integer getAineId() {
        return this.aineId;
    }

    public String getAineNimi() {
        return this.aineNimi;
    }

    public void setAineNimi(String aineNimi) {
        this.aineNimi = aineNimi;
    }

    public Integer getAnnosId() {
        return this.annosId;
    }

    public Integer getJarjestys() {
        return this.jarjestys;
    }

    public String getMaara() {
        return this.maara;
    }

    public String getOhje() {
        return this.ohje;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.aineNimi).append(", ").append(this.maara);

        if (!this.ohje.isEmpty()) {
            sb.append(" (").append(this.ohje).append(")");
        }

        return sb.toString();
    }

}