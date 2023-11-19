package cr.ac.una.pac.man;

/**
 *
 * @author dario
 */
public class Statistics {
    
    String score;
    String time;
    String lifes;
    String ghost;

    public Statistics() {
    }
    
    public Statistics(String score, String time, String lifes, String ghost) {
        this.score = score;
        this.time = time;
        this.lifes = lifes;
        this.ghost = ghost;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLifes() {
        return lifes;
    }

    public void setLifes(String lifes) {
        this.lifes = lifes;
    }

    public String getGhost() {
        return ghost;
    }

    public void setGhost(String ghost) {
        this.ghost = ghost;
    }
    
    
    
    
    
}
