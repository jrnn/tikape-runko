package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Database db = new Database("jdbc:sqlite:opiskelijat.db");
        db.init();
        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(db);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "Ding dong shämmä lämmä. Tykkäätteks skändinääveist ääkkösist mitä häh. Tykkäättekxs?!?!");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelijat", opiskelijaDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());

    }

}