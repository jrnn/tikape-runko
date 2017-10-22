package tikape.runko;

import java.util.HashMap;
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
        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(db);  // get rid of this

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

        Spark.get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelijat", opiskelijaDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        Spark.get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());

    }

}