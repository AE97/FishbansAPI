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
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.ae97.fishbans.api.exceptions.NoSuchProviderException;
import net.ae97.fishbans.api.exceptions.NoSuchUUIDException;
import net.ae97.fishbans.api.exceptions.NoSuchUserException;

/**
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
     * @throws NoSuchProviderException Thrown if no {@link BanService} exists
     * with the name Fishbans
     * @since 1.0
     */
    public static List<Ban> getBans(String username, String service) throws IOException, NoSuchUserException, NoSuchProviderException {
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
     * @throws NoSuchProviderException Thrown if no {@link BanService} exists
     * with the name Fishbans
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid, String service) throws IOException, NoSuchUUIDException, NoSuchProviderException {
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
            List<Ban> banlist = checkCache(username.toLowerCase());
            if (banlist != null) {
                return banlist;
            }
        }
        return null;
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
            List<Ban> banlist = checkCache(uuid.toString());
            if (banlist != null) {
                return banlist;
            }
        }
        return null;
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
        if (!force) {
            List<Ban> banlist = checkCache(username.toLowerCase());
            if (banlist != null) {
                return banlist;
            }
        }
        return null;
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
        if (!force) {
            List<Ban> banlist = checkCache(uuid.toString());
            if (banlist != null) {
                return banlist;
            }
        }
        return null;
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
     * @throws NoSuchProviderException Thrown if no {@link BanService} exists
     * with the name Fishbans
     * @since 1.0
     */
    public static List<Ban> getBans(String username, String service, boolean force) throws IOException, NoSuchUserException, NoSuchProviderException {
        if (!force) {
            List<Ban> banlist = checkCache(username.toLowerCase());
            if (banlist != null) {
                return banlist;
            }
        }
        return null;
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
     * @throws NoSuchProviderException Thrown if no {@link BanService} exists
     * with the name Fishbans
     * @since 1.0
     */
    public static List<Ban> getBans(UUID uuid, String service, boolean force) throws IOException, NoSuchUUIDException, NoSuchProviderException {
        if (!force) {
            List<Ban> banlist = checkCache(uuid.toString());
            if (banlist != null) {
                return banlist;
            }
        }
        return null;
    }

    private static List<Ban> checkCache(String key) {
        synchronized (banCache) {
            BanCache cached = banCache.get(key);
            if (cached.getCacheTime() + cacheTime > System.currentTimeMillis()) {
                banCache.remove(key);
            } else {
                return cached.getBanList();
            }
        }
        return null;
    }

    private static JsonElement getJsonData(String url) throws IOException, JsonParseException {
        URL connURL = new URL(url);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connURL.openStream()));
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(reader);
            return element;
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
    }

    private class BanCache {

        private final List<Ban> banlist;
        private final long storageTime;

        private BanCache(List<Ban> banlist) {
            this.banlist = banlist;
            this.storageTime = System.currentTimeMillis();
        }

        protected long getCacheTime() {
            return storageTime;
        }

        protected List<Ban> getBanList() {
            return banlist;
        }
    }
}
