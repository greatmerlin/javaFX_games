import ffhs.model.Color;
import ffhs.model.Player;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CheckersTest {
    @Test
    public void testStonesLocation() {
        Player p = new Player(Color.BLACK, "", 8);
        //Stein 1 (0|0)
        assertEquals(0, p.getTokens()[0].getIndexX());
        assertEquals(0, p.getTokens()[0].getIndexY());
        //Stein 12 (6|2)
        assertEquals(6, p.getTokens()[11].getIndexX());
        assertEquals(2, p.getTokens()[11].getIndexY());
        //Gesamtmenge
        assertEquals(12, p.getTokens().length);
    }
}
