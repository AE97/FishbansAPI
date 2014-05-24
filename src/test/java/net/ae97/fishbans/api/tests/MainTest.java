package net.ae97.fishbans.api.tests;

import java.io.IOException;
import java.util.List;
import net.ae97.fishbans.api.Ban;
import net.ae97.fishbans.api.BanService;
import net.ae97.fishbans.api.Fishbans;
import net.ae97.fishbans.api.exceptions.NoSuchBanServiceException;
import net.ae97.fishbans.api.exceptions.NoSuchUserException;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Lord_Ralex
 */
public class MainTest {

    @Before
    public void setUp() {
        Fishbans.clearCache();
    }

    @After
    public void tearDown() {
        Fishbans.clearCache();
    }

    @Test
    public void testUsername() throws IOException, NoSuchUserException {
        System.out.println("Testing username");
        List<Ban> bans = Fishbans.getBans("kalfin", true);
        System.out.println("Bans: " + StringUtils.join(bans, ", "));
    }

    @Test
    public void testUUID() {
    }

    @Test
    public void testUsernameDefinedService() throws IOException, NoSuchUserException {
        System.out.println("Testing username and defined service");
        List<Ban> bans = Fishbans.getBans("kalfin", BanService.GLIZER, true);
        System.out.println("Bans: " + StringUtils.join(bans, ", "));
    }

    @Test
    public void testUUIDDefinedService() {
    }

    @Test
    public void testUsernameInvalidService() throws IOException, NoSuchUserException {
        System.out.println("Testing username and invalid service");
        try {
            Fishbans.getBans("kalfin", "NotABanService");
            fail();
        } catch (NoSuchBanServiceException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Test
    public void testUUIDInvalidService() {
    }

    @Test
    public void testInvalidUsername() throws IOException {
        System.out.println("Testing invalid username");
        try {
            Fishbans.getBans("no");
            fail();
        } catch (NoSuchUserException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Test
    public void testInvalidUUID() {
    }
}
