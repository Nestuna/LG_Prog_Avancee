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

    // UTILS ---------------------------------------
    public static void saveSpells(List<Integer> spell) {
        // Copie les sorts initiaux. Utile pour savoir s'il faut REST ou pas.
        spellsSavedList.add(spell);
    }
    public static int findIndexWithId(int id, ArrayList<List<Integer>> list) {
        // Permet d'avoir l'index d'une liste dans une liste grâce à son id 
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get(0) == id) {
                index = i;
            }
        }

        return index;
    }
    // ACTION & COMMAND ---------------------------------------
    public static String actionToMake(ArrayList<List<Integer>> commands, List<Integer> inventoryPlayer, ArrayList<List<Integer>> spellsList) {
        // Fonction principale appeler par le programme
        // Renvoit l'action à effectuer
        String action;
        List<Integer> command = mostRupees(commands);
        int commandId = command.get(0);
        List<Integer> commandItems = new ArrayList<Integer>();

        // On copie les items, sans l'actionId et le prix pour plus de facilitée
        for (int i=2; i < command.size(); i++) {
            commandItems.add(command.get(i));
        }

        System.err.println("Inventaire :" + inventoryPlayer);
        if (!inventoryIsGood(commandItems, inventoryPlayer)) {
            System.err.println("Commande pas possible !");  
            action = castCommand(commandItems, inventoryPlayer, spellsList);
        } else {
            action = "BREW " + Integer.toString(commandId); ;
        }

        return action;
    }

    public static List<Integer> mostRupees(ArrayList<List<Integer>> commands) {
        // Renvoit la commande la plus rentable parmi une liste de commande
        List<Integer> mostProfitableCommand = commands.get(0);
        int higherPrice = commands.get(0).get(1);
        for(List<Integer> command : commands)
            if(command.get(1) > higherPrice) {
                higherPrice = command.get(1);
                mostProfitableCommand = command;
            }
        System.err.println("Commande la plus rentable :" + mostProfitableCommand);
        return mostProfitableCommand;
    }

    public static boolean inventoryIsGood(List<Integer> itemList, List<Integer> inventory) {
        // Verifie que l'iventaire contient tous les ingredients necessaires
        for (int i = 0; i < itemList.size(); i++) {
            if ((inventory.get(i) + itemList.get(i)) < 0) {
                return false;
            }
        }
        return true;
    }

    // CAST ---------------------------------------
    public static String castCommand(List<Integer> command, List<Integer> inventory, ArrayList<List<Integer>> spellsList) {
        // Cherche à avoir les ingredients necessaire pour effectuer une commande
        // grâce aux sorts
        // Renvoit l'action à effectuer
        List<Integer> itemsToCast = findItemsToCast(command, inventory);
        int actionId = findSpellToCast(itemsToCast, spellsList, inventory);
        String action;
        if (actionId == -1) {
            action = "REST";
        } else {
            action = "CAST " + actionId;
        }
        return action; 
    }

    public static List<Integer> findItemsToCast(List<Integer> itemsList, List<Integer> inventory) {
        // Renvoit les ingredients qui doivent être construit grâce à un sort
        List<Integer> itemsToCast = new ArrayList<Integer>(Arrays.asList(0,0,0,0));
        for (int i = 0; i < itemsList.size(); i++) {
            if ((inventory.get(i) + itemsList.get(i)) < 0) {
                itemsToCast.set(i, inventory.get(i) + itemsList.get(i));
            }
        }
        System.err.println("Items à caster:" + itemsToCast);
        return itemsToCast;
    }

    public static int findSpellToCast(List<Integer> itemsList, ArrayList<List<Integer>> spellsList, List<Integer> inventory) {
        // Trouve le sort à effectuer 
        // Renvoit l'id du sort 
        int spellToCastId = 0;

        for (int i = 0; i < itemsList.size(); i++) {
            if (itemsList.get(i) < 0) {
                spellToCastId = findSpellForItem(i);
                System.err.println("Sort à caster:" + spellToCastId);

                int spellIndex = findIndexWithId(spellToCastId, spellsList);
                if (spellIndex == -1) {
                    return -1;
                } else {

                    // On copie les items, sans l'id
                    List<Integer> spell = spellsList.get(spellIndex);
                    List<Integer> spellItems = new ArrayList<Integer>();
                    for (int j=1; j < spell.size(); j++) {
                        spellItems.add(spell.get(j));
                    }

                    if (!inventoryIsGood(spellItems, inventory)) {
                        spellToCastId = findSpellToCast(spellItems, spellsList, inventory);
                    }
                }
               
            }
        }
        return spellToCastId;
    }

    public static int findSpellForItem(int itemIndex) {
        // Trouver le sort qui correspond à un ingrédient
        // Renvoit l'id du sort
        System.err.println("itemIndex : " + itemIndex);
        int spellId = -1;
        for (List<Integer> spell : spellsSavedList) {
            if (spell.get(itemIndex+1) > 0) {
                spellId = spell.get(0);
            }
        }
        return spellId;
    }
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int actionCount = in.nextInt(); // the number of spells and recipes in play
            // System.err.println("actionCount :" + actionCount);

            // INIT VARIABLES
            ArrayList<List<Integer>> commandsList = new ArrayList<List<Integer>>();
            ArrayList<List<Integer>> spellsPlayerList = new ArrayList<List<Integer>>();
            ArrayList<List<Integer>> spellsOpponentList = new ArrayList<List<Integer>>();
            List<Integer> inventoryPlayerList = new ArrayList<Integer>();
            List <Integer> inventoryOpponentList = new ArrayList<Integer>();
            int scorePlayer;
            int scoreOpponent;
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
                    List<Integer> command = Arrays.asList(actionId, price, delta0, delta1, delta2, delta3);
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
                    inventoryPlayerList = Arrays.asList(inv0, inv1, inv2, inv3);
                    scorePlayer = score;
                } else if (i==1) {
                    inventoryOpponentList = Arrays.asList(inv0, inv1, inv2, inv3);
                    scoreOpponent = score;
                }

            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            System.err.println("Sorts sauvegardés: " + spellsSavedList);
            System.err.println("Sorts Joueur disponible: " + spellsPlayerList);
            System.err.println("Sorts Adversaire disponible: " + spellsPlayerList);
            
            // in the first league: BREW <id> | WAIT; later: BREW <id> | CAST <id> [<times>] | LEARN <id> | REST | WAIT
            String action = actionToMake(commandsList, inventoryPlayerList, spellsPlayerList);
            System.out.println(action);
        }
    }
}