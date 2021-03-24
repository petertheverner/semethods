import com.napier.sem.App;
import com.napier.sem.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class MyTest  //Tests for methods in main application
{

    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    //Tests below highlight the get country population method
    // For these methods there are tests to produce null and exception tests

    //test for exception thrown
    @Test
    void TestExceptionthrown()
    {
        assertThrows(NullPointerException.class, this::throwsException);
    }
    void throwsException() throws NullPointerException
    {
        throw new NullPointerException();
    }


    //Tests for get population method

    @Test
    void testnonnull()
    {
        assertNotNull("Hello");
    }

    //test for not null to population search
    @Test
    void populationTestContainsNull()
    {
        Country tempCountry = new Country();

    }
    //test for not null to population search
    @Test
    void populationsearchnull()
    {

    }

    //tests for




}
