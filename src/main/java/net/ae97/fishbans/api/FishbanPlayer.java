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

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

/**
 * This is the ban record for a user. It contains the list of bans this user
 * (determined by UUID or username) in no particular order.
 *
 * @since 1.0
 *
 * @author Lord_Ralex
 */
public class FishbanPlayer {

    private final LinkedList<Ban> banlist;
    private final EnumMap<BanService, LinkedList<Ban>> counts = new EnumMap<BanService, LinkedList<Ban>>(BanService.class);
    private final String playerName;
    private final UUID playerUUID;

    protected FishbanPlayer(List<Ban> banlist, String name, UUID uuid) {
        this.banlist = new LinkedList<Ban>(banlist);
        for (BanService service : BanService.values()) {
            counts.put(service, new LinkedList<Ban>());
        }
        for (Ban ban : this.banlist) {
            if (ban.getService() != null) {
                counts.get(ban.getService()).add(ban);
            }
        }
        this.playerName = name;
        this.playerUUID = uuid;
    }

    /**
     * Get the list of {@link Ban}s this particular player has. This is never
     * null.
     *
     * @return List of Bans for this player, never null.
     */
    public List<Ban> getBanList() {
        //TODO: Replace with Immutable lists, either using custom-made or Google guava
        return (List<Ban>) banlist.clone();
    }

    /**
     * Get the list of {@link Ban}s this particular player has from a given
     * {@link BanService}. This is never null.
     *
     * @param service Service to retrieve bans from
     *
     * @return List of Bans from that service, never null
     */
    public List<Ban> getBanList(BanService service) {
        //TODO: Replace with Immutable lists, either using custom-made or Google guava
        return (List<Ban>) counts.get(service).clone();
    }

    /**
     * Gets the number of bans this player has
     *
     * @return Number of bans
     */
    public int getBanCount() {
        return banlist.size();
    }

    /**
     * Get the number of {@link Ban}s this particular player has from a given
     * {@link BanService}
     *
     * @param service Service to retrieve ban count from
     *
     * @return Number of bans
     */
    public int getBanCount(BanService service) {
        return counts.get(service).size();
    }

    /**
     * Returns the {@link UUID} for this player
     *
     * @return {@link UUID} for this player
     */
    public UUID getUUID() {
        return playerUUID;
    }

    /**
     * Returns the username for this player
     *
     * @return Username for this player
     */
    public String getName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "FishbanPlayer{name=" + playerName + ", uuid=" + playerUUID.toString() + ", banlist={" + StringUtils.join(banlist, ", ") + "}";
    }
}
