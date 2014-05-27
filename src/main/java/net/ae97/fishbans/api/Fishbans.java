/*
 * Copyright (C) 2014 Lord_Ralex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.ae97.fishbans.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Pattern;
import net.ae97.fishbans.api.exceptions.NoSuchBanServiceException;
import net.ae97.fishbans.api.exceptions.NoSuchUUIDException;
import net.ae97.fishbans.api.exceptions.NoSuchUserException;

/**
 * The Fishbans API main class. This class is how information about bans may be
 * retrieved. This uses the API which is documented at
 * http://fishbans.com/docs.php.
 *
 * @since 1.0
 *
 * @author Lord_Ralex
 */
public class Fishbans {

    private static final HashMap<String, BanCache> banCache = new HashMap<String, BanCache>();
    private static final long cacheTime = 1000 * 60 * 5;
    private static final Pattern uuidConvertor = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    public static FishbanPlayer getFishbanPlayer(String name, boolean force) throws IOException, NoSuchUserException {
        if (!force) {
            FishbanPlayer player = checkCache(name);
            if (player != null) {
                return player;
            }
        }
        return getData(name);
    }

    public static FishbanPlayer getFishbanPlayer(UUID uuid, boolean force) throws IOException, NoSuchUUIDException {
        if (!force) {
            FishbanPlayer player = checkCache(uuid.toString());
            if (player != null) {
                return player;
            }
        }
        return getData(uuid);
    }

    /**
     * Retrieves the list of {@link Ban} on a player. This will check the local
     * cache to avoid network calls if not needed.
     *
     * @param username Username of player
     *
     * @return List of Bans on the player, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(String username) throws IOException, NoSuchUserException {
        return getBans(username, false);
    }

    /**
     * Retrieves the list of {@link Ban} on an {@link UUID}. This will check the
     * local cache to avoid network calls if not needed.
     *
     * @param uuid UUID to check bans on
     *
     * @return List of Bans on the UUID, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no UUID exists on Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid) throws IOException, NoSuchUUIDException {
        return getBans(uuid, false);
    }

    /**
     * Retrieves the list of {@link Ban} on a player for a particular
     * {@link BanService}. This will check the local cache to avoid network
     * calls if not needed.
     *
     * @param username Username of player
     * @param service BanService to check for bans
     *
     * @return List of Bans on the player, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(String username, BanService service) throws IOException, NoSuchUserException {
        return getBans(username, service, false);
    }

    /**
     * Retrieves the list of {@link Ban} on an {@link UUID} for a particular
     * {@link BanService}. This will check the local cache to avoid network
     * calls if not needed.
     *
     * @param uuid UUID to check bans on
     * @param service BanService to check for bans
     *
     * @return List of Bans on the player, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no {@link UUID} with that name
     * exists on Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid, BanService service) throws IOException, NoSuchUUIDException {
        return getBans(uuid, service, false);
    }

    /**
     * Retrieves the list of {@link Ban} on a player for a particular
     * {@link BanService}. This will check the local cache to avoid network
     * calls if not needed.
     *
     * @param username Username of player
     * @param service Ban service to check for bans
     *
     * @return List of Bans on the player, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * @throws NoSuchBanServiceException Thrown if no {@link BanService} exists
     * with the name Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(String username, String service) throws IOException, NoSuchUserException, NoSuchBanServiceException {
        return getBans(username, service, false);
    }

    /**
     * Retrieves the list of {@link Ban} on an {@link UUID} for a particular
     * {@link BanService}. This will check the local cache to avoid network
     * calls if not needed.
     *
     * @param uuid UUID to check bans on
     * @param service Ban service to check for bans
     *
     * @return List of Bans on the player, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no {@link UUID} with that name
     * exists on Fishbans
     * @throws NoSuchBanServiceException Thrown if no {@link BanService} exists
     * with the name Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid, String service) throws IOException, NoSuchUUIDException, NoSuchBanServiceException {
        return getBans(uuid, service, false);
    }

    /**
     * Retrieves the list of {@link Ban} on a player.
     *
     * @param username Username of player
     * @param force True to ignore cached data, false to permit cached data
     *
     * @return List of Bans on the player, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(String username, boolean force) throws IOException, NoSuchUserException {
        return Fishbans.getFishbanPlayer(username, force).getBanList();
    }

    /**
     * Retrieves the list of {@link Ban} on an {@link UUID}.
     *
     * @param uuid UUID to check bans on
     * @param force True to ignore cached data, false to permit cached data
     *
     * @return List of Bans on the player, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no {@link UUID} with that name
     * exists on Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid, boolean force) throws IOException, NoSuchUUIDException {
        return Fishbans.getFishbanPlayer(uuid, force).getBanList();
    }

    /**
     * Retrieves the list of {@link Ban} on a player.
     *
     * @param username Username of player
     * @param service Ban service to check for bans
     * @param force True to ignore cached data, false to permit cached data
     *
     * @return List of Bans on the player, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(String username, BanService service, boolean force) throws IOException, NoSuchUserException {
        if (service == null) {
            throw new IllegalArgumentException("BanService cannot be null");
        }
        return Fishbans.getFishbanPlayer(username, force).getBanList(service);
    }

    /**
     * Retrieves the list of {@link Ban} on an {@link UUID}.
     *
     * @param uuid UUID to check bans on
     * @param service Ban service to check for bans
     * @param force True to ignore cached data, false to permit cached data
     *
     * @return List of Bans on the player, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no {@link UUID} with that name
     * exists on Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid, BanService service, boolean force) throws IOException, NoSuchUUIDException {
        if (service == null) {
            throw new IllegalArgumentException("BanService cannot be null");
        }
        return Fishbans.getFishbanPlayer(uuid, force).getBanList(service);
    }

    /**
     * Retrieves the list of {@link Ban} on a player for a particular
     * {@link BanService}.
     *
     * @param username Username of player
     * @param service Ban service to check for bans
     * @param force True to ignore cached data, false to permit cached data
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * @throws NoSuchBanServiceException Thrown if no {@link BanService} exists
     * with the name Fishbans
     * @since 1.0
     */
    public static List<Ban> getBans(String username, String service, boolean force) throws IOException, NoSuchUserException, NoSuchBanServiceException {
        BanService banservice = BanService.getService(service);
        if (banservice == null) {
            throw new NoSuchBanServiceException(service);
        }
        return Fishbans.getFishbanPlayer(username, force).getBanList(banservice);
    }

