package net.picklecraft.Modules.Ignore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;

/**
 * Copyright (c) 2011-2014
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Pickle
 *
 */
public class IgnorePlayer {

    private boolean allIgnore = false;
    /*
     * Converted this from "raw" player objects to String of the player's names.
     * Due to the fact player objects are null when json is parsed.
     */
    private final List<UUID> ignoreList = new ArrayList<>();
    private final UUID uuid;
    private final IgnoreModule module;

    public IgnorePlayer(IgnoreModule module, UUID uuid) {
        this.module = module;
        this.uuid = uuid;
    }

    public void unignorePlayer(UUID uuid, boolean inform) {
        Player currentPlayer = module.getPlugin().getServer().getPlayer(this.uuid);
        if (uuid != null) {
            if (ignoreList.contains(uuid)) {
                ignoreList.remove(uuid);
                if (inform) {
                    currentPlayer.sendMessage(
                            module.getPlugin().getStringFromConfig("ignorecraft.messages.info.removed", uuid)
                    );
                }
            } else if (inform) {
                currentPlayer.sendMessage(
                        module.getPlugin().getStringFromConfig("ignorecraft.messages.errors.isnotignored", uuid)
                );
            }
        }
    }

    public void ignorePlayer(UUID uuid, boolean inform) {
        Player currentPlayer = module.getPlugin().getServer().getPlayer(this.uuid);
        Player ignorePlayer = module.getPlugin().getServer().getPlayer(uuid);
        if (uuid != null) {
            if (!ignoreList.contains(uuid)) {
                if (inform) {
                    currentPlayer.sendMessage(
                            module.getPlugin().getStringFromConfig("ignorecraft.messages.info.added", ignorePlayer.getName())
                    );
                    ignorePlayer.sendMessage(
                            module.getPlugin().getStringFromConfig("ignorecraft.messages.info.ignored", currentPlayer.getName())
                    );
                }
                ignoreList.add(uuid);
            } else if (inform) {
                currentPlayer.sendMessage(
                        module.getPlugin().getStringFromConfig("ignorecraft.messages.errors.alreadyignored", ignorePlayer.getName())
                );
            }
        }
    }

    public boolean isAllIgnored() {
        return allIgnore;
    }

    public void toggleAllIgnore(boolean inform) {
        allIgnore = !allIgnore;
        if (inform) {
            Player currentPlayer = module.getPlugin().getServer().getPlayer(this.uuid);
            if (allIgnore) {
                currentPlayer.sendMessage(
                        module.getPlugin().getStringFromConfig("ignorecraft.messages.info.allignore_on")
                );
            } else {
                currentPlayer.sendMessage(
                        module.getPlugin().getStringFromConfig("ignorecraft.messages.info.allignore_off")
                );
            }
        }
    }

    public Player getPlayer() {
        return module.getPlugin().getServer().getPlayer(uuid);
    }
    public UUID getUUID() {
        return uuid;
    }

    @Deprecated
    public String getPlayerName() {
        return getPlayer().getName();
    }

    public boolean isIgnored(UUID player) {
        return ignoreList.contains(player);
    }

    public List<UUID> getIgnoreList() {
        return ignoreList;
    }

}
