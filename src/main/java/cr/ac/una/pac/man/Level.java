package cr.ac.una.pac.man;

/**
 *
 * @author dario
 */
public class Level {
    
    String name;
    String levelNumber;
    boolean available;
    boolean complete;
    String score;

    public Level() {
    }

    public Level(String name, String levelNumber, boolean available, boolean complete, String score) {
        this.name = name;
        this.levelNumber = levelNumber;
        this.available = available;
        this.complete = complete;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(String levelNumber) {
        this.levelNumber = levelNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
    
    
    
}
