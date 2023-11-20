package cr.ac.una.pac.man;

/**
 *
 * @author dario
 */
public class TopPlayers {
    
    String name;
    String score;

    public TopPlayers() {
    }

    public TopPlayers(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    
}
