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

/**
 * This is the ban record for a user. It contains the list of bans this user
 * (determined by UUID or username) in no particular order.
 *
 * @since 1.0
 * @author Lord_Ralex
 */
public class PlayerBans {

    private final LinkedList<Ban> banlist;
    private final EnumMap<BanService, LinkedList<Ban>> counts = new EnumMap<BanService, LinkedList<Ban>>(BanService.class);

    protected PlayerBans(List<Ban> banlist) {
        this.banlist = new LinkedList<Ban>(banlist);
        for (BanService service : BanService.values()) {
            counts.put(service, new LinkedList<Ban>());
        }
        for (Ban ban : this.banlist) {
            if (ban.getService() != null) {
                LinkedList<Ban> old = counts.get(ban.getService());
                old.add(ban);
                counts.put(ban.getService(), old);
            }
        }
    }

    /**
     * Get the list of {@link Ban}s this particular player has. This is never
     * null.
     *
     * @return List of Bans for this player, never null.
     */
    public List<Ban> getBanList() {
        return (List<Ban>) banlist.clone();
    }

    /**
     * Get the list of {@link Ban}s this particular player has from a given
     * {@link BanService}. This is never null.
     *
     * @param service Service to retrieve bans from
     * @return List of Bans from that service, never null
     */
    public List<Ban> getBanList(BanService service) {
        return counts.get(service);
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
     * @return Number of bans
     */
    public int getBanCount(BanService service) {
        return counts.get(service).size();
    }
}
