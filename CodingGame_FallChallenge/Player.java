import java.util.*;
import java.io.*;
import java.math.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.lang.Object;
/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
    // GLOBAL VARIABLES
    static ArrayList<List<Integer>> spellsSavedList = new ArrayList<List<Integer>> () ;
    static Boolean spellsAreSaved = false; 

    public static void saveSpells(List<Integer> spell) {
        spellsSavedList.add(spell);
    }

    public static String actionToMake(ArrayList<List<Integer>> commands, List<Integer> inventoryPlayer) {
        int commandId;
        String action;
        List<Integer> command = mostRupees(commands);
        if (!possibleCommand(command, inventoryPlayer)) {
            action = castCommand(command, inventoryPlayer);
        } else {
            action = "BREW " + commandId;
        }
        return action;
    }

    public static List<Integer> mostRupees(ArrayList<List<Integer>> commands) {
        List<Integer> mostProfitableCommand = commands.get(0);
        int higherPrice = commands.get(0).get(5);
        for(List<Integer> command : commands)
            if(command.get(5) > higherPrice) {
                higherPrice = command.get(5);
                mostProfitableCommand = command;
            }
        System.err.println("Commande la plus rentable :" + mostProfitableCommand);
        return mostProfitableCommand;
    }

    public static boolean possibleCommand(List<Integer> command, List<Integer> inventory) {
        for (int i = 1; i < command.size() - 1; i++) {
            if ((inventory.get(i) + command.get(i)) < 0) {
                return false;
            }
        }
        return true;
    }

    public static String castCommand(List<Integer> command, List<Integer> inventory) {
        List<Integer> itemsToCast = findItemsToCast(command, inventory);
        int spellToCast = findSpellToCast(itemsToCast);
        String castAction;

        return castAction;
    }

    public static List<Integer> findItemsToCast(List<Integer> command, List<Integer> inventory) {
        List<Integer> itemsToCast = new ArrayList<Integer>(Arrays.asList(0,0,0,0));
        for (int i = 1; i < command.size() - 1; i++) {
            if ((inventory.get(i) + command.get(i)) < 0) {
                itemsToCast.set(i-1, inventory.get(i) + command.get(i));
            }
        }
        System.err.println("Items à caster:" + itemsToCast);
        return itemsToCast;
    }

    public static int findSpellToCast(List<Integer> items) {
        int spellToCast;

        System.err.println("Sort à caster:" + spellToCast);
        return spellToCast;
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int actionCount = in.nextInt(); // the number of spells and recipes in play
            System.err.println("actionCount :" + actionCount);

            // INIT VARIABLES
            ArrayList<List<Integer>> commandsList = new ArrayList<List<Integer>>();
            ArrayList<List<Integer>> spellsPlayerList = new ArrayList<List<Integer>>();
            ArrayList<List<Integer>> spellsOpponentList = new ArrayList<List<Integer>>();
            List<Integer> inventoryPlayerList = new ArrayList<Integer>();
            List <Integer> inventoryOpponentList = new ArrayList<Integer>();
            spellsAreSaved = spellsSavedList.size() > 0; // false au début du jeu

            for (int i = 0; i < actionCount; i++) {
                int actionId = in.nextInt(); // the unique ID of this spell or recipe
                String actionType = in.next(); // in the first league: BREW; later: CAST, OPPONENT_CAST, LEARN, BREW
                int delta0 = in.nextInt(); // tier-0 ingredient change
                int delta1 = in.nextInt(); // tier-1 ingredient change
                int delta2 = in.nextInt(); // tier-2 ingredient change
                int delta3 = in.nextInt(); // tier-3 ingredient change
                int price = in.nextInt(); // the price in rupees if this is a potion
                int tomeIndex = in.nextInt(); // in the first two leagues: always 0; later: the index in the tome if this is a tome spell, equal to the read-ahead tax; For brews, this is the value of the current urgency bonus
                int taxCount = in.nextInt(); // in the first two leagues: always 0; later: the amount of taxed tier-0 ingredients you gain from learning this spell; For brews, this is how many times you can still gain an urgency bonus
                boolean castable = in.nextInt() != 0; // in the first league: always 0; later: 1 if this is a castable player spell
                boolean repeatable = in.nextInt() != 0; // for the first two leagues: always 0; later: 1 if this is a repeatable player spell
                


                // SPELLS : on verifie si c'est un sort, et à qui appartient ce sort
                if (castable) {
                    if (actionType.equals("CAST")) {
                        List<Integer> spell = Arrays.asList(actionId, delta0, delta1, delta2, delta3);
                        spellsPlayerList.add(spell);
                        // On stocke la liste des sorts initiaux
                        if (!spellsAreSaved) {
                            saveSpells(spell);
                        }
                    } else if (actionType.equals("OPPONENT_CAST")) {
                        List<Integer> spell = Arrays.asList(actionId, delta0, delta1, delta2, delta3);
                        spellsOpponentList.add(spell);
                    }

                } else {
                    List<Integer> command = Arrays.asList(actionId, delta0, delta1, delta2, delta3, price);
                    commandsList.add(command);
                }
               
               
            }
        

           for (int i = 0; i < 2; i++) {
                int inv0 = in.nextInt(); // tier-0 ingredients in inventory
                int inv1 = in.nextInt();
                int inv2 = in.nextInt();
                int inv3 = in.nextInt();
                int score = in.nextInt(); // amount of rupees
                if (i==0) {
                    inventoryPlayerList = Arrays.asList(inv0, inv1, inv2, inv3, score);
                } else if (i==1) {
                    inventoryOpponentList = Arrays.asList(inv0, inv1, inv2, inv3, score);
                }

            }
            
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            System.err.println("Sorts sauvegardés: " + spellsSavedList);
            System.err.println("Sorts Joueur disponible: " + spellsPlayerList);
            System.err.println("Sorts Adversaire disponible: " + spellsPlayerList);
            
            // in the first league: BREW <id> | WAIT; later: BREW <id> | CAST <id> [<times>] | LEARN <id> | REST | WAIT
            String action = actionToMake(commandsList, inventoryPlayerList));
            System.out.println("BREW " + action );
        }
    }
}