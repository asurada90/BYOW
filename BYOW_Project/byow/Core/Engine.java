package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.*;
import java.lang.module.FindException;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    private boolean gameOver = false;
    private boolean toggleLOS = true;
    private boolean victory = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        String seed = "";
        String input = "";
        drawMenu();
        MapGenerator mapGen;
        while (true) {
            input = solicitCharInput();
            if (input.equalsIgnoreCase("n")) {
                drawSeedSolicitation("");
                seed = solicitSeedInput();
                mapGen = new MapGenerator(Engine.WIDTH, Engine.HEIGHT,
                        seed.substring(1, seed.length() - 1));
                mapGen.generateRooms();
                mapGen.generateHallways();
                mapGen.buildWalls();
                mapGen.generateDoor();
                mapGen.generateAvatar();
                mapGen.generateKey();
                break;
            } else if (input.equalsIgnoreCase("l")) {
                TETile[][] savedMap = readMapFromSavedData();
                mapGen = new MapGenerator(savedMap);
                break;
            } else if (input.equalsIgnoreCase("q")) {
                System.exit(0);
            } else {

            }
        }

        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);
        String action = "";
        while(!gameOver) {
            if (victory) {
                drawVictoryScreen();
                action = solicitCharInput();
                if (action.toLowerCase().equals("n")) {
                    drawSeedSolicitation("");
                    seed = solicitSeedInput();
                    mapGen = new MapGenerator(Engine.WIDTH, Engine.HEIGHT,
                            seed.substring(1, seed.length() - 1));
                    mapGen.generateRooms();
                    mapGen.generateHallways();
                    mapGen.buildWalls();
                    mapGen.generateDoor();
                    mapGen.generateAvatar();
                    mapGen.generateKey();
                    StdDraw.setFont(fontSmall);
                } else {
                    gameOver = true;
                    continue;
                }
            } else if (toggleLOS) {
                ter.renderFrame(mapGen.getMap());
            } else if (!toggleLOS) {
                ter.renderFrame(mapGen.createLineOfSightMap());
            }

            mapGen.displayHUD();
            action = solicitCharInput();
            if (action.toLowerCase().equals("a")) {
                victory = mapGen.moveAvatarLeft();
            } else if (action.toLowerCase().equals("w")) {
                victory = mapGen.moveAvatarUp();
            } else if (action.toLowerCase().equals("d")) {
                victory = mapGen.moveAvatarRight();
            } else if (action.toLowerCase().equals("s")) {
                victory = mapGen.moveAvatarDown();
            } else if (action.toLowerCase().equals("t")) {
                toggleLOS = !toggleLOS;
            } else if (action.toLowerCase().equals("n")) {
                drawSeedSolicitation("");
                seed = solicitSeedInput();
                mapGen = new MapGenerator(Engine.WIDTH, Engine.HEIGHT,
                        seed.substring(1, seed.length() - 1));
                mapGen.generateRooms();
                mapGen.generateHallways();
                mapGen.buildWalls();
                mapGen.generateDoor();
                mapGen.generateAvatar();
                mapGen.generateKey();
                StdDraw.setFont(fontSmall);
            } else if (action.equals(":")) {
                action = solicitCharInput();
                if (action.toLowerCase().equals("q")) {
                    saveMapToTxtFile(mapGen.getMap());
                    gameOver = true;
                }
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        Engine engine = new Engine();
        engine.interactWithKeyboard();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        String inputString = input.toLowerCase();
        String defaultSeed = "123456789";
        MapGenerator mapGen = new MapGenerator(Engine.WIDTH, Engine.HEIGHT, defaultSeed);
        if (inputString.charAt(0) != 'n' && inputString.charAt(0) != 'l') {
            // error
        } else if (inputString.charAt(0) == 'n') {
            int indexOfEndSeed = inputString.indexOf('s');
            String seed = inputString.substring(1, indexOfEndSeed);
            // System.out.println(seed);
            mapGen = new MapGenerator(Engine.WIDTH, Engine.HEIGHT, seed);
            mapGen.generateRooms();
            mapGen.generateHallways();
            mapGen.buildWalls();
            mapGen.generateDoor();
            mapGen.generateAvatar();
            int movement = indexOfEndSeed + 1;

            String action = "";
            while(!gameOver && movement < inputString.length()) {
                action = inputString.substring(movement, movement + 1);
                // System.out.println(action);
                if (action.toLowerCase().equals("a")) {
                    gameOver = mapGen.moveAvatarLeft();
                } else if (action.toLowerCase().equals("w")) {
                    gameOver = mapGen.moveAvatarUp();
                } else if (action.toLowerCase().equals("d")) {
                    gameOver = mapGen.moveAvatarRight();
                } else if (action.toLowerCase().equals("s")) {
                    gameOver = mapGen.moveAvatarDown();
                } else if (action.equals(":")) {
                    movement += 1;
                    if (movement >= inputString.length()) {
                        return mapGen.getMap();
                    }
                    action = inputString.substring(movement, movement + 1);
                    if (action.toLowerCase().equals("q")) {
                        saveMapToTxtFile(mapGen.getMap());
                        gameOver = true;
                    }
                }
                movement += 1;
            }

        } else if (inputString.charAt(0) == 'l') {
            TETile[][] savedMap = readMapFromSavedData();
            mapGen = new MapGenerator(savedMap);
            int movement = 1;

            String action = "";
            while(!gameOver && movement < inputString.length()) {
                action = inputString.substring(movement, movement + 1);
                if (action.toLowerCase().equals("a")) {
                    gameOver = mapGen.moveAvatarLeft();
                } else if (action.toLowerCase().equals("w")) {
                    gameOver = mapGen.moveAvatarUp();
                } else if (action.toLowerCase().equals("d")) {
                    gameOver = mapGen.moveAvatarRight();
                } else if (action.toLowerCase().equals("s")) {
                    gameOver = mapGen.moveAvatarDown();
                } else if (action.equals(":")) {
                    movement += 1;
                    if (movement >= inputString.length()) {
                        return mapGen.getMap();
                    }
                    action = inputString.substring(movement, movement + 1);
                    if (action.toLowerCase().equals("q")) {
                        saveMapToTxtFile(mapGen.getMap());
                        gameOver = true;
                    }
                }
                movement += 1;
            }
        }

        return mapGen.getMap();
    }

    /**
     * Takes user input and converts it to a String.
     * @return input String
     */
    private String solicitCharInput() {
        int length = 0;
        String typed = "";

        while (length != 1) {
            if (StdDraw.hasNextKeyTyped()) {
                typed += StdDraw.nextKeyTyped();
                length += 1;
            }
        }

        return typed;
    }

    public String solicitSeedInput() {

        int length = 0;
        String typed = "";
        char key = ' ';
        int end = 19;

        while (length != end) {
            if (StdDraw.hasNextKeyTyped()) {
                key = StdDraw.nextKeyTyped();
                if (key == 's' || key == 'S') {
                    if (typed.equals("")) {
                        // int random = (int) (Math.random() * 900000000000.0);
                        // typed = Integer.toString(random);
                        typed = "123456789";
                    }
                    length = end;
                } else if (Character.isDigit(key)) {
                    typed += key;
                    drawSeedSolicitation(typed);
                    length += 1;
                }
            }
        }
        return typed;
    }

    private void drawVictoryScreen() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 60);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "YOU ESCAPED, CONGRATS!!!");
        Font fontSmall = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, HEIGHT / 3, "(hit n to start a new game, or anything else to exit)");
        StdDraw.show();
    }

    private void drawMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "Main Menu");

        Font fontSmall = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, 2 * HEIGHT / 4, "New Game (N)");
        StdDraw.text(WIDTH / 2, 2 * HEIGHT / 4 - 5, "Load Game (L)");
        StdDraw.text(WIDTH / 2, 2 * HEIGHT / 4 - 10, "Quit (Q)");
        StdDraw.show();
    }

    private void drawSeedSolicitation(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "Enter desired seed:");

        StdDraw.text(WIDTH / 2, 2 * HEIGHT / 4, s);

        Font fontMedium = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontMedium);
        StdDraw.text(WIDTH / 2, 1 * HEIGHT / 4, "(hit s to start playing now)");
        StdDraw.text(WIDTH / 2, 1 * HEIGHT / 4 - 3, "[default seed is 123456789]");
        StdDraw.show();
    }

    private TETile[][] readMapFromSavedData() {
        TETile[][] toReturn = new TETile[WIDTH][HEIGHT];
        try {
            FileReader fileReader = new FileReader("saveData.txt");

            char tile = ' ';
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    tile = (char) fileReader.read();
                    if (tile == '#') {
                        toReturn[i][j] = Tileset.WALL;
                    } else if (tile == '.') {
                        toReturn[i][j] = Tileset.FLOOR;
                    } else if (tile == '@') {
                        toReturn[i][j] = Tileset.AVATAR;
                    } else if (tile == 'L') {
                        toReturn[i][j] = Tileset.LOCKED_DOOR;
                    } else if (tile == ' ') {
                        toReturn[i][j] = Tileset.NOTHING;
                    } else if (tile == 'F') {
                        toReturn[i][j] = Tileset.FLOWER;
                    } else if (tile == 'U') {
                        toReturn[i][j] = Tileset.UNLOCKED_DOOR;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return toReturn;
    }

    private void saveMapToTxtFile(TETile[][] toBeSaved) {
        try {
            FileWriter fileWriter = new FileWriter("saveData.txt");
            String map = "";
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    if (toBeSaved[i][j] == Tileset.WALL) {
                        map += "#";
                    } else if (toBeSaved[i][j] == Tileset.FLOOR) {
                        map += ".";
                    } else if (toBeSaved[i][j] == Tileset.AVATAR) {
                        map += "@";
                    } else if (toBeSaved[i][j] == Tileset.LOCKED_DOOR) {
                        map += "L";
                    } else if (toBeSaved[i][j] == Tileset.NOTHING) {
                        map += " ";
                    } else if (toBeSaved[i][j] == Tileset.FLOWER) {
                        map += "F";
                    } else if (toBeSaved[i][j] == Tileset.UNLOCKED_DOOR) {
                        map += "U";
                    }
                }
            }
            fileWriter.write(map);
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
