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


public class StrException extends Exception
{
    protected boolean mRet = false;
    protected String mUserMsg = null;

    public StrException(){init();};

    public StrException(String message)
    {
        super(message);
        init();
    }

     public StrException(String message, String userMsg)
    {
        super(message);
        mUserMsg = userMsg;
        init();
    }

    public StrException(String message, boolean ret)
    {
        super(message);
        mRet = ret;
        init();
    }

    protected void init()
    {

    }


    public boolean ret() {return mRet;}
    public String userMsg(){return mUserMsg;}
    public boolean isUserMsg(){return (mUserMsg!= null);}

    protected void setRet(boolean state)
    {
        mRet = state;
    }
}