    /**
     * Retrieves the list of {@link Ban} on an {@link UUID} for a particular
     * {@link BanService}.
     *
     * @param uuid UUID to check bans on
     * @param service Ban service to check for bans
     * @param force True to ignore cached data, false to permit cached data
     *
     * @return List of Bans on the player, never null
     *
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no {@link UUID} with that name
     * exists on Fishbans
     * @throws NoSuchBanServiceException Thrown if no {@link BanService} exists
     * with the name Fishbans
     *
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid, String service, boolean force) throws IOException, NoSuchUUIDException, NoSuchBanServiceException {
        BanService banservice = BanService.getService(service);
        if (banservice == null) {
            throw new NoSuchBanServiceException(service);
        }
        return Fishbans.getFishbanPlayer(uuid, force).getBanList(banservice);
    }

    public static void clearCache() {
        synchronized (banCache) {
            banCache.clear();
        }
    }

    private static FishbanPlayer checkCache(String key) {
        key = key.toLowerCase();
        synchronized (banCache) {
            BanCache cached = banCache.get(key);
            if (cached != null && cached.getCacheTime() + cacheTime < System.currentTimeMillis()) {
                return cached.getBans();
            }
        }
        return null;
    }

    private static FishbanPlayer getData(UUID uuid) throws IOException, NoSuchUUIDException {
        String name = getUsernameFromUUID(uuid);
        if (name == null) {
            throw new NoSuchUUIDException(uuid);
        }
        try {
            return getData(name);
        } catch (NoSuchUserException ex) {
            throw new NoSuchUUIDException(ex);
        }
    }

    private static FishbanPlayer getData(String name) throws IOException, NoSuchUserException {
        URL connURL = new URL("http://api.fishbans.com/bans/" + name);
        BufferedReader reader = null;
        JsonElement element = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connURL.openStream()));
            JsonParser parser = new JsonParser();
            element = parser.parse(reader);
        } catch (JsonParseException e) {
            throw new IOException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        if (element == null) {
            return null;
        }
        JsonObject maps = element.getAsJsonObject();
        if (!maps.get("success").getAsBoolean()) {
            throw new NoSuchUserException(name);
        }
        JsonObject bans = maps.getAsJsonObject("bans").getAsJsonObject("service");
        LinkedList<Ban> banlist = new LinkedList<Ban>();
        for (Entry<String, JsonElement> banEntry : bans.entrySet()) {
            BanService provider = BanService.getService(banEntry.getKey());
            JsonObject obj = banEntry.getValue().getAsJsonObject();
            if (obj.get("bans").getAsInt() == 0) {
                continue;
            }
            for (Entry<String, JsonElement> banListing : obj.get("ban_info").getAsJsonObject().entrySet()) {
                banlist.add(new Ban(provider, banListing.getKey(), banListing.getValue().getAsString()));
            }
        }
        FishbanPlayer playerBans = new FishbanPlayer(banlist, maps.getAsJsonObject("bans").get("username").getAsString(), convertToUUID(maps.getAsJsonObject("bans").get("uuid").getAsString()));
        banCache.put(name.toLowerCase(), new BanCache(playerBans));
        return playerBans;
    }

    /**
     * Converts a String with no -s into a UUID. Exists to convert Mojang UUID
     * to Java {@link UUID}.
     *
     * @param input UUID in String form
     *
     * @return Converted UUID
     *
     * @since 1.0
     */
    public static UUID convertToUUID(String input) {
        String converted = uuidConvertor.matcher(input).replaceAll("$1-$2-$3-$4-$5");
        return UUID.fromString(converted);
    }

    /**
     * Gets the username for a particular UUID by asking the
     * https://sessionserver.mojang.com server for the information. This only
     * exists to permit UUID support as Fishbans does not accept it at the time
     * of 1.0 release.
     *
     * @param uuid The UUID to convert
     * @return The username, or null if no username
     * @since 1.0
     * @throws IOException Thrown when communication to the Mojang server fails
     */
    public static String getUsernameFromUUID(UUID uuid) throws IOException {
        URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", ""));
        BufferedReader reader = null;
        JsonObject response = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String reply;
            reply = reader.readLine();
            if (reply == null) {
                return null;
            }
            response = new JsonParser().parse(reply).getAsJsonObject();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        if (reader == null || response == null) {
            return null;
        }
        if (response.has("error")) {
            return null;
        } else {
            return response.get("name").getAsString();
        }
    }

    private static class BanCache {

        private final FishbanPlayer banlist;
        private final long storageTime;

        private BanCache(FishbanPlayer banlist) {
            this.banlist = banlist;
            this.storageTime = System.currentTimeMillis();
        }

        protected long getCacheTime() {
            return storageTime;
        }

        protected FishbanPlayer getBans() {
            return banlist;
        }
    }
}
