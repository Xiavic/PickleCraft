package net.picklecraft.Modules.InvisibilityNerf;

/**
 *
 * Copyright (c) 2012
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
 */
public class SpyTimer implements Runnable {
	private InvisibilityNerfModule module;
	private int tick = 20;

	public SpyTimer(InvisibilityNerfModule module, int tick) {
		this.module = module;
		this.tick = tick;
	}

	@Override
	public void run() {
		for (int i=0; i < module.getPlayers().size(); i++) {
			SpyPlayer spy = module.getPlayers().get(i);
			if (spy.getDuration() <= 0) {
				spy.setInvisible(false);
				module.getPlayers().remove(i);
			}
			else {
				spy.setDuration(spy.getDuration() - tick);
				if (spy.getInvisible()) {
					spy.setCoolDown(spy.getCoolDown() - tick);
				}
				else {
					spy.setCoolDown(spy.getCoolDown() + tick);
				}
			}
		}
	}

}
