// Toisin kuin liitostaulussa, luokka sisältää myös tiedon raaka-aineen nimestä.
// Tämä helpottaa drinkkikohtaisten ainesten listaamista. Oliota vastaava Dao-luokka
// ohittaa tämän attribuutin kokonaan, koska se ei tietokannan kanssa keskusteltaessa
// ole oleellinen. Raaka-aineiden nimet täytetään vain, kun drinkkikohtainen sivu
// avataan (ks. "purukumiratkaisu" Main-luokan lopussa).

package tikape.runko.domain;

public class AnnosAine implements Comparable<AnnosAine> {
    private final Integer aineId;
    private String aineNimi; // <--Tämä on se kummajainen jota yllä kommentoidaan.
    private final Integer annosId;
    private final Integer jarjestys;
    private final String maara;
    private final String ohje;

    public AnnosAine(Integer aineId, Integer annosId, Integer jarjestys, String maara, String ohje) {
        this.aineId = aineId;
        this.aineNimi = "";
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

    @Override // Merkkijonoesitystä käytetään annos.html-sivulle tulostettaessa
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.aineNimi).append(", ").append(this.maara);

        if (!this.ohje.isEmpty()) {
            sb.append(" (").append(this.ohje).append(")");
        }

        return sb.toString();
    }

    @Override // AnnosAineet järjestetään annetun "jarjestys"-luvun mukaan
    public int compareTo(AnnosAine o) {
        return this.jarjestys - o.getJarjestys();
    }

}