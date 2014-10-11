/*
 * Copyright (C) 2014 Joshua
 *
 * This file is a part of FishbansAPI
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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * The list of ban services that http://fishbans.com retrieves bans from.
 *
 * @since 1.1
 * @author Lord_Ralex
 */
public class BanServices {

    private static final HashSet<BanServices> registeredServices = new HashSet<BanServices>();
    private final String name;
    private final String displayName;
    private final boolean legacy;

    static {
        registeredServices.add(new BanServices("McBans", false));
        registeredServices.add(new BanServices("Minebans", false));
        registeredServices.add(new BanServices("McBouncer", false));
        registeredServices.add(new BanServices("McBlockIt", true));
        registeredServices.add(new BanServices("Glizer", false));
    }

    public static BanServices getService(String name) {
        synchronized (registeredServices) {
            for (BanServices service : registeredServices) {
                if (service.getName().equalsIgnoreCase(name)) {
                    return service;
                }
            }
        }
        return null;
    }

    public static Set<BanServices> getBanServices() {
        synchronized (registeredServices) {
            return (Set< BanServices>) registeredServices.clone();
        }
    }

    public static void updateServices() throws IOException {
        synchronized (registeredServices) {
            //TODO: Add sync to the API which updates this listing
        }
    }

    private BanServices(String displayName, boolean legacy) {
        this.name = displayName.toUpperCase();
        this.displayName = displayName;
        this.legacy = legacy;
    }

    /**
     * Returns the user-friendly name for this ban service.
     *
     * @return User-friendly name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns whether the ban information for this ban service is legacy data
     * on http://fishbans.com. Legacy ban services are ban services who no
     * longer run or permit Fishbans from retrieving their data. Any data stored
     * by Fishbans from such providers may be old and not contain new bans.
     *
     * @return True if bans are legacy data, false otherwise
     */
    public boolean isLegacy() {
        return legacy;
    }

    /**
     * Returns the standardized name for this ban service.
     *
     * @return The name of this ban service
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BanServices{id=" + this.getName() + ", displayName=" + getDisplayName() + ", legacy=" + isLegacy() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BanServices) {
            return ((BanServices) obj).getName().equals(this.getName());
        }
        return false;
    }
}
