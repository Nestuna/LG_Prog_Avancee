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
    // public static boolean possibleCommand(int[][] inventory) {
    //     for (int i = 0; i < inventory.length; i++) {
    //         for(int j = 0; j < inventory[0].length; j++) {
    //             if(inventory[i][j] == 0) return false;
    //         }
    //     }
    //     return true;
    // }
    // public static ArrayList<Integer> findPossiblesCommands(int[][] commands, int[][] inventory) {
    //     ArrayList<Integer> possiblesCommands = new ArrayList<Integer>();
    //     int[][] inventoryAfterSpell = new int[inventory.length][inventory[0].length];

    //     for (int i = 2; i < commands.length; i++) {
    //         for (int j = 2; j < 4; j++) {
    //             inventoryAfterSpell[i][j] = inventory[i][j] + commands[i][j];
    //         }
    //         if(possibleCommand(inventoryAfterSpell)) possiblesCommands.add(commands[i][0]);
    //     }

    //     return possiblesCommands;
    // }
    // GLOBAL VARIABLES
    static ArrayList<List<Integer>> listOfSpells = new ArrayList<List<Integer>> () ;
    static Boolean spellsSaved = false;

    public static void saveSpells(List<Integer> spell) {
        System.err.println(spell);
        listOfSpells.add(spell);
    }
    public static int mostRupees(ArrayList<List<Integer>> commands) {
        int actionId = commands.get(0).get(0);
        int higherPrice = commands.get(0).get(5);
        for(List<Integer> i : commands)
            if(i.get(5) > higherPrice) {
                actionId = i.get(0);
                higherPrice = i.get(5);
            }
        return actionId;
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int actionCount = in.nextInt(); // the number of spells and recipes in play

            // INIT VARIABLES
            ArrayList<List<Integer>> commandsArray = new ArrayList<List<Integer>>();
            ArrayList<List<Integer>> spellsPlayerArray = new ArrayList<List<Integer>>();
            ArrayList<List<Integer>> spellsOpponentArray = new ArrayList<List<Integer>>();
            int[] inventoryArray = new int[5];

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
                
                // HashMap<Object, Object> command = new HashMap<Object,Object>() {{
                //     put("actionId" , actionId);
                //     put("actionType", actionType);
                //     put("delta0" , delta0);
                //     put("delta1" , delta1);
                //     put("delta1" , delta2);
                //     put("delta1" , delta3);
                //     put("delta1" , price);
                //     put("tomeIndex", tomeIndex);
                //     put("taxCount", taxCount);
                //     put("castable", castable);
                //     put("repetable", repeatable);
                // }};

                // SPELLS
                if (castable) {
                    if (spellsPlayerArray.size() <= 5) {
                        List<Integer> spell = Arrays.asList(actionId, delta0, delta1, delta2, delta3);
                        spellsPlayerArray.add(spell);
                    } else {
                        List<Integer> spell = Arrays.asList(actionId, delta0, delta1, delta2, delta3);
                        spellsOpponentArray.add(spell);
                        if (!spellsSaved) {
                            saveSpells(spell);
                        }
                    }
                    
                } else {
                    List<Integer> command = Arrays.asList(actionId, delta0, delta1, delta2, delta3, price);
                    commandsArray.add(command);
                }
            }
            
            // SAUVEGARDE DES SORTS INITAUX
            spellsSaved = true;

           for (int i = 0; i < 2; i++) {
                int inv0 = in.nextInt(); // tier-0 ingredients in inventory
                int inv1 = in.nextInt();
                int inv2 = in.nextInt();
                int inv3 = in.nextInt();
                int score = in.nextInt(); // amount of rupees
                if (i==0) {
                    inventoryArray[0] = inv0;
                    inventoryArray[1] = inv1;
                    inventoryArray[2] = inv2;
                    inventoryArray[3] = inv3;
                    inventoryArray[4] = score;
                }

            }
            
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            System.err.println("Sorts disponible: " + spellsPlayerArray);
            System.err.println("Sorts sauvegardés: " + listOfSpells);

            // System.err.println(findPossiblesCommands(commandsArray, inventoryArray));
            // in the first league: BREW <id> | WAIT; later: BREW <id> | CAST <id> [<times>] | LEARN <id> | REST | WAIT
            String action = Integer.toString(mostRupees(commandsArray));
            System.out.println("BREW " + action );
        }
    }
}