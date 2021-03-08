import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Parser {

    public ArrayList<Question> parseRequirements(JSONObject obj) {
        ArrayList<Question> questions = new ArrayList<Question>();

        try {
            JSONArray array = obj.getJSONArray("questions");

            for(int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                int id = jsonObject.getInt("id");
                String type = jsonObject.getString("type");
                int originId = jsonObject.getInt("originId");
                int destinationId = jsonObject.getInt("destinationId");

                Question question = new Question(id, type, originId, destinationId);
                questions.add(question);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return questions;
    };

    public ArrayList<Planet> parseForPlanets(JSONObject obj) {
        ArrayList<Planet> planets = new ArrayList<>();

        System.out.println(obj);
        try {
            JSONArray jsonArray = obj.getJSONArray("planets");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject subObj = jsonArray.getJSONObject(i);

                int planetId = subObj.getInt("id");
                JSONArray jsonPortalArray = subObj.getJSONArray("portals");

                Portal[] portals = new Portal[jsonPortalArray.length()];
                for(int j = 0; j < jsonPortalArray.length(); j++) {
                    JSONObject portalObj = jsonPortalArray.getJSONObject(j);

                    int portalId = portalObj.getInt("id");
                    int destinationId = portalObj.getInt("destinationId");
                    int costs = portalObj.getInt("costs");

                    Portal portal = new Portal(portalId, destinationId, costs);
                    portals[j] = portal;

                }

                Planet planet = new Planet(planetId, portals);
                planets.add(planet);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return planets;
    }

    public String parseNextUrl(JSONObject obj) {
        try {
            return obj.getString("linkToNextTask");
        } catch (JSONException e) {
            System.out.println(obj.toString());
            e.printStackTrace();
        }

        return "Not Found";
    }

    public int parseUrlForStage(String currentUrl) {
        String[] a = currentUrl.split("/");
        return Integer.parseInt(a[7]);
    }

    public String parseUrlForToken(String currentUrl) {
        String[] a = currentUrl.split("/")[9].split("=");
        //String[] b = a[9].split("=");
        return a[1];
    }

    public boolean parseUrlForFinish(String currentUrl) {
        return currentUrl.contains("finish");
    }

    // Generate
    public JSONObject generateJsonAnswerList(ArrayList<Answer> answers) {
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();

        try {
            for(Answer answer : answers) {
                JSONObject objA = new JSONObject();
                objA.put("questionId", answer.questionId);
                objA.put("reachable", answer.reachable);

                array.put(objA);
            }

            obj.put("answers", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
