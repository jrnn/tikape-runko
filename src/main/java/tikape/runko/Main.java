package tikape.runko;

import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;
import tikape.runko.domain.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Database db = new Database("jdbc:sqlite:annokset.db");
        db.init();
        AnnosDao annokset = new AnnosDao(db);
        RaakaAineDao aineet = new RaakaAineDao(db);
        AnnosAineDao liitokset = new AnnosAineDao(db);

        Spark.get("/", (req, res) -> {
            HashMap hm = new HashMap<>();
            hm.put("annokset", annokset.findAll());

            return new ModelAndView(hm, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/aineet", (req, res) -> {
            HashMap hm = new HashMap<>();
            hm.put("aineet", aineet.findAll());

            return new ModelAndView(hm, "aineet");
        }, new ThymeleafTemplateEngine());

        Spark.post("/aineet", (req, res) -> {
            RaakaAine r = new RaakaAine(-1, req.queryParams("nimi"));
            aineet.saveOrUpdate(r);

            res.redirect("/aineet");
            return "";
        });

        Spark.get("/annokset", (req, res) -> {
            HashMap hm = new HashMap<>();
            hm.put("annokset", annokset.findAll());
            hm.put("aineet", aineet.findAll());

            return new ModelAndView(hm, "annokset");
        }, new ThymeleafTemplateEngine());

        Spark.post("/annokset", (req, res) -> {
            Annos a = new Annos(-1, req.queryParams("nimi"));
            annokset.saveOrUpdate(a);

            res.redirect("/annokset");
            return "";
        });

        Spark.post("/lisaa", (req, res) -> {
            // Hylätään kelvottomat tai puutteelliset syötteet jo tässä kohtaa
            // try-catch -rakenteella. Ikävä kyllä käyttäjä ei saa mitään
            // kuittausta siitä, onnistuiko lisäys vai ei.
            try {
                AnnosAine a = new AnnosAine(
                        Integer.parseInt(req.queryParams("aine_id")),
                        Integer.parseInt(req.queryParams("annos_id")),
                        Integer.parseInt(req.queryParams("jarjestys")),
                        req.queryParams("maara"),
                        req.queryParams("ohje")
                );

                liitokset.saveOrUpdate(a);
            } catch (Exception e) {
                e.printStackTrace();
            }

            res.redirect("/annokset");
            return "";
        });

        Spark.get("/annokset/:id", (req, res) -> {
            Integer annosId = Integer.parseInt(req.params(":id"));
            List<AnnosAine> a = setAineNimiForAll(
                    liitokset.findAllFor(annosId),
                    aineet.findAll()
            ); // Liitetään drinkin raaka-aineisiin nimet

            HashMap hm = new HashMap<>();
            hm.put("annos", annokset.findOne(annosId));
            hm.put("aineet", a);

            return new ModelAndView(hm, "annos");
        }, new ThymeleafTemplateEngine());

        Spark.post("/poista/:id", (req, res) -> {
            Integer annosId = Integer.parseInt(req.params(":id"));
            annokset.delete(annosId);
            liitokset.deleteFor(annosId);

            res.redirect("/annokset");
            return "";
        });

    }

    // Purukumiratkaisu jolla liitetään raaka-aineiden nimet liitostaulusta
    // palautetulle otokselle (käytetään vain sivulla annos.html, joka listaa
    // yksittäisen drinkin ainesosat).
    public static List<AnnosAine> setAineNimiForAll(List<AnnosAine> aa, List<RaakaAine> ra) {
        for (AnnosAine a : aa) {
            Integer aineId = a.getAineId();

            for (RaakaAine r : ra) {
                if (r.getId().equals(aineId)) {
                    a.setAineNimi(r.getNimi());
                }

            }

        }

        return aa;
    }

}