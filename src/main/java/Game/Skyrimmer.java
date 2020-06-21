package Game;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Skyrimmer extends Application {
    Location currentLocation;
    int health;
    int totalValueScore;
    int bagSpace;
    List<Item> bag;
    Map<String,Item> itemMap;

    List<Item> itemInBagGoalsList;
    Map<Item,NPC> itemOnNPCGoalsMap;

    Label currentLocationLabel = new Label();
    Label totalValueScoreLabel = new Label();
    Label bagSpaceLabel = new Label();
    Label healthLabel = new Label();
    Label bagSpaceWarningLabel;
    Label healthWarningLabel;

    VBox items = new VBox();
    VBox neighbours = new VBox();
    VBox enemies = new VBox();
    VBox trading = new VBox();
    VBox giving = new VBox();
    VBox bagItems = new VBox();
    VBox crafting = new VBox();
    BorderPane root = new BorderPane();

    VBox itemInBagGoals = new VBox();
    VBox itemOnNPCGoals = new VBox();

    ImageView imageView;

    @Override
    public void start(Stage stage) {
        Button chooseProblem = new Button("Select Problem");
        Label selectedProblemNameLabel = new Label();
        chooseProblem.setOnAction(event -> {
            Window explorer = root.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Pick a Skyrimmer PDDL Problem File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Skyrimmer PDDL Problem Files", "*.pddl"));
            File file = fileChooser.showOpenDialog(explorer);
            if (file != null ) {
                try {
                    setProblem(new Problem(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)));
                    selectedProblemNameLabel.setText("  Selected problem: " + file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        HBox gameTop = new HBox();
        gameTop.getChildren().add(new VBox(new Label("Go to neighbouring locations:"),neighbours));
        gameTop.getChildren().add(new VBox(new Label("Pickup items in current location:"),items));
        gameTop.getChildren().add(new VBox(new Label("Drop items in bag:"),bagItems));
        gameTop.getChildren().add(new VBox(new Label("Attack enemies:"),enemies));

        HBox gameBottom = new HBox();
        gameBottom.getChildren().add(new VBox(new Label("Trade with friendlies:"),trading));
        gameBottom.getChildren().add(new VBox(new Label("Give items to friendlies:"),giving));
        gameBottom.getChildren().add(new VBox(new Label("Craft items:"),crafting));

        VBox goals = new VBox(new Label("Goals:"),itemInBagGoals,itemOnNPCGoals);

        gameTop.setSpacing(20);
        gameBottom.setSpacing(20);
        gameTop.setPadding(new Insets(0,0,20,0));
        gameBottom.setPadding(new Insets(0,0,20,0));
        goals.setPadding(new Insets(0,0,20,0));

        Image image = new Image("outside.jpg",400,225,true,true);
        imageView = new ImageView(image);

        VBox game = new VBox(gameTop,gameBottom,goals,imageView);
        BorderPane.setMargin(game, new Insets(10,10,10,0));

        root.setCenter(game);
        root.setTop(new HBox(chooseProblem,selectedProblemNameLabel));
        root.setBottom(new VBox(currentLocationLabel,healthLabel,totalValueScoreLabel,bagSpaceLabel));
        root.setPadding(new Insets(20,20,20,20));
        stage.setScene(new Scene(root,1400,800));
        stage.show();
    }

    void setProblem(Problem problem) {
        currentLocation = problem.currentLocation;
        health = problem.health;
        totalValueScore = problem.totalValueScore;
        bagSpace = problem.bagSpace;
        bag = problem.bag;
        itemMap = problem.items;

        itemInBagGoalsList = problem.itemInBagGoals;
        itemOnNPCGoalsMap = problem.itemOnNPCGoals;

        currentLocationLabel.setText("Current location: " + currentLocation.name);
        healthLabel.setText("Health: " + health);
        totalValueScoreLabel.setText("Total value score: " + totalValueScore);
        bagSpaceLabel.setText("Bag space: " + bagSpace);

        setNeighbours();
        setLocationItems();
        setEnemies();
        setTrading();
        setGiving();
        setBagItems();
        setCrafting();

        setGoals();
    }

    void setNeighbours() {
        neighbours.getChildren().clear();
        for (Location location : currentLocation.neighbours) {
            Button goToLocation = new Button(location.name);
            Label label = new Label();
            if (!location.itemNeeds.isEmpty()) {
                label.setText(" requires:");
                for (Item item : location.itemNeeds) {
                    label.setText(label.getText() + " " + item.name);
                }
            }
            goToLocation.setOnAction(event -> {
                boolean canAccess = true;
                for (Item item : location.itemNeeds) {
                    if (!bag.contains(item)) {
                        canAccess = false;
                        break;
                    }
                }
                if (canAccess) {
                    goToLocation(location);
                    healthLabel.setText("Health: " + health);
                    currentLocationLabel.setText("Current location: " + currentLocation.name);
                    setImage();
                }
            });
            neighbours.getChildren().add(new HBox(goToLocation,label));
        }
    }

    void setImage() {
        try {
            String name = currentLocation.name + ".jpg";
            imageView.setImage(new Image(name,400,225,true,true));
        } catch (Exception e) {
            Image image = new Image("outside.jpg",400,225,true,true);
            imageView.setImage(image);
        }
    }

    void setLocationItems() {
        items.getChildren().clear();
        for (Item item : currentLocation.items) {
            Button pickup = new Button(item.name);
            pickup.setOnAction(pickupEvent -> {
                if (bagSpace >= item.space) {
                    pickupItem(item);
                    healthLabel.setText("Health: " + health);
                } else if (!items.getChildren().contains(bagSpaceWarningLabel)) {
                    bagSpaceWarningLabel = new Label("Bag too full");
                    items.getChildren().add(bagSpaceWarningLabel);
                }
            });
            items.getChildren().add(pickup);
        }
    }

    void setBagItems() {
        bagItems.getChildren().clear();
        for (Item item : bag) {
            Button drop = new Button(item.name);
            drop.setOnAction(dropEvent -> dropItem(item));
            bagItems.getChildren().add(drop);
        }
        totalValueScoreLabel.setText("Total value score: " + totalValueScore);
        bagSpaceLabel.setText("Bag space: " + bagSpace);
    }

    void setEnemies() {
        enemies.getChildren().clear();
        for (int i = 0; i < currentLocation.npcs.size(); i++) {
            if (currentLocation.npcs.get(i).isEnemy) {
                NPC enemy = currentLocation.npcs.get(i);
                Button attack = new Button(enemy.name);
                attack.setOnAction(event -> {
                    if (health > 1) {
                        attackEnemy(enemy);
                        healthLabel.setText("Health: " + health);
                    } else if (!enemies.getChildren().contains(healthWarningLabel)) {
                        healthWarningLabel = new Label("Health too low");
                        enemies.getChildren().add(healthWarningLabel);
                    }
                });
                enemies.getChildren().add(attack);
            }
        }
    }

    void setTrading() {
        trading.getChildren().clear();
        for (int i = 0; i < currentLocation.npcs.size(); i++) {
            if (!currentLocation.npcs.get(i).isEnemy) {
                NPC friendly = currentLocation.npcs.get(i);
                Label friendlyLabel = new Label(friendly.name);
                if (friendly.doesNotWantAnything) {
                    trading.getChildren().add(
                            new HBox(friendlyLabel, new Label(" does not want anything else!")));
                } else {
                    ComboBox<String> itemGives = new ComboBox<>();
                    for (Item item : friendly.itemWants) {
                        itemGives.getItems().add(item.name);
                    }
                    ComboBox<String> itemTakes = new ComboBox<>();
                    for (Item item : friendly.items) {
                        itemTakes.getItems().add(item.name);
                    }

                    Button tradeButton = new Button("Trade");
                    tradeButton.setOnAction(event -> {
                        if (itemGives.getValue() == null || itemTakes.getValue() == null) return;
                        Item itemGive = itemMap.get(itemGives.getValue());
                        Item itemTake = itemMap.get(itemTakes.getValue());
                        if (bagSpace >= itemTake.space) {
                            if (bag.contains(itemGive)) {
                                tradeWithFriendly(friendly, itemGive, itemTake);
                                if (friendly.itemWants.equals(friendly.items)) {
                                    friendly.doesNotWantAnything = true;
                                }
                                setTrading();
                            }
                        } else if (!trading.getChildren().contains(bagSpaceWarningLabel)) {
                            bagSpaceWarningLabel = new Label("Bag too full");
                            trading.getChildren().add(bagSpaceWarningLabel);
                        }
                    });
                    trading.getChildren().add(
                            new HBox(friendlyLabel, new Label(" will give "), itemTakes,
                                    new Label(" for "), itemGives, tradeButton));
                }
            }
        }
    }

    void setGiving() {
        giving.getChildren().clear();
        for (int i = 0; i < currentLocation.npcs.size(); i++) {
            if (!currentLocation.npcs.get(i).isEnemy) {
                NPC friendly = currentLocation.npcs.get(i);
                Label friendlyLabel = new Label(friendly.name + " ");
                ComboBox<String> items = new ComboBox<>();
                for (Item item : bag) {
                    items.getItems().add(item.name);
                }
                Button give = new Button("Give");
                give.setOnAction(event -> {
                    if (items.getValue() != null) {
                        giveItem(friendly,itemMap.get(items.getValue()));
                        setGiving();
                    }
                });
                giving.getChildren().add(new HBox(friendlyLabel,items,give));
            }
        }
    }

    void setCrafting() {
        crafting.getChildren().clear();
        for (Item item : itemMap.values()) {
            if (item.canBeCrafted) {
                HBox craftLine = new HBox();
                Button craft = new Button(item.name);
                craft.setOnAction(event -> {
                    boolean canCraft = true;
                    for (Item piece : item.piecesRequired) {
                        if (!bag.contains(piece)) {
                            canCraft = false;
                            break;
                        }
                    }
                    if (item.locationToCraft != null && currentLocation != item.locationToCraft) {
                        canCraft = false;
                    }
                    if (canCraft) {
                        craft(item);
                        crafting.getChildren().remove(craftLine);
                    }
                });
                Label piecesRequired = new Label(" requires: ");
                for (Item piece : item.piecesRequired) {
                    piecesRequired.setText(piecesRequired.getText() + piece.name + " ");
                }
                Label location = new Label();
                if (item.locationToCraft != null) {
                    location.setText(". Location to craft: " + item.locationToCraft.name);
                }
                craftLine.getChildren().addAll(craft,piecesRequired,location);
                crafting.getChildren().add(craftLine);
            }
        }
    }

    void setGoals() {
        itemInBagGoals.getChildren().clear();
        for (Item item : itemInBagGoalsList) {
            Label label = new Label(item.name + " in bag ");
            CheckBox checkBox = new CheckBox();
            checkBox.setDisable(true);
            if (bag.contains(item)) {
                checkBox.setSelected(true);
            }
            itemInBagGoals.getChildren().add(new HBox(label,checkBox));
        }
        itemOnNPCGoals.getChildren().clear();
        for (Map.Entry<Item,NPC> entry : itemOnNPCGoalsMap.entrySet()) {
            Label label = new Label(entry.getKey().name + " on " + entry.getValue().name + " ");
            CheckBox checkBox = new CheckBox();
            checkBox.setDisable(true);
            if (entry.getValue().items.contains(entry.getKey())) {
                checkBox.setSelected(true);
            }
            itemOnNPCGoals.getChildren().add(new HBox(label,checkBox));
        }
        checkAndSetWin();
    }

    void checkAndSetWin() {
        boolean won = true;
        for (Item item : itemInBagGoalsList) {
            if (!bag.contains(item)) {
                won = false;
                break;
            }
        }
        for (Map.Entry<Item,NPC> entry : itemOnNPCGoalsMap.entrySet()) {
            if (!entry.getValue().items.contains(entry.getKey())) {
                won = false;
                break;
            }
        }
        if (won) {
            new Alert(Alert.AlertType.INFORMATION, "Goal state reached!").showAndWait();
        }
    }

    void goToLocation(Location location) {
        for (NPC npc : currentLocation.npcs) {
            if (npc.isEnemy) health--;
        }
        currentLocation = location;
        setNeighbours();
        setLocationItems();
        setEnemies();
        setTrading();
        setGiving();
    }

    void pickupItem(Item item) {
        totalValueScore += item.value;
        bagSpace -= item.space;
        bag.add(item);
        currentLocation.items.remove(item);
        for (NPC npc : currentLocation.npcs) {
            if (npc.isEnemy) health--;
        }
        setLocationItems();
        setBagItems();
        setGiving();

        setGoals();
    }

    void dropItem(Item item) {
        totalValueScore -= item.value;
        bagSpace += item.space;
        bag.remove(item);
        currentLocation.items.add(item);
        setLocationItems();
        setBagItems();
        setGiving();

        setGoals();
    }

    void attackEnemy(NPC enemy) {
        currentLocation.npcs.remove(enemy);
        health--;
        currentLocation.items.addAll(enemy.items);
        setLocationItems();
        setEnemies();
    }

    void tradeWithFriendly(NPC friendly, Item itemGive, Item itemTake) {
        bag.remove(itemGive);
        friendly.items.add(itemGive);
        totalValueScore -= itemGive.value;
        bagSpace += itemGive.space;
        bag.add(itemTake);
        friendly.items.remove(itemTake);
        totalValueScore += itemTake.value;
        bagSpace -= itemTake.space;
        setBagItems();
        setGiving();

        setGoals();
    }

    void giveItem(NPC friendly, Item itemGive) {
        bag.remove(itemGive);
        friendly.items.add(itemGive);
        totalValueScore -= itemGive.value;
        bagSpace += itemGive.space;
        setBagItems();
        setTrading();

        setGoals();
    }

    void craft(Item item) {
        bag.add(item);
        totalValueScore += item.value;
        for (Item piece : item.piecesRequired) {
            bag.remove(piece);
            totalValueScore -= piece.value;
        }
        setBagItems();
        setGiving();

        setGoals();
    }
}

class Location {
    String name;
    List<Item> itemNeeds = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    List<NPC> npcs = new ArrayList<>();
    List<Location> neighbours = new ArrayList<>();
    Location(String name) {this.name = name;}
}

class Item {
    String name;
    int value;
    int space;
    boolean canBeCrafted;
    Location locationToCraft;
    List<Item> piecesRequired = new ArrayList<>();
    Item(String name) {this.name = name;}
}

class NPC {
    String name;
    List<Item> items = new ArrayList<>();
    List<Item> itemWants = new ArrayList<>();
    boolean isEnemy;
    boolean doesNotWantAnything;
    NPC(String name) {this.name = name;}
}

class Potion extends Item {
    Potion(String name) {super(name);}
}
