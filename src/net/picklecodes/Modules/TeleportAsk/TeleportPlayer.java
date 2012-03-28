package net.picklecodes.Modules.TeleportAsk;

import net.picklecodes.PickleCraftPlugin;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * Copyright (c) 2011-2012
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Pickle
 * 
 */

public class TeleportPlayer {
	private static int MaxDenyTimer = 30, MaxReturnTimer = 30;
	
	private TeleportAskModule module;
	private Player player;
	private TeleportPlayer requestPlayer;
	
	private int denyTimer, returnTimer;
	private boolean isTPHereRequest, isPlayerC, hasDualRequest, hasAcceptedDualRequest, canReturn;
	private Location returnLocation;

	
	public TeleportPlayer(TeleportAskModule module, Player player) {
		this.module = module;
		this.player = player;
		MaxDenyTimer = module.getPlugin().getConfig().getInt("teleportask.denytimer",30);
		MaxReturnTimer = module.getPlugin().getConfig().getInt("teleportask.returntimer",30);
	}
	
	public boolean HasRequest() {
		return requestPlayer != null;
	}
	public boolean HasDualRequest() {
		return hasDualRequest;
	}
	public boolean HasAcceptedDualRequest() {
		return hasAcceptedDualRequest;
	}
	
	public Player getPlayer() { return player; }
	public Player getRequestPlayer() { return requestPlayer.getPlayer(); }
			
