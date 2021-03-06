/*
 * Copyright (C) 2015 Ouadban Youssef(tafayor.dev@gmail.com)
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


package com.tafayor.selfcamerashot.taflib.types;


public class BreakException extends RuntimeException
{
    boolean mRet = false;
    public BreakException(){};

    public BreakException(String message)
    {
        super(message);
    }

    public BreakException(String message, boolean ret)
    {
        super(message);
        mRet = ret;
    }

    @Override
    public String getMessage()
    {
        return "BreakException : " + super.getMessage();
    }

    public boolean ret() {return mRet;}
}
