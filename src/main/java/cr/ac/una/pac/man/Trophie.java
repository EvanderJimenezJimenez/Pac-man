package cr.ac.una.pac.man;

/**
 *
 * @author dario
 */
public class Trophie {

    String name;
    String score;
    boolean complete;

    public Trophie() {
    }

    public Trophie(String name, String score, boolean complete) {
        this.name = name;
        this.score = score;
        this.complete = complete;
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

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return "Trophie{" + "name=" + name + ", score=" + score + ", complete=" + complete + '}';
    }
    
    

}
