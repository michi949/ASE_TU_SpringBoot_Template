import org.json.JSONObject;

import javax.sound.sampled.Port;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ProblemSolver {
    private NetworkManager networkManager = new NetworkManager();
    private Parser parser = new Parser();
    private String BASEURL = "https://reset.inso.tuwien.ac.at/asetest/assignment/51824673";
    private String token = "";
    private ArrayList<Planet> planets;

    public ProblemSolver() {
        System.out.println("Problem Solver started!");
    }

    public void start() {
        this.token = "";
        this.getInitialToken();
        this.handleStages(this.BASEURL + "/stage/1/testcase/1?token=" + this.token);
    }

    // Init Token
    private void getInitialToken() {
            String val = this.networkManager.getInitTokenRequest(this.BASEURL + "/token");
            System.out.println("Inital Token: " + val);
            this.token = val;
    }

    private void handleStages(String currentUrl) {
        boolean finishTask = this.parser.parseUrlForFinish(currentUrl);

        if ( finishTask ) {
            String finalToken = this.networkManager.getFinishTask(currentUrl);
            this.writeFinalTokenToFile(finalToken);
        } else {
            int currentStage = this.parser.parseUrlForStage(currentUrl);

            switch (currentStage) {
                case 1:
                    if(!Config.stageOnePdf) {
                       Config.stageOnePdf = this.networkManager.loadPdfFile(this.BASEURL + "/stage/1/pdf?token=" + token, 1);
                    }

                    String nextStage = this.stageOneTask(currentUrl);
                    this.handleStages(nextStage);
                    break;
                case 2:
                    if(!Config.stageTwoPdf) {
                        String currentToken = this.parser.parseUrlForToken(currentUrl);
                        Config.stageTwoPdf = this.networkManager.loadPdfFile(this.BASEURL + "/stage/2/pdf?token=" + currentToken, 2);
                    }


                    break;
                default:
                    System.out.println("No Stage Found!");
                    break;
            }
        }
    }

    //Stage 2
    private String stageTwoTask(String url) {
        JSONObject obj = this.networkManager.getTaskFromStage(url);
        //ArrayList<Question> questions = this.parser.parseRequirements(obj);
        ArrayList<Planet> planets = this.parser.parseForPlanets(obj);

        Answer answers = new Answer(0, false);
        boolean reachabel = checkAllPlanetRechable(planets);

        JSONObject postJson = this.parser.generateJsonAnswerList(answers);
        JSONObject resp = this.networkManager.postStageOneTestCase(url, postJson);
        String nextTask = this.parser.parseNextUrl(resp);

        return nextTask;
    }

    private boolean checkAllPlanetRechable(ArrayList<Planet> planets) {


    }

    //Stage 1
    private String stageOneTask(String url) {
        JSONObject obj = this.networkManager.getTaskFromStage(url);
        ArrayList<Question> questions = this.parser.parseRequirements(obj);
        ArrayList<Planet> planets = this.parser.parseForPlanets(obj);

        ArrayList<Answer> answers = new ArrayList<>();
        for(Question question: questions) {
            switch (question.type) {
                case "REACHABLE":
                    answers.add(checkPlanetReachable(planets, question));
                    break;
                default:
                    break;
            }
        }

        JSONObject postJson = this.parser.generateJsonAnswerList(answers);
        JSONObject resp = this.networkManager.postStageOneTestCase(url, postJson);
        String nextTask = this.parser.parseNextUrl(resp);

        return nextTask;
    };

    private Answer checkPlanetReachable(ArrayList<Planet> planets, Question question) {
        Answer answer = new Answer(question.id, false);
        this.planets = planets;

        Planet originPlanet = planets.stream().filter(planet -> planet.id == question.originId).findAny().orElse(null);

        if (originPlanet == null) {
            return answer;
        }

        if (originPlanet.id == question.destinationId) {
            answer.reachable = true;
            return answer;
        }

        answer.reachable = checkPlanets(originPlanet, question.destinationId, new ArrayList<>());
        return answer;
    }


    private boolean checkPlanets(Planet currentPlanet, int destinationId, ArrayList<Planet> vistiedPotals) {

        if (currentPlanet.id == destinationId) {
            return true;
        }

        if(vistiedPotals.contains(currentPlanet)) {
            return false;
        }

        for (Portal portal: currentPlanet.portals) {
            Planet bla = planets.stream().filter(planet -> planet.id == portal.destinationId).findFirst().orElse(null);
            vistiedPotals.add(currentPlanet);
            boolean blfda = checkPlanets(bla, destinationId, vistiedPotals);

            if(blfda) {
                return true;
            }
        }

        return false;
    }

    //Helper
    private void writeFinalTokenToFile(String finalToken) {
        PrintWriter out = null;
        System.out.println(finalToken);
        try {
            out = new PrintWriter("token.txt");
            out.println(finalToken);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

