package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem {
    Location currentLocation;
    int health;
    int totalValueScore;
    int bagSpace;
    List<Item> bag = new ArrayList<>();

    Map<String,Location> locations = new HashMap<>();
    Map<String,Item> items = new HashMap<>();
    Map<String,NPC> npcs = new HashMap<>();

    List<Item> itemInBagGoals = new ArrayList<>();
    Map<Item,NPC> itemOnNPCGoals = new HashMap<>();

    Problem(List<String> lines) {
        boolean goalRead = false;
        int goalLine = 0;
        for (int i = 0; (i < lines.size() && !goalRead); i++) {
            String line = lines.get(i);
            //objects
            if (line.contains(" - location")) {
                for (String name : line.split(" - location")[0].split("\\s+")) {
                    locations.put(name, new Location(name));
                }
            } else if (line.contains(" - item")) {
                for (String name : line.split(" - item")[0].split("\\s+")) {
                    items.put(name, new Item(name));
                }
            } else if (line.contains(" - npc")) {
                for (String name : line.split(" - npc")[0].split("\\s+")) {
                    npcs.put(name, new NPC(name));
                }
            } else if (line.contains(" - potion")) {
                for (String name : line.split(" - potion")[0].split("\\s+")) {
                    items.put(name, new Potion(name));
                }
            }
            //init
            else if (line.contains(" (health) ")) {
                health = Integer.parseInt(line.split("\\)\\s+")[1].split("\\)")[0]);
            } else if (line.contains(" (total-value-score) ")) {
                totalValueScore = Integer.parseInt(line.split("\\)\\s+")[1].split("\\)")[0]);
            } else if (line.contains(" (bag-space) ")) {
                bagSpace = Integer.parseInt(line.split("\\)\\s+")[1].split("\\)")[0]);
            } else if (line.contains("value ")) {
                Item item = items.get(line.split("value ")[1].split("\\)")[0]);
                item.value = Integer.parseInt(line.split("\\)\\s+")[1].split("\\)")[0]);
            } if (line.contains("item-space ")) {
                Item item = items.get(line.split("item-space ")[1].split("\\)")[0]);
                item.space = Integer.parseInt(line.split("\\)\\s+")[1].split("\\)")[0]);
            } else if (line.contains("item-in-bag ")) {
                bag.add(items.get(line.split("item-in-bag\\s+")[1].split("\\)")[0]));
            } else if (line.contains("item-at-location ")) {
                Item item = items.get(line.split("item-at-location ")[1].split("\\s+")[0]);
                Location location = locations.get(line.split(item.name + "\\s+")[1].split("\\)")[0]);
                location.items.add(item);
            } else if (line.contains("item-on-npc ")) {
                Item item = items.get(line.split("item-on-npc ")[1].split("\\s+")[0]);
                NPC npc = npcs.get(line.split(item.name + "\\s+")[1].split("\\)")[0]);
                npc.items.add(item);
            } else if (line.contains("npc-at-location ")) {
                NPC npc = npcs.get(line.split("npc-at-location ")[1].split("\\s+")[0]);
                Location location = locations.get(line.split(npc.name + "\\s+")[1].split("\\)")[0]);
                location.npcs.add(npc);
            } else if (line.contains("location-has-neighbour ")) {
                Location location1 = locations.get(line.split("location-has-neighbour ")[1].split("\\s+")[0]);
                Location location2 = locations.get(line.split(location1.name + "\\s+")[1].split("\\)")[0]);
                location1.neighbours.add(location2);
                if (!location2.neighbours.contains(location1)) {
                    location2.neighbours.add(location1);
                }
            } if (line.contains("npc-wants-item ")) {
                NPC npc = npcs.get(line.split("npc-wants-item ")[1].split("\\s+")[0]);
                npc.itemWants.add(items.get(line.split(npc.name + "\\s+")[1].split("\\)")[0]));
            } else if (line.contains("npc-enemy ") && !(line.contains("not"))) {
                npcs.get(line.split("npc-enemy\\s+")[1].split("\\)")[0]).isEnemy = true;
            } else if (line.contains("player-at-location ")) {
                currentLocation = locations.get(line.split("player-at-location\\s+")[1].split("\\)")[0]);
            } else if (line.contains("can-craft-item ")) {
                items.get(line.split("can-craft-item\\s+")[1].split("\\)")[0]).canBeCrafted = true;
            } else if (line.contains("item-requires-item ")) {
                Item item1 = items.get(line.split("item-requires-item ")[1].split("\\s+")[0]);
                Item item2 = items.get(line.split(item1.name + "\\s+")[1].split("\\)")[0]);
                item1.piecesRequired.add(item2);
            } else if (line.contains("location-requires-item ")) {
                Location location = locations.get(line.split("location-requires-item ")[1].split("\\s+")[0]);
                Item item = items.get(line.split(location.name + "\\s+")[1].split("\\)")[0]);
                location.itemNeeds.add(item);
            } else if (line.contains("location-to-craft ")) {
                Item item = items.get(line.split("location-to-craft ")[1].split("\\s+")[0]);
                item.locationToCraft = locations.get(line.split(item.name + "\\s+")[1].split("\\)")[0]);
            } else if (line.contains(":goal")) {
                goalRead = true;
                goalLine = i;
            }
        }

        for (int i = goalLine; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("item-in-bag ")) {
                itemInBagGoals.add(items.get(line.split("item-in-bag\\s+")[1].split("\\)")[0]));
            } else if (line.contains("item-on-npc ")) {
                Item item = items.get(line.split("item-on-npc ")[1].split("\\s+")[0]);
                NPC npc = npcs.get(line.split(item.name + "\\s+")[1].split("\\)")[0]);
                itemOnNPCGoals.put(item,npc);
            }
        }
    }
}
