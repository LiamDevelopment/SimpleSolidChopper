import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.*;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;
import java.awt.geom.Rectangle2D;

//Meta tag
@ScriptMeta(developer = "SNUSMEISTER", name = "Super Simple Power Chopper", desc = "Does woodcutting!")

public class Main extends Script implements RenderListener {

    static boolean settingsSet = false;

    static String treeType = "null";

    private boolean Banking = true;

    private StopWatch stopWatch;

    private String currentAction = "";

    private static int startXP = Skills.getExperience(Skill.WOODCUTTING);

    private String logType = "";

    private String treeName = "";

    private static boolean treeInArea() {
        SceneObject tree = SceneObjects.getNearest("Tree");
        return Movement.isInteractable(tree.getPosition());
    }

    private static boolean hasAxe() {
        //Check if user has axe in the inventory or in the equipments
        return Inventory.contains(1351, 1349, 1353, 1361, 1355, 1357, 1359) || Equipment.contains(1351, 1349, 1353, 1361, 1355, 1357, 1359);
    }

    private static boolean inventoryFull() {
        return Inventory.isFull();
    }

    //Initialize
    @Override
    public void onStart() {
        currentAction = "Loading script...";
        javax.swing.SwingUtilities.invokeLater(() -> new SimpleGUI(this));
        stopWatch = StopWatch.start();
        super.onStart();
    }

    @Override
    public int loop() {

        if (treeType.equals("Tree")) {
            logType = "Logs";
            treeName = "Tree";
        }
        if (treeType.equals("Oak")) {
            logType = "Oak logs";
            treeName = "Oak";
        }
        if (treeType.equals("Willow")) {
            logType = "Willow logs";
            treeName = "Willow";
        }
        if (treeType.equals("Maple")) {
            logType = "Maple logs";
            treeName = "Maple";
        }
        if (treeType.equals("Yew")) {
            logType = "Yew logs";
            treeName = "Yew";
        }

        if (settingsSet) {

            if (treeInArea() && hasAxe()) {

                if (inventoryFull()) {
                    if (Banking) {



                    } else {

                        if (!Tabs.isOpen(Tab.INVENTORY)) {

                            Time.sleep(Random.low(200, 700));
                            Tabs.open(Tab.INVENTORY);

                        }

                        currentAction = "Dropping logs...";

                        for (Item treeLog : Inventory.getItems(treeLog -> treeLog.getName().equals(logType))) {

                            treeLog.interact("Drop");
                            Time.sleep(Random.low(200, 400));

                        }

                    }

                }

                final SceneObject treeTree = SceneObjects.getNearest(treeName);

                if(treeTree != null) {

                    //If player is not running toggle run on
                    if (!Movement.isRunEnabled() && Movement.getRunEnergy() > 65 && Players.getLocal().isMoving()) {

                        Movement.toggleRun(true);

                    }

                    if (Players.getLocal().getAnimation() == -1 && !Movement.isDestinationSet()) {

                        treeTree.interact("Chop down");
                        currentAction = "Walking to closest tree (Distance: " + treeTree.distance() + ")...";

                    } else {

                        currentAction = "Waiting for player to chop the tree...";

                        //Timeout in 45 seconds and return to code...
                        Time.sleepUntil(() -> (Players.getLocal().getAnimation() == -1), 45000);

                        int randomThreshold = Random.high(10, 7000);

                        if (randomThreshold < 3100) {
                            currentAction = "Executing random bluff...";
                            Time.sleep(Random.high(3300, 9150));
                        }

                    }
                }

            } else {

                currentAction = "Waiting until there is tree nearby...";

        }

        } else {

            currentAction = "Waiting for user to apply settings...";

        }

        return Random.high(300, 1300);
    }

    @Override
    public void notify(RenderEvent renderEvent) {

        //runtime in seconds.
        Graphics g = renderEvent.getSource();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int y = 202;
        int x = 9;

        long XPGained = (Skills.getExperience(Skill.WOODCUTTING) - startXP);


        Color GUIColor = new Color(0, 0, 0, 145);
        g2.setColor(GUIColor);
        g2.fillRect(x, y, 336, 130);
        g2.setColor(Color.white);
        g2.drawRect(x, y, 336, 130);
        g2.setColor(Color.green);
        g2.setFont(new Font(".SF NS Text", Font.PLAIN, 14));
        g2.drawString("[" + Players.getLocal().getName() + ']' + " Super Simple Power Chopper", x + 5, y + 16);

        Color TextColor = new Color(255, 255, 255);

        g2.setColor(TextColor);
        g2.setFont(new Font(".SF NS Text", Font.PLAIN, 12));
        g2.drawString("Current action: " + currentAction, x + 5, y + 35);
        g2.drawString("Time running: " + stopWatch.toElapsedString(), x + 5, y + 50);
        g2.drawString("Target tree type: " + treeType, x + 5, y + 66);

        long seconds = stopWatch.getElapsed().getSeconds();

        if(seconds > 25 && XPGained > 100){

            double XPHour = Math.round(XPGained * 3600 / seconds) / 1000.0;
            g2.drawString("Experience/Hour: " + XPHour + "k", x + 5, y + 90);

        } else {

            g2.drawString("Experience/Hour: Calculating average...", x + 5, y + 90);

        }

        g2.drawString("Experience gained: " + XPGained, x + 5, y + 106);
        g2.drawString("Experience to next level: " + Skills.getExperienceToNextLevel(Skill.WOODCUTTING) + " (" + (Skills.getCurrentLevel(Skill.WOODCUTTING) + 1) + ")", x + 5, y + 122);

    }
}


