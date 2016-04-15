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


package com.tafayor.selfcamerashot.taflib.helpers;

import android.content.Context;
import android.util.Base64;

import com.tafayor.selfcamerashot.App;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;


public class SerializeHelper
{

    public static String TAG = SerializeHelper.class.getSimpleName();

    private Context mContext;





    private static class Loader {  private static final SerializeHelper INSTANCE = new SerializeHelper(App.getContext());}

    public static SerializeHelper getInstance( )
    {
        return Loader.INSTANCE;
    }

    private SerializeHelper(Context context)
    {
        mContext = context;
    }





    //----------------------------------------------------------------------------------------------
    // serialize
    //----------------------------------------------------------------------------------------------
    public static<M,K> String serialize(HashMap<M, K> Map)
    {
        try
        {
            ByteArrayOutputStream mem_out = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(mem_out);

            out.writeObject(Map);

            byte[] bytes =  mem_out.toByteArray();

            String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);

            out.close();
            mem_out.close();

            return base64;

        }
        catch (IOException e)
        {
            LogHelper.logx(e);
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------
    // deserialize
    //----------------------------------------------------------------------------------------------
    public static<M,K> HashMap<M, K> deserialize(String data)
    {
        try
        {
            byte[] bytes = Base64.decode(data, Base64.DEFAULT );
            ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(mem_in);

            HashMap<M, K> map = (HashMap<M, K>)in.readObject();

            in.close();
            mem_in.close();

            return map;
        }
        catch (StreamCorruptedException e)
        {
            LogHelper.logx(e);
            return null;
        }
        catch (ClassNotFoundException e)
        {
            LogHelper.logx(e);
            return null;
        }
        catch (IOException e)
        {
            LogHelper.logx(e);
            return null;
        }
    }



}
