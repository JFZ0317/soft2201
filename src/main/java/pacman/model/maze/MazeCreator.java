package pacman.model.maze;

import pacman.model.entity.factory.GhostFactory;
import pacman.model.entity.factory.PelletFactory;
import pacman.model.entity.factory.PlayerFactory;
import pacman.model.entity.factory.WallFactory;
import pacman.model.entity.staticentity.WallVisual;

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
        GhostFactory ghostfactory = new GhostFactory();
        PelletFactory pelletFactory = new PelletFactory();
        WallFactory wallFactory = new WallFactory();
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
                    if (currentChar == 'g'){
                        maze.addRenderable(ghostfactory.createEntity(display_x,display_y), RenderableType.GHOST,display_x,display_y);
                    }
                    if (currentChar == '7'){
                        maze.addRenderable(pelletFactory.createEntity(display_x,display_y),RenderableType.PELLET,display_x,display_y);
                    }
                    if (currentChar == '1'){
                        maze.addRenderable(wallFactory.createEntity(display_x,display_y, WallVisual.Horizontal_Wall),RenderableType.HORIZONTAL_WALL,display_x,display_y);
                    }
                    if (currentChar == '2'){
                        maze.addRenderable(wallFactory.createEntity(display_x,display_y, WallVisual.Vertical_Wall),RenderableType.VERTICAL_WALL,display_x,display_y);
                    }
                    if (currentChar == '3'){
                        maze.addRenderable(wallFactory.createEntity(display_x,display_y, WallVisual.up_left),RenderableType.UP_LEFT_WALL,display_x,display_y);
                    }
                    if (currentChar == '4'){
                        maze.addRenderable(wallFactory.createEntity(display_x,display_y, WallVisual.up_right),RenderableType.UP_RIGHT_WALL,display_x,display_y);
                    }
                    if (currentChar == '5'){
                        maze.addRenderable(wallFactory.createEntity(display_x,display_y, WallVisual.down_left),RenderableType.DOWN_LEFT_WALL,display_x,display_y);
                    }
                    if (currentChar == '6'){
                        maze.addRenderable(wallFactory.createEntity(display_x,display_y, WallVisual.down_right),RenderableType.DOWN_RIGHT_WALL,display_x,display_y);
                    }

                    /**display_y
                     * TO DO: Ghost, wall, pillet
                     */
                }

                y += 1;
            }

            scanner.close();
            maze.setTarget();
        }
        catch (FileNotFoundException e){
            System.out.println("No maze file was found.");
            exit(0);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Error");
            exit(0);
        }

        return maze;
    }
}
