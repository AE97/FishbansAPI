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
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import net.ae97.fishbans.api.exceptions.NoSuchBanServiceException;
import net.ae97.fishbans.api.exceptions.NoSuchUUIDException;
import net.ae97.fishbans.api.exceptions.NoSuchUserException;

/**
 * The Fishbans API main class. This class is how information about bans may be
 * retrieved. This uses the API which is documented at
 * http://fishbans.com/docs.php.
 *
 * @since 1.0
 * @author Lord_Ralex
 */
public class Fishbans {

    private static final HashMap<String, BanCache> banCache = new HashMap<String, BanCache>();
    private static final long cacheTime = 1000 * 60 * 5;

    /**
     * Retrieves the list of {@link Ban} on a player. This will check the local
     * cache to avoid network calls if not needed.
     *
     * @param username Username of player
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * Fishbans
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
     * @return List of Bans on the UUID, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no UUID exists on Fishbans
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
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * Fishbans
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
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no {@link UUID} with that name
     * exists on Fishbans
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
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * @throws NoSuchBanServiceException Thrown if no {@link BanService} exists
     * with the name Fishbans
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
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no {@link UUID} with that name
     * exists on Fishbans
     * @throws NoSuchBanServiceException Thrown if no {@link BanService} exists
     * with the name Fishbans
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
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * Fishbans
     * @since 1.0
     */
    public static List<Ban> getBans(String username, boolean force) throws IOException, NoSuchUserException {
        if (!force) {
            PlayerBans banlist = checkCache(username);
            if (banlist != null) {
                return banlist.getBanList();
            }
        }
        PlayerBans bans = getData(username);
        if (bans == null) {
            throw new NoSuchUserException(username);
        }
        return bans.getBanList();
    }

    /**
     * Retrieves the list of {@link Ban} on an {@link UUID}.
     *
     * @param uuid UUID to check bans on
     * @param force True to ignore cached data, false to permit cached data
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no {@link UUID} with that name
     * exists on Fishbans
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid, boolean force) throws IOException, NoSuchUUIDException {
        if (!force) {
            PlayerBans banlist = checkCache(uuid.toString());
            if (banlist != null) {
                return banlist.getBanList();
            }
        }
        PlayerBans bans = getData(uuid.toString());
        if (bans == null) {
            throw new NoSuchUUIDException(uuid);
        }
        return bans.getBanList();
    }

    /**
     * Retrieves the list of {@link Ban} on a player.
     *
     * @param username Username of player
     * @param service Ban service to check for bans
     * @param force True to ignore cached data, false to permit cached data
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUserException Thrown if no user with that name exists on
     * Fishbans
     * @since 1.0
     */
    public static List<Ban> getBans(String username, BanService service, boolean force) throws IOException, NoSuchUserException {
        if (service == null) {
            throw new IllegalArgumentException("BanService cannot be null");
        }
        if (!force) {
            PlayerBans banlist = checkCache(username);
            if (banlist != null) {
                return banlist.getBanList(service);
            }
        }
        PlayerBans bans = getData(username);
        if (bans == null) {
            throw new NoSuchUserException(username);
        }
        return bans.getBanList(service);
    }

    /**
     * Retrieves the list of {@link Ban} on an {@link UUID}.
     *
     * @param uuid UUID to check bans on
     * @param service Ban service to check for bans
     * @param force True to ignore cached data, false to permit cached data
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no {@link UUID} with that name
     * exists on Fishbans
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid, BanService service, boolean force) throws IOException, NoSuchUUIDException {
        if (service == null) {
            throw new IllegalArgumentException("BanService cannot be null");
        }
        if (!force) {
            PlayerBans banlist = checkCache(uuid.toString());
            if (banlist != null) {
                return banlist.getBanList(service);
            }
        }
        PlayerBans bans = getData(uuid.toString());
        if (bans == null) {
            throw new NoSuchUUIDException(uuid);
        }
        return bans.getBanList(service);
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
        if (service == null) {
            throw new NoSuchBanServiceException(service);
        }
        if (!force) {
            PlayerBans banlist = checkCache(username);
            if (banlist != null) {
                return banlist.getBanList(banservice);
            }
        }
        PlayerBans bans = getData(username);
        if (bans == null) {
            throw new NoSuchUserException(username);
        }
        return bans.getBanList(banservice);
    }

    /**
     * Retrieves the list of {@link Ban} on an {@link UUID} for a particular
     * {@link BanService}.
     *
     * @param uuid UUID to check bans on
     * @param service Ban service to check for bans
     * @param force True to ignore cached data, false to permit cached data
     * @return List of Bans on the player, never null
     * @throws IOException Thrown if the data cannot be retrieved from Fishbans
     * @throws NoSuchUUIDException Thrown if no {@link UUID} with that name
     * exists on Fishbans
     * @throws NoSuchBanServiceException Thrown if no {@link BanService} exists
     * with the name Fishbans
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid, String service, boolean force) throws IOException, NoSuchUUIDException, NoSuchBanServiceException {
        BanService banservice = BanService.getService(service);
        if (service == null) {
            throw new NoSuchBanServiceException(service);
        }
        if (!force) {
            PlayerBans banlist = checkCache(uuid.toString());
            if (banlist != null) {
                return banlist.getBanList(banservice);
            }
        }
        PlayerBans bans = getData(uuid.toString());
        if (bans == null) {
            throw new NoSuchUUIDException(uuid);
        }
        return bans.getBanList(banservice);
    }

    private static PlayerBans checkCache(String key) {
        key = key.toLowerCase();
        synchronized (banCache) {
            BanCache cached = banCache.get(key);
            if (cached.getCacheTime() + cacheTime > System.currentTimeMillis()) {
            } else {
                return cached.getBans();
            }
        }
        return null;
    }

    private static PlayerBans getData(String name) throws IOException, JsonParseException {
        URL connURL = new URL("http://api.fishbans.com/bans/" + name);
        BufferedReader reader = null;
        JsonElement element = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connURL.openStream()));
            JsonParser parser = new JsonParser();
            element = parser.parse(reader);
        } catch (IOException e) {
            throw e;
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
            return null;
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
        PlayerBans playerBans = new PlayerBans(banlist);
        banCache.put(name.toLowerCase(), new BanCache(playerBans));
        return playerBans;
    }

    private static class BanCache {

        private final PlayerBans banlist;
        private final long storageTime;

        private BanCache(PlayerBans banlist) {
            this.banlist = banlist;
            this.storageTime = System.currentTimeMillis();
        }

        protected long getCacheTime() {
            return storageTime;
        }

        protected PlayerBans getBans() {
            return banlist;
        }
    }
}
