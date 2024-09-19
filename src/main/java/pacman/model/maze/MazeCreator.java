package pacman.model.maze;

import pacman.model.entity.factory.PlayerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Responsible for creating renderables and storing it in the Maze
 */
public class MazeCreator {

    private final String fileName;
    public static final int RESIZING_FACTOR = 16;

    public MazeCreator(String fileName){
        this.fileName = fileName;
    }

    public Maze createMaze(){
        File f = new File(this.fileName);
        Maze maze = new Maze();
        PlayerFactory playerfactory = new PlayerFactory();
        try {
            Scanner scanner = new Scanner(f);

            int y = 0;

            while (scanner.hasNextLine()){

                String line = scanner.nextLine();
                char[] row = line.toCharArray();

                for (int x = 0; x < row.length; x++){
                    /**
                     * TO DO: Implement Factory Method Pattern
                     */
                    char currentChar = row[x];
                    int display_x = x * (448/28);
                    int display_y = y * (576/36);

                    if (currentChar == 'p') {
                        maze.addRenderable(playerfactory.createEntity(display_x,display_y), RenderableType.PACMAN,display_x,display_y);
                    }
                    /**
                     * TO DO: Ghost, wall, pillet
                     */
                }

                y += 1;
            }

            scanner.close();
        }
        catch (FileNotFoundException e){
            System.out.println("No maze file was found.");
            exit(0);
        } catch (Exception e){
            System.out.println("Error");
            exit(0);
        }

        return maze;
    }
}
