package net.picklecraft.Modules.Counter;

import java.text.NumberFormat;
import java.text.ParseException;
import org.bukkit.block.Sign;

/**
 *
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
 *
 * @author Pickle
 */
public class CounterSign {

    private static final int expireTime = 12000; // 2 minutes
    private static final NumberFormat formatter = NumberFormat.getInstance();
    private final Sign sign;

    private long lastUpdate;

    private int count,rollover;
    private final int max;                 

    public CounterSign(Sign sign, int max, int count, int rollover) {
        this.sign = sign;
        this.max = max;
        this.count = count;
        this.rollover = rollover;
        this.sign.setLine(1, formatter.format(this.max));
    }

    public Sign getSign() {
        return sign;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - lastUpdate >= expireTime;
    }

    public int getCount() {
        return count;
    }

    public int getMax() {
        return max;
    }

    public int getRollover() {
        return rollover;
    }

    public void Count() {
        boolean rolledOver = false;
        if (++count > max) {
            count = 0;
            rollover++;
            sign.setLine(3, formatter.format(rollover));
            rolledOver = true;
        }
        sign.setLine(2, formatter.format(count));
        sign.update();
        lastUpdate = System.currentTimeMillis();
        if (rolledOver) {
            Power();
        }
    }

    public void Power() {
        //do something?
    }

    public static CounterSign create(Sign sign) {
        int max, count, rollover;
        try {
            max = formatter.parse(sign.getLine(1)).intValue();
        } catch (ParseException e) {
            max = Integer.MAX_VALUE;
        }
        try {
            count = formatter.parse(sign.getLine(2)).intValue();
        } catch (ParseException e) {
            count = 0;
        }
        try {
            rollover = formatter.parse(sign.getLine(3)).intValue();
        } catch (ParseException e) {
            rollover = 0;
        }
        return new CounterSign(sign, max, count, rollover);
    }

}
