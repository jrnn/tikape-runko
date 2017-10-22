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
            HashMap map = new HashMap<>();
            map.put("viesti", "Ding dong shämmä lämmä. Tykkäätteks skändinääveist ääkkösist mitä häh. Tykkäättekxs?!?!");

            return new ModelAndView(map, "index");
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
            try {
                AnnosAine a = new AnnosAine(
                        Integer.parseInt(req.queryParams("aine_id")),
                        "",
                        Integer.parseInt(req.queryParams("annos_id")),
                        Integer.parseInt(req.queryParams("jarjestys")),
                        req.queryParams("maara"),
                        req.queryParams("ohje")
                );
                System.out.println(a);//debugging
                liitokset.saveOrUpdate(a);
            } catch (Exception e) {
                System.out.println("Jotain meni pieleen: " + e);//debugging
                e.printStackTrace();//debugging
            }

            res.redirect("/annokset");
            return "";
        });

        Spark.get("/annokset/:id", (req, res) -> {
            Integer annosId = Integer.parseInt(req.params(":id"));
            List<AnnosAine> a = setAineNimiForAll(
                    liitokset.findAllFor(annosId),
                    aineet.findAll()
            );

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
    // palautetulle otokselle
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