	/*
	* get the closest safest point to teleport.
	*/
	public boolean TeleportToCoord(int x,int y, int z) {
		World world = player.getPlayer().getWorld();
		for (int i = y; i < world.getMaxHeight(); i++) {
			Block airCheckBlock = world.getBlockAt(x, i, z);
			if (airCheckBlock.getType() == Material.AIR) {
				if (airCheckBlock.getRelative(BlockFace.UP).getType() == Material.AIR) {
					if (player.getPlayer().getGameMode() == GameMode.CREATIVE) {
						//player is creative, ignore safety precautions.
						setLastLocation();
						PickleCraftPlugin.log.info(player.getPlayer().getName() +" Teleported to X:"+x+" Y:"+ i +" Z:"+z);
						player.getPlayer().teleport(new Location(world, x, i, z));
						return true;
					}
					else {
						//prevent as teleporting in the air or above a lava pool.
						Block footBlock = airCheckBlock.getRelative(BlockFace.DOWN);
						if (footBlock.getType() == Material.STATIONARY_LAVA
								|| footBlock.getType() == Material.LAVA
								|| footBlock.getType() == Material.AIR) { 
							/* I dunno why.
							 *  but it won't work when I use || and !=...
							 *  but allows this set up.....ikr?
							 */
						}
						else {
							//safe to teleport.
							setLastLocation();
							PickleCraftPlugin.log.info(player.getPlayer().getName() +" Teleported to X:"+x+" Y:"+ i +" Z:"+z);
							player.getPlayer().teleport(new Location(world, x, i, z));
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public void setLastLocation() {
		returnLocation = player.getLocation();
		returnTimer = MaxReturnTimer;
		canReturn = true;
	}
	
	public void ReturnToLastLocation() {
		if (canReturn) {
			player.sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.info.canreturn")
					);
			player.teleport(returnLocation);
			canReturn = false;
		}
		else {
			player.getPlayer().sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.errors.cantreturn")
					);
		}
		
	}
	
	public void DualRequest(TeleportPlayer playerB, TeleportPlayer playerC) { 
		// this class will act as "PlayerA"
		// playerC will teleport to playerb.
		if (playerB.HasRequest()) {
			player.sendMessage(
				module.getPlugin().getStringFromConfig("teleportask.messages.errors.requestpending"
				, playerB.getPlayer().getName())
				);
		}
		else if (playerC.HasRequest()) {
			player.sendMessage(
				 module.getPlugin().getStringFromConfig("teleportask.messages.errors.requestpending"
					, playerC.getPlayer().getName())
					);
			}
		else {
			playerB.requestPlayer = playerC;
			playerB.hasDualRequest = true;
			playerB.denyTimer = MaxDenyTimer;
			playerC.requestPlayer = playerB;
			playerC.hasDualRequest = true;
			playerC.isPlayerC = true;
			playerC.denyTimer = MaxDenyTimer;
			player.sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.info.requestsent")
					);
			playerB.getPlayer().sendMessage(
				module.getPlugin().getStringFromConfig("teleportask.messages.info.tpdualrequest"
				, player.getName()
				, playerC.getPlayer().getName())
				);	
			playerC.getPlayer().sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.info.tpheredualrequest"
					, player.getName()
					, playerB.getPlayer().getName())
					);	
			playerB.getPlayer().sendMessage(
				module.getPlugin().getStringFromConfig("teleportask.messages.info.showcommands")
				);	
			playerC.getPlayer().sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.info.showcommands")
					);	
		}
	}
	
	
	public void Request(TeleportPlayer requestPlayer, boolean isTPHereRequest) {
		if (HasRequest()) {
			requestPlayer.getPlayer().sendMessage(
				module.getPlugin().getStringFromConfig("teleportask.messages.errors.requestpending"
				, player.getName())
				);
		}
		else {
			this.isTPHereRequest = isTPHereRequest;	
			this.requestPlayer = requestPlayer;
			denyTimer = MaxDenyTimer;
			requestPlayer.getPlayer().sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.info.requestsent")
					);
			if (isTPHereRequest) {
				player.sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.info.tphererequest"
					, requestPlayer.getPlayer().getName())
					);		
			}
			else {
				player.sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.info.tprequest"
					, requestPlayer.getPlayer().getName())
					);	
			}
			player.sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.info.showcommands")
				);	
			
		}
	}
	
	public void Deny() {
		Deny(false); 
	}
	public void Deny(boolean auto) {
		if (HasRequest()) {
			if (!auto) {
				player.sendMessage(
						module.getPlugin().getStringFromConfig("teleportask.messages.info.deny")
						);
				requestPlayer.getPlayer().sendMessage(
						module.getPlugin().getStringFromConfig("teleportask.messages.info.deniedrequest"
						, player.getName())
						);
			}
			else {
				player.sendMessage(
						module.getPlugin().getStringFromConfig("teleportask.messages.info.autodeny")
						);
				requestPlayer.getPlayer().sendMessage(
						module.getPlugin().getStringFromConfig("teleportask.messages.info.autodeniedrequest"
						, player.getName())
						);
			}		
			if (hasDualRequest) {
				requestPlayer.CleanUp();
			}
			CleanUp();
		}
		else {
			if (!auto) {
				player.sendMessage(
						module.getPlugin().getStringFromConfig("teleportask.messages.errors.havenorequests")
						);
			}
		}
	}
	
	public boolean AcceptDual() {
		if (HasDualRequest()) { //lets see if we have a dual request?
			if (requestPlayer.HasDualRequest()) {
				if (HasAcceptedDualRequest()) {
					player.sendMessage(
							module.getPlugin().getStringFromConfig("teleportask.messages.errors.alreadyaccepted")
							);
				}
				else if (requestPlayer.HasAcceptedDualRequest()) {
					if (isPlayerC == false) { 
						//if this is false, that must mean that this is PlayerB
						requestPlayer.getPlayer().sendMessage(
								module.getPlugin().getStringFromConfig("teleportask.messages.info.teleporting")
								);
						player.sendMessage(
								module.getPlugin().getStringFromConfig("teleportask.messages.info.incomingplayer"
								, requestPlayer.getPlayer().getName())
								);
						requestPlayer.Teleport(this);
					}
					else {
						//if this is wasn't false, that must mean that this is PlayerC
						player.getPlayer().sendMessage(
								module.getPlugin().getStringFromConfig("teleportask.messages.info.teleporting")
								);
						requestPlayer.getPlayer().sendMessage(
								module.getPlugin().getStringFromConfig("teleportask.messages.info.incomingplayer"
								, player.getName())
								);
						Teleport(requestPlayer);
					}
				}
				else {
					player.sendMessage(
							module.getPlugin().getStringFromConfig("common.messages.info.accept")
							);
					requestPlayer.getPlayer().sendMessage(
							module.getPlugin().getStringFromConfig("teleportask.messages.info.tpdualaccept"
							, player.getName())
							);
					hasAcceptedDualRequest = true;
				}
			}
			else {
				//something failed? :c
				hasDualRequest = false;
				PickleCraftPlugin.log.warning("PlayerB has request, but playerC does not, how the....");
			}
			return true;
		}
		return false;
	}
	
	public void Accept() {
		if (HasRequest()){
				if (AcceptDual())  {
					return;
				}
			 // nope, chuck testa.. now check for normal requests
				if (isTPHereRequest) {
					player.sendMessage(
							module.getPlugin().getStringFromConfig("teleportask.messages.info.teleporting")
							);
					requestPlayer.getPlayer().sendMessage(
							module.getPlugin().getStringFromConfig("teleportask.messages.info.incomingplayer"
							, player.getName())
							);	
					Teleport(requestPlayer);
				}
				else {
					requestPlayer.getPlayer().sendMessage(
							module.getPlugin().getStringFromConfig("teleportask.messages.info.teleporting")
							);
					player.sendMessage(
							module.getPlugin().getStringFromConfig("teleportask.messages.info.incomingplayer"
							, requestPlayer.getPlayer().getName())
							);
					requestPlayer.Teleport(this);
				}
				requestPlayer = null;
			}
		else {
			player.sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.errors.havenorequests")
					);
		}
	}
	
	
	public void Teleport(TeleportPlayer tPlayer) {
		setLastLocation();
		player.teleport(tPlayer.getPlayer());
		if (PickleCraftPlugin.hasPerm(player, "IgnoreCraft.teleport.return")) {
			player.sendMessage(
					module.getPlugin().getStringFromConfig("teleportask.messages.info.showreturncommand")
					);
		}
		PickleCraftPlugin.log.info(player.getName() +" Teleported to "+ tPlayer.getPlayer().getName());	
		tPlayer.CleanUp();
		CleanUp();
	}
	
	/*
	 * remove unnecessary stuff...
	 */
	public void CleanUp() {
		requestPlayer = null;
		isTPHereRequest = false;
		isPlayerC = false;
		hasDualRequest = false;
		hasAcceptedDualRequest = false;
	}
	
	public void Update() {
		if (HasRequest()) {
			if (MaxDenyTimer != 0) {
				if (denyTimer > 0) { 
					 denyTimer--;
				}
				else {
					Deny(true);
				}
			}
		}
		if (MaxReturnTimer != 0) {
			if (returnTimer > 0) { 
				 returnTimer--;
			}
			else {
				canReturn = false;
			}
		}
	}
}
