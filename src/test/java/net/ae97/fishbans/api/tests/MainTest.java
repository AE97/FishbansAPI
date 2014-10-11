package net.ae97.fishbans.api.tests;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import junit.framework.Assert;
import net.ae97.fishbans.api.Ban;
import net.ae97.fishbans.api.BanServices;
import net.ae97.fishbans.api.Fishbans;
import net.ae97.fishbans.api.exceptions.NoSuchBanServiceException;
import net.ae97.fishbans.api.exceptions.NoSuchUUIDException;
import net.ae97.fishbans.api.exceptions.NoSuchUserException;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Lord_Ralex
 */
public class MainTest {

    private final String username = "kalfin";
    private final UUID uuid = UUID.fromString("61699b2e-d327-4a01-9f1e-0ea8c3f06bc6");

    @Before
    public void setUp() {
        Fishbans.clearCache();
    }

    @After
    public void tearDown() {
        Fishbans.clearCache();
    }

    @Test
    public void testUUIDConversion() {
        System.out.println(Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})").matcher("12345678123412341234123456789012").replaceAll("$1-$2-$3-$4-$5"));
        Assert.assertEquals(UUID.fromString("12345678-1234-1234-1234-123456789012"), Fishbans.convertToUUID("12345678123412341234123456789012"));
    }

    //@Test
    public void testUsername() throws IOException, NoSuchUserException {
        System.out.println("Testing username");
        List<Ban> bans = Fishbans.getBans(username);
        System.out.println("Bans: " + StringUtils.join(bans, ", "));
    }

    //@Test
    public void testUUID() throws IOException, NoSuchUUIDException {
        System.out.println("Testing UUID");
        List<Ban> bans = Fishbans.getBans(uuid);
        System.out.println("Bans: " + StringUtils.join(bans, ", "));
    }

    //@Test
    public void testUsernameDefinedService() throws IOException, NoSuchUserException {
        System.out.println("Testing username and defined service");
        List<Ban> bans = Fishbans.getBans(username, BanServices.getService("GLIZER"));
        System.out.println("Bans: " + StringUtils.join(bans, ", "));
    }

    //@Test
    public void testUUIDDefinedService() throws IOException, NoSuchUUIDException {
        System.out.println("Testing UUID and defined service");
        List<Ban> bans = Fishbans.getBans(uuid, BanServices.getService("GLIZER"));
        System.out.println("Bans: " + StringUtils.join(bans, ", "));
    }

    //@Test
    public void testUsernameInvalidService() throws IOException, NoSuchUserException {
        System.out.println("Testing username and invalid service");
        try {
            Fishbans.getBans(username, "NotABanService");
            Assert.fail();
        } catch (NoSuchBanServiceException ex) {
        }
    }

    //@Test
    public void testUUIDInvalidService() throws IOException, NoSuchUUIDException {
        System.out.println("Testing UUID");
        try {
            Fishbans.getBans(uuid, "NotABanService");
            Assert.fail();
        } catch (NoSuchBanServiceException ex) {
        }
    }

    //@Test
    public void testInvalidUsername() throws IOException {
        System.out.println("Testing invalid username");
        try {
            Fishbans.getBans("no");
            Assert.fail();
        } catch (NoSuchUserException ex) {
        }
    }

    //@Test
    public void testInvalidUUID() throws IOException {
        System.out.println("Testing invalid UUID");
        try {
            Fishbans.getBans(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            Assert.fail();
        } catch (NoSuchUUIDException ex) {
        }
    }

    //@Test
    public void testUUIDUserConversion() throws IOException {
        Assert.assertEquals("UUID checker failed to get correct UUID", "Dinnerbone", Fishbans.getUsernameFromUUID(UUID.fromString("61699b2e-d327-4a01-9f1e-0ea8c3f06bc6")));
        Assert.assertNull("UUID checker failed to get correct UUID", Fishbans.getUsernameFromUUID(UUID.fromString("00000000-0000-0000-0000-000000000000")));
    }
}
