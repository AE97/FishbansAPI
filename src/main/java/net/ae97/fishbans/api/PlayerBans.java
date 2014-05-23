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
 * @since 1.0
 * @author Lord_Ralex
 */
public class PlayerBans {

    private final LinkedList<Ban> banlist;
    private final EnumMap<BanService, Integer> counts = new EnumMap<BanService, Integer>(BanService.class);

    protected PlayerBans(List<Ban> banlist) {
        this.banlist = new LinkedList<Ban>(banlist);
        for (BanService service : BanService.values()) {
            counts.put(service, 0);
        }
        for (Ban ban : this.banlist) {
            if (ban.getService() != null) {
                counts.put(ban.getService(), counts.get(ban.getService()));
            }
        }
    }

    public List<Ban> getBanList() {
        return (List<Ban>) banlist.clone();
    }

    public List<Ban> getBanList(BanService service) {
        LinkedList<Ban> bans = new LinkedList<Ban>();
        for (Ban ban : banlist) {
            if (ban.getService() == service) {
                bans.add(ban);
            }
        }
        return bans;
    }

    public int getBanCount() {
        return banlist.size();
    }

    public int getBanCount(BanService service) {
        return counts.get(service);
    }
}
